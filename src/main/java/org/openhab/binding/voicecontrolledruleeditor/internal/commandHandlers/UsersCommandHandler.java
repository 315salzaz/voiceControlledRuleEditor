package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.voicecontrolledruleeditor.internal.UserInformation;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleExternals.RuleAddingHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleExternals.RuleRenamingHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals.RuleEditingController;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.BaseHandlerState;
import org.openhab.core.automation.RuleRegistry;

public class UsersCommandHandler {

    private List<UserInformation> userInformations;

    private static UsersCommandHandler instance;

    private static ICommandHandler createHandler(HandleCommandResult handleCommandResult, RuleRegistry ruleRegistry) {
        BaseHandlerState newHandlerState = handleCommandResult.getBaseHandlerState();
        switch (newHandlerState) {
            case ADDING_RULE:
                return new RuleAddingHandler(ruleRegistry);
            case RENAMING_RULE:
                return new RuleRenamingHandler(ruleRegistry);
            case DELETING_RULE:
            case EDITING_RULE:
                if (!handleCommandResult.getArg().isEmpty())
                    return new RuleEditingController(ruleRegistry, handleCommandResult.getArg());
                return new RuleEditingController(ruleRegistry);
            default:
                return new DefaultController();
        }
    }

    private UsersCommandHandler() {
        this.userInformations = new ArrayList<UserInformation>();
    }

    private UserInformation findUserInformation(String deviceIdentifier) {
        for (var i = 0; i < instance.userInformations.size(); i++) {
            if (instance.userInformations.get(i).getDeviceIdentifier().equals(deviceIdentifier))
                return instance.userInformations.get(i);
        }

        var newUserInformation = new UserInformation(deviceIdentifier);
        instance.userInformations.add(newUserInformation);
        return newUserInformation;
    }

    public static void handleUserCommand(String commandString, String deviceIdentifier, RuleRegistry ruleRegistry) {
        if (instance == null) {
            instance = new UsersCommandHandler();
        }

        var userInformation = instance.findUserInformation(deviceIdentifier);

        var newBaseHandlerState = userInformation.getCommandHandler().doHandleCommand(commandString);
        if (newBaseHandlerState != null)
            userInformation.setCommandHandler(createHandler(newBaseHandlerState, ruleRegistry));
    }

    public static void activateFailSafe() {
        instance.userInformations.clear();
    }
}
