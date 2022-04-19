package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers;

import org.openhab.binding.voicecontrolledruleeditor.internal.assistant.Instructions;
import org.openhab.binding.voicecontrolledruleeditor.internal.assistant.StatusReport;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.BaseHandlerState;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.TTSConstants;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.UserInputs;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.VoiceManagerUtils;

public class DefaultController implements ICommandHandler {

    public DefaultController() {
    }

    @Override
    public HandleCommandResult doHandleCommand(String commandString) {
        if (StatusReport.isStatusReportCommand(commandString)) {
            StatusReport.waitingForCommand();
            return null;
        }

        if (Instructions.isInstructionsCommand(commandString)) {
            Instructions.waitingForCommand();
            return null;
        }

        if (commandString.contains(UserInputs.CREATE_NEW_RULE_ARR[0])
                && commandString.contains(UserInputs.CREATE_NEW_RULE_ARR[1]))
            return new HandleCommandResult(BaseHandlerState.ADDING_RULE);

        if (commandString.contains(UserInputs.RENAME_RULE[0]) && commandString.contains(UserInputs.RENAME_RULE[1]))
            return new HandleCommandResult(BaseHandlerState.RENAMING_RULE);

        if (commandString.contains(UserInputs.BEGIN_EDITING[0]) && commandString.contains(UserInputs.BEGIN_EDITING[1]))
            return new HandleCommandResult(BaseHandlerState.EDITING_RULE);

        VoiceManagerUtils.say(String.format(TTSConstants.COMMAND_NOT_FOUND, commandString));
        return null;
    }
}
