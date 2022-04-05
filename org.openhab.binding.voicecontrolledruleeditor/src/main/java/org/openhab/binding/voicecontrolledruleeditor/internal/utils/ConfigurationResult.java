package org.openhab.binding.voicecontrolledruleeditor.internal.utils;

public class ConfigurationResult {
    private final String type;
    private final Object value;
    private final ConfigurationAction configurationAction;

    public enum ConfigurationAction {
        SET,
        REMOVE,
        ADD
    }

    public ConfigurationResult(String type, Object value, ConfigurationAction configurationAction) {
        this.type = type;
        this.value = value;
        this.configurationAction = configurationAction;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public ConfigurationAction getConfigurationAction() {
        return configurationAction;
    }
}
