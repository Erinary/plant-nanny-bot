package ru.erinary.plantnannybot.api.command.register;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.erinary.plantnannybot.api.BotMessages;
import ru.erinary.plantnannybot.api.command.CommandHandler;
import ru.erinary.plantnannybot.api.model.UserModel;
import ru.erinary.plantnannybot.api.router.MessageRoutingService;
import ru.erinary.plantnannybot.api.router.dto.IncomingMessage;
import ru.erinary.plantnannybot.api.router.dto.ReplyMessage;
import ru.erinary.plantnannybot.service.exceptions.EntityAlreadyExistsException;
import ru.erinary.plantnannybot.service.user.UserService;

@Component
public class RegisterCommandHandler implements CommandHandler {

    private final Logger logger = LoggerFactory.getLogger(RegisterCommandHandler.class);
    private final UserService userService;

    /**
     * Creates a new {@link RegisterCommandHandler} instance.
     *
     * @param userService {@link UserService}
     */
    public RegisterCommandHandler(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public ReplyMessage handle(final IncomingMessage message) {
        var model = new UserModel(message.user().getId(), message.chatId());
        try {
            userService.save(model);
            logger.info("User {} was registered", message.user().getId());
            return new ReplyMessage(message.chatId(), BotMessages.REGISTER_SUCCESS);
        } catch (EntityAlreadyExistsException e) {
            logger.warn("Repeated registration attempt by user {}", message.user().getId());
            return new ReplyMessage(message.chatId(), BotMessages.REGISTER_ALREADY_EXISTS);
        }
    }

    @Override
    public MessageRoutingService.Command command() {
        return MessageRoutingService.Command.REGISTER;
    }
}