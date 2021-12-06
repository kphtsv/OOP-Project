package ru.betry.urfuSchedule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class MathMech {
    // private HashSet<String> AUDIENCES; // TODO: дописать аудитории на 5 этаже и убрать те, в которых не проводятся пары
    private static final String[] AUDIENCES = new String[] {"601", "605", "607", "609", "611", "613", "615", "617", "621", "623",
            "625", "612", "614", "616", "618", "620", "622", "622а", "624", "626", "628", "630", "632", "634", "636",
            "638", "640", "515"};

    public HashMap<String, String> groups = new HashMap<>(); // 'КН-203' -> '200208' (пример)

    public MathMech() {
        initializeGroups();
        // initializeCabinets();
    }

    // TODO пофиксить проблему с нахождением файла
    private void initializeGroups() {
        try {
//            var a = Paths.get(".").toAbsolutePath();
//            System.out.println(a);
            // TODO как бы это ни было тупо, я не могу разобраться с тем, как прочитать файл, ибо Java его не видит
            // а ещё почему-то текущая директория всегда ведет только до папки с проектом
            var directory = "src\\main\\java\\ru\\betry\\urfuSchedule\\";

            for (var line : Files.readAllLines(Paths.get("groups.txt"))) {
                var info = line.split(" ");
                var urfuNotion = info[0];
                var mmNotion = info[1]; // пока что не должно использоваться

                groups.put(mmNotion, urfuNotion);
            }
        } catch (IOException e) { // если не читаются строки в файле
            e.printStackTrace();
        }
    }

//    private void initializeCabinets() {
//        // реализуется более корректно в сервисе, getAllCabinets(date)
//        AUDIENCES = new HashSet<>(Arrays.asList(audiences));
//    }

    // возвращает новый HashSet каждый раз для того, чтобы можно было убирать из него элементы без последствий
    // используется для подсчёта свободных аудиторий
    public static HashSet<String> getClonedAudiences() {
        return new HashSet<>(Arrays.asList(AUDIENCES));
    }
}
