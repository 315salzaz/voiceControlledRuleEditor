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
}
