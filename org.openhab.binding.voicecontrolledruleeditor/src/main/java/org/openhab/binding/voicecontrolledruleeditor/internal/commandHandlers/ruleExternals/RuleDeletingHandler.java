package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleExternals;

import org.openhab.binding.voicecontrolledruleeditor.internal.assistant.Instructions;
import org.openhab.binding.voicecontrolledruleeditor.internal.assistant.StatusReport;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleDelete.RuleDeleteWaitingForNameConfirmationState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleDelete.RuleDeleteWaitingForNameState;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.BaseHandlerState;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.TTSConstants;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.UserInputs;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.RuleRegistryUtils;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.VoiceManagerUtils;
import org.openhab.core.automation.Rule;

public class RuleDeletingHandler implements ICommandHandler {

    private AbstractHandlerState handlerState;
    private Rule rule;

    public RuleDeletingHandler() {
        handlerState = new RuleDeleteWaitingForNameState(this);
        VoiceManagerUtils.say(TTSConstants.DELETE_RULE_NAME);
    }

    public HandleCommandResult handleNameInputed(String ruleName) {
        Rule ruleFromName = RuleRegistryUtils.getRuleFromName(ruleName);

        if (ruleFromName == null) {
            VoiceManagerUtils.say(String.format(TTSConstants.RULE_NOT_FOUND, ruleName));
            return null;
        }

        VoiceManagerUtils.say(String.format(TTSConstants.DELETE_RULE_NAME_CONFIRMATION, ruleFromName.getName()));
        handlerState = new RuleDeleteWaitingForNameConfirmationState(this);
        return null;
    }

    public HandleCommandResult handleNameConfirmation(String commandString) {

        if (UserInputs.isEquals(UserInputs.CONFIRM_ARRAY, commandString)) {
            var deletedRule = RuleRegistryUtils.deleteRule(rule.getUID());
            if (deletedRule == null) {
                VoiceManagerUtils.say(TTSConstants.ERROR_OCCURED);
                return new HandleCommandResult(BaseHandlerState.DEFAULT);
            }

            VoiceManagerUtils.say(String.format(TTSConstants.RULE_DELETED, rule.getName()));
            return new HandleCommandResult(BaseHandlerState.DEFAULT);
        }

        if (UserInputs.isEquals(UserInputs.DENY_ARRAY, commandString)) {
            handlerState = new RuleDeleteWaitingForNameState(this);
            VoiceManagerUtils.say(TTSConstants.DELETE_RULE_NAME);
            return null;
        }

        VoiceManagerUtils.say(String.format(TTSConstants.COMMAND_NOT_FOUND, commandString));
        return null;
    }

    public void nameInputedInstruction() {
        Instructions.enterRuleName();
    }

    public void nameInputedStatus() {
        StatusReport.deleteRuleWaitingForName();
    }

    public void nameConfirmationInstruction() {
        Instructions.confirmation();
    }

    public void nameConfirmationStatus() {
        StatusReport.deleteRuleWaitingForConfirmation(rule.getName());
    }

    @Override
    public HandleCommandResult doHandleCommand(String commandString) {
        if (commandString.equals("cancel")) {
            VoiceManagerUtils.say(TTSConstants.RULE_CREATION_CANCELED);
            return new HandleCommandResult(BaseHandlerState.DEFAULT);
        }

        if (handlerState.tryHandleInstructions(commandString) || handlerState.tryHandleStatusReport(commandString)) {
            return null;
        }

        return handlerState.handleCommand(commandString);
    }
}
