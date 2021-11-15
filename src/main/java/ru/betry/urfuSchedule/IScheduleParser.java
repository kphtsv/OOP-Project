package ru.betry.urfuSchedule;

import java.io.IOException;

public interface IScheduleParser {
    public String[] extractSchedule(String schedulePageContent, int daysAhead) throws IOException;
}
