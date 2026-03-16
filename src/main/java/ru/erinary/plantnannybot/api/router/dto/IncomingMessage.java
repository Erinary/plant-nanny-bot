package ru.erinary.plantnannybot.api.router.dto;

import org.telegram.telegrambots.meta.api.objects.User;

/**
 * DTO for an incoming message from Telegram.
 *
 * @param chatId    the unique ID of the chat
 * @param user      Telegram user
 * @param text      message text
 * @param isCommand true if the message is a command
 */
public record IncomingMessage(Long chatId, User user, String text, boolean isCommand) {
}
