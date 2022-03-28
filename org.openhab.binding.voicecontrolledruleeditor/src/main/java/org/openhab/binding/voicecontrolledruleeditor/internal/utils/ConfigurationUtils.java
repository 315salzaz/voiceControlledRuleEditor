package org.openhab.binding.voicecontrolledruleeditor.internal.utils;

import java.util.Arrays;
import java.util.stream.Stream;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ConfigurationType;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.UserInputs;
import org.openhab.core.thing.ThingStatus;

public class ConfigurationUtils {

    private static String getTimeValue(String valueString) {

        String hours;
        String minutes;

        try {
            if (valueString.contains("hours") && valueString.contains("minutes")) {
                var splits = valueString.split(" ");

                hours = splits[0];
                minutes = splits[2];
            } else if (valueString.contains(":")) {
                // 315salzaz not handled case: 5 p.m. (split may f- this up)
                var splits = valueString.split(":");
                var splitB = splits[1];

                if (valueString.contains("a.m.") || valueString.contains("p.m."))
                    splitB = splitB.split(" ")[0];

                hours = splits[0];
                minutes = splitB;
            } else {
                int valueNumber = Integer.parseInt(valueString);
                hours = "" + valueNumber / 100;
                minutes = "" + valueNumber % 100;
            }
        } catch (Exception e) {
            return null;
        }

        return hours + ":" + minutes;
    }

    private static Integer tryParseInt(String value) {
        Integer parsedValue;
        try {
            parsedValue = Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }

        return parsedValue;
    }

    // 315salzaz transfer to thing utils?
    private static String getThingStatus(String value) {
        if (!Arrays.stream(ThingStatus.values()).anyMatch(ts -> ts.name().equalsIgnoreCase(value))) {
            return null;
        }

        return value.toUpperCase();
    }

    private static String tryParseOperator(String value) {
        String newValue = null;
        if (value.contains("equal") || value.contains("=")) {
            newValue = "=";
        }

        if (value.contains("less") || value.contains("<")) {
            if (newValue.equals("=")) {
                return "<=";
            }

            return "<";
        }

        if (value.contains("greater") || value.contains("more") || value.contains(">")) {
            if (newValue.equals("=")) {
                return ">=";
            }
            return ">";
        }

        return newValue;
    }

    private static String tryParseDayOfWeek(String value) {
        if (value.equals("monday")) {
            return "MON";
        }
        if (value.equals("tuesday")) {
            return "TUE";
        }
        if (value.equals("wednesday")) {
            return "WED";
        }
        if (value.equals("thursday")) {
            return "THU";
        }
        if (value.equals("friday")) {
            return "FRI";
        }
        if (value.equals("saturday")) {
            return "SAT";
        }
        if (value.equals("sunday")) {
            return "SUN";
        }
        return null;
    }

    private static String tryParseEmail(String value) {
        String[] splits = value.split(" at ");
        String usernamePart = StringUtils.withoutSpaces(splits[0]);
        String domailPart = StringUtils.withoutSpaces(splits[1]);

        if (!domailPart.contains(".") || domailPart.endsWith(".")) {
            return null;
        }

        return usernamePart + "@" + domailPart;
    }

    private static String tryParseAudioFileName(String value) {
        String[] audioFileNames = Stream.of(AudioManagerUtils.getAudioFiles()).map(f -> f.getName())
                .toArray(String[]::new);

        String[] filteredNames = Stream.of(audioFileNames).filter(n -> n.contains(value)).toArray(String[]::new);

        if (filteredNames.length == 0) {
            return null;
        }

        // 315salzaz Transfer to StringUtils
        String longestMatchString = "";
        int longestMatchLength = 0;
        for (int i = 0; i < filteredNames.length; i++) {
            int startIndex = filteredNames[i].indexOf(value);
            int matchLengthOfCurrent = 0;

            for (int j = startIndex; j < filteredNames[i].length(); j++) {
                if (value.charAt(j - startIndex) == filteredNames[i].charAt(j)) {
                    matchLengthOfCurrent++;
                } else {
                    break;
                }
            }

            if (matchLengthOfCurrent > longestMatchLength) {
                longestMatchString = filteredNames[i];
                longestMatchLength = matchLengthOfCurrent;
            }
        }

        return longestMatchString;
    }

