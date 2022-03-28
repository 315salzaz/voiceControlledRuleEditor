package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states;

import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;

public abstract class AbstractHandlerState {
    protected ICommandHandler handler;

    public AbstractHandlerState(ICommandHandler handler) {
        this.handler = handler;
    }

    public abstract HandleCommandResult handleCommand(String commandString);
}
