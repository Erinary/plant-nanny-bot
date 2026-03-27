package ru.erinary.plantnannybot.api.wizard.store;

import org.springframework.stereotype.Component;
import ru.erinary.plantnannybot.api.wizard.ConversationState;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory implementation of {@link ConversationStateStore}.
 */
@Component
public class InMemoryConversationStateStore implements ConversationStateStore {

    private final ConcurrentMap<Long, ConversationState> storage;

    /**
     * Creates a new {@link InMemoryConversationStateStore} instance.
     */
    public InMemoryConversationStateStore() {
        this.storage = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<ConversationState> get(final Long chatId) {
        return Optional.ofNullable(storage.get(chatId));
    }

    @Override
    public void put(final Long chatId, final ConversationState step) {
        storage.put(chatId, step);
    }

    @Override
    public void clear(final Long chatId) {
        storage.remove(chatId);
    }

}
