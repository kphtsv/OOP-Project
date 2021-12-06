package ru.betry.urfuSchedule;

import ru.betry.urfuSchedule.models.Weekday;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;

public class UrfuScheduleService {
    public static class InvalidGroupException extends Exception {
        public InvalidGroupException(String errorMessage) { super(errorMessage); }
    }

    public IUrfuAPI api;
    private final IScheduleParser parser;
    private final MathMech mathMech;

    public UrfuScheduleService() {
        api = new UrfuScheduleApi();
        parser = new UrfuScheduleParser();
         mathMech = new MathMech();
         System.out.println(mathMech.groups);
    }

    /**
     * Принимает на вход группу и дату, возвращает расписание на некоторое кол-во дней вперёд.
     * @param group группа в формате МЕН-хххххх
     * @param date дата
     * @param daysAhead на сколько дней вперёд нужно вернуть расписание
     * @return расписание на неделю, которая включает в себя дату date
     */

    // достаём данные из таблицы
    public String[] getScheduleByGroup(String group, Date date, int daysAhead) throws InvalidGroupException, IOException {
        var schedulePageContent = api.getSchedulePageContent(group, date);
        return parser.extractFormattedSchedule(schedulePageContent, daysAhead);
    }

    private Weekday[] getWeekdayScheduleByGroup(String group, Date date, int daysAhead) throws InvalidGroupException, IOException {
        var schedulePageContent = api.getSchedulePageContent(group, date);
        return parser.extractSchedule(schedulePageContent, daysAhead);
    }

//    public String[] getAvailableCabinets(Date date) {
//        var mm = new MathMech();
//        var groups = mm.groups.values();
//
//        return new String[0];
//    }

    // public String[] getCabinets(Date date, int daysAhead) {
    public HashSet<String> getCabinets(Date date, int daysAhead) {
        var mm = new MathMech();
        var groups = mm.groups.values();
        var cabinets = new HashSet<String>();

        for (var group : groups) {
            try {
                var schedule = getWeekdayScheduleByGroup(group, date, daysAhead);
                for (var day : schedule) {
                    for (var lesson : day.classes) {
                        var cabinet = parser.extractCabinet(lesson.extraInfo);

                        if (cabinet != null)
                            cabinets.add(cabinet);
                    }
                }
            } catch (InvalidGroupException e) {
                System.out.println("Incorrect group: " + group);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return cabinets; // .toArray(new String[0])
    }

    public HashSet<String> getAllDynamicCabinets() {
        return getCabinets(new Date(), 7);
    }

    public String[] getFreeCabinets(Date date) {
        // var unused = getAllDynamicCabinets();
        var unused = MathMech.getClonedAudiences();
        var usedToday = getCabinets(date, 1);

        for (var cabinet : usedToday) {
            unused.remove(cabinet);
        }

        return unused.toArray(new String[0]);
    }
}
