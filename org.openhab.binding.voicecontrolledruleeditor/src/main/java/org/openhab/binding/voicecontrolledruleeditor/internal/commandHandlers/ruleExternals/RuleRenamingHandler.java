package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleExternals;

import java.util.Arrays;

import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.BaseHandlerState;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.TTSConstants;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.UserInputs;
import org.openhab.core.automation.Rule;
import org.openhab.core.automation.RuleRegistry;
import org.openhab.core.automation.util.RuleBuilder;
import org.openhab.core.voice.VoiceManager;

public class RuleRenamingHandler implements ICommandHandler {
    private VoiceManager voiceManager;
    private RuleRegistry ruleRegistry;

    private HandlerState handlerState;
    private String oldRuleName;
    private String newRuleName;

    private enum HandlerState {
        WAITING_FOR_OLD_NAME,
        WAITING_FOR_NEW_NAME,
        WAITING_FOR_NAME_CONFIRMATION
    }

    public RuleRenamingHandler(VoiceManager voiceManager, RuleRegistry ruleRegistry) {
        this.voiceManager = voiceManager;
        this.ruleRegistry = ruleRegistry;
        handlerState = HandlerState.WAITING_FOR_OLD_NAME;

        initializeDialog();
    }

    private void initializeDialog() {
        voiceManager.say(TTSConstants.RENAME_OLD_RULE_NAME);
    }

    private boolean renameRule(String oldName, String newName) {
        Rule ruleToRename = ruleRegistry.getAll().stream().filter(rule -> rule.getName().equals(oldName)).findFirst()
                .get();

        Rule newRule = RuleBuilder.create(ruleToRename).withName(newName).build();
        Rule updateRule = ruleRegistry.update(newRule);
        return updateRule != null;
    }

    private HandleCommandResult handleOldNameInputed(String ruleName) {
        boolean doesRuleExist = ruleRegistry.getAll().stream().anyMatch((rule) -> ruleName.equals(rule.getName()));

        if (!doesRuleExist) {
            voiceManager.say(String.format(TTSConstants.RULE_NOT_FOUND, ruleName));
            return null;
        }

        oldRuleName = ruleName;
        voiceManager.say(String.format(TTSConstants.RENAMING_OLD_RULE, ruleName) + TTSConstants.NAME_RULE);
        handlerState = HandlerState.WAITING_FOR_NEW_NAME;
        return null;
    }

    private HandleCommandResult handleNewNameInputed(String ruleName) {
        boolean isNameValid = !ruleRegistry.getAll().stream().anyMatch((rule) -> ruleName.equals(rule.getName()));

        if (!isNameValid) {
            voiceManager.say(String.format(TTSConstants.RULE_ALREADY_EXISTS, ruleName));
            return null;
        }

        newRuleName = ruleName;
        handlerState = HandlerState.WAITING_FOR_NAME_CONFIRMATION;
        voiceManager.say(String.format(TTSConstants.CONFIRM_NEW_RULE_NAME, ruleName));
        return null;
    }

    private HandleCommandResult handleNewNameConfirmation(String command) {
        if (Arrays.stream(UserInputs.CONFIRM_ARRAY).anyMatch(confirmation -> confirmation.equals(command))) {
            boolean renameSuccessful = renameRule(oldRuleName, newRuleName);
            voiceManager.say(renameSuccessful ? TTSConstants.RULE_RENAMED : TTSConstants.ERROR_OCCURED);
            return new HandleCommandResult(BaseHandlerState.DEFAULT);
        }

        if (Arrays.stream(UserInputs.DENY_ARRAY).anyMatch(confirmation -> confirmation.equals(command))) {
            handlerState = HandlerState.WAITING_FOR_NEW_NAME;
            voiceManager.say(TTSConstants.NAME_RULE);
            return null;
        }

        voiceManager.say(String.format(TTSConstants.COMMAND_NOT_FOUND, command));
        return null;
    }

    public HandleCommandResult handleCommand(String commandString) {
        if (commandString.equals("cancel")) {
            voiceManager.say(TTSConstants.RULE_CREATION_CANCELED);
            return new HandleCommandResult(BaseHandlerState.DEFAULT);
        }

        switch (handlerState) {
            case WAITING_FOR_OLD_NAME:
                return handleOldNameInputed(commandString);
            case WAITING_FOR_NEW_NAME:
                return handleNewNameInputed(commandString);
            case WAITING_FOR_NAME_CONFIRMATION:
                return handleNewNameConfirmation(commandString);
            default:
                voiceManager.say(TTSConstants.ERROR_OCCURED);
                return new HandleCommandResult(BaseHandlerState.DEFAULT);
        }
    }
}
