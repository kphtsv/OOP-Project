package ru.betry.urfuSchedule;

import java.io.IOException;
import java.util.Date;

public interface IUrfuAPI {
    String getSchedulePageContent(String group, Date date) throws UrfuScheduleService.InvalidGroupException, IOException;
    boolean isGroupCorrect(String group);
}
