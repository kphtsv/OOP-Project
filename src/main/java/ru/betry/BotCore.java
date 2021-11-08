package ru.betry;

import ru.betry.urfuSchedule.UrfuScheduleApi;
import ru.betry.urfuSchedule.UrfuScheduleService;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class BotCore {
    private static final String helpEng = "It's a simple bot. Just have fun!";
    private static final String help = "Справка:\n  Этот бот написан для удобного доступа к расписанию УрФУ";
    //todo: дописать справку
    public String[] getHelp() {
        return new String[] {help};
    }

    public String[] getUnknownCommand() {
        return new String[] {"Неизвестная команда"};
    }

    public String[] getNotSupportedType() {
        return new String[] {"Этот тип бота ещё не поддерживается"};
    }

    public String[] getNotCommandMessage(ChatInfoClass chatInfo) {
        if (Objects.equals(chatInfo.getState(), "ready")) {
            return getEchoMessage("Ваше сообщение: " + "_" + chatInfo.getUserMessage() + "_");
        } else {
            return getSetGroup(chatInfo);
        }
    }

    public String[] getRestart(ChatInfoClass chatInfo) {
        chatInfo.restartChat();
        chatInfo.setState("groupRequest");
        return new String[] {"Бот перезапущен.", "Пожалуйста введите свою учебную группу в формате МЕН-******"};
    }

    public String[] getChangeGroup(ChatInfoClass chatInfo) {
        chatInfo.restartChat();
        return getSetGroup(chatInfo);
    }

    public String[] getSchedule(ChatInfoClass chatInfo) {
        if (Objects.equals(chatInfo.getState(), "ready")) {
            var group = chatInfo.getStudyGroup();
            String[] schedule = new String[0];
            try {
                schedule = UrfuScheduleService.getScheduleByGroup(group, new Date(), 1);
            } catch (UrfuScheduleService.InvalidGroupException e) {
                System.out.println("Такого не должно было быть, но группа неверная");
            } catch (IOException e) {
                System.out.println("IOException! Не знаю, что это значит.");
            }

            return schedule;
        } else {
            return getSetGroup(chatInfo);
        }
    }

    private String[] getSetGroup(ChatInfoClass chatInfo) {
        switch (chatInfo.getState()) {
            case "new": {
                chatInfo.setState("groupRequest");
                return new String[]{"Пожалуйста введите свою учебную группу в формате МЕН-******"};
            }
            case "groupRequest": {
                if (UrfuScheduleApi.isGroupCorrect(chatInfo.getUserMessage())) {
                    chatInfo.setStudyGroup(chatInfo.getUserMessage());
                    chatInfo.setState("ready");
                    return new String[] {"Группа успешно выбрана"};
                } else {
                    return new String[] {"Введена неверная группа"};
                }
            }
            default:
                return new String[] {"Группа уже введена"};
        }
    }

    private String[] getEchoMessage(String message){
        return new String[] {message};
    }

}
