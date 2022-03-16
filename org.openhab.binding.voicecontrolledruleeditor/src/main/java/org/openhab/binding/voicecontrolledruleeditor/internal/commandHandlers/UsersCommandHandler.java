package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.voicecontrolledruleeditor.internal.UserInformation;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleExternals.RuleAddingHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleExternals.RuleRenamingHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals.RuleEditingController;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.BaseHandlerState;
import org.openhab.core.automation.RuleRegistry;
import org.openhab.core.voice.VoiceManager;

public class UsersCommandHandler {

    private List<UserInformation> userInformations;
    private VoiceManager voiceManager;

    private static UsersCommandHandler instance;

    private static ICommandHandler createHandler(HandleCommandResult handleCommandResult, VoiceManager voiceManager,
            RuleRegistry ruleRegistry) {
        BaseHandlerState newHandlerState = handleCommandResult.getBaseHandlerState();
        switch (newHandlerState) {
            case ADDING_RULE:
                return new RuleAddingHandler(voiceManager, ruleRegistry);
            case RENAMING_RULE:
                return new RuleRenamingHandler(voiceManager, ruleRegistry);
            case DELETING_RULE:
            case EDITING_RULE:
                if(!handleCommandResult.getArg().isEmpty())
                    return new RuleEditingController(voiceManager, ruleRegistry, handleCommandResult.getArg());
                return new RuleEditingController(voiceManager, ruleRegistry);
            default:
                return new DefaultController(voiceManager);
        }
    }

    private UsersCommandHandler(VoiceManager voiceManager) {
        this.userInformations = new ArrayList<UserInformation>();
        this.voiceManager = voiceManager;
    }

    private UserInformation findUserInformation(String deviceIdentifier) {
        for (var i = 0; i < instance.userInformations.size(); i++) {
            if (instance.userInformations.get(i).getDeviceIdentifier().equals(deviceIdentifier))
                return instance.userInformations.get(i);
        }

        var newUserInformation = new UserInformation(deviceIdentifier, instance.voiceManager);
        instance.userInformations.add(newUserInformation);
        return newUserInformation;
    }

    public static void handleUserCommand(String commandString, String deviceIdentifier, VoiceManager voiceManager,
            RuleRegistry ruleRegistry) {
        if (instance == null) {
            instance = new UsersCommandHandler(voiceManager);
        }

        var userInformation = instance.findUserInformation(deviceIdentifier);

        var newBaseHandlerState = userInformation.getCommandHandler().handleCommand(commandString);
        if (newBaseHandlerState != null)
            userInformation.setCommandHandler(createHandler(newBaseHandlerState, voiceManager, ruleRegistry));
    }

    public static void activateFailSafe() {
        instance.userInformations.clear();
    }
}
