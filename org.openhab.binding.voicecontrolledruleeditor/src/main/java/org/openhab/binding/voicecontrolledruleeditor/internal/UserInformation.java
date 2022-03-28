package org.openhab.binding.voicecontrolledruleeditor.internal;

import java.util.Date;

import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.DefaultController;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;

public class UserInformation {
    public final long handlerExpirationTime = 1000 * 60 * 5;
    private ICommandHandler commandHandler;

    private String deviceIdentifier;
    private Date lastHandlerInteraction;

    public UserInformation(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
        this.lastHandlerInteraction = new Date();
        this.commandHandler = new DefaultController();
    }

    public ICommandHandler getCommandHandler() {
        if (new Date().getTime() > (lastHandlerInteraction.getTime() + handlerExpirationTime)) {
            commandHandler = new DefaultController();
        }

        lastHandlerInteraction = new Date();
        return commandHandler;
    }

    public void setCommandHandler(ICommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }
}
