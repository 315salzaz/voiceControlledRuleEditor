package org.openhab.binding.voicecontrolledruleeditor.internal.constants;

import java.util.Arrays;

import org.openhab.binding.voicecontrolledruleeditor.internal.utils.StringUtils;

public class UserInputs {
    public static String STATUS_REPORT = "status report";
    public static String TELL_ME_HOW = "tell me how";

    public static String[] CREATE_NEW_RULE_ARR = { "create", "new" };
    public static String[] RENAME_RULE = { "rename", " rule" };
    public static String[] BEGIN_EDITING = { "begin", "editing" };
    public static String[] CONFIRM_ARRAY = { "okay", "confirm", "yes" };
    public static String[] DENY_ARRAY = { "deny", "denied", "reject", "no", "refuse" };
    public static String CANCEL = "cancel";
    public static String BACK = "back";

    public static String[] CREATE_ARR = { "create", "add", "build" };
    public static String EDIT = "edit";
    public static String[] REMOVE_ARR = { "delete", "remove" };
    public static String RENAME = "rename";
    public static String TRIGGER = "trigger";
    public static String ACTION = "action";
    public static String CONDITION = "condition";

    // Trigger types:
    // System:
    public static String ON_SYSTEM_START_LEVEL = "on system start level";
    // Thing:
    public static String ON_THING_STATUS_CHANGES = "on status changes for a thing";
    public static String[] ON_THING_STATUS_UPDATE_ARR = { "on status update for a thing",
            "on status updated for a thing" };
    public static String ON_CHANNEL_ACTIVATED = "on channel activated"; // a trigger channel fires
    // Item:
    public static String ON_ITEM_COMMAND_RECEIVED = "on item command received";
    public static String[] ON_ITEM_STATE_CHANGES_ARR = { "when item state is changed",
            "when an item state is changed" };
    public static String[] ON_ITEM_STATE_UPDATED_ARR = { "when item state is updated",
            "when an item state is updated" };
    // Group:
    public static String ON_GROUP_MEMBER_RECEIVES_COMMAND = "when a group member receives a command";
    public static String ON_GROUP_MEMBER_STATE_CHANGES = "when a group member state is changed";
    public static String ON_GROUP_MEMBER_STATE_UPDATED = "when a group member state is updated";
    // Time:
    // 315salzaz Cron is postponed
    public static String ON_CRON_TRIGGER = "on a chronological trigger";
    public static String ON_FIXED_TIME_OF_DAY = "on a fixed time of day";

    // Action types
    // Rule:
    public static String ACTION_RULE_ENABLEMENT = "do rule enamblement";
    public static String ACTION_RUN_RULE = "do run rule";
    // Item:
    public static String ACTION_ITEM_COMMAND = "do item command";
    public static String ACTION_ITEM_STATE_UPDATE = "do item state update";
    // Media
    public static String ACTION_MEDIA_PLAY = "do media play";
    public static String ACTION_MEDIA_SAY = "do media say";
    // Notification
    public static String ACTION_NOTIFICATION_SEND_BROADCAST = "do send notification broadcast";
    public static String ACTION_NOTIFICATION_SEND_LOG = "do send notification log";
    public static String ACTION_NOTIFICATION_SEND = "do send notification action";

    // Condition types
    // Time:
    public static String CONDITION_TIME_OF_DAY = "time of day condition";
    public static String CONDITION_DAY_OF_WEEK = "day of week";
    // Item:
    public static String CONDITION_ITEM_STATE = "item state condition";
    // Astrological:
    public static String CONDITION_HOLIDAY = "holiday condition";
    public static String CONDITION_NOT_HOLIDAY = "not holiday condition";
    public static String CONDITION_WEEKEND = "weekend condition";
    public static String CONDITION_WEEKDAY = "weekday condition";

    public static String[] CONFIGURE_ARR = { "set configuration", "configure" };

    public static String COMPLETE = "complete";

    public static String CONFIGURE_TIME = "set configuration of time to ";
    public static String CONFIGURE_START_TIME = "set configuration of starting time to ";
    public static String CONFIGURE_END_TIME = "set configuration of ending time to ";
    public static String CONFIGURE_OPERATOR = "configure operator ";
    public static String CONFIGURE_OFFSET = "offset by ";
    public static String CONFIGURE_THING_ID = "configure thing id from name ";
    public static String CONFIGURE_START_LEVEL = "set configuration of starting level ";
    public static String CONFIGURE_STATUS = "set configuration of status to ";
    public static String CONFIGURE_PREVIOUS_STATUS = "set configuration of previous status to ";
    public static String CONFIGURE_EVENT = "set configuration of event to ";
    public static String CONFIGURE_ITEM_NAME = "set configuration of item name to ";
    public static String CONFIGURE_STATE = "set configuration of state to ";
    public static String CONFIGURE_PREVIOUS_STATE = "set configuration of previous state to ";
    public static String CONFIGURE_GROUP_NAME = "set configuration of group name to ";
    public static String CONFIGURE_COMMAND = "set configuration of command to ";
    public static String CONFIGURE_ENABLE = "configure enable to ";
    // 315salzaz generalized ids is a good idea?
    public static String[] CONFIGURE_ADD_RULE_ID_ARR = { "add a rule with name ", "add a rule with description ",
            "add a rule called " };
    public static String[] CONFIGURE_REMOVE_RULE_ID_ARR = { "remove a rule called", "remove a rule with name ",
            "remove a rule with description " };
    public static String[] CONFIGURE_ADD_DAY_OF_WEEK_ARR = { "add weekday ", "add a weekday " };
    public static String[] CONFIGURE_REMOVE_DAY_OF_WEEK_ARR = { "remove weekday ", "remove a weekday " };
    public static String CONFIGURE_CONSIDER_CONDITIONS = "set consider conditions to ";
    public static String CONFIGURE_AUDIO_OUTPUT = "set audio output to number ";
    public static String CONFIGURE_SOUND = "set sound to number ";
    // 315salzaz could be combined
    public static String CONFIGURE_TEXT = "set text to ";
    public static String CONFIGURE_MESSAGE = "set message to ";
    public static String CONFIGURE_USER_EMAIL = "set user email to ";

    public static boolean isEquals(String userInputConst, String commandString) {
        return userInputConst.equals(commandString);
    }

    public static boolean isEquals(String[] userInputConst, String commandString) {
        return Arrays.stream(userInputConst).anyMatch(x -> isEquals(x, commandString));
    }

    public static boolean contains(String userInputConst, String commandString) {
        return commandString.contains(userInputConst);
    }

    public static boolean contains(String[] userInputConst, String commandString) {
        return Arrays.stream(userInputConst).anyMatch(x -> contains(x, commandString));
    }

    public static boolean beginsWith(String userInputConst, String commandString) {
        return commandString.indexOf(userInputConst) == 0;
    }

    public static boolean beginsWith(String[] userInputConst, String commandString) {
        return Arrays.stream(userInputConst).anyMatch(x -> beginsWith(x, commandString));
    }

    public static String getSpecificFromArray(String[] array, String command) {
        return StringUtils.getSpecificFromArray(array, command);
    }
}
