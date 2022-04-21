package org.openhab.binding.voicecontrolledruleeditor.internal.utils;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.voicecontrolledruleeditor.internal.constants.Enums.ModuleType;
import org.openhab.core.automation.Action;
import org.openhab.core.automation.Condition;
import org.openhab.core.automation.Module;
import org.openhab.core.automation.Rule;
import org.openhab.core.automation.RuleRegistry;
import org.openhab.core.automation.Trigger;
import org.openhab.core.automation.util.RuleBuilder;

public class RuleRegistryUtils {
    private static RuleRegistry ruleRegistry;

    private static Rule getRuleFromName(String expression) {
        String[] names = ruleRegistry.getAll().stream().map(r -> StringUtils.withoutSpaces(r.getName()))
                .toArray(String[]::new);
        String foundName = StringUtils.longestMatching(names, StringUtils.withoutSpaces(expression), true);
        if (foundName == null) {
            return null;
        }

        return ruleRegistry.getAll().stream().filter(r -> r.getName().equals(foundName)).findFirst().orElse(null);
    }

    private static Rule getRuleFromDescription(String expression) {
        String[] descriptions = ruleRegistry.getAll().stream().filter(r -> !r.getDescription().isEmpty())
                .map(r -> StringUtils.withoutSpaces(r.getDescription())).toArray(String[]::new);
        String foundDescription = StringUtils.longestMatching(descriptions, StringUtils.withoutSpaces(expression));
        if (foundDescription.isEmpty()) {
            return null;
        }

        return ruleRegistry.getAll().stream().filter(r -> r.getDescription().equals(foundDescription)).findFirst()
                .orElse(null);
    }

    public static Rule getRuleFromNameOrDescription(String expression) {
        Rule ruleFromName = getRuleFromName(expression);
        if (ruleFromName != null) {
            return ruleFromName;
        }

        return getRuleFromDescription(expression);
    }

    private static Action getActionFromLabelOrDescription(String expression, Rule rule) {
        String[] allLabels = rule.getActions().stream().map(a -> a.getLabel()).toArray(String[]::new);
        String[] allDescriptions = rule.getActions().stream().map(a -> a.getDescription()).toArray(String[]::new);

        var matchingLabel = StringUtils.longestMatching(allLabels, expression, true);
        if (matchingLabel != null)
            return rule.getActions().stream().filter(a -> a.getLabel().equals(matchingLabel)).findFirst().orElse(null);

        var matchingDescription = StringUtils.longestMatching(allDescriptions, expression, true);
        return rule.getActions().stream().filter(a -> a.getDescription().equals(matchingDescription)).findFirst()
                .orElse(null);
    }

    private static Condition getConditionFromLabelOrDescription(String expression, Rule rule) {
        String[] allLabels = rule.getConditions().stream().map(a -> a.getLabel()).toArray(String[]::new);
        String[] allDescriptions = rule.getConditions().stream().filter(a -> a.getDescription() != null)
                .map(a -> a.getDescription()).toArray(String[]::new);

        var matchingLabel = StringUtils.longestMatching(allLabels, expression, true);
        if (matchingLabel != null)
            return rule.getConditions().stream().filter(a -> a.getLabel().equals(matchingLabel)).findFirst()
                    .orElse(null);

        var matchingDescription = StringUtils.longestMatching(allDescriptions, expression, true);
        if (matchingDescription == null) {
            return null;
        }

        return rule.getConditions().stream().filter(a -> a.getDescription().equals(matchingDescription)).findFirst()
                .orElse(null);
    }

    private static Trigger getTriggerFromLabelOrDescription(String expression, Rule rule) {
        String[] allLabels = rule.getTriggers().stream().map(a -> a.getLabel()).toArray(String[]::new);
        String[] allDescriptions = rule.getTriggers().stream().map(a -> a.getDescription()).toArray(String[]::new);

        var matchingLabel = StringUtils.longestMatching(allLabels, expression, true);
        if (matchingLabel != null)
            return rule.getTriggers().stream().filter(a -> a.getLabel().equals(matchingLabel)).findFirst().orElse(null);

        var matchingDescription = StringUtils.longestMatching(allDescriptions, expression, true);
        return rule.getTriggers().stream().filter(a -> a.getDescription().equals(matchingDescription)).findFirst()
                .orElse(null);
    }

