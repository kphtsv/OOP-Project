package ru.betry.urfuSchedule;

import ru.betry.urfuSchedule.models.ClassInfo;
import ru.betry.urfuSchedule.models.Weekday;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class UrfuScheduleParser implements IScheduleParser { // rename SimpleParser
    private static final Pattern weeklyTableRE;
    private static final Pattern weekdayRawRE;
    private static final Pattern weekdayDateInfoRE;
    private static final Pattern weekdayClassesRE;
    private static final Pattern classInfoRE;
    private static final Pattern extraInfoRe;

    static {
        weeklyTableRE = Pattern.compile("(?s)<table class=\"shedule-group-table\">.*?</table>");
        weekdayRawRE = Pattern.compile("(?s)<tr class=\"divide\">.*?<tr class=\"divide\">.*?</tr>");
        weekdayDateInfoRE = Pattern.compile("(?s)<b>(?<date>.*?)</b>.*?weekday-name\">[ |\\n]*(?<weekdayName>.*?)[ |\\n]*</");
        weekdayClassesRE = Pattern.compile("(?s)<tr class=\"shedule-weekday-row\">.*?</tr>");
        classInfoRE = Pattern.compile("(?s)<td class=\"shedule-weekday-time\">(?<time>.*?)</td>.*?<dd>[ |\\n]*(?<order>\\d)\\.[ |\\n]*(?<name>.*?)[ |\\n]*</dd>*.*?<dt>(?<extraInfo>.*?)</dt>");
        extraInfoRe = Pattern.compile("(?s)<span class=\"teacher\">(.*?)</");
    }

<<<<<<< Updated upstream
    public static String getScheduleTableContent(String pageContent) throws IOException {
=======
    private String getScheduleTableContent(String pageContent) throws IOException {
>>>>>>> Stashed changes
        var matcher = weeklyTableRE.matcher(pageContent);
        if (!matcher.find())
            throw new IOException();
        return matcher.group(0);
    }

<<<<<<< Updated upstream
    public static ArrayList<Weekday> extractSchedule(String tableContent, int daysAhead) {
=======
    public String[] extractSchedule(String pageContent, int daysAhead) throws IOException {
        var tableContent = getScheduleTableContent(pageContent);
>>>>>>> Stashed changes
        var matcher = weekdayRawRE.matcher(tableContent);
        var weeklySchedule = new ArrayList<Weekday>();

        while (matcher.find() && weeklySchedule.size() < daysAhead) {
            var weekdayInfoRaw = matcher.group(0);
            weeklySchedule.add(extractWeekday(weekdayInfoRaw));
        }

        return weeklySchedule;
    }

    private Weekday extractWeekday(String weekdayInfoRaw) {
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
            classes.add(extractClassInfo(classInfoRaw));
        }

        return new Weekday(name, date, classes);
    }

    private ClassInfo extractClassInfo(String rawInfo) {
        var matcher = classInfoRE.matcher(rawInfo);
        if (matcher.find()) {
            var order = matcher.group("order");
            var time = matcher.group("time");
            var name = matcher.group("name");
            var extraInfo = formatRawExtraClassInfo(matcher.group("extraInfo"));

            return new ClassInfo(order, time, name, extraInfo);
        }
        else
            return null;
    }

    private String formatRawExtraClassInfo(String rawExtraInfo) {
        var matcher = extraInfoRe.matcher(rawExtraInfo);
        var result = new StringBuilder();
        while (matcher.find()) {
            result.append(matcher.group(1));
            result.append(". ");
        }
        return result.toString();
    }
}