    private static ConfigurationResult getConfigurationResult(ConfigurationType configurationType, String commandString,
            String userInputConst) {
        int splitIndex = commandString.indexOf(userInputConst) + userInputConst.length();
        // 315salzaz what is valueString if nothing is inputed as value?
        String valueString = commandString.substring(splitIndex);
        Object value;
        switch (configurationType) {
            case START_LEVEL:
            case OFFSET:
                value = tryParseInt(valueString);
                break;
            case TIME:
            case START_TIME:
            case END_TIME:
                value = getTimeValue(valueString);
                break;
            case COMMAND:
            case STATE:
            case TEXT:
            case MESSAGE:
            case PREVIOUS_STATE:
                value = valueString;
                break;
            case ITEM_NAME:
                value = ItemUtils.getItemFromNameOrLabel(valueString).getName();
                break;
            case THING_ID:
                value = ThingUtils.getThingFromLabel(valueString).getUID().getAsString();
                break;
            case STATUS:
            case PREVIOUS_STATUS:
                value = getThingStatus(valueString);
                break;
            case CONSIDER_CONDITIONS:
            case ENABLE:
                value = Boolean.parseBoolean(valueString);
            case OPERATOR:
                value = tryParseOperator(valueString);
            case DAY_OF_WEEK:
                value = tryParseDayOfWeek(valueString);
            case USER_ID:
                value = tryParseEmail(valueString);
            case SOUND:
                value = tryParseAudioFileName(valueString);
                // 315salzaz group name postponed. Don't know how to get stuff from model or sth...
                // 315salzaz event postponed. Cant get chanels to work
            case GROUP_NAME:
            case EVENT:

                // 315salzaz newly added
            case RULE_IDS:
                // get rule from name or label???
            case SINK:
                // 315salzaz let's hope i don't need it yet (will fill in default)

            default:
                value = null;
                break;
        }

        return new ConfigurationResult(configurationType.type, value);
    }

