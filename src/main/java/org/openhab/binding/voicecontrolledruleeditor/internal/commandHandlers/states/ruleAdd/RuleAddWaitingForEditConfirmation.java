package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleAdd;

import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleExternals.RuleAddingHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState;

public class RuleAddWaitingForEditConfirmation extends AbstractHandlerState {

    public RuleAddWaitingForEditConfirmation(ICommandHandler handler) {
        super(handler);
    }

    @Override
    public HandleCommandResult handleCommand(String commandString) {
        return ((RuleAddingHandler) handler).handleEditingConfirmation(commandString);
    }

    @Override
    public boolean tryHandleInstructions(String commandString) {
        if (super.tryHandleInstructions(commandString)) {
            ((RuleAddingHandler) handler).editingConfirmationStatus();
            return true;
        }

        return false;
    }

    @Override
    public boolean tryHandleStatusReport(String commandString) {
        if (super.tryHandleStatusReport(commandString)) {
            ((RuleAddingHandler) handler).editingConfirmationStatus();
            return true;
        }

        return false;
    }
}
