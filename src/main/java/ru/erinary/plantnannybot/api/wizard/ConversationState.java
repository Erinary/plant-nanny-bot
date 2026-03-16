package ru.erinary.plantnannybot.api.wizard;

/**
 * Represents the current conversational state of a user within a wizard flow.
 */
public class ConversationState {

    private final Long chatId;
    private final ConversationMode mode;
    private final String step;
    private final ConversationDraft draft;

    /**
     * Creates a new {@link ConversationState} instance.
     *
     * @param chatId user's chat id
     * @param mode   conversation mode
     * @param step   current step
     * @param draft  temporary data collected during the conversation
     */
    public ConversationState(final Long chatId,
                             final ConversationMode mode,
                             final String step,
                             final ConversationDraft draft) {
        this.chatId = chatId;
        this.mode = mode;
        this.step = step;
        this.draft = draft;
    }

    public Long getChatId() {
        return chatId;
    }

    public ConversationMode getMode() {
        return mode;
    }

    public String getStep() {
        return step;
    }

    public ConversationDraft getDraft() {
        return draft;
    }

}
