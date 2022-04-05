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
        TRIGGER,
        ACTION,
        CONDITION
    }

    public static enum ModuleTypeValue {
        ACTION_RULE_ENABLEMENT("core.RuleEnablementAction"),
        ACTION_RUN_RULE("core.RunRuleAction"),
        ACTION_ITEM_COMMAND("core.ItemCommandAction"),
        ACTION_ITEM_STATE_UPDATE("core.ItemStateUpdateAction"),
        ACTION_MEDIA_PLAY("media.PlayAction"),
        ACTION_MEDIA_SAY("media.SayAction"),
        ACTION_NOTIFICATION_SEND_BROADCAST("notification.SendBroadcastNotification"),
        ACTION_NOTIFICATION_SEND_LOG("notification.SendLogNotification"),
        ACTION_NOTIFICATION_SEND("notification.SendNotification"),

        TRIGGER_SYSTEM_START_LEVEL("core.SystemStartlevelTrigger"),
        TRIGGER_THING_STATUS_CHANGES("core.ThingStatusChangeTrigger"),
        TRIGGER_THING_STATUS_UPDATE("core.ThingStatusUpdateTrigger"),
        TRIGGER_ITEM_COMMAND_TRIGGER("core.ItemCommandTrigger"),
        TRIGGER_ITEM_STATE_CHANGES("core.ItemStateChangeTrigger"),
        TRIGGER_ITEM_STATE_UPDATED("core.ItemStateUpdateTrigger"),
        TRIGGER_FIXED_TIME_OF_DAY("timer.TimeOfDayTrigger"),
        TRIGGER_CHANNEL_ACTIVATED("core.ChannelEventTrigger"),
        TRIGGER_GROUP_MEMBER_RECEIVES_COMMAND("core.GroupCommandTrigger"),
        TRIGGER_GROUP_MEMBER_STATE_CHANGES("core.GroupStateChangeTrigger"),
        TRIGGER_GROUP_MEMBER_STATE_UPDATED("core.GroupStateUpdateTrigger"),
        TRIGGER_CRON_TRIGGER("timer.GenericCronTrigger"),

        CONDITION_ITEM_STATE("core.ItemStateCondition"),
        CONDITION_TIME_OF_DAY("core.TimeOfDayCondition"),
        CONDITION_HOLIDAY("ephemeris.HolidayCondition"),
        CONDITION_WEEKDAY("ephemeris.WeekdayCondition"),
        CONDITION_WEEKEND("ephemeris.WeekendCondition"),
        CONDITION_NOT_HOLIDAY("ephemeris.NotHolidayCondition"),
        CONDITION_DAY_OF_WEEK("timer.DayOfWeekCondition");

        public final String value;

        public static ModuleTypeValue getFromValue(String valueString) {
            return Arrays.stream(values()).filter(v -> v.value.equals(valueString)).findFirst().orElse(null);
        }

        private ModuleTypeValue(String value) {
            this.value = value;
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
