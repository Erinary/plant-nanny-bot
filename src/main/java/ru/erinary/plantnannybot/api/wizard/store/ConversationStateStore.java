package ru.erinary.plantnannybot.api.wizard.store;

import ru.erinary.plantnannybot.api.wizard.ConversationState;

import java.util.Optional;

/**
 * Abstraction for storing and retrieving ConversationState objects.
 */
public interface ConversationStateStore {

    /**
     * Retrieves the current conversation state for a given chat ID.
     *
     * @param chatID the unique identifier of the chat
     * @return an Optional containing the {@link ConversationState} if found
     */
    Optional<ConversationState> get(Long chatID);

    /**
     * Stores a conversation state.
     *
     * @param chatId the unique identifier of the chat
     * @param step   the {@link ConversationState} to be stored
     */
    void put(Long chatId, ConversationState step);

    /**
     * Clears the conversation state for a given chat ID.
     *
     * @param chatID the unique identifier of the chat
     */
    void clear(Long chatID);

}
