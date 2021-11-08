package ru.betry.urfuSchedule;

import ru.betry.urfuSchedule.models.Weekday;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class UrfuScheduleService {
    public static class InvalidGroupException extends Exception {
        public InvalidGroupException(String errorMessage) { super(errorMessage); }
    }

    /**
     * Принимает на вход группу и дату, возвращает расписание на некоторое кол-во дней вперёд.
     * @param group группа в формате МЕН-хххххх
     * @param date дата
     * @param daysAhead на сколько дней вперёд нужно вернуть расписание
     * @return расписание на неделю, которая включает в себя дату date
     */

    // достаём данные из таблицы
    public static ArrayList<Weekday> getScheduleByGroup(String group, Date date, int daysAhead) throws InvalidGroupException, IOException {
        var schedulePageContent = UrfuScheduleApi.getSchedulePageContent(group, date);
        var tableContent = UrfuScheduleParser.getScheduleTableContent(schedulePageContent);

        return UrfuScheduleParser.extractSchedule(tableContent, daysAhead);
    }
}
