package ru.erinary.plantnannybot.api.command.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.erinary.plantnannybot.api.BotCommand;
import ru.erinary.plantnannybot.api.BotMessages;
import ru.erinary.plantnannybot.api.command.CommandHandler;
import ru.erinary.plantnannybot.api.router.dto.IncomingMessage;
import ru.erinary.plantnannybot.api.router.dto.ReplyMessage;

@Component
public class StartCommandHandler implements CommandHandler {

    private final Logger logger = LoggerFactory.getLogger(StartCommandHandler.class);

    @Override
    public ReplyMessage handle(final IncomingMessage message) {
        var tgUser = message.user();
        logger.info("User {} started the bot", tgUser.getId());
        return new ReplyMessage(message.chatId(), BotMessages.INIT);
    }

    @Override
    public BotCommand command() {
        return BotCommand.START;
    }
}
