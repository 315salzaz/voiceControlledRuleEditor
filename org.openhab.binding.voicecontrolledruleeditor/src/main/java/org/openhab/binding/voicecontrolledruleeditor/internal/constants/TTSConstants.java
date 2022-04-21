package org.openhab.binding.voicecontrolledruleeditor.internal.constants;

public class TTSConstants {
    public static String NAME_RULE = "What will be the name of your rule?";
    public static String RULE_ALREADY_EXISTS = "A rule with name %s already exists. Please try again.";
    public static String CONFIRM_NEW_RULE_NAME = "Rule name %s. Would you like to confirm?";
    public static String RULE_CREATION_CANCELED = "Rule creation canceled";
    public static String RULE_NOT_FOUND = "Rule %s not found. Please try again.";
    public static String RULE_CREATED_START_EDITING_CONFIRMATION = "Rule created. Would you like to start editing it?";
    public static String DELETE_RULE_NAME = "Which rule would you like to delete?";
    public static String EDITTING_RULE_NAME = "Which rule would you like to edit?";
    public static String RENAME_OLD_RULE_NAME = "Which rule would you like to rename?";
    public static String RENAMING_OLD_RULE = "Renaming rule %s.";
    public static String RULE_RENAMED = "Rule renamed.";
    public static String EDITING_RULE = "Editing rule %s.";
    public static String SELECT_EDITING_TYPE = "Would you like to edit action, trigger or conditon?";
    public static String RULE_EDITING_CANCELED = "Rule editing canceled";
    public static String EDIT_RULE_WAITING_FOR_EDIT_TYPE = "Please select rule editing type";

    public static String ADD_LABEL = "Please add label";
    public static String ERROR_OCCURED = "An error has occured.";
    public static String COMMAND_NOT_FOUND = "Command %s not found";
    public static String ERROR_NO_IDENTIFIER = "Error. Please add device identifier on your application";
    public static String TRIGGER_TYPE_NOT_FOUND = "Trigger type %s not found";
    public static String CONDITION_TYPE_NOT_FOUND = "Condition type %s not found";
    public static String ACTION_TYPE_NOT_FOUND = "Action type %s not found";
    public static String SELECT_MODULE_TYPE = "Please select %s type";
    public static String INPUT_MODULE_LABEL = "Please input %s label";
    public static String MODULE_NOT_FOUND = "Module not found";
    public static String LABEL_ALREADY_EXISTS = "Label already exists";

    public static String CONFIGURATION_NOT_FOUND = "Configuration %s not found";
    public static String ADD_CONFIGURATION = "Please add configuration. When finished say \"complete\"";
    public static String MODULE_CREATED = "Module %s created";
    public static String MODULE_CHANGED = "Module %s changed";
    public static String MODULE_DELETE_CONFIRMATION = "Module %s will be deleted, this action is permament would you like to confirm?";
    public static String MODULE_DELETED = "Module %s deleted";
    public static String OPERATION_SUCCESSFUL = "Operation successful";
    public static String TYPE_INVALID = "Type invalid";

    // Status reporting
    public static String STATUS_WAITING_FOR_COMMAND = "Waiting for command";
    public static String STATUS_CREATING_RULE_WAITING_FOR_RULE_NAME = "Creating rule, waiting for rule name";
    public static String STATUS_CONFIRM_NEW_RULE_NAME = CONFIRM_NEW_RULE_NAME;
    public static String STATUS_RULE_CREATED_START_EDITING_CONFIRMATION = RULE_CREATED_START_EDITING_CONFIRMATION;
    public static String STATUS_RENAME_RULE_OLD_NAME = RENAME_OLD_RULE_NAME;
    public static String STATUS_RENAME_RULE_NEW_NAME = NAME_RULE;
    public static String STATUS_RENAME_RULE_NEW_NAME_CONFIRMATION = CONFIRM_NEW_RULE_NAME;
    public static String STATUS_EDIT_RULE_NAME_INPUT = EDITTING_RULE_NAME;
    public static String STATUS_EDIT_RULE_WAITING_FOR_EDIT_TYPE = EDIT_RULE_WAITING_FOR_EDIT_TYPE;

    public static String STATUS_EDIT_MODULE_WAITING_FOR_MODULE_TYPE = "Waiting for %s module type";
    public static String STATUS_EDIT_MODULE_WAITING_FOR_LABEL = "Waiting for label";
    public static String STATUS_EDIT_MODULE_WAITING_FOR_CONFIGURATION = "Waiting for configuration";

    // Instructions
    public static String INSTRUCTION_WAITING_FOR_COMMAND = "You can: create new rule, rename rule or begin editing";
    public static String INSTRUCTION_ENTER_RULE_NAME = "Enter rule name";
    public static String INSTRUCTION_EDIT_RULE_WAITING_FOR_EDIT_TYPE = "State the action, create, edit or remove and module type, trigger, action or condition. For example create trigger";

    public static String INSTRUCTION_EDIT_MODULE_WAITING_FOR_MODULE_TYPE = "Select between %s, for example tell me how %s";
    public static String INSTRUCTION_EDIT_MODULE_WAITING_FOR_LABEL = "Enter label";

    public static String INSTRUCTION_EDIT_MODULE_WAITING_FOR_MODULE_TYPE_FOR_GROUP = "Available commands %s";
    public static String ADD_OR_REMOVE_RULE_WITH_NAME = "Add or remove rule with a name";
    public static String ADD_OR_REMOVE_WEEKDAY = "Add or remove weekday";

    public static String INSTRUCTION_LIST_MISSING_CONFIGURATIONS = "Missing configurations are, %s";
    public static String INSTRUCTION_LIST_OF_AVAILABLE_CONFIGURATIONS = "Available configurations are, %s";
    public static String INSTRUCTION_CONFIRM_OR_DENY = "Confirm or deny";
}
