package ru.betry;

public class BotCore {
    private static final String help = "It's a simple bot. Just have fun!";

    public String[] getHelp() {
        return new String[] {help};
    }

    public String[] getUnknownCommand() {
        return new String[] {"Unknown command"};
    }

    public String[] getNotSupportedType() {
        return new String[] {"This type of bot not supported now"};
    }

    public String[] getSimpleMessage(String userId, String userMessage) {
        return getEchoMessage(userMessage); //todo написать сущность обработчика
    }

    public String[] getRestart() {
        //todo потереть бд
        return new String[] {"Bot restarted.", help};
    }

    private String[] getEchoMessage(String userMessage){
        return new String[] {userMessage};
    }
}
