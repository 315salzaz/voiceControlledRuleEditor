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

public class RuleAddingHandler implements ICommandHandler {
    public final BaseHandlerState handlerType = BaseHandlerState.ADDING_RULE;

    private VoiceManager voiceManager;
    private RuleRegistry ruleRegistry;

    private HandlerState handlerState;
    private String currentRuleName;
    private String currentRuleId;

    private enum HandlerState {
        WAITING_FOR_NEW_NAME,
        WAITING_FOR_NAME_CONFIRMATION,
        WAITING_FOR_EDITING_CONFIRMATION
    }

    public RuleAddingHandler(VoiceManager voiceManager, RuleRegistry ruleRegistry) {
        this.voiceManager = voiceManager;
        this.ruleRegistry = ruleRegistry;
        handlerState = HandlerState.WAITING_FOR_NEW_NAME;

        initializeDialog();
    }

    private void initializeDialog() {
        voiceManager.say(TTSConstants.NAME_RULE);
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

    private void addRule(String ruleName) {
        currentRuleId= getNextUID();

        Rule newRule = RuleBuilder.create(currentRuleId).withName(ruleName).build();
        ruleRegistry.add(newRule);
    }

    private HandleCommandResult handleNameInputed(String ruleName) {
        boolean isRuleNameValid = !ruleRegistry.getAll().stream().anyMatch((rule) -> ruleName.equals(rule.getName()));

        if (!isRuleNameValid) {
            voiceManager.say(String.format(TTSConstants.RULE_ALREADY_EXISTS, ruleName));
            return null;
        }

        currentRuleName = ruleName;
        handlerState = HandlerState.WAITING_FOR_NAME_CONFIRMATION;
        voiceManager.say(String.format(TTSConstants.CONFIRM_NEW_RULE_NAME, ruleName));
        return null;
    }

    private HandleCommandResult handleNameConfirmation(String command) {
        if (Arrays.stream(UserInputs.CONFIRM_ARRAY).anyMatch(confirmation -> confirmation.equals(command))) {
            addRule(currentRuleName);
            handlerState = HandlerState.WAITING_FOR_EDITING_CONFIRMATION;
            voiceManager.say(TTSConstants.RULE_CREATED_START_EDITING_CONFIRMATION);
            return null;
        }

        if (Arrays.stream(UserInputs.DENY_ARRAY).anyMatch(confirmation -> confirmation.equals(command))) {
            handlerState = HandlerState.WAITING_FOR_NEW_NAME;
            voiceManager.say(TTSConstants.NAME_RULE);
            return null;
        }

        voiceManager.say(String.format(TTSConstants.COMMAND_NOT_FOUND, command));
        return null;
    }

    private HandleCommandResult handleEditingConfirmation(String command) {
        if (Arrays.stream(UserInputs.CONFIRM_ARRAY).anyMatch(confirmation -> confirmation.equals(command)))
            return new HandleCommandResult(BaseHandlerState.EDITING_RULE, currentRuleId);

        if (Arrays.stream(UserInputs.DENY_ARRAY).anyMatch(confirmation -> confirmation.equals(command)))
            return new HandleCommandResult(BaseHandlerState.DEFAULT);

        voiceManager.say(String.format(TTSConstants.COMMAND_NOT_FOUND, command));
        return null;
    }

    public HandleCommandResult handleCommand(String commandString) {
        if (commandString.equals("cancel")) {
            voiceManager.say(TTSConstants.RULE_CREATION_CANCELED);
            return new HandleCommandResult(BaseHandlerState.DEFAULT);
        }

        switch (handlerState) {
            case WAITING_FOR_NEW_NAME:
                return handleNameInputed(commandString);
            case WAITING_FOR_NAME_CONFIRMATION:
                return handleNameConfirmation(commandString);
            case WAITING_FOR_EDITING_CONFIRMATION:
                return handleEditingConfirmation(commandString);
            default:
                voiceManager.say(TTSConstants.ERROR_OCCURED);
                return new HandleCommandResult(BaseHandlerState.DEFAULT);
        }
    }
}
