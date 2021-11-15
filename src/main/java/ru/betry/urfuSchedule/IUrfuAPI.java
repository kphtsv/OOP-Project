package ru.betry.urfuSchedule;

import java.io.IOException;
import java.util.Date;

public interface IUrfuAPI {
    boolean isGroupCorrect(String group);
    String getSchedulePageContent(String group, Date date) throws UrfuScheduleService.InvalidGroupException, IOException;
}
