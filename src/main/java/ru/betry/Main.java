package ru.betry;

public class Main {
    public static void main(String[] args) {
        TelegramBotProject myTelegramBot = new TelegramBotProject();
        myTelegramBot.run();

//        try {
//            var group = "лем-200208";
//            // var date = new Date(2021, Calendar.OCTOBER,26);
//            var date = new Date();
//            var link = UrfuScheduleApi.getScheduleUrl(group, date);
//            System.out.println(link);
//
//            // new Date(2021, Calendar.OCTOBER,26)
//            var schedule = UrfuScheduleService.getScheduleByGroup("лем-200208", date, 3);
//            for (var day: schedule) {
//                System.out.println(day);
//                System.out.println();
//            }
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }
}
