package ru.betry.urfuSchedule;

import ru.betry.urfuSchedule.models.ClassInfo;
import ru.betry.urfuSchedule.models.Weekday;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class UrfuScheduleParser {
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

    public static String getScheduleTableContent(String pageContent) throws IOException {

        var matcher = weeklyTableRE.matcher(pageContent);
        if (!matcher.find())
            throw new IOException();
        return matcher.group(0);
    }

    public static String[] extractSchedule(String tableContent, int daysAhead) {
        var matcher = weekdayRawRE.matcher(tableContent);
        var weeklySchedule = new ArrayList<String>();

        while (matcher.find() && weeklySchedule.size() < daysAhead) {
            var weekdayInfoRaw = matcher.group(0);
            weeklySchedule.add(extractWeekday(weekdayInfoRaw).toString());
        }

        return weeklySchedule.toArray(new String[0]);
    }

    public static Weekday extractWeekday(String weekdayInfoRaw) {
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
            classes.add(extractClass(classInfoRaw));
        }

        return new Weekday(name, date, classes);
    }

    public static ClassInfo extractClass(String rawInfo) {
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

    private static String formatRawExtraClassInfo(String rawExtraInfo) {
        var matcher = extraInfoRe.matcher(rawExtraInfo);
        var result = new StringBuilder();
        result.append("   ");
        while (matcher.find()) {
            result.append(matcher.group(1));
            result.append("\n   ");
        }
        return result.toString();
    }
}
