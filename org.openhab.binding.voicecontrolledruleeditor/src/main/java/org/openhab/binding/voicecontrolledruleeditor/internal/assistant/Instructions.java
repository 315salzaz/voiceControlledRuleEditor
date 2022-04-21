package org.openhab.binding.voicecontrolledruleeditor.internal.assistant;

import java.util.stream.Stream;

import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals.AbstractModuleBuilder;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ModuleTypeValue;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.TTSConstants;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.UserInputs;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.VoiceManagerUtils;

public class Instructions {
    public static boolean isInstructionsCommand(String commandString) {
        return UserInputs.beginsWith(UserInputs.TELL_ME_HOW, commandString);
    }

    public static void waitingForCommand() {
        VoiceManagerUtils.say(TTSConstants.INSTRUCTION_WAITING_FOR_COMMAND);
    }

    public static void enterRuleName() {
        VoiceManagerUtils.say(TTSConstants.INSTRUCTION_ENTER_RULE_NAME);
    }

    public static void editRuleTypeInput() {
        VoiceManagerUtils.say(TTSConstants.INSTRUCTION_EDIT_RULE_WAITING_FOR_EDIT_TYPE);
    }

    public static void editModuleWaitingForMissingConfiguration(AbstractModuleBuilder moduleBuilder) {
        var availableConfigurationInstructionsArray = Stream.of(moduleBuilder.getMissingAvailableConfigurations())
                .map(c -> c.instruction).toArray(String[]::new);

        var availableConfigurationInstructionsJoined = String.join(", ", availableConfigurationInstructionsArray);

        VoiceManagerUtils.say(String.format(TTSConstants.INSTRUCTION_LIST_MISSING_CONFIGURATIONS,
                availableConfigurationInstructionsJoined));
    }

    public static void editModuleWaitingForConfiguration(AbstractModuleBuilder moduleBuilder) {
        var availableConfigurationInstructionsArray = Stream.of(moduleBuilder.getAvailableConfigurations())
                .map(c -> c.instruction).toArray(String[]::new);

        var availableConfigurationInstructionsJoined = String.join(", ", availableConfigurationInstructionsArray);

        VoiceManagerUtils.say(String.format(TTSConstants.INSTRUCTION_LIST_OF_AVAILABLE_CONFIGURATIONS,
                availableConfigurationInstructionsJoined));
    }

    public static void editModuleWaitingForModuleType(AbstractModuleBuilder moduleBuilder, String commandString) {
        String[] splits = commandString.split(" ");
        String[] groups;
        switch (moduleBuilder.getModuleType()) {
            case ACTION:
                groups = ModuleTypeValue.getActionGroups();
                break;
            case CONDITION:
                groups = ModuleTypeValue.getConditionGroups();
                break;
            case TRIGGER:
                groups = ModuleTypeValue.getTriggerGroups();
                break;
            default:
                groups = null;
                break;
        }

        if (groups == null) {
            return;
        }

        if (!Stream.of(groups).anyMatch(g -> Stream.of(splits).anyMatch(s -> s.contains(g)))) {
            if (splits.length < 4) {
                VoiceManagerUtils.say(String.format(TTSConstants.INSTRUCTION_EDIT_MODULE_WAITING_FOR_MODULE_TYPE,
                        String.join(", ", groups), groups[0]));
            }
            return;
        }

        for (int i = 0; i < splits.length; i++) {
            var groupValues = ModuleTypeValue.getGroupValues(moduleBuilder.getModuleType(), splits[i]);
            if (groupValues != null) {
                VoiceManagerUtils
                        .say(String.format(TTSConstants.INSTRUCTION_EDIT_MODULE_WAITING_FOR_MODULE_TYPE_FOR_GROUP,
                                String.join(", ", Stream.of(groupValues).map(v -> v.input).toArray(String[]::new))));
                return;
            }
        }
    }

    public static void editModuleWaitingForLabel() {
        VoiceManagerUtils.say(TTSConstants.INSTRUCTION_EDIT_MODULE_WAITING_FOR_LABEL);
    }
}
