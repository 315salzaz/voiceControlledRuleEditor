package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals;

import java.util.Arrays;
import java.util.stream.Stream;

import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleEdit.RuleEditCreateModuleState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleEdit.RuleEditEditModuleState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleEdit.RuleEditRemoveModuleState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleEdit.RuleEditWaitingForModuleTypeState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleEdit.RuleEditWaitingForNameState;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.BaseHandlerState;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.TTSConstants;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.UserInputs;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.ConfigurationResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.ConfigurationUtils;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.RuleRegistryUtils;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.StringUtils;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.VoiceManagerUtils;
import org.openhab.core.automation.Action;
import org.openhab.core.automation.Condition;
import org.openhab.core.automation.Module;
import org.openhab.core.automation.Rule;
import org.openhab.core.automation.RuleRegistry;
import org.openhab.core.automation.Trigger;
import org.openhab.core.automation.util.RuleBuilder;

public class RuleEditingController implements ICommandHandler {
    private RuleRegistry ruleRegistry;
    private String ruleId;
    private AbstractModuleBuilder moduleBuilderHandler;

    private AbstractHandlerState handlerState;

    public RuleEditingController(RuleRegistry ruleRegistry, String ruleId) {
        this(ruleRegistry);
        this.ruleId = ruleId;
        handlerState = new RuleEditWaitingForModuleTypeState(this);
    }

    public RuleEditingController(RuleRegistry ruleRegistry) {
        this.ruleRegistry = ruleRegistry;
        handlerState = new RuleEditWaitingForNameState(this);
        VoiceManagerUtils.say(TTSConstants.EDITTING_RULE_NAME);
    }

    public void changeState(AbstractHandlerState newState) {
        handlerState = newState;
    }

    private String getNextModuleId(Rule rule) {
        var actionIds = rule.getActions().stream().map(x -> x.getId());
        var triggerIds = rule.getTriggers().stream().map(x -> x.getId());
        var conditionIds = rule.getConditions().stream().map(x -> x.getId());

        var moduleIds = Stream.concat(actionIds, Stream.concat(triggerIds, conditionIds)).toArray();

        int currentIndex = 1;

        while (true) {
            String currentId = String.valueOf(currentIndex);

            if (!Arrays.stream(moduleIds).anyMatch(moduleId -> moduleId.equals(currentId)))
                return currentId;
            currentIndex++;
        }
    }

    private void createModule() {
        var moduleType = moduleBuilderHandler.getModuleType();
        Module module = moduleBuilderHandler.build(getNextModuleId(ruleRegistry.get(ruleId)));

        Rule ruleToCreateFrom = ruleRegistry.get(ruleId);
        if (ruleToCreateFrom == null)
            return;

        RuleBuilder ruleBuilder = RuleBuilder.create(ruleToCreateFrom);

        switch (moduleType) {
            case TRIGGER:
                // 315salzaz withTriggers should have existing ones as well.
                ruleBuilder.withTriggers((Trigger) module);
                ruleRegistry.update(ruleBuilder.build());
                return;
            case ACTION:
                // 315salzaz withActions should have existing ones as well.
                ruleBuilder.withActions((Action) module);
                ruleRegistry.update(ruleBuilder.build());
                return;
            case CONDITION:
                // 315salzaz withActions should have existing ones as well.
                ruleBuilder.withConditions((Condition) module);
                ruleRegistry.update(ruleBuilder.build());
            default:
                break;
        }
    }

    public HandleCommandResult handleNameInputed(String commandString) {
        var bestNameMatch = StringUtils.longestMatching(
                ruleRegistry.getAll().stream().map(r -> r.getName()).toArray(String[]::new), commandString);
        var rulesWithName = ruleRegistry.getAll().stream().filter(x -> x.getName().equals(bestNameMatch));
        Rule rule = rulesWithName.findFirst().orElse(null);

        if (rule == null) {
            VoiceManagerUtils.say(String.format(TTSConstants.RULE_NOT_FOUND, commandString));
            return null;
        }

        ruleId = rule.getUID();

        handlerState = new RuleEditWaitingForModuleTypeState(this);
        // 315salzaz Voice managers should get transferred to state ctros
        VoiceManagerUtils.say(String.format(TTSConstants.EDITING_RULE, rule.getName()));
        return null;
    }

