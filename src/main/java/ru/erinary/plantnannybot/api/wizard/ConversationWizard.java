package ru.erinary.plantnannybot.api.wizard;

/**
 * Interface for conversation wizards.
 */
public interface ConversationWizard {

    /**
     * Starts a new conversation.
     *
     * @param chatId user's chat id
     * @return initial conversation state
     */
    ConversationState start(Long chatId);

    /**
     * Processes the next step of the conversation.
     *
     * @param state   current conversation state
     * @param message user's message
     * @return updated conversation state
     */
    ConversationState nextStep(ConversationState state, String message);

    /**
     * Returns the conversation mode supported by this wizard.
     *
     * @return conversation mode
     */
    ConversationMode supportedMode();

}
