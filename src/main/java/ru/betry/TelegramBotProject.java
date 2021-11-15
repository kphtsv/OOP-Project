package ru.betry;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class TelegramBotProject {

    private static String TOKEN = "2047247271:AAHVSjz9DwVwHcNJG2BkqqPUZVAPYIoHUuQ";
    private static String USERNAME = "javaoop2021bot";
    private TelegramBot bot;
    private ParserToBotCore logic;
    private DataTestCollectionRepository dataTestCollectionRepository;

    public TelegramBotProject()
    {
        bot = new TelegramBot(TOKEN);
        logic = new ParserToBotCore();
        dataTestCollectionRepository = new DataTestCollectionRepository();
    }

    public TelegramBotProject(String TOKEN, String USERNAME)
    {
        this.TOKEN = TOKEN;
        this.USERNAME = USERNAME;
        bot = new TelegramBot(TOKEN);
        logic = new ParserToBotCore();
        dataTestCollectionRepository = new DataTestCollectionRepository();
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
                chatInfo.update(dataTestCollectionRepository.getItem(chatInfo.getChatId()));


                String[] messages = logic.getAnswerForUser(chatInfo);

                Keyboard replyKey = new ReplyKeyboardMarkup(
                        new KeyboardButton("/get_my_schedule")
                ).resizeKeyboard(true);


                for (String message : messages)

                    bot.execute(new SendMessage(it.message().chat().id(),
                            new String(message.getBytes(Charset.forName("cp1251")), StandardCharsets.UTF_8))
                            .parseMode(ParseMode.Markdown)
                            .replyMarkup(replyKey));

                dataTestCollectionRepository.checkAndPush(chatInfo.makeDocument());
            });

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
