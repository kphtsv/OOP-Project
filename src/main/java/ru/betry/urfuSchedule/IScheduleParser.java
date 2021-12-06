package ru.betry.urfuSchedule;

import ru.betry.urfuSchedule.models.Weekday;

import java.io.IOException;

public interface IScheduleParser {
    String[] extractFormattedSchedule(String schedulePageContent, int daysAhead) throws IOException;
    Weekday[] extractSchedule(String schedulePageContent, int daysAhead) throws IOException;
    String extractCabinet(String extraInfo);
}
