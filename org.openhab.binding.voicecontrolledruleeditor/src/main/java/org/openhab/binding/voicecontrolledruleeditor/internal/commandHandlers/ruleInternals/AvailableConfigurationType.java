package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ConfigurationType;

public class AvailableConfigurationType {
    public ConfigurationType type;
    public boolean required;
    public boolean canContainMultiple;

    public AvailableConfigurationType(ConfigurationType type, boolean required) {
        this(type, required, false);
    }

    public AvailableConfigurationType(ConfigurationType type, boolean required, boolean canContainMultiple) {
        this.type = type;
        this.required = required;
        this.canContainMultiple = canContainMultiple;
    }
}
