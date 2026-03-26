package ru.erinary.plantnannybot.api.model;

/**
 * A model for a user's plant.
 *
 * @param name     plant's name
 * @param tgUserId user's telegram id
 * @param notes    plant's notes
 */
public record PlantModel(String name, Long tgUserId, String notes) {
}
