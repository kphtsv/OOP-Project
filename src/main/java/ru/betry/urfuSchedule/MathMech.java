package ru.betry.urfuSchedule;

import ru.betry.GroupsResources;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class MathMech {
    // private HashSet<String> AUDIENCES; // TODO: дописать аудитории на 5 этаже и убрать те, в которых не проводятся пары
    private static final String[] AUDIENCES = new String[] {"601", "605", "607", "609", "611", "613", "615", "617", "621", "623",
            "625", "612", "614", "616", "618", "620", "622", "622а", "624", "626", "628", "630", "632", "634", "636",
            "638", "640", "515"};

    public HashMap<String, String> allGroups = new HashMap<>(); // 'КН-203' -> '200208' (пример)
    private final UrfuScheduleService service;
    private final IScheduleParser parser;

    public MathMech(UrfuScheduleService service, IScheduleParser parser) {
        this.service = service;
        this.parser = parser;
        initializeGroups();
        // initializeCabinets();
    }

    // TODO пофиксить проблему с нахождением файла
    private void initializeGroups() {
        allGroups = new GroupsResources().groups;
    }

//    private void initializeCabinets() {}

    // возвращает новый HashSet каждый раз для того, чтобы можно было убирать из него элементы без последствий
    // используется для подсчёта свободных аудиторий
    public static HashSet<String> getClonedAudiences() {
        return new HashSet<>(Arrays.asList(AUDIENCES));
    }

    private HashMap<Integer, HashSet<String>> getCabinetsUsed(Date date, int daysAhead) {
        var groups = allGroups.values();
        var cabinetsUsed = new HashMap<Integer, HashSet<String>>(); // номер пары, использованные кабинеты в этот промежуток времени

        for (var group : groups) {
            try {
                var schedule = service.getWeekdayScheduleByGroup(group, date, daysAhead);
                for (var day : schedule) {
                    for (var lesson : day.classes) {
                        var cabinet = parser.extractCabinet(lesson.extraInfo);
                        Integer order = lesson.order;

                        if (cabinet != null) {
                            if (!cabinetsUsed.containsKey(order))
                                cabinetsUsed.put(order, new HashSet<>());
                            cabinetsUsed.get(order).add(cabinet);
                        }
                    }
                }

            } catch (UrfuScheduleService.InvalidGroupException e) {
                System.out.println("Incorrect group: " + group);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (var order : cabinetsUsed.keySet()) {
            System.out.println(order);
            System.out.println(cabinetsUsed.get(order));
        }

        return cabinetsUsed;
    }

    private HashMap<Integer, HashSet<String>> getFreeCabinets(Date date) {
        var unused = new HashMap<Integer, HashSet<String>>();
        var usedToday = getCabinetsUsed(date, 1);

        for (var order : usedToday.keySet()) {
            var unusedByOrder = MathMech.getClonedAudiences();
            for (var cabinet : usedToday.get(order))
                unusedByOrder.remove(cabinet);

            unused.put(order, unusedByOrder);
        }

        return unused;
    }

    // нет учёта порядка при выведении order
    // выводит только те order, в которых кто-то занимался в аудитории

    public String getFreeCabinetsFormatted(Date date) {
        var unused = getFreeCabinets(date);
        var message = new StringBuilder();
        message.append("*Свободные кабинеты*");

        for (var order : unused.keySet()) {
            message.append("\n\n");

            var cabinets = unused.get(order).toArray(new String[0]);
            Arrays.sort(cabinets);

            message.append("_Пара №").append(order).append(":_ \n");
            message.append(String.join(", ", cabinets));
        }

        return message.toString();
    }
}
