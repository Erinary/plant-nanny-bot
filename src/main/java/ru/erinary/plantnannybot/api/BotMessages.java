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

    public static final String ADD_PLANT_ASK_NOTES = """
            Thank you! Now, please provide some notes about your plant.
            Or, use /skip to skip this step or /cancel to cancel the operation.""";

    public static final String ADD_PLANT_SUCCESS = "Thank you! Your plant was added successfully.";

    /**
     * Unknown command.
     */
    public static final String UNKNOWN_COMMAND_ERROR = "Oh, I don't know this command";

    /**
     * Error message when user input is invalid.
     */
    public static final String INPUT_ERROR = "Sorry, I couldn't understand your input. Please try to use one of the commands.";

}
