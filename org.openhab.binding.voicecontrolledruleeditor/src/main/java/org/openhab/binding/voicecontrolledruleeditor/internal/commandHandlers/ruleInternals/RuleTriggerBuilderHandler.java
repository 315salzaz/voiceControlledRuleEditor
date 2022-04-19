package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ConfigurationType;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ModuleType;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ModuleTypeValue;
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

    protected AvailableConfigurationType[] getAvailableConfigurationTypes(ModuleTypeValue moduleType) {
        switch (moduleType) {
            case TRIGGER_SYSTEM_START_LEVEL:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.START_LEVEL, true) };
            case TRIGGER_THING_STATUS_CHANGES:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.THING_ID, true),
                        new AvailableConfigurationType(ConfigurationType.PREVIOUS_STATUS, true),
                        new AvailableConfigurationType(ConfigurationType.STATUS, true) };
            case TRIGGER_THING_STATUS_UPDATE:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.THING_ID, true),
                        new AvailableConfigurationType(ConfigurationType.STATUS, true) };
            case TRIGGER_ITEM_COMMAND_TRIGGER:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.ITEM_NAME, true),
                        new AvailableConfigurationType(ConfigurationType.COMMAND, true) };
            case TRIGGER_ITEM_STATE_CHANGES:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.ITEM_NAME, true),
                        new AvailableConfigurationType(ConfigurationType.STATE, true),
                        new AvailableConfigurationType(ConfigurationType.PREVIOUS_STATE, true) };
            case TRIGGER_ITEM_STATE_UPDATED:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.ITEM_NAME, true),
                        new AvailableConfigurationType(ConfigurationType.STATE, true) };
            case TRIGGER_FIXED_TIME_OF_DAY:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.TIME, true) };
            case TRIGGER_CHANNEL_ACTIVATED:
            case TRIGGER_GROUP_MEMBER_RECEIVES_COMMAND:
            case TRIGGER_GROUP_MEMBER_STATE_CHANGES:
            case TRIGGER_GROUP_MEMBER_STATE_UPDATED:
            case TRIGGER_CRON_TRIGGER:
            default:
                VoiceManagerUtils.say(TTSConstants.ERROR_OCCURED);
                return null;
        }
    }

    public AbstractModuleBuilder createWithTypeFromCommand(String commandString) {
        // 315salzaz read this out after config is added and a confirmation
        if (UserInputs.contains(UserInputs.ON_SYSTEM_START_LEVEL, commandString)) {
            type = ModuleTypeValue.TRIGGER_SYSTEM_START_LEVEL.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.TRIGGER_SYSTEM_START_LEVEL);
        } else if (UserInputs.contains(UserInputs.ON_THING_STATUS_CHANGES, commandString)) {
            type = ModuleTypeValue.TRIGGER_THING_STATUS_CHANGES.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.TRIGGER_THING_STATUS_CHANGES);
        } else if (UserInputs.contains(UserInputs.ON_THING_STATUS_UPDATE_ARR, commandString)) {
            type = ModuleTypeValue.TRIGGER_THING_STATUS_UPDATE.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.TRIGGER_THING_STATUS_UPDATE);
        } else if (UserInputs.contains(UserInputs.ON_ITEM_COMMAND_RECEIVED, commandString)) {
            type = ModuleTypeValue.TRIGGER_ITEM_COMMAND_TRIGGER.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.TRIGGER_ITEM_COMMAND_TRIGGER);
        } else if (UserInputs.contains(UserInputs.ON_ITEM_STATE_CHANGES_ARR, commandString)) {
            type = ModuleTypeValue.TRIGGER_ITEM_STATE_CHANGES.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.TRIGGER_ITEM_STATE_CHANGES);
        } else if (UserInputs.contains(UserInputs.ON_ITEM_STATE_UPDATED_ARR, commandString)) {
            type = ModuleTypeValue.TRIGGER_ITEM_STATE_UPDATED.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.TRIGGER_ITEM_STATE_UPDATED);
        } else if (UserInputs.contains(UserInputs.ON_FIXED_TIME_OF_DAY, commandString)) {
            type = ModuleTypeValue.TRIGGER_FIXED_TIME_OF_DAY.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.TRIGGER_FIXED_TIME_OF_DAY);
        } else if (UserInputs.contains(UserInputs.ON_CHANNEL_ACTIVATED, commandString)) {
            // 315salzaz postponed
            type = ModuleTypeValue.TRIGGER_CHANNEL_ACTIVATED.value;
        } else if (UserInputs.contains(UserInputs.ON_GROUP_MEMBER_RECEIVES_COMMAND, commandString)) {
            // 315salzaz postponed
            type = ModuleTypeValue.TRIGGER_GROUP_MEMBER_RECEIVES_COMMAND.value;
        } else if (UserInputs.contains(UserInputs.ON_GROUP_MEMBER_STATE_CHANGES, commandString)) {
            // 315salzaz postponed
            type = ModuleTypeValue.TRIGGER_GROUP_MEMBER_STATE_CHANGES.value;
        } else if (UserInputs.contains(UserInputs.ON_GROUP_MEMBER_STATE_UPDATED, commandString)) {
            // 315salzaz postponed
            type = ModuleTypeValue.TRIGGER_GROUP_MEMBER_STATE_UPDATED.value;
        } else if (UserInputs.contains(UserInputs.ON_CRON_TRIGGER, commandString)) {
            // 315salzaz
            type = ModuleTypeValue.TRIGGER_CRON_TRIGGER.value;
            VoiceManagerUtils.say("Cron is postponed");
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

    @Override
    public String[] getTypeGroups() {
        return null;
    }
}
