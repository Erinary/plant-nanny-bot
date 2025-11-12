package ru.erinary.plantnannybot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

/**
 * Represents a Telegram user.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "tg_user_id")
    private Long tgUserId;

    @Column(name = "tg_chat_id")
    private Long chatId;

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Creates a new {@link User} instance.
     *
     * @param tgUserId user's Telegram id
     * @param chatId   chat id
     */
    public User(final Long tgUserId, final Long chatId) {
        this.id = UUID.randomUUID();
        this.tgUserId = tgUserId;
        this.chatId = chatId;
    }

    public UUID getId() {
        return id;
    }

    public Long getTgUserId() {
        return tgUserId;
    }

    public Long getChatId() {
        return chatId;
    }
}
