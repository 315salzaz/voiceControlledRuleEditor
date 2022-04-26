package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleExternals;

import java.util.Arrays;

import org.openhab.binding.voicecontrolledruleeditor.internal.assistant.Instructions;
import org.openhab.binding.voicecontrolledruleeditor.internal.assistant.StatusReport;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleAdd.RuleAddWaitingForEditConfirmation;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleAdd.RuleAddWaitingForNameConfirmationState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleAdd.RuleAddWaitingForNameState;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.BaseHandlerState;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.TTSConstants;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.UserInputs;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.RuleRegistryUtils;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.VoiceManagerUtils;
import org.openhab.core.automation.Rule;
import org.openhab.core.automation.RuleRegistry;
import org.openhab.core.automation.util.RuleBuilder;

public class RuleAddingHandler implements ICommandHandler {
    public final BaseHandlerState handlerType = BaseHandlerState.ADDING_RULE;

    private RuleRegistry ruleRegistry;

    private AbstractHandlerState handlerState;
    private String currentRuleName;
    private String currentRuleId;

    public RuleAddingHandler(RuleRegistry ruleRegistry) {
        this.ruleRegistry = ruleRegistry;
        handlerState = new RuleAddWaitingForNameState(this);
        VoiceManagerUtils.say(TTSConstants.NAME_RULE);
    }

    private String getNextUID() {
        int currentIndex = 1;
        while (true) {
            String currentName = String.format("VoiceControlled.Rule.%d", currentIndex);

            boolean isIdValid = ruleRegistry.getAll().stream().map(Rule::getUID)
                    .allMatch(uid -> !uid.equals(currentName));

            if (isIdValid)
                break;

            currentIndex++;
        }

        return String.format("VoiceControlled.Rule.%d", currentIndex);
    }

    // 315salzaz transfer to ruleRegistry utils?
    private void addRule(String ruleName) {
        currentRuleId = getNextUID();

        Rule newRule = RuleBuilder.create(currentRuleId).withName(ruleName).build();
        ruleRegistry.add(newRule);
    }

    public HandleCommandResult handleNameInputed(String ruleName) {
        boolean isRuleNameValid = null == RuleRegistryUtils.getRuleFromName(ruleName);

        if (!isRuleNameValid) {
            VoiceManagerUtils.say(String.format(TTSConstants.RULE_ALREADY_EXISTS, ruleName));
            return null;
        }

        currentRuleName = ruleName;
        handlerState = new RuleAddWaitingForNameConfirmationState(this);
        StatusReport.waitingForRuleNameConfirmation(currentRuleName);
        return null;
    }

    public HandleCommandResult handleNameConfirmation(String command) {
        if (Arrays.stream(UserInputs.CONFIRM_ARRAY).anyMatch(confirmation -> confirmation.equals(command))) {
            addRule(currentRuleName);

            handlerState = new RuleAddWaitingForEditConfirmation(this);
            VoiceManagerUtils.say(TTSConstants.RULE_CREATED_START_EDITING_CONFIRMATION);
            return null;
        }

        if (Arrays.stream(UserInputs.DENY_ARRAY).anyMatch(confirmation -> confirmation.equals(command))) {
            handlerState = new RuleAddWaitingForNameState(this);
            VoiceManagerUtils.say(TTSConstants.NAME_RULE);
            return null;
        }

        VoiceManagerUtils.say(String.format(TTSConstants.COMMAND_NOT_FOUND, command));
        return null;
    }

    public HandleCommandResult handleEditingConfirmation(String command) {
        if (Arrays.stream(UserInputs.CONFIRM_ARRAY).anyMatch(confirmation -> confirmation.equals(command)))
            return new HandleCommandResult(BaseHandlerState.EDITING_RULE, currentRuleId);

        if (Arrays.stream(UserInputs.DENY_ARRAY).anyMatch(confirmation -> confirmation.equals(command)))
            return new HandleCommandResult(BaseHandlerState.DEFAULT);

        VoiceManagerUtils.say(String.format(TTSConstants.COMMAND_NOT_FOUND, command));
        return null;
    }

    public void nameInputStatus() {
        StatusReport.createRuleWaitingForName();
    }

    public void nameInputInstruction() {
        // 315salzaz read out rule names?
        Instructions.enterRuleName();
    }

    public void nameConfirmationStatus() {
        StatusReport.waitingForRuleNameConfirmation(currentRuleName);
    }

    public void editingConfirmationStatus() {
        StatusReport.waitingForEditingConfirmation();
    }

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
