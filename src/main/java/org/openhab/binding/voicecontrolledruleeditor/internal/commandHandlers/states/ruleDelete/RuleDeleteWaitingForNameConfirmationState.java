package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleDelete;

import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleExternals.RuleDeletingHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState;

public class RuleDeleteWaitingForNameConfirmationState extends AbstractHandlerState {

    public RuleDeleteWaitingForNameConfirmationState(ICommandHandler handler) {
        super(handler);
    }

    @Override
    public HandleCommandResult handleCommand(String commandString) {
        return ((RuleDeletingHandler) handler).handleNameConfirmation(commandString);
    }

    @Override
    public boolean tryHandleInstructions(String commandString) {
        if (super.tryHandleInstructions(commandString)) {
            ((RuleDeletingHandler) handler).nameConfirmationInstruction();
            return true;
        }

        return false;
    }

    @Override
    public boolean tryHandleStatusReport(String commandString) {
        if (super.tryHandleStatusReport(commandString)) {
            ((RuleDeletingHandler) handler).nameConfirmationStatus();
            return true;
        }

        return false;
    }
}
