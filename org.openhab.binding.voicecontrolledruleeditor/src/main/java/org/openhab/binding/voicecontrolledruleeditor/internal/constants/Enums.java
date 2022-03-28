package org.openhab.binding.voicecontrolledruleeditor.internal.constants;

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
