package ru.betry;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;

public class TelegramBotProject {

    private static String TOKEN = "1965393172:AAEk3ECo5L7pP87xiaUzJ0eUVSO9_36MSHs";
    private static String USERNAME = "just_easly_bot";
    private TelegramBot bot;
    private BotLogic logic;

    public TelegramBotProject()
    {
        bot = new TelegramBot(TOKEN);
        logic = new BotLogic();
    }

    public TelegramBotProject(String TOKEN, String USERNAME)
    {
        this.TOKEN = TOKEN;
        this.USERNAME = USERNAME;
        bot = new TelegramBot(TOKEN);
        logic = new BotLogic();
    }

    public void run() {
        bot.setUpdatesListener(updates -> {
            System.out.println(updates);
            updates.forEach(it -> {
                bot.execute(
                    new SendMessage(it.message().chat().id(),
                    logic.getAnswerForUser(it.message().text(), BotType.Telegram))
                );
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    //any bot logic
}
