package ru.betry;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;


public class TelegramBotProject implements IBotClass {

    private static String TOKEN = "1965393172:AAEk3ECo5L7pP87xiaUzJ0eUVSO9_36MSHs";
    private static String USERNAME = "just_easly_bot";
    private TelegramBot bot;
    private ParserToBotCore logic;

    public TelegramBotProject()
    {
        bot = new TelegramBot(TOKEN);
        logic = new ParserToBotCore();
    }

    public TelegramBotProject(String TOKEN, String USERNAME)
    {
        this.TOKEN = TOKEN;
        this.USERNAME = USERNAME;
        bot = new TelegramBot(TOKEN);
        logic = new ParserToBotCore();
    }

    public void run() {
        bot.setUpdatesListener(updates -> {
            System.out.println(updates);
            updates.forEach(it -> {

                ChatInfoClass chatInfo = new ChatInfoClass(
                        it.message().chat().id().toString(),
                        it.message().text(),
                        BotType.Telegram);

                String[] messages = logic.getAnswerForUser(chatInfo);

                for (String message : messages)
                    bot.execute(new SendMessage(it.message().chat().id(), message));
            });

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
