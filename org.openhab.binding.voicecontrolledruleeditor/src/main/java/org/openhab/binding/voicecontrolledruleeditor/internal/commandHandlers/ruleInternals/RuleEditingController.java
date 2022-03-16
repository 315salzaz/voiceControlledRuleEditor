package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.BaseHandlerState;

import java.util.Arrays;

import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.TTSConstants;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.UserInputs;
import org.openhab.core.automation.RuleRegistry;
import org.openhab.core.voice.VoiceManager;

public class RuleEditingController implements ICommandHandler {
    private VoiceManager voiceManager;
    private RuleRegistry ruleRegistry;
    private String ruleId;
    private IBuilder builderHandler;

    // an iterator instead of this may help
    private enum HandlerState {
        WAITING_FOR_RULE_NAME,
        WAITING_FOR_TYPE,
        BUILDING_TRIGGER,
        BUILDING_ACTION,
        BUILDING_CONDITION
    }

    private HandlerState handlerState;

    public RuleEditingController(VoiceManager voiceManager, RuleRegistry ruleRegistry, String ruleId) {
        this(voiceManager, ruleRegistry);
        this.ruleId = ruleId;
        handlerState = HandlerState.WAITING_FOR_TYPE;

    }

    public RuleEditingController(VoiceManager voiceManager, RuleRegistry ruleRegistry) {
        this.voiceManager = voiceManager;
        this.ruleRegistry = ruleRegistry;
        handlerState = HandlerState.WAITING_FOR_RULE_NAME;
    }

    private HandleCommandResult handleNameInputed(String commandString) {
        var rulesWithName = ruleRegistry.getAll().stream().filter(x -> x.getName().equals(commandString));
        if (rulesWithName.findFirst().isEmpty()) {
            voiceManager.say(String.format(TTSConstants.RULE_NOT_FOUND, commandString));
            return null;
        }

        ruleId = rulesWithName.findFirst().get().getUID();
        handlerState = HandlerState.WAITING_FOR_TYPE;
        voiceManager.say(String.format(TTSConstants.EDITING_RULE, rulesWithName.findFirst().get().getName()));
        return null;
    }

    private HandleCommandResult handleTypeInputed(String commandString) {
        if (Arrays.stream(UserInputs.CREATE_ARR).anyMatch(x -> commandString.contains(x))
                && commandString.contains(UserInputs.TRIGGER)) {
            handlerState = HandlerState.BUILDING_TRIGGER;
            builderHandler = new RuleTriggerBuilderHandler(voiceManager, ruleRegistry);
            return null;
        }
        return null;
    }

    private HandleCommandResult handleBuilderCommand(String commandString)

    public HandleCommandResult handleCommand(String commandString) {
        if (commandString.equals(UserInputs.CANCEL)) {
            voiceManager.say(TTSConstants.RULE_EDITING_CANCELED);
            return new HandleCommandResult(BaseHandlerState.DEFAULT);
        }

        switch (handlerState) {
            case WAITING_FOR_RULE_NAME:
                return handleNameInputed(commandString);
            case WAITING_FOR_TYPE:
                return handleTypeInputed(commandString);
            case BUILDING_TRIGGER:
                return builderHandler.handleBuilderCommand(commandString);

            // A factory for T.A.C (not sure if it's good. Think about it)
            // Actually using builder here and building on top of T.A.C may be a good idea

            default:
                voiceManager.say(TTSConstants.ERROR_OCCURED);
                return new HandleCommandResult(BaseHandlerState.DEFAULT);
        }
    }
}
