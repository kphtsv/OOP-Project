package ru.betry.urfuSchedule.models;

public class ClassInfo {
    public String order;
    public String time;
    public String name;
    public String extraInfo;

    private final static String TO_STRING_FORMAT_TEMPLATE = "_%s. %s (%s)_\n%s";

    public ClassInfo(String order, String time, String name, String extraInfo) {
        this.order = order;
        this.time = time;
        this.name = name;
        this.extraInfo = extraInfo;
    }

    @Override
    public String toString() {
        return String.format(TO_STRING_FORMAT_TEMPLATE, order, name, time, extraInfo);
    }
}

