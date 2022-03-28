package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ConfigurationType;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ModuleType;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.TTSConstants;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.UserInputs;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.VoiceManagerUtils;
import org.openhab.core.automation.Module;
import org.openhab.core.automation.util.TriggerBuilder;
import org.openhab.core.config.core.Configuration;

public class RuleTriggerBuilderHandler extends AbstractModuleBuilder {

    public RuleTriggerBuilderHandler() {
        super();
    }

    public AbstractModuleBuilder createWithTypeFromCommand(String commandString) {
        // 315salzaz read this out after config is added and a confirmation
        if (UserInputs.contains(UserInputs.ON_SYSTEM_START_LEVEL, commandString)) {
            type = "core.SystemStartlevelTrigger";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.START_LEVEL, true) };
        } else if (UserInputs.contains(UserInputs.ON_THING_STATUS_CHANGES, commandString)) {
            type = "core.ThingStatusChangeTrigger";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.THING_ID, true),
                    new AvailableConfigurationType(ConfigurationType.PREVIOUS_STATUS, true),
                    new AvailableConfigurationType(ConfigurationType.STATUS, true) };
        } else if (UserInputs.contains(UserInputs.ON_THING_STATUS_UPDATE_ARR, commandString)) {
            type = "core.ThingStatusUpdateTrigger";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.THING_ID, true),
                    new AvailableConfigurationType(ConfigurationType.STATUS, true) };
        } else if (UserInputs.contains(UserInputs.ON_CHANNEL_ACTIVATED, commandString)) {
            // 315salzaz postponed
            type = "core.ChannelEventTrigger";
        } else if (UserInputs.contains(UserInputs.ON_ITEM_COMMAND_RECEIVED, commandString)) {
            type = "core.ItemCommandTrigger";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.ITEM_NAME, true),
                    new AvailableConfigurationType(ConfigurationType.COMMAND, true) };
        } else if (UserInputs.contains(UserInputs.ON_ITEM_STATE_CHANGES_ARR, commandString)) {
            type = "core.ItemStateChangeTrigger";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.ITEM_NAME, true),
                    new AvailableConfigurationType(ConfigurationType.STATE, true),
                    new AvailableConfigurationType(ConfigurationType.PREVIOUS_STATE, true) };
        } else if (UserInputs.contains(UserInputs.ON_ITEM_STATE_UPDATED_ARR, commandString)) {
            type = "core.ItemStateUpdateTrigger";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.ITEM_NAME, true),
                    new AvailableConfigurationType(ConfigurationType.STATE, true) };
        } else if (UserInputs.contains(UserInputs.ON_GROUP_MEMBER_RECEIVES_COMMAND, commandString)) {
            // 315salzaz postponed
            type = "core.GroupCommandTrigger";
        } else if (UserInputs.contains(UserInputs.ON_GROUP_MEMBER_STATE_CHANGES, commandString)) {
            // 315salzaz postponed
            type = "core.GroupStateChangeTrigger";
        } else if (UserInputs.contains(UserInputs.ON_GROUP_MEMBER_STATE_UPDATED, commandString)) {
            // 315salzaz postponed
            type = "core.GroupStateUpdateTrigger";
        } else if (UserInputs.contains(UserInputs.ON_CRON_TRIGGER, commandString)) {
            // 315salzaz
            VoiceManagerUtils.say("Cron is postponed");
            // type = "timer.GenericCronTrigger";
        } else if (UserInputs.contains(UserInputs.ON_FIXED_TIME_OF_DAY, commandString)) {
            type = "timer.TimeOfDayTrigger";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.TIME, true) };
        } else {
            VoiceManagerUtils.say(String.format(TTSConstants.TRIGGER_TYPE_NOT_FOUND, commandString));
        }

        return this;
    }

    public Module build(String id) {
        Configuration config = new Configuration(getConfigurationProperties());
        return TriggerBuilder.create().withId(id).withLabel(label).withTypeUID(type).withConfiguration(config).build();
    }

    public Enums.ModuleType getModuleType() {
        return ModuleType.TRIGGER;
    }
}
