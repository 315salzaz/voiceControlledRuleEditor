package org.openhab.binding.voicecontrolledruleeditor.internal.utils;

public class StringUtils {
    public static String withoutSpaces(String string) {
        return string.replaceAll("\\s+", "");
    }
}
