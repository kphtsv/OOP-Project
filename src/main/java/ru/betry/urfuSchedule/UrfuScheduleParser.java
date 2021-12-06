package ru.betry.urfuSchedule;

import ru.betry.urfuSchedule.models.ClassInfo;
import ru.betry.urfuSchedule.models.Weekday;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class UrfuScheduleParser implements IScheduleParser {
    private static final Pattern weeklyTableRE;
    private static final Pattern weekdayRawRE;
    private static final Pattern weekdayDateInfoRE;
    private static final Pattern weekdayClassesRE;
    private static final Pattern classInfoRE;
    private static final Pattern extraInfoRe;
    private static final Pattern cabinetRE;

    static {
        weeklyTableRE = Pattern.compile("(?s)<table class=\"shedule-group-table\">.*?</table>");
        weekdayRawRE = Pattern.compile("(?s)<tr class=\"divide\">.*?<tr class=\"divide\">.*?</tr>");
        weekdayDateInfoRE = Pattern.compile("(?s)<b>(?<date>.*?)</b>.*?weekday-name\">[ |\\n]*(?<weekdayName>.*?)[ |\\n]*</");
        weekdayClassesRE = Pattern.compile("(?s)<tr class=\"shedule-weekday-row\">.*?</tr>");
        classInfoRE = Pattern.compile("(?s)<td class=\"shedule-weekday-time\">(?<time>.*?)</td>.*?<dd>[ |\\n]*(?<order>\\d)\\.[ |\\n]*(?<name>.*?)[ |\\n]*</dd>*.*?<dt>(?<extraInfo>.*?)</dt>");
        extraInfoRe = Pattern.compile("(?s)<span class=\"teacher\">(.*?)</");
        cabinetRE = Pattern.compile("[0-9]{3}\\D?");
    }

    public String getScheduleTableContent(String pageContent) throws IOException {

        var matcher = weeklyTableRE.matcher(pageContent);
        if (!matcher.find())
            throw new IOException();
        return matcher.group(0);
    }

    public String[] extractFormattedSchedule(String schedulePageContent, int daysAhead) throws IOException {
        // TODO костыль, можно сделать изящнее?
        var schedule = extractSchedule(schedulePageContent, daysAhead);
        var result = new String[schedule.length];
        for (var i = 0; i < schedule.length; i++) {
            result[i] = schedule[i].toString();
        }
        return result;
    }

    public Weekday[] extractSchedule(String schedulePageContent, int daysAhead) throws IOException {
        var tableContent = getScheduleTableContent(schedulePageContent);
        var matcher = weekdayRawRE.matcher(tableContent);
        var weeklySchedule = new ArrayList<Weekday>();

        while (matcher.find() && weeklySchedule.size() < daysAhead) {
            var weekdayInfoRaw = matcher.group(0);
            weeklySchedule.add(extractWeekday(weekdayInfoRaw));
        }

        return weeklySchedule.toArray(new Weekday[0]);
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
            classes.add(extractClass(classInfoRaw));
        }

        return new Weekday(name, date, classes);
    }

    private ClassInfo extractClass(String rawInfo) {
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
        result.append("   ");
        while (matcher.find()) {
            result.append(matcher.group(1));
            result.append("\n   ");
        }
        return result.toString();
    }

    public String extractCabinet(String extraInfo) {
        var matcher = cabinetRE.matcher(extraInfo);
        return matcher.find() ? matcher.group(0) : null;
    }
}
