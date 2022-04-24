package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.defaultController;

import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.DefaultController;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState;

public class DefaultControllerRuleEnablementConfirmationState extends AbstractHandlerState {

    public DefaultControllerRuleEnablementConfirmationState(ICommandHandler handler) {
        super(handler);
    }

    @Override
    public HandleCommandResult handleCommand(String commandString) {
        return ((DefaultController) handler).handleRuleEnablementConfirmation(commandString);
    }

    @Override
    public boolean tryHandleInstructions(String commandString) {
        if (super.tryHandleInstructions(commandString)) {
            ((DefaultController) handler).confirmationInstruction();
            return true;
        }

        return false;
    }

    @Override
    public boolean tryHandleStatusReport(String commandString) {
        if (super.tryHandleStatusReport(commandString)) {
            ((DefaultController) handler).ruleEnablementConfirmationStatus();
            return true;
        }

        return false;
    }
}
