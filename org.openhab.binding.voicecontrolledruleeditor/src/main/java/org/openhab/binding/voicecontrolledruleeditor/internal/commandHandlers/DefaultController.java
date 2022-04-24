package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers;

import org.openhab.binding.voicecontrolledruleeditor.internal.assistant.Instructions;
import org.openhab.binding.voicecontrolledruleeditor.internal.assistant.StatusReport;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.defaultController.DefaultControllerBaseState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.defaultController.DefaultControllerRuleEnablementConfirmationState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.defaultController.DefaultControllerRuleEnablementState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.defaultController.DefaultControllerRunRuleConfirmationState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.defaultController.DefaultControllerRunRuleNameInputState;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.BaseHandlerState;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.TTSConstants;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.UserInputs;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.RuleManagerUtils;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.RuleRegistryUtils;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.VoiceManagerUtils;
import org.openhab.core.automation.Rule;

public class DefaultController implements ICommandHandler {

    private AbstractHandlerState handlerState;
    private Rule rule;

    public DefaultController() {
        handlerState = new DefaultControllerBaseState(this);
    }

    public HandleCommandResult handleRunRuleNameInputed(String ruleName) {
        Rule ruleFromName = RuleRegistryUtils.getRuleFromName(ruleName);

        if (ruleFromName == null) {
            VoiceManagerUtils.say(String.format(TTSConstants.RULE_NOT_FOUND, ruleName));
            return null;
        }

        rule = ruleFromName;
        VoiceManagerUtils.say(String.format(TTSConstants.RUN_RULE_CONFIRM, ruleName));
        handlerState = new DefaultControllerRunRuleConfirmationState(this);
        return null;
    }

    public HandleCommandResult handleRunRuleConfirmation(String commandString) {
        // 315salzaz cancelation, command not found
        if (UserInputs.isEquals(UserInputs.CONFIRM_ARRAY, commandString)) {
            RuleManagerUtils.runRule(rule.getUID());
            handlerState = new DefaultControllerBaseState(this);
            VoiceManagerUtils.say(TTSConstants.RUN_RULE_INPUT_NAME);
            return null;
        }

        if (UserInputs.isEquals(UserInputs.DENY_ARRAY, commandString)) {
            handlerState = new DefaultControllerRunRuleNameInputState(this);
            VoiceManagerUtils.say(TTSConstants.RUN_RULE_INPUT_NAME);
            return null;
        }

        return null;
    }

    public HandleCommandResult handleRuleEnablementNameInputed(String ruleName) {
        Rule ruleFromName = RuleRegistryUtils.getRuleFromName(ruleName);

        if (ruleFromName == null) {
            VoiceManagerUtils.say(String.format(TTSConstants.RULE_NOT_FOUND, ruleName));
            return null;
        }

        rule = ruleFromName;

        var isRuleEnabled = RuleManagerUtils.getRuleState(rule.getUID());
        var followingStateStringValue = RuleManagerUtils.getRuleStateStringValue(!isRuleEnabled);
        VoiceManagerUtils.say(String.format(TTSConstants.RULE_ENABLEMENT_CONFIRMATION, ruleFromName.getName(),
                followingStateStringValue));
        handlerState = new DefaultControllerRuleEnablementConfirmationState(this);
        return null;
    }

    public HandleCommandResult handleRuleEnablementConfirmation(String commandString) {
        if (UserInputs.isEquals(UserInputs.CONFIRM_ARRAY, commandString)) {
            RuleManagerUtils.changeRuleState(rule.getUID());
            handlerState = new DefaultControllerBaseState(this);

            var newRuleState = RuleManagerUtils.getRuleState(rule.getUID());
            VoiceManagerUtils.say(String.format(TTSConstants.RULE_STATE_CHANGED, rule.getName(),
                    RuleManagerUtils.getRuleStateStringValue(newRuleState)));
            return null;
        }

        if (UserInputs.isEquals(UserInputs.DENY_ARRAY, commandString)) {
            handlerState = new DefaultControllerRuleEnablementState(this);
            VoiceManagerUtils.say(TTSConstants.RULE_ENABLEMENT_INPUT_NAME);
            return null;
        }

        return null;
    }

    public HandleCommandResult handleBaseCommand(String commandString) {
        if (commandString.contains(UserInputs.CREATE_NEW_RULE_ARR[0])
                && commandString.contains(UserInputs.CREATE_NEW_RULE_ARR[1]))
            return new HandleCommandResult(BaseHandlerState.ADDING_RULE);

        if (commandString.contains(UserInputs.RENAME_RULE[0]) && commandString.contains(UserInputs.RENAME_RULE[1]))
            return new HandleCommandResult(BaseHandlerState.RENAMING_RULE);

        if (commandString.contains(UserInputs.BEGIN_EDITING[0]) && commandString.contains(UserInputs.BEGIN_EDITING[1]))
            return new HandleCommandResult(BaseHandlerState.EDITING_RULE);

        if (commandString.contains(UserInputs.RUN_RULE)) {
            handlerState = new DefaultControllerRunRuleNameInputState(this);
            VoiceManagerUtils.say(TTSConstants.RUN_RULE_INPUT_NAME);
            return null;
        }

        if (commandString.contains(UserInputs.CHANGE_RULE_STATE)) {
            handlerState = new DefaultControllerRuleEnablementState(this);
            VoiceManagerUtils.say(TTSConstants.RULE_ENABLEMENT_INPUT_NAME);
            return null;
        }

        VoiceManagerUtils.say(String.format(TTSConstants.COMMAND_NOT_FOUND, commandString));
        return null;
    }

    public void waitingForCommandStatus() {
        StatusReport.waitingForCommand();
    }

    public void waitingForCommandInstruction() {
        Instructions.waitingForCommand();
    }

    public void runRuleNameStatus() {
        StatusReport.runRuleName();
    }

    public void enterRuleNameInstruction() {
        Instructions.enterRuleName();
    }

    public void confirmationInstruction() {
        Instructions.confirmation();
    }

    public void runRuleConfirmationStatus() {
        StatusReport.runRuleConfirmation(rule.getName());
    }

    public void ruleEnablementNameStatus() {
        StatusReport.ruleEnablementName();
    }

    public void ruleEnablementConfirmationStatus() {
        StatusReport.ruleEnablementConfirmation(rule.getName(), !RuleManagerUtils.getRuleState(rule.getUID()));
    }

    @Override
    public HandleCommandResult doHandleCommand(String commandString) {
        if (handlerState.tryHandleInstructions(commandString) || handlerState.tryHandleStatusReport(commandString)) {
            return null;
        }

        return handlerState.handleCommand(commandString);
    }
}
