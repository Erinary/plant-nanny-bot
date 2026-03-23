package ru.erinary.plantnannybot.api.wizard;

/**
 * Result of a conversation wizard.
 *
 * @param replyText reply text to a user
 * @param nextState conversation state
 */
public record WizardStepResult(String replyText, ConversationState nextState) {
}
