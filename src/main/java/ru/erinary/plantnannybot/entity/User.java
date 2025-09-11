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

    /**
     * Default constructor.
     */
    protected User() {
    }

    /**
     * Creates a new {@link User} instance.
     *
     * @param tgUserId user's Telegram id
     */
    public User(final Long tgUserId) {
        this.id = UUID.randomUUID();
        this.tgUserId = tgUserId;
    }

    public UUID getId() {
        return id;
    }

    public Long getTgUserId() {
        return tgUserId;
    }
}
