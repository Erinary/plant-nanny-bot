package ru.erinary.plantnannybot.api.wizard;

/**
 * Represents a high-level conversational scenario (wizard) that is currently
 * active for a user session.
 */
public enum ConversationMode {

    /**
     * No active conversation.
     */
    NONE,

    /**
     * Add a new plant.
     */
    ADD_PLANT

}
