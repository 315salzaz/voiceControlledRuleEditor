package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.TTSConstants;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.UserInputs;
import org.openhab.core.automation.Module;
import org.openhab.core.automation.RuleRegistry;
import org.openhab.core.voice.VoiceManager;

public class RuleTriggerBuilderHandler /*implements IBuilder*/ {
    private VoiceManager voiceManager;
    private RuleRegistry ruleRegistry;

    private String type;
    private String[] availableConfigurations;

    public RuleTriggerBuilderHandler(VoiceManager voiceManager, RuleRegistry ruleRegistry) {
        this.voiceManager = voiceManager;
        this.ruleRegistry = ruleRegistry;
    }

    public IBuilder withTypeFromCommand(String commandString) {
        if (UserInputs.contains(UserInputs.ON_SYSTEM_START_LEVEL, commandString)) {
            type = "core.SystemStartlevelTrigger";
            availableConfigurations = new String[] {"time"};
        } else if (UserInputs.contains(UserInputs.ON_THING_STATUS_CHANGES, commandString)) {
            type = "core.ThingStatusChangeTrigger";
            availableConfigurations = new String[] {"thing name", ""};
        } else if (UserInputs.contains(UserInputs.ON_THING_STATUS_UPDATE_ARR, commandString)) {
            type = "core.ThingStatusUpdateTrigger";
        } else if (UserInputs.contains(UserInputs.ON_CHANNEL_ACTIVATED, commandString)) {
            type = "core.ChannelEventTrigger";
        } else if (UserInputs.contains(UserInputs.ON_ITEM_COMMAND_RECEIVED, commandString)) {
            type = "core.ItemCommandTrigger";
        } else if (UserInputs.contains(UserInputs.ON_ITEM_STATE_CHANGES_ARR, commandString)) {
            type = "core.ItemStateChangeTrigger";
        } else if (UserInputs.contains(UserInputs.ON_ITEM_STATE_UPDATED_ARR, commandString)) {
            type = "core.ItemStateUpdateTrigger";
        } else if (UserInputs.contains(UserInputs.ON_GROUP_MEMBER_RECEIVES_COMMAND, commandString)) {
            type = "core.GroupCommandTrigger";
        } else if (UserInputs.contains(UserInputs.ON_GROUP_MEMBER_STATE_CHANGES, commandString)) {
            type = "core.GroupStateChangeTrigger";
        } else if (UserInputs.contains(UserInputs.ON_GROUP_MEMBER_STATE_UPDATED, commandString)) {
            type = "core.GroupStateUpdateTrigger";
        } else if (UserInputs.contains(UserInputs.ON_CRON_TRIGGER, commandString)) {
            type = "timer.GenericCronTrigger";
        } else if (UserInputs.contains(UserInputs.ON_FIXED_TIME_OF_DAY, commandString)) {
            type = "timer.TimeOfDayTrigger";
        } else {
            voiceManager.say(TTSConstants.TRIGGER_TYPE_NOT_FOUND, commandString);
        }

        return this;
    }

    public Module build() {
        // TODO Auto-generated method stub
        return null;
    }

}
