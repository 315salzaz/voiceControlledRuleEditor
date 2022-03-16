package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.BaseHandlerState;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.TTSConstants;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.UserInputs;
import org.openhab.core.voice.VoiceManager;

public class DefaultController implements ICommandHandler {
    private VoiceManager voiceManager;

    public DefaultController(VoiceManager voiceManager) {
        this.voiceManager = voiceManager;
    }

    @Override
    public HandleCommandResult handleCommand(String commandString) {
        // For debug purpose. Remove after
        if (commandString.equals("status report")) {
            voiceManager.say("default controller");
            return null;
        }

        if (commandString.contains(UserInputs.CREATE_NEW_RULE_ARR[0])
                && commandString.contains(UserInputs.CREATE_NEW_RULE_ARR[1]))
            return new HandleCommandResult(BaseHandlerState.ADDING_RULE);

        if (commandString.contains(UserInputs.RENAME_RULE[0]) && commandString.contains(UserInputs.RENAME_RULE[1]))
            return new HandleCommandResult(BaseHandlerState.RENAMING_RULE);

        voiceManager.say(String.format(TTSConstants.COMMAND_NOT_FOUND, commandString));
        return null;
    }
}
