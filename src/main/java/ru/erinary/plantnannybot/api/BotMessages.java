package ru.erinary.plantnannybot.api;

/**
 * Static messages of the bot.
 */
public class BotMessages {

    /**
     * Welcome message.
     */
    public static final String INIT = """
            🌿 Welcome to PlantNanny!
            I'll help you remember to water your plants — so they stay happy and green 🌱
            
            Here's what you can do:
            /register – Register yourself to start using the bot
            /plants – View your plants and their watering schedules
            /addplant - Add a new plant to your list
            /cancel - Cancel the current action
            
            More features coming soon!
            """;

    /**
     * User registration was successful.
     */
    public static final String REGISTER_SUCCESS = "You’re successfully registered \uD83C\uDF31";

    /**
     * User is already registered.
     */
    public static final String REGISTER_ALREADY_EXISTS = "You’re already registered \uD83C\uDF31";

    /**
     * User has no plants.
     */
    public static final String EMPTY_PLANT_LIST = "You don't have any plants yet";

    /**
     * Add plant wizard messages.
     */
    public static final String ADD_PLANT_ASK_NAME = "Please give your plant a name, or you can use /cancel command";

    public static final String ADD_PLANT_NAME_NOT_EMPTY = "Plant name can't be empty, please try again";

    public static final String ADD_PLANT_ASK_NOTES = """
            Thank you! Now, please provide some notes about your plant.
            Or, use /skip to skip this step or /cancel to cancel the operation.""";

    public static final String ADD_PLANT_SUCCESS = "Thank you! Your plant was added successfully.";

    /**
     * Cancel current action.
     */
    public static final String CANCEL_ACTION = "Current action was cancelled";

    /**
     * Error message when there is no active action to cancel.
     */
    public static final String NO_ACTIVE_ACTION_ERROR = "There is no active action to cancel";

    /**
     * Error message when the user tries to start a new action while there is already an active one.
     */
    public static final String ACTION_IN_PROGRESS_ERROR = """
            You already have an action in progress.
            Please finish it first or send /cancel to cancel it.""";

    /**
     * Error message when there is no active conversation step to skip.
     */
    public static final String NO_ACTIVE_CONVERSATION_STEP_ERROR = "There is no active conversation step to skip";

    /**
     * Error message when the user tries to skip the step, but it's not possible.
     */
    public static final String STEP_SKIP_ERROR = "Current step can't be skipped, please try again";

    /**
     * Unknown command.
     */
    public static final String UNKNOWN_COMMAND_ERROR = "Oh, I don't know this command";

    /**
     * Unsupported command.
     */
    public static final String UNSUPPORTED_COMMAND_ERROR = "Oh, this command is not supported yet, sorry!";

    /**
     * Error message when the user tries to use a command that requires registration.
     */
    public static final String USER_MUST_BE_REGISTERED_ERROR = "You must be registered to use this command. Please use /register first.";

    /**
     * Error message when user input is invalid.
     */
    public static final String INPUT_ERROR = "Sorry, I couldn't understand your input. Please try to use one of the commands.";

}
