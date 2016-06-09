package com.smartbear.readyapi.testserver.cucumber.builders;

public class Support {
    public static String expandFormat(String format) {
        if (format.indexOf('/') == -1) {
            return "application/" + format;
        }

        return format;
    }
}
