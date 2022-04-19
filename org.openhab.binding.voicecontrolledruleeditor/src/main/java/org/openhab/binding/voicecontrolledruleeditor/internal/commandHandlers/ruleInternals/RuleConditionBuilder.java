package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ConfigurationType;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ModuleType;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ModuleTypeValue;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.TTSConstants;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.UserInputs;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.VoiceManagerUtils;
import org.openhab.core.automation.Module;
import org.openhab.core.automation.util.ConditionBuilder;
import org.openhab.core.config.core.Configuration;

public class RuleConditionBuilder extends AbstractModuleBuilder {

    public RuleConditionBuilder() {
        super();
    }

    protected AvailableConfigurationType[] getAvailableConfigurationTypes(ModuleTypeValue moduleType) {
        switch (moduleType) {

            case CONDITION_ITEM_STATE:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.ITEM_NAME, true),
                        new AvailableConfigurationType(ConfigurationType.STATE, true),
                        new AvailableConfigurationType(ConfigurationType.OPERATOR, true) };
            case CONDITION_TIME_OF_DAY:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.START_TIME, true),
                        new AvailableConfigurationType(ConfigurationType.END_TIME, true) };
            case CONDITION_HOLIDAY:
            case CONDITION_WEEKDAY:
            case CONDITION_WEEKEND:
            case CONDITION_NOT_HOLIDAY:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.OFFSET, true) };
            case CONDITION_DAY_OF_WEEK:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.DAY_OF_WEEK, true) };
            default:
                return null;
        }
    }

    public AbstractModuleBuilder createWithTypeFromCommand(String commandString) {
        if (UserInputs.contains(UserInputs.CONDITION_ITEM_STATE, commandString)) {
            type = ModuleTypeValue.CONDITION_ITEM_STATE.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.CONDITION_ITEM_STATE);
        } else if (UserInputs.contains(UserInputs.CONDITION_TIME_OF_DAY, commandString)) {
            type = ModuleTypeValue.CONDITION_TIME_OF_DAY.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.CONDITION_TIME_OF_DAY);
        } else if (UserInputs.contains(UserInputs.CONDITION_HOLIDAY, commandString)) {
            type = ModuleTypeValue.CONDITION_HOLIDAY.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.CONDITION_HOLIDAY);
        } else if (UserInputs.contains(UserInputs.CONDITION_WEEKDAY, commandString)) {
            type = ModuleTypeValue.CONDITION_WEEKDAY.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.CONDITION_WEEKDAY);
        } else if (UserInputs.contains(UserInputs.CONDITION_WEEKEND, commandString)) {
            type = ModuleTypeValue.CONDITION_WEEKEND.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.CONDITION_WEEKEND);
        } else if (UserInputs.contains(UserInputs.CONDITION_NOT_HOLIDAY, commandString)) {
            type = ModuleTypeValue.CONDITION_NOT_HOLIDAY.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.CONDITION_NOT_HOLIDAY);
        } else if (UserInputs.contains(UserInputs.CONDITION_DAY_OF_WEEK, commandString)) {
            type = ModuleTypeValue.CONDITION_DAY_OF_WEEK.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.CONDITION_DAY_OF_WEEK);
        } else {
            VoiceManagerUtils.say(String.format(TTSConstants.CONDITION_TYPE_NOT_FOUND, commandString));
        }
        return this;
    }

    public Module build(String id) {
        Configuration config = new Configuration(getConfigurationProperties());
        return ConditionBuilder.create().withId(id).withLabel(label).withTypeUID(type).withConfiguration(config)
                .build();
    }

    public ModuleType getModuleType() {
        return ModuleType.CONDITION;
    }

    @Override
    public String[] getTypeGroups() {

        return null;
    }
}
