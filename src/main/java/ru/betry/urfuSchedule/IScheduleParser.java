package ru.betry.urfuSchedule;

import java.io.IOException;

public interface IScheduleParser {
    public String[] extractSchedule(String tableContent, int daysAhead) throws IOException;
}
