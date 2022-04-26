package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ConfigurationType;

public class AvailableConfigurationType {
    public ConfigurationType type;
    public boolean required;
    public boolean canContainMultiple;
    public String instruction;

    public AvailableConfigurationType(ConfigurationType type, boolean required, String instruction) {
        this(type, required, instruction, false);
    }

    public AvailableConfigurationType(ConfigurationType type, boolean required, String instruction,
            boolean canContainMultiple) {
        this.type = type;
        this.required = required;
        this.instruction = instruction;
        this.canContainMultiple = canContainMultiple;
    }
}
