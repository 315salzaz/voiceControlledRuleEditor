package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals;

import org.openhab.core.automation.Module;

public interface IBuilder {
    IBuilder handleCommand(String commandString);
    Module getBuiltObject();
}
