package ru.erinary.plantnannybot.api.command;

import ru.erinary.plantnannybot.api.router.MessageRoutingService;
import ru.erinary.plantnannybot.api.router.dto.IncomingMessage;
import ru.erinary.plantnannybot.api.router.dto.ReplyMessage;

/**
 * Interface for command handlers that work with one-shot commands (e.g., /start, /registers, etc.).
 */
public interface CommandHandler {

    /**
     * Handles a user's command.
     *
     * @param message user's message
     * @return reply message
     */
    ReplyMessage handle(IncomingMessage message);

    /**
     * Returns the command supported by this handler.
     *
     * @return command
     */
    MessageRoutingService.Command command();
}
