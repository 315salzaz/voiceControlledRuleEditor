package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleAdd;

import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleExternals.RuleAddingHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState;

public class RuleAddWaitingForNameConfirmationState extends AbstractHandlerState {

    public RuleAddWaitingForNameConfirmationState(ICommandHandler handler) {
        super(handler);
    }

    @Override
    public HandleCommandResult handleCommand(String commandString) {
        return ((RuleAddingHandler) handler).handleNameConfirmation(commandString);
    }

    @Override
    public boolean tryHandleInstructions(String commandString) {
        if (super.tryHandleInstructions(commandString)) {
            ((RuleAddingHandler) handler).nameConfirmationStatus();
            return true;
        }

        return false;
    }

    @Override
    public boolean tryHandleStatusReport(String commandString) {
        if (super.tryHandleStatusReport(commandString)) {
            ((RuleAddingHandler) handler).nameConfirmationStatus();
            return true;
        }

        return false;
    }
}
