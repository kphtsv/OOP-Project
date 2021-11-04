package ru.betry;

import java.util.regex.Pattern;

public class ClassInfo {
    public String order;
    public String time;
    public String name;
    public String extraInfo;

    private static final Pattern classInfoRE = Pattern.compile("(?s)<td class=\"shedule-weekday-time\">(?<time>.*?)</td>.*?<dd>[ |\\n]*(?<order>\\d)\\.[ |\\n]*(?<name>.*?)[ |\\n]*</dd>*.*?<dt>(?<extraInfo>.*?)</dt>");
    private static final Pattern extraInfoRe = Pattern.compile("(?s)<span class=\"teacher\">(.*?)</");
    private final static String TO_STRING_FORMAT_TEMPLATE = "__%s. %s (%s)__\n%s";

    public ClassInfo(String order, String time, String name, String extraInfo) {
        this.order = order;
        this.time = time;
        this.name = name;
        this.extraInfo = extraInfo;
    }

    public static ClassInfo Create(String rawInfo) {
        var matcher = classInfoRE.matcher(rawInfo);
        if (matcher.find()) {
            var order = matcher.group("order");
            var time = matcher.group("time");
            var name = matcher.group("name");
            var extraInfo = formatRawExtraClassInfo(matcher.group("extraInfo"));

            return new ClassInfo(order, time, name, extraInfo);
        }
        else
            return null;
    }

    private static String formatRawExtraClassInfo(String rawExtraInfo) {
        var matcher = extraInfoRe.matcher(rawExtraInfo);
        var result = new StringBuilder();
        while (matcher.find()) {
            result.append(matcher.group(1));
            result.append(". ");
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return String.format(TO_STRING_FORMAT_TEMPLATE, order, name, time, extraInfo);
    }
}

