package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ConfigurationType;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ModuleType;
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

    public AbstractModuleBuilder createWithTypeFromCommand(String commandString) {
        if (UserInputs.contains(UserInputs.CONDITION_ITEM_STATE, commandString)) {
            type = "core.ItemStateCondition";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.ITEM_NAME, true),
                    new AvailableConfigurationType(ConfigurationType.STATE, true),
                    new AvailableConfigurationType(ConfigurationType.OPERATOR, true) };
        } else if (UserInputs.contains(UserInputs.CONDITION_TIME_OF_DAY, commandString)) {
            type = "core.TimeOfDayCondition";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.START_TIME, true),
                    new AvailableConfigurationType(ConfigurationType.END_TIME, true) };
        } else if (UserInputs.contains(UserInputs.CONDITION_HOLIDAY, commandString)) {
            type = "ephemeris.HolidayCondition";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.OFFSET, true) };
        } else if (UserInputs.contains(UserInputs.CONDITION_WEEKDAY, commandString)) {
            type = "ephemeris.WeekdayCondition";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.OFFSET, true) };
        } else if (UserInputs.contains(UserInputs.CONDITION_WEEKEND, commandString)) {
            type = "ephemeris.WeekendCondition";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.OFFSET, true) };
        } else if (UserInputs.contains(UserInputs.CONDITION_NOT_HOLIDAY, commandString)) {
            type = "ephemeris.NotHolidayCondition";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.OFFSET, true) };
        } else if (UserInputs.contains(UserInputs.CONDITION_DAY_OF_WEEK, commandString)) {
            type = "timer.DayOfWeekCondition";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.DAY_OF_WEEK, true) };
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
}
