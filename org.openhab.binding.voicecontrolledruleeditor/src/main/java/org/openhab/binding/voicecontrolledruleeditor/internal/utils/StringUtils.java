package org.openhab.binding.voicecontrolledruleeditor.internal.utils;

public class StringUtils {
    public static String withoutSpaces(String string) {
        return string.replaceAll("\\s+", "");
    }

    public static String longestMatching(String[] stringList, String expression) {
        return longestMatching(stringList, expression, false);
    }

    public static String longestMatching(String[] stringList, String expression, boolean removeSpaces) {
        String longestMatchString = null;
        int longestMatchLength = 0;

        for (int i = 0; i < stringList.length; i++) {
            int startIndex = stringList[i].indexOf(expression);
            int matchLengthOfCurrent = 0;

            for (int j = startIndex; j < stringList[i].length(); j++) {
                if (expression.charAt(j - startIndex) == stringList[i].charAt(j)) {
                    matchLengthOfCurrent++;
                } else {
                    break;
                }
            }

            if (matchLengthOfCurrent > longestMatchLength) {
                longestMatchString = stringList[i];
                longestMatchLength = matchLengthOfCurrent;
            }
        }

        return longestMatchString;
    }
}
