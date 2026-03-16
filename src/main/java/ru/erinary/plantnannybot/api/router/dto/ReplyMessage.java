package ru.erinary.plantnannybot.api.router.dto;

/**
 * DTO for a reply message.
 *
 * @param chatId the unique ID of the chat
 * @param text   message text
 */
public record ReplyMessage(Long chatId, String text) {
}
