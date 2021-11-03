package ru.betry;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UrfuScheduleSocket {
    public final static String ID_QUERY_URL;
    public final static String SCHEDULE_URL;

    private static final Pattern weeklyTableRE;
    private static final Pattern weekdayRawRE;

    static {
        ID_QUERY_URL = "https://urfu.ru/api/schedule/groups/suggest/?query=";
        SCHEDULE_URL = "https://urfu.ru/ru/students/study/schedule/#student/";

        weeklyTableRE = Pattern.compile("(?s)<table class=\"shedule-group-table\">.*?</table>");
        weekdayRawRE = Pattern.compile("(?s)<tr class=\"divide\">.*?<tr class=\"divide\">.*?</tr>");
    }

    private static String getPageContent(String url) throws IOException {
        var connection = new URL(url).openConnection();
        var scanner = new Scanner(connection.getInputStream());
        scanner.useDelimiter("\\Z");
        var content = scanner.next();
        scanner.close();

        return content;
    }

    public static class InvalidGroupException extends Exception {
        public InvalidGroupException(String errorMessage) {
            super(errorMessage);
        }
    }

    private static String getWeekScheduleUrl(String group, Date date) throws IOException, InvalidGroupException {
        var idQueryResponse = getPageContent(ID_QUERY_URL + group);

        var gson = new GsonBuilder().setPrettyPrinting().create();
        var response = gson.fromJson(idQueryResponse, IdQueryResponse.class);

        if (response.suggestions.size() < 1)
            throw new InvalidGroupException("Invalid group.");

        var id = response.suggestions.get(0).data;
        var dateFormatted = new SimpleDateFormat("yyyyMMdd").format(date);

        return SCHEDULE_URL + id + "/" + dateFormatted + "/"; // нужен ли последний + "/" ?
    }

    // TODO сделать опцию выгрузки расписания не только на неделю, но и на день

    /**
     * Принимает на вход группу и дату, возвращает расписание на неделю вперёд.
     * @param group группа в формате МЕН-хххххх
     * @param date дата
     * @return расписание на неделю, которая включает в себя дату date
     */

    // достаём данные из таблицы
    public static ArrayList<Weekday> getScheduleByGroup(String group, Date date) throws InvalidGroupException, IOException {
        var url = getWeekScheduleUrl(group, date);
        var schedulePageContent = getPageContent(url);

        var matcher = weeklyTableRE.matcher(schedulePageContent);
        if (!matcher.find())
            throw new IOException();

        var tableContent = matcher.group(0);
        return extractWeeklySchedule(tableContent);
    }

    private static ArrayList<Weekday> extractWeeklySchedule(String tableContent) {
        var matcher = weekdayRawRE.matcher(tableContent);
        var weeklySchedule = new ArrayList<Weekday>();

        while (matcher.find() && weeklySchedule.size() < 7) {
            var weekdayInfoRaw = matcher.group(0);
            weeklySchedule.add(Weekday.Create(weekdayInfoRaw));
        }

        return weeklySchedule;
    }
}
