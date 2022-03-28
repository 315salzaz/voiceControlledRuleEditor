package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ConfigurationType;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ModuleType;
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

    public AbstractModuleBuilder createWithTypeFromCommand(String commandString) {
        // 315salzaz read this out after config is added and a confirmation
        if (UserInputs.contains(UserInputs.ACTION_RULE_ENABLEMENT, commandString)) {
            type = "core.RuleEnablementAction";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.ENABLE, true),
                    new AvailableConfigurationType(ConfigurationType.RULE_IDS, true, true) };
        } else if (UserInputs.contains(UserInputs.ACTION_RUN_RULE, commandString)) {
            type = "core.RunRuleAction";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.CONSIDER_CONDITIONS, true),
                    new AvailableConfigurationType(ConfigurationType.RULE_IDS, true, true) };
        } else if (UserInputs.contains(UserInputs.ACTION_ITEM_COMMAND, commandString)) {
            type = "core.ItemCommandAction";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.COMMAND, true),
                    new AvailableConfigurationType(ConfigurationType.ITEM_NAME, true) };
        } else if (UserInputs.contains(UserInputs.ACTION_ITEM_STATE_UPDATE, commandString)) {
            type = "core.ItemStateUpdateAction";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.ITEM_NAME, true),
                    new AvailableConfigurationType(ConfigurationType.STATE, true) };
        } else if (UserInputs.contains(UserInputs.ACTION_MEDIA_PLAY, commandString)) {
            type = "media.PlayAction";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.SINK, false),
                    new AvailableConfigurationType(ConfigurationType.SOUND, true) };
        } else if (UserInputs.contains(UserInputs.ACTION_MEDIA_SAY, commandString)) {
            type = "media.SayAction";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.SINK, false),
                    new AvailableConfigurationType(ConfigurationType.TEXT, true) };
        } else if (UserInputs.contains(UserInputs.ACTION_NOTIFICATION_SEND_BROADCAST, commandString)) {
            type = "notification.SendBroadcastNotification";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.MESSAGE, true) };
        } else if (UserInputs.contains(UserInputs.ACTION_NOTIFICATION_SEND_LOG, commandString)) {
            type = "notification.SendLogNotification";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.MESSAGE, true) };
        } else if (UserInputs.contains(UserInputs.ACTION_NOTIFICATION_SEND, commandString)) {
            type = "notification.SendNotification";
            availableConfigurations = new AvailableConfigurationType[] {
                    new AvailableConfigurationType(ConfigurationType.MESSAGE, true),
                    new AvailableConfigurationType(ConfigurationType.USER_ID, true) };
        } else {
            VoiceManagerUtils.say(String.format(TTSConstants.TRIGGER_TYPE_NOT_FOUND, commandString));
        }

        return this;
    }

    public Module build(String id) {
        Configuration config = new Configuration(getConfigurationProperties());
        return ActionBuilder.create().withId(id).withLabel(label).withTypeUID(type).withConfiguration(config).build();
    }

    public ModuleType getModuleType() {
        return ModuleType.ACTION;
    }
}
