package ru.betry.urfuSchedule;

import com.google.gson.GsonBuilder;
import ru.betry.urfuSchedule.models.IdQueryResponse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class UrfuScheduleApi implements IUrfuAPI {
    public final static String ID_QUERY_URL;
    public final static String SCHEDULE_URL;

    static {
        ID_QUERY_URL = "https://urfu.ru/api/schedule/groups/suggest/?query=";
        SCHEDULE_URL = "https://urfu.ru/api/schedule/groups/lessons/";
    }

<<<<<<< Updated upstream
    public static boolean isGroup(String group) {
=======
    public boolean isGroupCorrect(String group) {
>>>>>>> Stashed changes
        try {
            getGroupId(group);
            return true;
        } catch (UrfuScheduleService.InvalidGroupException | IOException e) {
            return false;
        }
    }

<<<<<<< Updated upstream
    public static String getSchedulePageContent(String group, Date date) throws UrfuScheduleService.InvalidGroupException, IOException {
        return getPageContent(getScheduleUrl(group, date));
    }

    public static String getPageContent(URL url) throws IOException {
=======
    private String getPageContent(URL url) throws IOException {
>>>>>>> Stashed changes
        var connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        connection.connect();

        var scanner = new Scanner(connection.getInputStream());
        scanner.useDelimiter("\\Z");
        var content = scanner.next();
        scanner.close();

        return content;
    }

    private URL getQueryURL(String group) throws MalformedURLException {
        return new URL(ID_QUERY_URL + URLEncoder.encode(group, StandardCharsets.UTF_8));
    }

    private int getGroupId(String group) throws IOException, UrfuScheduleService.InvalidGroupException {
        var idQueryResponse = getPageContent(getQueryURL(group));

        var gson = new GsonBuilder().setPrettyPrinting().create();
        var response = gson.fromJson(idQueryResponse, IdQueryResponse.class);

        if (response.suggestions.size() < 1)
            throw new UrfuScheduleService.InvalidGroupException("Invalid group.");

        return response.suggestions.get(0).data;
    }

    private URL getScheduleUrl(String group, Date date) throws IOException, UrfuScheduleService.InvalidGroupException {
        var id = getGroupId(group);
        var dateFormatted = new SimpleDateFormat("yyyyMMdd").format(date);

        return new URL(SCHEDULE_URL + id + "/" + dateFormatted + "/");
    }
<<<<<<< Updated upstream
=======

    public String getSchedulePageContent(String group, Date date) throws UrfuScheduleService.InvalidGroupException, IOException {
        return getPageContent(getScheduleUrl(group, date));
    }
>>>>>>> Stashed changes
}
