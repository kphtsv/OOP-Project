package ru.betry.urfuSchedule.models;

import java.util.ArrayList;

public class Weekday {
    public String name;
    public String date;
    public ArrayList<ClassInfo> classes;

    public Weekday(String name, String date, ArrayList<ClassInfo> classes) {
        this.name = name;
        this.date = date;
        this.classes = classes;
    }

    @Override
    public String toString() { // todo незачем вызывать каждый раз у неизменяемых
        var sb = new StringBuilder();
        sb.append(String.format("**%s, %s:**", name, date));
        for (var classInfo: classes) {
            sb.append("\n");
            sb.append(classInfo);
        }
        return sb.toString();
    }
}
