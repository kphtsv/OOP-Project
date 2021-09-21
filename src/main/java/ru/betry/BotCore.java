package ru.betry;

public class BotCore {
    private static final String help = "It's a simple bot. Just have fun!";

    public String getHelp() {
        return help;
    }

    public String getUnknownCommand() {
        return "Unknown command";
    }

    public String getNotSupportedType() {
        return "This type of bot not supported now";
    }

    public String getSimpleMessage(String userId, String userMessage) {
        return getEchoMessage(userMessage); //написать сущность обработчика
    }

    public String getRestart() {
        //потереть бд
        return "Bot restarted.\n" + help;
    }

    private String getEchoMessage(String userMessage){
        return userMessage;
    }
}
