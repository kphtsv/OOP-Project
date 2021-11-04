package ru.betry.urfuSchedule;

import ru.betry.Weekday;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class UrfuScheduleParser {
    private static final Pattern weeklyTableRE;
    private static final Pattern weekdayRawRE;

    static {
        weeklyTableRE = Pattern.compile("(?s)<table class=\"shedule-group-table\">.*?</table>");
        weekdayRawRE = Pattern.compile("(?s)<tr class=\"divide\">.*?<tr class=\"divide\">.*?</tr>");
    }

    public static String getScheduleTableContent(String pageContent) throws IOException {

        var matcher = weeklyTableRE.matcher(pageContent);
        if (!matcher.find())
            throw new IOException();
        return matcher.group(0);
    }

    public static ArrayList<Weekday> extractSchedule(String tableContent, int daysAhead) {
        var matcher = weekdayRawRE.matcher(tableContent);
        var weeklySchedule = new ArrayList<Weekday>();

        while (matcher.find() && weeklySchedule.size() < daysAhead) {
            var weekdayInfoRaw = matcher.group(0);
            weeklySchedule.add(Weekday.Create(weekdayInfoRaw));
        }

        return weeklySchedule;
    }
}
