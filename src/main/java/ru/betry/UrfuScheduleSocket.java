package ru.betry;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// почему бы не static?
public class UrfuScheduleSocket {
    public final static String ID_QUERY_URL;
    public final static String SCHEDULE_URL;

    private static final Pattern groupIdRE;
    private static final Pattern weeklyTableRE;
    private static final Pattern weekdayInfoRE;
    private static final Pattern weekdayClassesRE;
    private static final Pattern classInfoRE;
    private static final Pattern extraInfoRe;

    static {
        ID_QUERY_URL = "https://urfu.ru/api/schedule/groups/suggest/?query=";
        SCHEDULE_URL = "https://urfu.ru/ru/students/study/schedule/#student/";

        groupIdRE = Pattern.compile("(?<=\"data\": )\\d*(?=})");
        weeklyTableRE = Pattern.compile("(?s)<table class=\"shedule-group-table\">.*?</table>");
        weekdayInfoRE = Pattern.compile("(?s)<tr class=\"divide\">.*?<b>(?<date>.*?)</b>(?<schedule>.*?)<tr class=\"divide\">.*?</tr>");
        weekdayClassesRE = Pattern.compile("(?s)<tr class=\"shedule-weekday-row\">.*?</tr>");
        classInfoRE = Pattern.compile("(?s)<td class=\"shedule-weekday-time\">(?<time>.*?)</td>.*?<dd>[ |\\n]*(?<order>\\d)\\.[ |\\n]*(?<name>.*?)[ |\\n]*</dd>*.*?<dt>(?<extraInfo>.*?)</dt>");
        extraInfoRe = Pattern.compile("(?s)<span class=\"teacher\">(.*?)</");
    }

    private static String getPageContent(String url) throws IOException {
        URLConnection connection = null;
        connection =  new URL(url).openConnection();
        Scanner scanner = new Scanner(connection.getInputStream());
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

    // TODO распарсить json
    private static String getWeekScheduleUrl(String group, Date date) throws IOException, InvalidGroupException {
        String idQueryResponse = getPageContent(ID_QUERY_URL + group);;
        Matcher matcher = groupIdRE.matcher(idQueryResponse);

        if (!matcher.find())
            throw new InvalidGroupException("Invalid group");

        String id = matcher.group(0);
        String dateFormatted = new SimpleDateFormat("yyyyMMdd").format(date);

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
        String url = getWeekScheduleUrl(group, date);
        String schedulePageContent = getPageContent(url);

        Matcher matcher = weeklyTableRE.matcher(schedulePageContent);
        if (!matcher.find())
            throw new IOException();
        String tableContent = matcher.group(0);
        return extractWeeklySchedule(tableContent);
    }



    // достаём все дни
    private static Object extractWeeklySchedule(String tableContent) {
        Matcher matcher = weekdayInfoRE.matcher(tableContent);
        ArrayList<ArrayList<ClassInfo>> weeklySchedule = new ArrayList<>();
        while (matcher.find()) { // информация только по одному дню недели
            String weekdayInfo = matcher.group(0);
            // String weekdayDate = matcher.group("date");
            ArrayList<ClassInfo> weekdaySchedule = extractWeekdaySchedule(weekdayInfo);
            weeklySchedule.add(weekdaySchedule);
        }
        return weeklySchedule;
    }

    // достаём все пары
    private static ArrayList<ClassInfo> extractWeekdaySchedule(String weekdayInfo) {
        ArrayList<ClassInfo> weekdaySchedule = new ArrayList<>();
        Matcher matcher = weekdayClassesRE.matcher(weekdayInfo);
        while (matcher.find()) {
            String classInfoRaw = matcher.group(0);
            ClassInfo classInfo = extractClassInfo(classInfoRaw); // TODO пиздец глаза выпали нахуй ))0)
            weekdaySchedule.add(classInfo);
        }
        return weekdaySchedule;
    }

    // достаём все детали в описании пары
    private static ClassInfo extractClassInfo(String classInfoRaw) {
        Matcher matcher = classInfoRE.matcher(classInfoRaw);
        if (matcher.find()) {
            String order = matcher.group("order");
            String time = matcher.group("time");
            String name = matcher.group("name");
            String extraInfo = formatRawExtraClassInfo(matcher.group("extraInfo"));

            return new ClassInfo(order, time, name, extraInfo);
        }
        else
            return null;
    }

    private static String formatRawExtraClassInfo(String rawExtraInfo) {
        Matcher matcher = extraInfoRe.matcher(rawExtraInfo);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            result.append(matcher.group(1));
            result.append(". ");
        }
        return result.toString();
    }
}
