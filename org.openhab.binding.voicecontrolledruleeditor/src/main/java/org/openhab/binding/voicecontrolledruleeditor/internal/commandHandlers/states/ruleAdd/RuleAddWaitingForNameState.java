package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleAdd;

import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleExternals.RuleAddingHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState;

public class RuleAddWaitingForNameState extends AbstractHandlerState {

    public RuleAddWaitingForNameState(ICommandHandler handler) {
        super(handler);
    }

    @Override
    public HandleCommandResult handleCommand(String commandString) {
        return ((RuleAddingHandler) handler).handleNameInputed(commandString);
    }
}
