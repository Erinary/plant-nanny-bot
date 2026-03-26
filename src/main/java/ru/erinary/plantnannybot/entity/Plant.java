package ru.erinary.plantnannybot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a plant tracked by the user.
 */
@Entity
@Table(name = "plants")
public class Plant {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "last_watered_at")
    private Instant lastWateredAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "notes")
    private String notes;

    /**
     * Default constructor.
     */
    public Plant() {
    }

    /**
     * Creates a new {@link Plant} instance.
     *
     * @param name         plant's name
     * @param user         related {@link User}
     */
    public Plant(final String name, final User user, final String notes) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.user = user;
        this.notes = notes;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getLastWateredAt() {
        return lastWateredAt;
    }

    public void setLastWateredAt(final Instant lastWateredAt) {
        this.lastWateredAt = lastWateredAt;
    }

    public User getUser() {
        return user;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }
}
