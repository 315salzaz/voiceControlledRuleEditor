package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleEdit;

import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals.RuleEditingController;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState;

public class RuleEditEditModuleState extends AbstractHandlerState {

    public RuleEditEditModuleState(ICommandHandler handler) {
        super(handler);
    }

    @Override
    public HandleCommandResult handleCommand(String commandString) {
        return ((RuleEditingController) handler).handleEditBuilderCommand(commandString);
    }

    @Override
    public boolean tryHandleInstructions(String commandString) {
        if (super.tryHandleInstructions(commandString)) {
            ((RuleEditingController) handler).editBuilderInstruction();
            return true;
        }

        return false;
    }

    @Override
    public boolean tryHandleStatusReport(String commandString) {
        if (super.tryHandleStatusReport(commandString)) {
            ((RuleEditingController) handler).editBuilderStatus();
            return true;
        }

        return false;
    }
}
