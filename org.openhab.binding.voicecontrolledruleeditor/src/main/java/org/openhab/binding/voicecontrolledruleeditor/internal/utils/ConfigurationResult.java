package org.openhab.binding.voicecontrolledruleeditor.internal.utils;

public class ConfigurationResult {
    private final String type;
    private final Object value;

    public ConfigurationResult(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
