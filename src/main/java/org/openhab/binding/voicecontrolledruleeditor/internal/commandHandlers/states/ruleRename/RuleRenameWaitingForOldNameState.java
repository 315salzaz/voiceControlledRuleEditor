package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleRename;

import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleExternals.RuleRenamingHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState;

public class RuleRenameWaitingForOldNameState extends AbstractHandlerState {

    public RuleRenameWaitingForOldNameState(ICommandHandler handler) {
        super(handler);
    }

    @Override
    public HandleCommandResult handleCommand(String commandString) {
        return ((RuleRenamingHandler) handler).handleOldNameInputed(commandString);
    }

    @Override
    public boolean tryHandleInstructions(String commandString) {
        if (super.tryHandleInstructions(commandString)) {
            ((RuleRenamingHandler) handler).oldNameInstruction();
            return true;
        }

        return false;
    }

    @Override
    public boolean tryHandleStatusReport(String commandString) {
        if (super.tryHandleStatusReport(commandString)) {
            ((RuleRenamingHandler) handler).oldNameStatus();
            return true;
        }

        return false;
    }
}
