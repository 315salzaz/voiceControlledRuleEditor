package org.openhab.binding.voicecontrolledruleeditor.internal.utils;

import org.openhab.core.automation.RuleManager;

public class RuleManagerUtils {
    private static RuleManager ruleManager;

    public static void Prepare(RuleManager ruleManager) {
        if (RuleManagerUtils.ruleManager == null) {
            RuleManagerUtils.ruleManager = ruleManager;
        }
    }

    public static void runRule(String ruleId) {
        ruleManager.runNow(ruleId);
    }

    public static void changeRuleState(String ruleId) {
        ruleManager.setEnabled(ruleId, !getRuleState(ruleId));
    }

    public static boolean getRuleState(String ruleId) {
        return ruleManager.isEnabled(ruleId);
    }

    public static String getRuleStateStringValue(Boolean isEnabled) {
        if (isEnabled) {
            return "enabled";
        }

        return "disabled";
    }
}
