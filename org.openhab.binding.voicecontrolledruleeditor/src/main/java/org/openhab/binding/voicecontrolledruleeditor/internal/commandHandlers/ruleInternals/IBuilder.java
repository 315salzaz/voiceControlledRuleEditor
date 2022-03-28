package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.ConfigurationResult;
import org.openhab.core.automation.Module;

// 315salzaz lots of DRY. Could be abstract class
public interface IBuilder {
    boolean canAddConfiguration(String configurationType);

    IBuilder createWithTypeFromCommand(String commandString);

    IBuilder withConfiguration(ConfigurationResult configuration);

    IBuilder withLabel(String label);

    boolean isCreated();

    boolean hasLabel();

    Module build(String id);

    Enums.ModuleType getModuleType();
}
