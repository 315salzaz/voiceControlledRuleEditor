package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states;

import org.openhab.binding.voicecontrolledruleeditor.internal.assistant.Instructions;
import org.openhab.binding.voicecontrolledruleeditor.internal.assistant.StatusReport;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;

public abstract class AbstractHandlerState {
    protected ICommandHandler handler;
    protected ConfirmationState confirmationState = ConfirmationState.NONE;

    public AbstractHandlerState(ICommandHandler handler) {
        this.handler = handler;
    }

    public static enum ConfirmationState {
        NONE,
        CONFIRMING,
        DONE
    }

    public ConfirmationState getConfirmationState() {
        return confirmationState;
    }

    public void updateConfirmationState(ConfirmationState newState) {
        confirmationState = newState;
    }

    public abstract HandleCommandResult handleCommand(String commandString);

    // Must override with super()
    public boolean tryHandleInstructions(String commandString) {
        return Instructions.isInstructionsCommand(commandString);
    }

    // Must override with super()
    public boolean tryHandleStatusReport(String commandString) {
        return StatusReport.isStatusReportCommand(commandString);
    }
}
