package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleExternals;

import java.util.Arrays;

import org.openhab.binding.voicecontrolledruleeditor.internal.assistant.Instructions;
import org.openhab.binding.voicecontrolledruleeditor.internal.assistant.StatusReport;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleRename.RuleRenameWaitingForNameConfirmationState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleRename.RuleRenameWaitingForNewNameState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleRename.RuleRenameWaitingForOldNameState;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.BaseHandlerState;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.TTSConstants;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.UserInputs;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.RuleRegistryUtils;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.VoiceManagerUtils;
import org.openhab.core.automation.Rule;
import org.openhab.core.automation.RuleRegistry;
import org.openhab.core.automation.util.RuleBuilder;

public class RuleRenamingHandler implements ICommandHandler {
    private RuleRegistry ruleRegistry;

    private AbstractHandlerState handlerState;
    private String oldRuleName;
    private String newRuleName;

    public RuleRenamingHandler(RuleRegistry ruleRegistry) {
        this.ruleRegistry = ruleRegistry;
        handlerState = new RuleRenameWaitingForOldNameState(this);

        initializeDialog();
    }

    private void initializeDialog() {
        VoiceManagerUtils.say(TTSConstants.RENAME_OLD_RULE_NAME);
    }

    private boolean renameRule(String oldName, String newName) {
        Rule ruleToRename = ruleRegistry.getAll().stream().filter(rule -> rule.getName().equals(oldName)).findFirst()
                .get();

        Rule newRule = RuleBuilder.create(ruleToRename).withName(newName).build();
        Rule updateRule = ruleRegistry.update(newRule);
        return updateRule != null;
    }

    public HandleCommandResult handleOldNameInputed(String ruleName) {
        // 315salzaz This should actually get rule rather that bool and use it
        boolean doesRuleExist = null != RuleRegistryUtils.getRuleFromName(ruleName);

        if (!doesRuleExist) {
            VoiceManagerUtils.say(String.format(TTSConstants.RULE_NOT_FOUND, ruleName));
            return null;
        }

        oldRuleName = ruleName;
        handlerState = new RuleRenameWaitingForNewNameState(this);
        VoiceManagerUtils.say(String.format(TTSConstants.RENAMING_OLD_RULE, ruleName) + " " + TTSConstants.NAME_RULE);
        return null;
    }

    public HandleCommandResult handleNewNameInputed(String ruleName) {
        // 315salzaz fix all of this!!!
        boolean isNameValid = !ruleRegistry.getAll().stream().anyMatch((rule) -> ruleName.equals(rule.getName()));

        if (!isNameValid) {
            VoiceManagerUtils.say(String.format(TTSConstants.RULE_ALREADY_EXISTS, ruleName));
            return null;
        }

        newRuleName = ruleName;
        handlerState = new RuleRenameWaitingForNameConfirmationState(this);
        VoiceManagerUtils.say(String.format(TTSConstants.CONFIRM_NEW_RULE_NAME, ruleName));
        return null;
    }

    public HandleCommandResult handleNameConfirmation(String command) {
        if (Arrays.stream(UserInputs.CONFIRM_ARRAY).anyMatch(confirmation -> confirmation.equals(command))) {
            boolean renameSuccessful = renameRule(oldRuleName, newRuleName);
            VoiceManagerUtils.say(renameSuccessful ? TTSConstants.RULE_RENAMED : TTSConstants.ERROR_OCCURED);
            return new HandleCommandResult(BaseHandlerState.DEFAULT);
        }

        if (Arrays.stream(UserInputs.DENY_ARRAY).anyMatch(confirmation -> confirmation.equals(command))) {
            handlerState = new RuleRenameWaitingForNewNameState(this);
            VoiceManagerUtils.say(TTSConstants.NAME_RULE);
            return null;
        }

        VoiceManagerUtils.say(String.format(TTSConstants.COMMAND_NOT_FOUND, command));
        return null;
    }

    public void oldNameStatus() {
        StatusReport.renameRuleOldName();
    }

    public void oldNameInstruction() {
        Instructions.enterRuleName();
    }

    public void newNameStatus() {
        StatusReport.renameRuleNewName();
    }

    public void newNameInstruction() {
        Instructions.enterRuleName();
    }

    public void newNameConfirmationStatus() {
        StatusReport.renameRuleNewNameConfirmation();
    }

    public HandleCommandResult doHandleCommand(String commandString) {
        if (commandString.equals("cancel")) {
            VoiceManagerUtils.say(TTSConstants.RULE_CREATION_CANCELED);
            return new HandleCommandResult(BaseHandlerState.DEFAULT);
        }

        return handlerState.handleCommand(commandString);
    }
}
