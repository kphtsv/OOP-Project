package ru.betry;


enum BotType{
    Telegram,
    YandexAlise
}

public class BotLogic {
    private static String TelegramHelp = "It's a simple bot. Just have fun!";  //написать нормальную справку

    public String getAnswerForUser(String userMessage, BotType type) {
        switch (type) {
            case Telegram: {
                if (userMessage.charAt(0) == '/')
                    return getTelegramCommand(userMessage);
                else
                    return getSimpleMessage(userMessage);
            }
            default:
                return "This type of bot not supported now";
        }
    }

    private String getTelegramCommand(String userCommand){
        switch (userCommand.substring(1)) {
            case ("help"):
                return TelegramHelp;
            default:
                return "Unknown Command";
        }
    }

    private String getSimpleMessage(String userMessage){
        return getEchoMessage(userMessage); //пока так
    }

    private String getEchoMessage(String userMessage){
        return userMessage;
    }
}
