package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ConfigurationType;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ModuleType;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ModuleTypeValue;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.TTSConstants;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.UserInputs;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.VoiceManagerUtils;
import org.openhab.core.automation.Module;
import org.openhab.core.automation.util.ActionBuilder;
import org.openhab.core.config.core.Configuration;

public class RuleActionBuilder extends AbstractModuleBuilder {

    public RuleActionBuilder() {
        super();
    }

    protected AvailableConfigurationType[] getAvailableConfigurationTypes(ModuleTypeValue moduleType) {
        switch (moduleType) {
            case ACTION_RULE_ENABLEMENT:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.ENABLE, true, UserInputs.CONFIGURE_ENABLE),
                        new AvailableConfigurationType(ConfigurationType.RULE_IDS, true,
                                TTSConstants.ADD_OR_REMOVE_RULE_WITH_NAME, true) };
            case ACTION_RUN_RULE:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.CONSIDER_CONDITIONS, true,
                                UserInputs.CONFIGURE_CONSIDER_CONDITIONS),
                        new AvailableConfigurationType(ConfigurationType.RULE_IDS, true,
                                TTSConstants.ADD_OR_REMOVE_RULE_WITH_NAME, true) };
            case ACTION_ITEM_COMMAND:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.COMMAND, true, UserInputs.CONFIGURE_COMMAND),
                        new AvailableConfigurationType(ConfigurationType.ITEM_NAME, true,
                                UserInputs.CONFIGURE_ITEM_NAME) };
            case ACTION_ITEM_STATE_UPDATE:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.ITEM_NAME, true,
                                UserInputs.CONFIGURE_ITEM_NAME),
                        new AvailableConfigurationType(ConfigurationType.STATE, true, UserInputs.CONFIGURE_STATE) };
            case ACTION_MEDIA_PLAY:
                // 315salzaz read you audio sink and soud numbers
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.SINK, false,
                                UserInputs.CONFIGURE_AUDIO_OUTPUT),
                        new AvailableConfigurationType(ConfigurationType.SOUND, true, UserInputs.CONFIGURE_SOUND) };
            case ACTION_MEDIA_SAY:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.SINK, false,
                                UserInputs.CONFIGURE_AUDIO_OUTPUT),
                        new AvailableConfigurationType(ConfigurationType.TEXT, true, UserInputs.CONFIGURE_TEXT) };
            case ACTION_NOTIFICATION_SEND_BROADCAST:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.MESSAGE, true, UserInputs.CONFIGURE_MESSAGE) };
            case ACTION_NOTIFICATION_SEND_LOG:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.MESSAGE, true, UserInputs.CONFIGURE_MESSAGE) };
            case ACTION_NOTIFICATION_SEND:
                return new AvailableConfigurationType[] {
                        new AvailableConfigurationType(ConfigurationType.MESSAGE, true, UserInputs.CONFIGURE_MESSAGE),
                        new AvailableConfigurationType(ConfigurationType.USER_ID, true,
                                UserInputs.CONFIGURE_USER_EMAIL) };
            default:
                VoiceManagerUtils.say(TTSConstants.ERROR_OCCURED);
                return null;
        }
    }

    public AbstractModuleBuilder createWithTypeFromCommand(String commandString) {
        // 315salzaz read this out after config is added and a confirmation <- What is the point of confirmation at this
        // point???
        if (UserInputs.contains(UserInputs.ACTION_RULE_ENABLEMENT, commandString)) {
            type = ModuleTypeValue.ACTION_RULE_ENABLEMENT.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.ACTION_RULE_ENABLEMENT);
        } else if (UserInputs.contains(UserInputs.ACTION_RUN_RULE, commandString)) {
            type = ModuleTypeValue.ACTION_RUN_RULE.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.ACTION_RUN_RULE);
        } else if (UserInputs.contains(UserInputs.ACTION_ITEM_COMMAND, commandString)) {
            type = ModuleTypeValue.ACTION_ITEM_COMMAND.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.ACTION_ITEM_COMMAND);
        } else if (UserInputs.contains(UserInputs.ACTION_ITEM_STATE_UPDATE, commandString)) {
            type = ModuleTypeValue.ACTION_ITEM_STATE_UPDATE.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.ACTION_ITEM_STATE_UPDATE);
        } else if (UserInputs.contains(UserInputs.ACTION_MEDIA_PLAY, commandString)) {
            type = ModuleTypeValue.ACTION_MEDIA_PLAY.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.ACTION_MEDIA_PLAY);
        } else if (UserInputs.contains(UserInputs.ACTION_MEDIA_SAY, commandString)) {
            type = ModuleTypeValue.ACTION_MEDIA_SAY.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.ACTION_MEDIA_SAY);
        } else if (UserInputs.contains(UserInputs.ACTION_NOTIFICATION_SEND_BROADCAST, commandString)) {
            type = ModuleTypeValue.ACTION_NOTIFICATION_SEND_BROADCAST.value;
            availableConfigurations = getAvailableConfigurationTypes(
                    ModuleTypeValue.ACTION_NOTIFICATION_SEND_BROADCAST);
        } else if (UserInputs.contains(UserInputs.ACTION_NOTIFICATION_SEND_LOG, commandString)) {
            type = ModuleTypeValue.ACTION_NOTIFICATION_SEND_LOG.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.ACTION_NOTIFICATION_SEND_LOG);
        } else if (UserInputs.contains(UserInputs.ACTION_NOTIFICATION_SEND, commandString)) {
            type = ModuleTypeValue.ACTION_NOTIFICATION_SEND.value;
            availableConfigurations = getAvailableConfigurationTypes(ModuleTypeValue.ACTION_NOTIFICATION_SEND);
        } else {
            VoiceManagerUtils.say(String.format(TTSConstants.TRIGGER_TYPE_NOT_FOUND, commandString));
        }

        return this;
    }

    public String[] getTypeGroups() {

        return null;
    }

    public Module build(String id) {
        Configuration config = new Configuration(getConfigurationProperties());
        return ActionBuilder.create().withId(id).withLabel(label).withTypeUID(type).withConfiguration(config).build();
    }

    public ModuleType getModuleType() {
        return ModuleType.ACTION;
    }
}
