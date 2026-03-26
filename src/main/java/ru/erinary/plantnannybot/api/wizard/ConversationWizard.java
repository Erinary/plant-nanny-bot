package ru.erinary.plantnannybot.api.wizard;

import ru.erinary.plantnannybot.api.router.dto.IncomingMessage;

/**
 * Interface for conversation wizards.
 */
public interface ConversationWizard {

    /**
     * Starts a new conversation.
     *
     * @param message user's message
     * @return initial conversation state
     */
    WizardStepResult start(IncomingMessage message);

    /**
     * Processes the next step of the conversation.
     *
     * @param state   current conversation state
     * @param message user's message
     * @return updated conversation state
     */
    WizardStepResult nextStep(ConversationState state, IncomingMessage message);

    /**
     * Returns the conversation mode supported by this wizard.
     *
     * @return conversation mode
     */
    ConversationMode supportedMode();

}
