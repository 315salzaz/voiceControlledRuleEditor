package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals;

import java.util.Arrays;
import java.util.stream.Stream;

import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState.ConfirmationState;
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
import org.openhab.core.automation.Module;
import org.openhab.core.automation.Rule;
import org.openhab.core.automation.RuleRegistry;

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
        VoiceManagerUtils.say(TTSConstants.EDITTING_RULE_NAME); // <-- 315salzaz Gets called every time. Transfer to
                                                                // state prequisite
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

    public HandleCommandResult handleEditBuilderCommand(String commandString) {
        if (!moduleBuilderHandler.isCreated()) {
            Module module = RuleRegistryUtils.getModuleFromLabelOrDescription(commandString, ruleRegistry.get(ruleId),
                    moduleBuilderHandler.getModuleType());
            if (module == null) {
                return null;
            }

            moduleBuilderHandler.createFromModule(module);
            return null;
        }

        if (UserInputs.isEquals(UserInputs.COMPLETE, commandString)) {
            VoiceManagerUtils.say(String.format(TTSConstants.MODULE_CHANGED, moduleBuilderHandler.label));
            handlerState = new RuleEditWaitingForModuleTypeState(this);

            Rule rule = ruleRegistry.get(ruleId);
            rule = RuleRegistryUtils.ruleWithEditedModule(rule, moduleBuilderHandler.build(ruleId),
                    moduleBuilderHandler.getModuleType());

            ruleRegistry.update(rule);
            handlerState = new RuleEditWaitingForModuleTypeState(this);
            return null;
        }

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
        if (!moduleBuilderHandler.isCreated()) {
            var rule = ruleRegistry.get(ruleId);
            var module = RuleRegistryUtils.getModuleFromLabelOrDescription(commandString, rule,
                    moduleBuilderHandler.getModuleType());

            moduleBuilderHandler.createFromModule(module).withId(module.getId());
            handlerState.updateConfirmationState(ConfirmationState.CONFIRMING);
            VoiceManagerUtils.say(String.format(TTSConstants.MODULE_DELETE_CONFIRMATION, module.getLabel()));
            return null;
        }

        if (handlerState.getConfirmationState() == ConfirmationState.CONFIRMING) {
            if (UserInputs.isEquals(UserInputs.CONFIRM_ARRAY, commandString)) {
                var rule = ruleRegistry.get(ruleId);
                var module = moduleBuilderHandler.build(moduleBuilderHandler.getId());

                VoiceManagerUtils.say(String.format(TTSConstants.MODULE_DELETED, moduleBuilderHandler.label));
                rule = RuleRegistryUtils.ruleWithRemovedModule(rule, module, moduleBuilderHandler.getModuleType());

                if (rule == null) {
                    return null;
                }

                ruleRegistry.update(rule);

                handlerState.updateConfirmationState(ConfirmationState.DONE);
                handlerState = new RuleEditWaitingForModuleTypeState(this);
                return null;
            }

            if (UserInputs.isEquals(UserInputs.DENY_ARRAY, commandString)) {
                moduleBuilderHandler = null;
                handlerState.updateConfirmationState(ConfirmationState.NONE);
                return null;
            }
        }

        VoiceManagerUtils.say(TTSConstants.ERROR_OCCURED);
        return null;
    }

    public HandleCommandResult handleCreateBuilderCommand(String commandString) {
        // 315salzaz I'd like this to be like rule naming (with confirmation)
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

        if (UserInputs.isEquals(UserInputs.COMPLETE, commandString)) {
            Rule rule = ruleRegistry.get(ruleId);
            rule = RuleRegistryUtils.ruleWithAddedModule(rule, moduleBuilderHandler.build(getNextModuleId(rule)),
                    moduleBuilderHandler.getModuleType());

            ruleRegistry.update(rule);
            handlerState = new RuleEditWaitingForModuleTypeState(this);
            return null;
        }

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
