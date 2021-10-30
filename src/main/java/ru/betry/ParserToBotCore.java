package ru.betry;

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
    getSchedule
}

public class ParserToBotCore {

    private BotCore core;

    public ParserToBotCore(){
        core = new BotCore();
    }

    public String[] getAnswerForUser(ChatInfoClass chatInfo) {
        CoreMessageType messageType = parseMessageType(chatInfo.getUserMessage(), chatInfo.getBotType());
        chatInfo.updateFromDataBase();

        return switch (messageType) {
            case help -> core.getHelp();
            case unknown -> core.getUnknownCommand();
            case notCommandMessage -> core.getNotCommandMessage(chatInfo);
            case restart -> core.getRestart(chatInfo);
            case changeGroup -> core.getChangeGroup(chatInfo);
            case getSchedule -> core.getSchedule(chatInfo);
            default -> core.getNotSupportedType();
        };
    }

    private CoreMessageType parseMessageType(String userMessage, BotType type) {
        switch (type) {
            case Telegram: {
                if (userMessage.startsWith("/"))
                    return getTelegramCommand(userMessage);
                else
                    return CoreMessageType.notCommandMessage;
            }
            default:
                return CoreMessageType.notSupportedType;
        }
    }

    private CoreMessageType getTelegramCommand(String userCommand){
        //незабудь обновить и getAnswerForUser
        switch (userCommand) {
            case ("/help"):
                return CoreMessageType.help;
            case ("/restart"):
                return CoreMessageType.restart;
            case ("/change_group"):
                return CoreMessageType.changeGroup;
            case ("/get_my_schedule"):
                return CoreMessageType.getSchedule;
            default:
                return CoreMessageType.unknown;
        }
    }
}