    public HandleCommandResult handleTypeInputed(String commandString) {
        if (UserInputs.contains(UserInputs.CREATE_ARR, commandString)) {
            handlerState = new RuleEditCreateModuleState(this);
        } else if (UserInputs.contains(UserInputs.EDIT, commandString)) {
            handlerState = new RuleEditEditModuleState(this);
        } else if (UserInputs.contains(UserInputs.REMOVE_ARR, commandString)) {
            handlerState = new RuleEditRemoveModuleState(this);
        } else {
            return null;
        }

        if (UserInputs.contains(UserInputs.TRIGGER, commandString)) {
            moduleBuilderHandler = new RuleTriggerBuilderHandler();
            VoiceManagerUtils.say(TTSConstants.SELECT_TRIGGER_TYPE);
            return null;
        }
        if (UserInputs.contains(UserInputs.ACTION, commandString)) {
            moduleBuilderHandler = new RuleActionBuilder();
            VoiceManagerUtils.say(TTSConstants.SELECT_ACTION_TYPE);
            return null;
        }
        if (UserInputs.contains(UserInputs.CONDITION, commandString)) {
            moduleBuilderHandler = new RuleConditionBuilder();
            VoiceManagerUtils.say(TTSConstants.SELECT_CONDITION_TYPE);
            return null;
        }

        return null;
    }

    private boolean tryComplete(String commandString) {
        if (!UserInputs.isEquals(UserInputs.COMPLETE, commandString))
            return false;

        createModule();
        VoiceManagerUtils.say(TTSConstants.MODULE_CREATED);
        handlerState = new RuleEditWaitingForModuleTypeState(this);
        return true;
    }

    public HandleCommandResult handleEditBuilderCommand(String commandString) {
        if (!moduleBuilderHandler.isCreated()) {
            Module module = RuleRegistryUtils.getModuleFromLabelOrDescription(commandString, ruleRegistry.get(ruleId),
                    moduleBuilderHandler.getModuleType());
            if (module == null) {
                return null;
            }

            moduleBuilderHandler.createFromModule(module);
        }

        if (tryComplete(commandString))
            return null;

        ConfigurationResult configuration = ConfigurationUtils.extractConfigurationFromCommand(commandString);
        if (configuration == null || configuration.getValue() == null) {
            VoiceManagerUtils.say(String.format(TTSConstants.CONFIGURATION_NOT_FOUND, commandString));
            return null;
        }

        if (moduleBuilderHandler.canAddConfiguration(configuration.getType()))
            moduleBuilderHandler.withConfiguration(configuration);

        return null;
    }

    public HandleCommandResult handleRemoveBuilderCommand(String commandString) {
        // Select module
        // Confirm

        return null;
    }

    // 315salzaz rename: handleCreateBuilderCommand
    public HandleCommandResult handleCreateBuilderCommand(String commandString) {
        // I'd like this to be like rule naming (with confirmation)
        if (!moduleBuilderHandler.isCreated()) {
            moduleBuilderHandler.createWithTypeFromCommand(commandString);
            if (moduleBuilderHandler.isCreated())
                VoiceManagerUtils.say(TTSConstants.ADD_LABEL);
            return null;
        }

        if (!moduleBuilderHandler.hasLabel()) {
            moduleBuilderHandler.withLabel(commandString);
            VoiceManagerUtils.say(TTSConstants.ADD_CONFIGURATION);
            return null;
        }

        if (tryComplete(commandString))
            return null;

        ConfigurationResult configuration = ConfigurationUtils.extractConfigurationFromCommand(commandString);

        if (configuration == null || configuration.getValue() == null) {
            VoiceManagerUtils.say(String.format(TTSConstants.CONFIGURATION_NOT_FOUND, commandString));
            return null;
        }

        if (moduleBuilderHandler.canAddConfiguration(configuration.getType()))
            moduleBuilderHandler.withConfiguration(configuration);

        return null;
    }

    public HandleCommandResult doHandleCommand(String commandString) {
        if (commandString.equals(UserInputs.CANCEL)) {
            VoiceManagerUtils.say(TTSConstants.RULE_EDITING_CANCELED);
            return new HandleCommandResult(BaseHandlerState.DEFAULT);
        }

        if (commandString.equals(UserInputs.BACK)) {
            VoiceManagerUtils.say(TTSConstants.SELECT_TRIGGER_TYPE);
            return null;
        }

        return handlerState.handleCommand(commandString);
    }
}
