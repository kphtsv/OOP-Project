package ru.betry;

//todo: реализовать ввод группы и состояния

import java.util.Arrays;
import java.util.Objects;

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

    public String[] getNotCommandMessage(ChatInfoClass chatInfo) {
        if (Objects.equals(chatInfo.getState(), "ready")) {
            return getEchoMessage("Your message is " + chatInfo.getUserMessage());
        } else {
            return getSetGroup(chatInfo);
        }
    }

    public String[] getRestart(ChatInfoClass chatInfo) {
        chatInfo.restartChat();
        chatInfo.setState("groupRequest");
        return new String[] {"Bot restarted.", "Please type your study group in format MEH-******"};
    }

    public String[] getChangeGroup(ChatInfoClass chatInfo) {
        chatInfo.restartChat();
        return getSetGroup(chatInfo);
    }

    public String[] getSchedule(ChatInfoClass chatInfo) {
        if (Objects.equals(chatInfo.getState(), "ready")) {
            //todo: написать вывод расписания
            return new String[] {"Not working yet"};
        } else {
            return getSetGroup(chatInfo);
        }
    }

    private String[] getSetGroup(ChatInfoClass chatInfo) {
        switch (chatInfo.getState()) {
            case "new": {
                chatInfo.setState("groupRequest");
                return new String[]{"Please type your study group in format MEH-******"};
            }
            case "groupRequest": {
                if (isGroupCorrect(chatInfo.getUserMessage())) {
                    chatInfo.setStudyGroup(chatInfo.getUserMessage());
                    chatInfo.setState("ready");
                    return new String[] {"Group successfully selected"};
                } else {
                    return new String[] {"Invalid group entered"};
                }
            }
            default:
                return new String[] {"Group already entered"};
        }
    }

    private String[] getEchoMessage(String message){
        return new String[] {message};
    }

    private boolean isGroupCorrect(String group){
        //todo: написать сверку с существующими группами
        return true;
    }
}
