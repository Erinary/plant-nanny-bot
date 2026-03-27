package ru.erinary.plantnannybot.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Commands of the bot.
 */
public enum BotCommand {
    /**
     * Initial command for an interaction with the bot.
     */
    START("/start", false),

    /**
     * Creates and save a new user.
     */
    REGISTER("/register", false),

    /**
     * Returns user's plants.
     */
    PLANTS("/plants", true),

    /**
     * Starts a new conversation about adding a new plant.
     */
    ADD_PLANT("/addplant", true),

    /**
     * Cancels the current conversation.
     */
    CANCEL("/cancel", false),

    /**
     * Skips the current conversation step, if it's possible.
     */
    SKIP("/skip", false);

    private final String text;
    private final boolean requiresRegistration;
    private static final Map<String, BotCommand> COMMAND_MAP;

    static {
        Map<String, BotCommand> map = new HashMap<>();
        for (BotCommand command : values()) {
            map.put(command.text, command);
        }
        COMMAND_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * Creates a new {@link BotCommand} instance.
     *
     * @param text                 the command text
     * @param requiresRegistration true if the command requires user registration
     */
    BotCommand(final String text, final boolean requiresRegistration) {
        this.text = text;
        this.requiresRegistration = requiresRegistration;
    }

    /**
     * Returns the command text.
     *
     * @return the command text
     */
    public String getText() {
        return text;
    }

    /**
     * Returns true if the command requires user registration.
     *
     * @return true if the command requires user registration
     */
    public boolean isRequiresRegistration() {
        return requiresRegistration;
    }

    /**
     * Returns a {@link BotCommand} instance from a text value.
     *
     * @param text text value of the command
     * @return a {@link BotCommand} instance
     */
    public static BotCommand fromText(final String text) {
        return COMMAND_MAP.get(text);
    }
}