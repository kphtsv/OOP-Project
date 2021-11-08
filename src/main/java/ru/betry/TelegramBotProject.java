package ru.betry;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


//todo:
public class TelegramBotProject {

    private static String TOKEN = "2047247271:AAHVSjz9DwVwHcNJG2BkqqPUZVAPYIoHUuQ";
    private static String USERNAME = "javaoop2021bot";
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
                        BotType.Telegram,
                        it.message().chat().username());

                String[] messages = logic.getAnswerForUser(chatInfo);

                for (String message : messages)

                    bot.execute(new SendMessage(it.message().chat().id(),
                            new String(message.getBytes(Charset.forName("cp1251")), StandardCharsets.UTF_8))
                            .parseMode(ParseMode.Markdown));
                chatInfo.updateToDataBase();
            });

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
