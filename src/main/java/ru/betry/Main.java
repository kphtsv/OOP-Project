package ru.betry;

import com.google.gson.GsonBuilder;

public class Main {
    public static void main(String[] args)
        {
            /*var gson = new GsonBuilder().setPrettyPrinting().create();
            var str = "{\"foo\":123, \"bar\":432}";
            var res = gson.fromJson(str, A.class);
            System.out.println(res.foo);*/

//            TelegramBotProject myTelegramBot = new TelegramBotProject();
//            myTelegramBot.run();

            var gson = new GsonBuilder().setPrettyPrinting().create();
            var str = "{\"suggestions\": [{\"data\": 986803, \"value\": \"\\u041c\\u0415\\u041d-200208\"}]}";
            var response = gson.fromJson(str, IdQueryResponse.class);
            System.out.println(response.suggestions.get(0).data);
        }
}

