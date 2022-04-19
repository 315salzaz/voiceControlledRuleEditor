package org.openhab.binding.voicecontrolledruleeditor.internal.constants;

import java.util.Arrays;

public class Enums {
    public static enum BaseHandlerState {
        DEFAULT,
        ADDING_RULE,
        RENAMING_RULE,
        EDITING_RULE,
        DELETING_RULE
    }

    public static enum ModuleType {
        TRIGGER("trigger"),
        ACTION("action"),
        CONDITION("condition");

        public final String value;

        ModuleType(String value) {
            this.value = value;
        }
    }

    public static enum ModuleTypeValue {
        ACTION_RULE_ENABLEMENT("core.RuleEnablementAction", UserInputs.ACTION_RULE_ENABLEMENT),
        ACTION_RUN_RULE("core.RunRuleAction", UserInputs.ACTION_RUN_RULE),
        ACTION_ITEM_COMMAND("core.ItemCommandAction", UserInputs.ACTION_ITEM_COMMAND),
        ACTION_ITEM_STATE_UPDATE("core.ItemStateUpdateAction", UserInputs.ACTION_ITEM_STATE_UPDATE),
        ACTION_MEDIA_PLAY("media.PlayAction", UserInputs.ACTION_MEDIA_PLAY),
        ACTION_MEDIA_SAY("media.SayAction", UserInputs.ACTION_MEDIA_SAY),
        ACTION_NOTIFICATION_SEND_BROADCAST("notification.SendBroadcastNotification",
                UserInputs.ACTION_NOTIFICATION_SEND_BROADCAST),
        ACTION_NOTIFICATION_SEND_LOG("notification.SendLogNotification", UserInputs.ACTION_NOTIFICATION_SEND_LOG),
        ACTION_NOTIFICATION_SEND("notification.SendNotification", UserInputs.ACTION_NOTIFICATION_SEND),

        TRIGGER_SYSTEM_START_LEVEL("core.SystemStartlevelTrigger", UserInputs.ON_SYSTEM_START_LEVEL),
        TRIGGER_THING_STATUS_CHANGES("core.ThingStatusChangeTrigger", UserInputs.ON_THING_STATUS_CHANGES),
        TRIGGER_THING_STATUS_UPDATE("core.ThingStatusUpdateTrigger", UserInputs.ON_THING_STATUS_UPDATE_ARR[0]),
        TRIGGER_ITEM_COMMAND_TRIGGER("core.ItemCommandTrigger", UserInputs.ON_ITEM_COMMAND_RECEIVED),
        TRIGGER_ITEM_STATE_CHANGES("core.ItemStateChangeTrigger", UserInputs.ON_ITEM_STATE_CHANGES_ARR[0]),
        TRIGGER_ITEM_STATE_UPDATED("core.ItemStateUpdateTrigger", UserInputs.ON_ITEM_STATE_UPDATED_ARR[0]),
        TRIGGER_FIXED_TIME_OF_DAY("timer.TimeOfDayTrigger", UserInputs.ON_FIXED_TIME_OF_DAY),
        TRIGGER_CHANNEL_ACTIVATED("core.ChannelEventTrigger", UserInputs.ON_CHANNEL_ACTIVATED),
        TRIGGER_GROUP_MEMBER_RECEIVES_COMMAND("core.GroupCommandTrigger", UserInputs.ON_GROUP_MEMBER_RECEIVES_COMMAND),
        TRIGGER_GROUP_MEMBER_STATE_CHANGES("core.GroupStateChangeTrigger", UserInputs.ON_GROUP_MEMBER_STATE_CHANGES),
        TRIGGER_GROUP_MEMBER_STATE_UPDATED("core.GroupStateUpdateTrigger", UserInputs.ON_GROUP_MEMBER_STATE_UPDATED),
        TRIGGER_CRON_TRIGGER("timer.GenericCronTrigger", UserInputs.ON_CRON_TRIGGER),

        CONDITION_ITEM_STATE("core.ItemStateCondition", UserInputs.CONDITION_ITEM_STATE),
        CONDITION_TIME_OF_DAY("core.TimeOfDayCondition", UserInputs.CONDITION_TIME_OF_DAY),
        CONDITION_HOLIDAY("ephemeris.HolidayCondition", UserInputs.CONDITION_HOLIDAY),
        CONDITION_WEEKDAY("ephemeris.WeekdayCondition", UserInputs.CONDITION_WEEKDAY),
        CONDITION_WEEKEND("ephemeris.WeekendCondition", UserInputs.CONDITION_WEEKEND),
        CONDITION_NOT_HOLIDAY("ephemeris.NotHolidayCondition", UserInputs.CONDITION_NOT_HOLIDAY),
        CONDITION_DAY_OF_WEEK("timer.DayOfWeekCondition", UserInputs.CONDITION_DAY_OF_WEEK);

