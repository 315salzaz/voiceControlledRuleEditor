package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ConfigurationType;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ModuleTypeValue;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.ConfigurationResult;
import org.openhab.core.automation.Module;

public abstract class AbstractModuleBuilder {
    protected String type = "";
    protected String label = "";
    protected AvailableConfigurationType[] availableConfigurations;
    private Map<String, Object> configurationProperties;

    public AbstractModuleBuilder() {
        configurationProperties = new HashMap<String, Object>();
    }

    public abstract AbstractModuleBuilder createWithTypeFromCommand(String commandString);

    public AbstractModuleBuilder createFromModule(Module module) {
        this.type = module.getTypeUID();
        this.configurationProperties = module.getConfiguration().getProperties();

        ModuleTypeValue moduleTypeValue = ModuleTypeValue.getFromValue(type);
        this.availableConfigurations = getAvailableConfigurationTypes(moduleTypeValue);

        return this;
    }

    protected abstract AvailableConfigurationType[] getAvailableConfigurationTypes(ModuleTypeValue moduleType);

    public abstract Enums.ModuleType getModuleType();

    public abstract Module build(String id);

    // 315salzaz Should get called by `build()` before actually building
    protected ConfigurationType[] getMissingProperties() {
        var missingPropertiesStream = Arrays.stream(availableConfigurations)
                .filter(ac -> ac.required && !configurationProperties.containsKey(ac.type.type)).map(x -> x.type);

        return missingPropertiesStream.toArray(ConfigurationType[]::new);
    }

    public final boolean canAddConfiguration(String configurationType) {
        return Arrays.stream(availableConfigurations).anyMatch(x -> x.type.type.contains(configurationType));
    }

    @SuppressWarnings("unchecked")
    public final AbstractModuleBuilder withConfiguration(ConfigurationResult configuration) {
        Object currentValue = configurationProperties.get(configuration.getType());

        switch (configuration.getConfigurationAction()) {
            case ADD:
                // 315salzaz TEST THIS A.S.A.P.
                if (currentValue == null) {
                    configurationProperties.put(configuration.getType(), List.of(configuration.getValue()));
                    break;
                }

                List<String> withAdded = (List<String>) currentValue;
                if (withAdded.contains(configuration.getValue()))
                    break;

                withAdded.add((String) configuration.getValue());

                configurationProperties.put(configuration.getType(), withAdded);
                break;
            case REMOVE:
                if (currentValue == null)
                    break;

                List<String> withRemoved = (List<String>) currentValue;
                withRemoved.remove(configuration.getValue());

                configurationProperties.put(configuration.getType(), withRemoved);
                break;
            case SET:
                configurationProperties.put(configuration.getType(), configuration.getValue());
                break;
            default:
                break;

        }
        return this;
    }

    public final AbstractModuleBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    public final boolean isCreated() {
        return !type.isEmpty();
    }

    public final boolean hasLabel() {
        return !label.isEmpty();
    }

    protected final Map<String, Object> getConfigurationProperties() {
        return configurationProperties;
    }
}
