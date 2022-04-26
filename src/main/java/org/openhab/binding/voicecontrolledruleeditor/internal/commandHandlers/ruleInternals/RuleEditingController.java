package org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ruleInternals;

import java.util.Arrays;
import java.util.stream.Stream;

import org.openhab.binding.voicecontrolledruleeditor.internal.assistant.Instructions;
import org.openhab.binding.voicecontrolledruleeditor.internal.assistant.StatusReport;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.HandleCommandResult;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.ICommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.AbstractHandlerState.ConfirmationState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleEdit.RuleEditCreateModuleState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleEdit.RuleEditEditModuleState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleEdit.RuleEditRemoveModuleState;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.states.ruleEdit.RuleEditWaitingForEditTypeState;
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
        this.ruleId = ruleId;
        this.ruleRegistry = ruleRegistry;
        handlerState = new RuleEditWaitingForEditTypeState(this);
        VoiceManagerUtils
                .say(String.format(TTSConstants.EDITING_RULE + " " + TTSConstants.EDIT_RULE_WAITING_FOR_EDIT_TYPE,
                        ruleRegistry.get(ruleId).getName()));
    }

    public RuleEditingController(RuleRegistry ruleRegistry) {
        this.ruleRegistry = ruleRegistry;
        handlerState = new RuleEditWaitingForNameState(this);
        VoiceManagerUtils.say(TTSConstants.EDITTING_RULE_NAME);
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

        handlerState = new RuleEditWaitingForEditTypeState(this);
        VoiceManagerUtils.say(String.format(
                TTSConstants.EDITING_RULE + " " + TTSConstants.EDIT_RULE_WAITING_FOR_EDIT_TYPE, rule.getName()));
        return null;
    }

    public boolean validHandleType(String commandString) {
        var actionTypeValid = UserInputs.contains(UserInputs.CREATE_ARR, commandString)
                || UserInputs.contains(UserInputs.EDIT, commandString)
                || UserInputs.contains(UserInputs.REMOVE_ARR, commandString);

        var moduleTypeValid = UserInputs.contains(UserInputs.TRIGGER, commandString)
                || UserInputs.contains(UserInputs.ACTION, commandString)
                || UserInputs.contains(UserInputs.CONDITION, commandString);

        return actionTypeValid && moduleTypeValid;
    }

    public HandleCommandResult handleTypeInputed(String commandString) {
        if (!validHandleType(commandString)) {
            VoiceManagerUtils.say(TTSConstants.TYPE_INVALID);
            return null;
        }

        if (UserInputs.contains(UserInputs.TRIGGER, commandString)) {
            moduleBuilderHandler = new RuleTriggerBuilderHandler();
        } else if (UserInputs.contains(UserInputs.ACTION, commandString)) {
            moduleBuilderHandler = new RuleActionBuilder();
        } else if (UserInputs.contains(UserInputs.CONDITION, commandString)) {
            moduleBuilderHandler = new RuleConditionBuilder();
        } else {
            return null;
        }

        if (UserInputs.contains(UserInputs.CREATE_ARR, commandString)) {
            handlerState = new RuleEditCreateModuleState(this);
            VoiceManagerUtils
                    .say(String.format(TTSConstants.SELECT_MODULE_TYPE, moduleBuilderHandler.getModuleType().value));
        } else if (UserInputs.contains(UserInputs.EDIT, commandString)) {
            handlerState = new RuleEditEditModuleState(this);
            VoiceManagerUtils
                    .say(String.format(TTSConstants.INPUT_MODULE_LABEL, moduleBuilderHandler.getModuleType().value));
        } else if (UserInputs.contains(UserInputs.REMOVE_ARR, commandString)) {
            handlerState = new RuleEditRemoveModuleState(this);
            VoiceManagerUtils
                    .say(String.format(TTSConstants.INPUT_MODULE_LABEL, moduleBuilderHandler.getModuleType().value));
        } else {
            return null;
        }

        return null;
    }

    public HandleCommandResult handleEditBuilderCommand(String commandString) {
        if (!moduleBuilderHandler.isCreated()) {
            Module module = RuleRegistryUtils.getModuleFromLabelOrDescription(commandString, ruleRegistry.get(ruleId),
                    moduleBuilderHandler.getModuleType());
            if (module == null) {
                VoiceManagerUtils.say(TTSConstants.MODULE_NOT_FOUND);
                return null;
            }

            moduleBuilderHandler.createFromModule(module);
            VoiceManagerUtils.say(TTSConstants.ADD_CONFIGURATION);

            return null;
        }

        if (UserInputs.isEquals(UserInputs.COMPLETE, commandString)) {
            VoiceManagerUtils.say(String.format(TTSConstants.MODULE_CHANGED, moduleBuilderHandler.label));

            Rule rule = ruleRegistry.get(ruleId);
            rule = RuleRegistryUtils.ruleWithEditedModule(rule, moduleBuilderHandler.build(ruleId),
                    moduleBuilderHandler.getModuleType());

            ruleRegistry.update(rule);
            handlerState = new RuleEditWaitingForEditTypeState(this);
            VoiceManagerUtils.say(TTSConstants.EDIT_RULE_WAITING_FOR_EDIT_TYPE);
            return null;
        }

        ConfigurationResult configuration = ConfigurationUtils.extractConfigurationFromCommand(commandString);
        if (configuration == null || configuration.getValue() == null) {
            VoiceManagerUtils.say(String.format(TTSConstants.CONFIGURATION_NOT_FOUND, commandString));
            return null;
        }

        if (moduleBuilderHandler.canAddConfiguration(configuration.getType())) {
            moduleBuilderHandler.withConfiguration(configuration);
            VoiceManagerUtils.say(TTSConstants.OPERATION_SUCCESSFUL);
        }

        return null;
    }

    public HandleCommandResult handleRemoveBuilderCommand(String commandString) {
        if (!moduleBuilderHandler.isCreated()) {
            var rule = ruleRegistry.get(ruleId);
            var module = RuleRegistryUtils.getModuleFromLabelOrDescription(commandString, rule,
                    moduleBuilderHandler.getModuleType());

            if (module == null) {
                VoiceManagerUtils.say(TTSConstants.MODULE_NOT_FOUND);
                return null;
            }

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
                handlerState = new RuleEditWaitingForEditTypeState(this);
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
        if (!moduleBuilderHandler.isCreated()) {
            moduleBuilderHandler.createWithTypeFromCommand(commandString);
            if (moduleBuilderHandler.isCreated())
                VoiceManagerUtils.say(TTSConstants.ADD_LABEL);
            return null;
        }

        if (!moduleBuilderHandler.hasLabel()) {
            if (RuleRegistryUtils.moduleLabelAlreadyExists(ruleRegistry.get(ruleId), commandString,
                    moduleBuilderHandler.getModuleType())) {
                VoiceManagerUtils.say(TTSConstants.LABEL_ALREADY_EXISTS);
                return null;
            }
            moduleBuilderHandler.withLabel(commandString);
            VoiceManagerUtils.say(TTSConstants.ADD_CONFIGURATION);
            return null;
        }

        if (UserInputs.isEquals(UserInputs.COMPLETE, commandString)) {
            Rule rule = ruleRegistry.get(ruleId);
            rule = RuleRegistryUtils.ruleWithAddedModule(rule, moduleBuilderHandler.build(getNextModuleId(rule)),
                    moduleBuilderHandler.getModuleType());

            ruleRegistry.update(rule);
            handlerState = new RuleEditWaitingForEditTypeState(this);
            var stringFormat = TTSConstants.MODULE_CREATED;
            if (moduleBuilderHandler.requiredConfigurationsFilledIn()) {
                stringFormat = TTSConstants.NOT_ALL_REQUIRED_CONFIGURATIONS_FILLED_IN + " " + stringFormat;
            }

            VoiceManagerUtils.say(String.format(stringFormat, moduleBuilderHandler.label));
            return null;
        }

        ConfigurationResult configuration = ConfigurationUtils.extractConfigurationFromCommand(commandString);

        if (configuration == null || configuration.getValue() == null) {
            VoiceManagerUtils.say(String.format(TTSConstants.CONFIGURATION_NOT_FOUND, commandString));
            return null;
        }

        if (moduleBuilderHandler.canAddConfiguration(configuration.getType())) {
            moduleBuilderHandler.withConfiguration(configuration);
            VoiceManagerUtils.say(TTSConstants.OPERATION_SUCCESSFUL);
        }

        return null;
    }

    public void typeInputStatus() {
        StatusReport.editRuleTypeInput();
    }

    public void typeInputInstruction() {
        Instructions.editRuleTypeInput();
    }

    public void nameInputStatus() {
        StatusReport.editRuleNameInput();
    }

    public void nameInputInstruction() {
        Instructions.enterRuleName();
    }

    public void removeBuilderStatus() {
        if (!moduleBuilderHandler.isCreated()) {
            StatusReport.editModuleWaitingForLabel();
            return;
        }

        StatusReport.removeModuleDeleteConfirmation(moduleBuilderHandler.label);
    }

    public void removeBuilderInstruction(String commandString) {
        if (!moduleBuilderHandler.isCreated()) {
            Instructions.editModuleWaitingForLabel(commandString, ruleRegistry.get(ruleId),
                    moduleBuilderHandler.getModuleType());
            return;
        }

        Instructions.confirmation();
    }

    public void editBuilderStatus() {
        if (!moduleBuilderHandler.isCreated()) {
            StatusReport.editModuleWaitingForLabel();
            return;
        }

        StatusReport.waitingForModuleConfiguration();
    }

    public void editBuilderInstruction(String commandString) {
        if (!moduleBuilderHandler.isCreated()) {
            Instructions.editModuleWaitingForLabel(commandString, ruleRegistry.get(ruleId),
                    moduleBuilderHandler.getModuleType());
            return;
        }

        Instructions.editModuleWaitingForConfiguration(moduleBuilderHandler);
    }

    public void createBuilderStatus() {
        if (!moduleBuilderHandler.isCreated()) {
            StatusReport.editModuleWaitingForModuleType(moduleBuilderHandler.getModuleType().value);
            return;
        }

        if (!moduleBuilderHandler.hasLabel()) {
            StatusReport.waitingForLabel();
            return;
        }

        StatusReport.waitingForModuleConfiguration();
    }

    public void createBuilderInstruction(String commandString) {
        if (!moduleBuilderHandler.isCreated()) {
            Instructions.editModuleWaitingForModuleType(moduleBuilderHandler, commandString);
            return;
        }

        if (!moduleBuilderHandler.hasLabel()) {
            StatusReport.waitingForLabel();
            return;
        }

        if (moduleBuilderHandler.getMissingProperties().length != 0) {
            Instructions.editModuleWaitingForMissingConfiguration(moduleBuilderHandler);
        } else {
            Instructions.editModuleWaitingForConfiguration(moduleBuilderHandler);
        }
    }

    public HandleCommandResult doHandleCommand(String commandString) {
        if (commandString.equals(UserInputs.CANCEL)) {
            VoiceManagerUtils.say(TTSConstants.RULE_EDITING_CANCELED);
            return new HandleCommandResult(BaseHandlerState.DEFAULT);
        }

        if (commandString.equals(UserInputs.BACK)) {
            if (ruleId == null) {
                return null;
            }

            handlerState = new RuleEditWaitingForEditTypeState(this);
            VoiceManagerUtils
                    .say(String.format(TTSConstants.EDITING_RULE + " " + TTSConstants.EDIT_RULE_WAITING_FOR_EDIT_TYPE,
                            ruleRegistry.get(ruleId).getName()));
            return null;
        }

        if (handlerState.tryHandleInstructions(commandString) || handlerState.tryHandleStatusReport(commandString)) {
            return null;
        }

        return handlerState.handleCommand(commandString);
    }
}
