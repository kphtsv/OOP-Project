package ru.betry;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Weekday {
    public String name;
    public String date;
    public ArrayList<ClassInfo> classes;

    private static final Pattern weekdayDateInfoRE = Pattern.compile("(?s)<b>(?<date>.*?)</b>.*?weekday-name\">[ |\\n]*(?<weekdayName>.*?)[ |\\n]*</");
    private static final Pattern weekdayClassesRE = Pattern.compile("(?s)<tr class=\"shedule-weekday-row\">.*?</tr>");

    public Weekday(String name, String date, ArrayList<ClassInfo> classes) {
        this.name = name;
        this.date = date;
        this.classes = classes;
    }

    public static Weekday Create(String weekdayInfoRaw) {
        var matcher = weekdayDateInfoRE.matcher(weekdayInfoRaw);
        String name = null;
        String date = null;

        if (matcher.find()) {
            name = matcher.group("weekdayName");
            date = matcher.group("date");
        }

        var classes = new ArrayList<ClassInfo>();
        matcher = weekdayClassesRE.matcher(weekdayInfoRaw);
        while (matcher.find()) {
            var classInfoRaw = matcher.group(0);
            classes.add(ClassInfo.Create(classInfoRaw));
        }

        return new Weekday(name, date, classes);
    }
}