        public final String value;
        public final String input;

        public static ModuleTypeValue getFromValue(String valueString) {
            return Arrays.stream(values()).filter(v -> v.value.equals(valueString)).findFirst().orElse(null);
        }

        public static String[] getActionGroups() {
            return new String[] { "core", "media", "notifications" };
        }

        public static String[] getTriggerGroups() {
            return new String[] { "item", "thing", "other" };
        }

        public static String[] getConditionGroups() {
            return new String[] { "astrological", "other" };
        }

        private static ModuleTypeValue[] getActionCoreValues() {
            return new ModuleTypeValue[] { ACTION_RULE_ENABLEMENT, ACTION_RUN_RULE, ACTION_ITEM_COMMAND,
                    ACTION_ITEM_STATE_UPDATE };
        }

        private static ModuleTypeValue[] getActionMediaValues() {
            return new ModuleTypeValue[] { ACTION_MEDIA_PLAY, ACTION_MEDIA_SAY };
        }

        private static ModuleTypeValue[] getActionNotificationValues() {
            return new ModuleTypeValue[] { ACTION_NOTIFICATION_SEND, ACTION_NOTIFICATION_SEND_BROADCAST,
                    ACTION_NOTIFICATION_SEND_LOG };
        }

        private static ModuleTypeValue[] getTriggerItemValues() {
            return new ModuleTypeValue[] { TRIGGER_ITEM_COMMAND_TRIGGER, TRIGGER_ITEM_STATE_CHANGES,
                    TRIGGER_ITEM_STATE_UPDATED };
        }

        private static ModuleTypeValue[] getTriggerThingValues() {
            return new ModuleTypeValue[] { TRIGGER_THING_STATUS_CHANGES, TRIGGER_THING_STATUS_UPDATE };
        }

        private static ModuleTypeValue[] getTriggerOtherValues() {
            return new ModuleTypeValue[] { TRIGGER_SYSTEM_START_LEVEL, TRIGGER_FIXED_TIME_OF_DAY };
        }

        private static ModuleTypeValue[] getConditionAstrologicalValues() {
            return new ModuleTypeValue[] { CONDITION_HOLIDAY, CONDITION_NOT_HOLIDAY, CONDITION_WEEKDAY,
                    CONDITION_WEEKEND };
        }

        private static ModuleTypeValue[] getConditionOtherValues() {
            return new ModuleTypeValue[] { CONDITION_ITEM_STATE, CONDITION_TIME_OF_DAY, CONDITION_DAY_OF_WEEK };
        }

        public static ModuleTypeValue[] getGroupValues(Enums.ModuleType moduleType, String group) {
            switch (moduleType) {
                case ACTION:
                    if (group.equals("core")) {
                        return getActionCoreValues();
                    }
                    if (group.equals("media")) {
                        return getActionMediaValues();
                    }
                    if (group.equals("notifications")) {
                        return getActionNotificationValues();
                    }
                    break;
                case CONDITION:
                    if (group.equals("astrological")) {
                        return getConditionAstrologicalValues();
                    }
                    if (group.equals("other")) {
                        return getConditionOtherValues();
                    }
                    break;
                case TRIGGER:
                    if (group.equals("item")) {
                        return getTriggerItemValues();
                    }
                    if (group.equals("thing")) {
                        return getTriggerThingValues();
                    }
                    if (group.equals("other")) {
                        return getTriggerOtherValues();
                    }
                    break;
                default:
                    break;
            }
            return null;
        }

        private ModuleTypeValue(String value, String input) {
            this.value = value;
            this.input = input;
        }
    }

    public static enum ConfigurationType {
        TIME("time"),
        START_TIME("startTime"),
        END_TIME("endTime"),
        THING_ID("thingUID"),
        START_LEVEL("startlevel"),
        STATUS("status"),
        PREVIOUS_STATUS("previousStatus"),
        EVENT("event"),
        ITEM_NAME("itemName"),
        STATE("state"),
        PREVIOUS_STATE("previousState"),
        GROUP_NAME("groupName"),
        COMMAND("command"),
        ENABLE("enable"),
        RULE_IDS("ruleUIDs"),
        CONSIDER_CONDITIONS("considerConditions"),
        SINK("sink"),
        SOUND("sound"),
        TEXT("text"),
        MESSAGE("message"),
        USER_ID("userId"),
        OPERATOR("operator"),
        OFFSET("offset"),
        DAY_OF_WEEK("days");

        public final String type;

        private ConfigurationType(String label) {
            this.type = label;
        }
    }
}
