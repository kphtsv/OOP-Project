package ru.betry;

<<<<<<< Updated upstream
public class BotCore {
    private static final String help = "It's a simple bot. Just have fun!";

=======
import ru.betry.urfuSchedule.UrfuScheduleService;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class BotCore {
    private final UrfuScheduleService service = new UrfuScheduleService();

    private static final String helpEng = "It's a simple bot. Just have fun!";
    private static final String help = "Справка:\n  Этот бот написан для удобного доступа к расписанию УрФУ";
    //todo: дописать справку
>>>>>>> Stashed changes
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
        return getEchoMessage(userMessage); //написать сущность обработчика
    }

    public String[] getRestart() {
        //потереть бд
        return new String[] {"Bot restarted.", help};
    }

<<<<<<< Updated upstream
    private String[] getEchoMessage(String userMessage){
        return new String[] {userMessage};
    }
=======
    public String[] getChangeGroup(ChatInfoClass chatInfo) {
        chatInfo.restartChat();
        return getSetGroup(chatInfo);
    }

    public String[] getSchedule(ChatInfoClass chatInfo) {
        if (Objects.equals(chatInfo.getState(), "ready")) {
            var group = chatInfo.getStudyGroup();
            String[] schedule = new String[0];
            try {
                schedule = service.getScheduleByGroup(group, new Date(), 1);
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
                if (service.api.isGroupCorrect(chatInfo.getUserMessage())) {
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

>>>>>>> Stashed changes
}
