package org.openhab.binding.voicecontrolledruleeditor.internal.assistant;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.TTSConstants;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.UserInputs;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.VoiceManagerUtils;

public class StatusReport {
    public static boolean isStatusReportCommand(String commandString) {
        return commandString.equals(UserInputs.STATUS_REPORT);
    }

    public static void waitingForCommand() {
        VoiceManagerUtils.say(TTSConstants.STATUS_WAITING_FOR_COMMAND);
    }

    public static void createRuleWaitingForName() {
        VoiceManagerUtils.say(TTSConstants.STATUS_CREATING_RULE_WAITING_FOR_RULE_NAME);
    }

    public static void waitingForLabel() {
        VoiceManagerUtils.say(TTSConstants.ADD_LABEL);
    }

    public static void waitingForModuleConfiguration() {
        VoiceManagerUtils.say(TTSConstants.STATUS_EDIT_MODULE_WAITING_FOR_CONFIGURATION);
    }

    public static void waitingForRuleNameConfirmation(String ruleName) {
        VoiceManagerUtils.say(String.format(TTSConstants.STATUS_CONFIRM_NEW_RULE_NAME, ruleName));
    }

    public static void waitingForEditingConfirmation() {
        VoiceManagerUtils.say(String.format(TTSConstants.STATUS_RULE_CREATED_START_EDITING_CONFIRMATION));
    }

    public static void renameRuleOldName() {
        VoiceManagerUtils.say(TTSConstants.STATUS_RENAME_RULE_OLD_NAME);
    }

    public static void renameRuleNewName() {
        VoiceManagerUtils.say(TTSConstants.STATUS_RENAME_RULE_NEW_NAME);
    }

    public static void renameRuleNewNameConfirmation() {
        VoiceManagerUtils.say(TTSConstants.STATUS_RENAME_RULE_NEW_NAME_CONFIRMATION);
    }

    public static void editRuleTypeInput() {
        VoiceManagerUtils.say(TTSConstants.STATUS_EDIT_RULE_WAITING_FOR_EDIT_TYPE);
    }

    public static void editRuleNameInput() {
        VoiceManagerUtils.say(TTSConstants.STATUS_EDIT_RULE_NAME_INPUT);
    }

    public static void editModuleWaitingForModuleType(String moduleType) {
        VoiceManagerUtils.say(String.format(TTSConstants.STATUS_EDIT_MODULE_WAITING_FOR_MODULE_TYPE, moduleType));
    }

    public static void editModuleWaitingForLabel() {
        VoiceManagerUtils.say(TTSConstants.STATUS_EDIT_MODULE_WAITING_FOR_LABEL);
    }

    public static void removeModuleDeleteConfirmation(String moduleLabel) {
        VoiceManagerUtils.say(String.format(TTSConstants.MODULE_DELETE_CONFIRMATION, moduleLabel));
    }
}
