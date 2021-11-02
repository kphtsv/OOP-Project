package ru.betry;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrfuScheduleSocket {
    public final static String ID_QUERY_URL;
    public final static String SCHEDULE_URL;

    private static final Pattern weeklyTableRE;
    private static final Pattern weekdayInfoRE;
    private static final Pattern weekdayClassesRE;
    private static final Pattern classInfoRE;
    private static final Pattern extraInfoRe;

    static {
        ID_QUERY_URL = "https://urfu.ru/api/schedule/groups/suggest/?query=";
        SCHEDULE_URL = "https://urfu.ru/ru/students/study/schedule/#student/";

        weeklyTableRE = Pattern.compile("(?s)<table class=\"shedule-group-table\">.*?</table>");
        weekdayInfoRE = Pattern.compile("(?s)<tr class=\"divide\">.*?<b>(?<date>.*?)</b>(?<schedule>.*?)<tr class=\"divide\">.*?</tr>");
        weekdayClassesRE = Pattern.compile("(?s)<tr class=\"shedule-weekday-row\">.*?</tr>");
        classInfoRE = Pattern.compile("(?s)<td class=\"shedule-weekday-time\">(?<time>.*?)</td>.*?<dd>[ |\\n]*(?<order>\\d)\\.[ |\\n]*(?<name>.*?)[ |\\n]*</dd>*.*?<dt>(?<extraInfo>.*?)</dt>");
        extraInfoRe = Pattern.compile("(?s)<span class=\"teacher\">(.*?)</");
    }

    private static String getPageContent(String url) throws IOException {
        URLConnection connection = null;
        connection =  new URL(url).openConnection();
        var scanner = new Scanner(connection.getInputStream());
        scanner.useDelimiter("\\Z");
        String content = scanner.next();
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

    /*
     * Важно! На странице по дате выгружается не только текущая неделя, но и несколько дней наперёд.
     * По дефолту метод должен возвращать расписание на всю неделю.
     * короче, пока будет возвращать всё, что ему дают на странице, фильтрацией буду заниматься позже
     * TODO сделать опцию выгрузки расписания не только на неделю, но и на день
     * TODO tableContent должна быть уже отсеяна от дней вне недели
     */

    /**
     * Принимает на вход группу и дату, возвращает расписание на неделю, которая включает эту дату
     * @param group группа в формате МЕН-хххххх
     * @param date дата
     * @return расписание на неделю, которая включает в себя дату date
     */

    // достаём данные из таблицы
    public static Object getScheduleByGroup(String group, Date date) throws InvalidGroupException, IOException {
        var url = getWeekScheduleUrl(group, date);
        var schedulePageContent = getPageContent(url);

        Matcher matcher = weeklyTableRE.matcher(schedulePageContent);
        if (!matcher.find())
            throw new IOException();
        var tableContent = matcher.group(0);

        return extractWeeklySchedule(tableContent);
    }

    // достаём все дни
    private static Object extractWeeklySchedule(String tableContent) {
        Matcher matcher = weekdayInfoRE.matcher(tableContent);
        var weeklySchedule = new ArrayList<ArrayList<ClassInfo>>();
        while (matcher.find()) {
            var weekdayInfo = matcher.group(0);
            // String weekdayDate = matcher.group("date");
            var weekdaySchedule = extractWeekdaySchedule(weekdayInfo);
            weeklySchedule.add(weekdaySchedule);
        }
        return weeklySchedule;
    }

    // достаём все пары
    private static ArrayList<ClassInfo> extractWeekdaySchedule(String weekdayInfo) {
        var weekdaySchedule = new ArrayList<ClassInfo>();
        var matcher = weekdayClassesRE.matcher(weekdayInfo);
        while (matcher.find()) {
            var classInfoRaw = matcher.group(0);
            var classInfo = extractClassInfo(classInfoRaw); // TODO пиздец глаза выпали нахуй ))0)
            weekdaySchedule.add(classInfo);
        }
        return weekdaySchedule;
    }

    // достаём все детали в описании пары
    private static ClassInfo extractClassInfo(String classInfoRaw) {
        Matcher matcher = classInfoRE.matcher(classInfoRaw);
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
        while (matcher.find()) {
            result.append(matcher.group(1));
            result.append(". ");
        }
        return result.toString();
    }
}
