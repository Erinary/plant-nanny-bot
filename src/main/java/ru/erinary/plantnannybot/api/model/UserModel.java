package ru.erinary.plantnannybot.api.model;

/**
 * A model for a Telegram user.
 *
 * @param tgUserId the unique ID of a Telegram user
 * @param chatId the unique ID of the chat
 */
public record UserModel(Long tgUserId, Long chatId) {
}
