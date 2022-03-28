package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleRename;

import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleExternals.RuleRenamingHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState;

public class RuleRenameWaitingForNewNameState extends AbstractHandlerState {

    public RuleRenameWaitingForNewNameState(ICommandHandler handler) {
        super(handler);
    }

    @Override
    public HandleCommandResult handleCommand(String commandString) {
        return ((RuleRenamingHandler) handler).handleNewNameInputed(commandString);
    }
}
