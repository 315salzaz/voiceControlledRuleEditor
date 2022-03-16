package org.openhab.binding.voicecontrolledruleeditor.internal;

import java.util.Date;

import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.DefaultController;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.core.voice.VoiceManager;

public class UserInformation {
    public final long handlerExpirationTime = 1000 * 60 * 5;
    private ICommandHandler commandHandler;

    private String deviceIdentifier;
    private Date lastHandlerInteraction;
    private VoiceManager voiceManager;

    public UserInformation(String deviceIdentifier, VoiceManager voiceManager) {
        this.deviceIdentifier = deviceIdentifier;
        this.lastHandlerInteraction = new Date();
        this.commandHandler = new DefaultController(voiceManager);
        this.voiceManager = voiceManager;
    }

    public ICommandHandler getCommandHandler() {
        if (new Date().getTime() > (lastHandlerInteraction.getTime() + handlerExpirationTime)) {
            commandHandler = new DefaultController(voiceManager);
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
