package org.openhab.binding.voicecontrolledruleeditor.internal.constants;

import java.util.Arrays;

public class UserInputs {
    public static String[] CREATE_NEW_RULE_ARR = { "create", "new" };
    public static String[] RENAME_RULE = { "rename", " rule" };
    public static String[] CONFIRM_ARRAY = { "okay", "confirm", "yes" };
    public static String[] DENY_ARRAY = { "deny", "denied", "reject", "no", "refuse" };
    public static String CANCEL = "cancel";

    public static String[] CREATE_ARR = { "create", "add", "build" };
    public static String RENAME = "rename";
    public static String[] REMOVE_ARR = { "delete", "remove" };
    public static String TRIGGER = "trigger";

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
    public static String ON_CRON_TRIGGER = "on a chronological trigger";
    public static String ON_FIXED_TIME_OF_DAY = "on a fixed time of day";

    public static boolean isEquals(String[] userInput, String commandString) {
        return Arrays.stream(userInput).anyMatch(x -> x.equals(commandString));
    }
    
    public static boolean isEquals(String userInput, String commandString) {
        return userInput.equals(commandString);
    }

    public static boolean contains(String[] userInput, String commandString) {
        return Arrays.stream(userInput).anyMatch(x -> x.contains(commandString));
    }
    
    public static boolean contains(String userInput, String commandString) {
        return commandString.contains(userInput);
    }
}
