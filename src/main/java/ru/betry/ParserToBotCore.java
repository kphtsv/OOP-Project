package ru.betry;

import ru.betry.urfuSchedule.UrfuScheduleService;

import java.io.IOException;

enum BotType {
    Telegram,
    YandexAlice
}

enum CoreMessageType {
    help,
    unknown,
    notCommandMessage,
    notSupportedType,
    restart,
    changeGroup,
    getSchedule,
    getWeekSchedule,
    getTomorrowSchedule,
    getFreeRooms
}

public class ParserToBotCore {

    private final BotCore core;

    public ParserToBotCore(){
        core = new BotCore();
    }

    public String[] getAnswerForUser(ChatInfoClass chatInfo) {
        CoreMessageType messageType = parseMessageType(chatInfo.getUserMessage(), chatInfo.getBotType());

        return switch (messageType) {
            case help -> core.getHelp();
            case unknown -> core.getUnknownCommand();
            case notCommandMessage -> core.getNotCommandMessage(chatInfo);
            case restart -> core.getRestart(chatInfo);
            case changeGroup -> core.getChangeGroup(chatInfo);
            case getSchedule -> core.getSchedule(chatInfo);
            case getTomorrowSchedule -> core.getTomorrowSchedule(chatInfo);
            case getWeekSchedule -> core.getWeekSchedule(chatInfo);
            case getFreeRooms -> core.getFreeRooms(chatInfo);
            default -> core.getNotSupportedType();
        };
    }

    private CoreMessageType parseMessageType(String userMessage, BotType type) {
        switch (type) {
            case Telegram: {
                if (userMessage.startsWith("/"))
                    return getTelegramCommand(userMessage);
                else
                    return getTextCommand(userMessage);
            }
            default:
                return CoreMessageType.notSupportedType;
        }
    }

    private CoreMessageType getTextCommand(String userMessage) {
        //не забудь обновить и getAnswerForUser
        return switch (userMessage) {
            case ("Расписание") -> CoreMessageType.getSchedule;
            case ("Расписание на завтра") -> CoreMessageType.getTomorrowSchedule;
            case ("Расписание на неделю") -> CoreMessageType.getWeekSchedule;
            case ("Свободные кабинеты") -> CoreMessageType.getFreeRooms;
            default -> CoreMessageType.notCommandMessage;
        };
    }

    private CoreMessageType getTelegramCommand(String userCommand){
        //не забудь обновить и getAnswerForUser
        return switch (userCommand) {
            case ("/start"), ("/help") -> CoreMessageType.help;
            case ("/restart") -> CoreMessageType.restart;
            case ("/change_group") -> CoreMessageType.changeGroup;
            default -> CoreMessageType.unknown;
        };
    }
}
