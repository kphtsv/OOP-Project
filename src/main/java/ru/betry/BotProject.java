package ru.betry;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;

public class BotProject {

    private static final String TOKEN = "1965393172:AAEk3ECo5L7pP87xiaUzJ0eUVSO9_36MSHs";
    private static final String USERNAME = "just_easly_bot";
    private TelegramBot bot;

    public BotProject() {
        bot = new TelegramBot(TOKEN);
    }

    public void start() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(it -> {
                bot.execute(new SendMessage(it.message().chat().id(), it.message().text()));
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    //any bot logic
}