    public static <T extends Module> Module getModuleFromLabelOrDescription(String expression, Rule rule,
            ModuleType moduleType) {
        switch (moduleType) {
            case ACTION:
                return getActionFromLabelOrDescription(expression, rule);

            case CONDITION:
                return getConditionFromLabelOrDescription(expression, rule);

            case TRIGGER:
                return getTriggerFromLabelOrDescription(expression, rule);

            default:
                return null;
        }
    }

    public static Rule ruleWithAddedModule(Rule rule, Module module, ModuleType moduleType) {
        switch (moduleType) {
            case ACTION:
                List<Action> actions = new ArrayList<Action>(rule.getActions());
                actions.add((Action) module);
                return RuleBuilder.create(rule).withActions(actions).build();
            case CONDITION:
                List<Condition> conditions = new ArrayList<Condition>(rule.getConditions());
                conditions.add((Condition) module);
                return RuleBuilder.create(rule).withConditions(conditions).build();
            case TRIGGER:
                List<Trigger> triggers = new ArrayList<Trigger>(rule.getTriggers());
                triggers.add((Trigger) module);
                return RuleBuilder.create(rule).withTriggers(triggers).build();
        }

        return null;
    }

    public static Rule ruleWithEditedModule(Rule rule, Module module, ModuleType moduleType) {
        switch (moduleType) {
            case ACTION:
                List<Action> actions = rule.getActions();
                actions.removeIf(a -> a.getId().equals(module.getId()));
                actions.add((Action) module);
                return RuleBuilder.create(rule).withActions(actions).build();
            case CONDITION:
                List<Condition> conditions = rule.getConditions();
                conditions.removeIf(c -> c.getId().equals(module.getId()));
                conditions.add((Condition) module);
                return RuleBuilder.create(rule).withConditions(conditions).build();
            case TRIGGER:
                List<Trigger> triggers = rule.getTriggers();
                triggers.removeIf(t -> t.getId().equals(module.getId()));
                triggers.add((Trigger) module);
                return RuleBuilder.create(rule).withTriggers(triggers).build();
        }

        return null;
    }

    public static Rule ruleWithRemovedModule(Rule rule, Module module, ModuleType moduleType) {
        switch (moduleType) {
            case ACTION:
                List<Action> actions = rule.getActions();
                actions.removeIf(a -> a.getId().equals(module.getId()));
                return RuleBuilder.create(rule).withActions(actions).build();
            case CONDITION:
                List<Condition> conditions = rule.getConditions();
                conditions.removeIf(c -> c.getId().equals(module.getId()));
                return RuleBuilder.create(rule).withConditions(conditions).build();
            case TRIGGER:
                List<Trigger> triggers = rule.getTriggers();
                triggers.removeIf(t -> t.getId().equals(module.getId()));
                return RuleBuilder.create(rule).withTriggers(triggers).build();
        }

        return null;
    }

    public static boolean moduleLabelAlreadyExists(Rule rule, String label, ModuleType moduleType) {
        String labelWithoutSpaces = StringUtils.withoutSpaces(label);

        switch (moduleType) {
            case ACTION:
                return rule.getActions().stream()
                        .anyMatch(a -> StringUtils.withoutSpaces(a.getLabel()).equalsIgnoreCase(labelWithoutSpaces));
            case CONDITION:
                return rule.getConditions().stream()
                        .anyMatch(c -> StringUtils.withoutSpaces(c.getLabel()).equalsIgnoreCase(labelWithoutSpaces));
            case TRIGGER:
                return rule.getTriggers().stream()
                        .anyMatch(t -> StringUtils.withoutSpaces(t.getLabel()).equalsIgnoreCase(labelWithoutSpaces));
        }

        return false;
    }
}
