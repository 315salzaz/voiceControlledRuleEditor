package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.BaseHandlerState;

public class HandleCommandResult {
    private final BaseHandlerState baseHandlerState;
    private final String arg;

    public HandleCommandResult(BaseHandlerState baseHandlerState) {
        this(baseHandlerState, "");
    }

    public HandleCommandResult(BaseHandlerState baseHandlerState, String arg) {
        this.baseHandlerState = baseHandlerState;
        this.arg = arg;
    }

    public BaseHandlerState getBaseHandlerState() {
        return baseHandlerState;
    }

    public String getArg() {
        return arg;
    }
}