    public static ConfigurationResult extractConfigurationFromCommand(String commandString) {
        if (UserInputs.contains(UserInputs.CONFIGURE_TIME, commandString))
            return getConfigurationResult(ConfigurationType.TIME, commandString, UserInputs.CONFIGURE_TIME);
        if (UserInputs.contains(UserInputs.CONFIGURE_THING_ID, commandString))
            return getConfigurationResult(ConfigurationType.THING_ID, commandString, UserInputs.CONFIGURE_THING_ID);
        if (UserInputs.contains(UserInputs.CONFIGURE_START_LEVEL, commandString))
            return getConfigurationResult(ConfigurationType.START_LEVEL, commandString,
                    UserInputs.CONFIGURE_START_LEVEL);
        if (UserInputs.contains(UserInputs.CONFIGURE_STATUS, commandString))
            return getConfigurationResult(ConfigurationType.STATUS, commandString, UserInputs.CONFIGURE_STATUS);
        if (UserInputs.contains(UserInputs.CONFIGURE_PREVIOUS_STATUS, commandString))
            return getConfigurationResult(ConfigurationType.PREVIOUS_STATUS, commandString,
                    UserInputs.CONFIGURE_PREVIOUS_STATUS);
        if (UserInputs.contains(UserInputs.CONFIGURE_EVENT, commandString))
            return getConfigurationResult(ConfigurationType.EVENT, commandString, UserInputs.CONFIGURE_EVENT);
        if (UserInputs.contains(UserInputs.CONFIGURE_ITEM_NAME, commandString))
            return getConfigurationResult(ConfigurationType.ITEM_NAME, commandString, UserInputs.CONFIGURE_ITEM_NAME);
        if (UserInputs.contains(UserInputs.CONFIGURE_STATE, commandString))
            return getConfigurationResult(ConfigurationType.STATE, commandString, UserInputs.CONFIGURE_STATE);
        if (UserInputs.contains(UserInputs.CONFIGURE_PREVIOUS_STATE, commandString))
            return getConfigurationResult(ConfigurationType.PREVIOUS_STATE, commandString,
                    UserInputs.CONFIGURE_PREVIOUS_STATE);
        if (UserInputs.contains(UserInputs.CONFIGURE_GROUP_NAME, commandString))
            return getConfigurationResult(ConfigurationType.GROUP_NAME, commandString, UserInputs.CONFIGURE_GROUP_NAME);
        if (UserInputs.contains(UserInputs.CONFIGURE_COMMAND, commandString))
            return getConfigurationResult(ConfigurationType.COMMAND, commandString, UserInputs.CONFIGURE_COMMAND);
        if (UserInputs.contains(UserInputs.CONFIGURE_ENABLE, commandString))
            return getConfigurationResult(ConfigurationType.ENABLE, commandString, UserInputs.CONFIGURE_ENABLE);
        if (UserInputs.contains(UserInputs.CONFIGURE_ADD_RULE_ID, commandString))
            return getConfigurationResult(ConfigurationType.RULE_IDS, commandString, UserInputs.CONFIGURE_ADD_RULE_ID);
        if (UserInputs.contains(UserInputs.CONFIGURE_CONSIDER_CONDITIONS, commandString))
            return getConfigurationResult(ConfigurationType.CONSIDER_CONDITIONS, commandString,
                    UserInputs.CONFIGURE_CONSIDER_CONDITIONS);
        if (UserInputs.contains(UserInputs.CONFIGURE_AUDIO_OUTPUT, commandString))
            return getConfigurationResult(ConfigurationType.SINK, commandString, UserInputs.CONFIGURE_AUDIO_OUTPUT);
        if (UserInputs.contains(UserInputs.CONFIGURE_SOUND, commandString))
            return getConfigurationResult(ConfigurationType.SOUND, commandString, UserInputs.CONFIGURE_SOUND);
        if (UserInputs.contains(UserInputs.CONFIGURE_TEXT, commandString))
            return getConfigurationResult(ConfigurationType.TEXT, commandString, UserInputs.CONFIGURE_TEXT);
        if (UserInputs.contains(UserInputs.CONFIGURE_MESSAGE, commandString))
            return getConfigurationResult(ConfigurationType.MESSAGE, commandString, UserInputs.CONFIGURE_MESSAGE);
        if (UserInputs.contains(UserInputs.CONFIGURE_USER_EMAIL, commandString))
            return getConfigurationResult(ConfigurationType.USER_ID, commandString, UserInputs.CONFIGURE_USER_EMAIL);
        if (UserInputs.contains(UserInputs.CONFIGURE_ADD_DAY_OF_WEEK, commandString))
            return getConfigurationResult(ConfigurationType.DAY_OF_WEEK, commandString,
                    UserInputs.CONFIGURE_ADD_DAY_OF_WEEK);
        if (UserInputs.contains(UserInputs.CONFIGURE_START_TIME, commandString))
            return getConfigurationResult(ConfigurationType.START_TIME, commandString, UserInputs.CONFIGURE_START_TIME);
        if (UserInputs.contains(UserInputs.CONFIGURE_END_TIME, commandString))
            return getConfigurationResult(ConfigurationType.END_TIME, commandString, UserInputs.CONFIGURE_END_TIME);
        if (UserInputs.contains(UserInputs.CONFIGURE_OPERATOR, commandString))
            return getConfigurationResult(ConfigurationType.OPERATOR, commandString, UserInputs.CONFIGURE_OPERATOR);
        if (UserInputs.contains(UserInputs.CONFIGURE_OFFSET, commandString))
            return getConfigurationResult(ConfigurationType.OFFSET, commandString, UserInputs.CONFIGURE_OFFSET);

        return null;
    }
}
