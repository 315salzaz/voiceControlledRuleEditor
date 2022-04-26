package org.openhab.binding.voicecontrolledruleeditor.internal.utils;

import java.util.stream.Stream;

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
            String configuredExpression = removeSpaces ? StringUtils.withoutSpaces(expression) : expression;
            String configuredListElement = removeSpaces ? StringUtils.withoutSpaces(stringList[i]) : stringList[i];

            int startIndex = configuredListElement.indexOf(configuredExpression);
            int matchLengthOfCurrent = 0;

            if (startIndex < 0) {
                continue;
            }

            for (int j = startIndex; j < configuredListElement.length() || j < configuredExpression.length(); j++) {

                if (configuredExpression.charAt(j - startIndex) == configuredListElement.charAt(j)) {
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

    public static String getSpecificFromArray(String[] array, String specific) {
        return Stream.of(array).filter(a -> specific.contains(a)).findFirst().orElse(null);
    }
}
