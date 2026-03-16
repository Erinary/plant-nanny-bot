package ru.erinary.plantnannybot.api;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.erinary.plantnannybot.api.router.MessageRoutingService;
import ru.erinary.plantnannybot.api.router.dto.IncomingMessage;
import ru.erinary.plantnannybot.api.router.dto.ReplyMessage;
import ru.erinary.plantnannybot.config.TelegramBotProperties;

/**
 * A realisation of a Telegram bot.
 */
@Component
public class PlantNannyBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(PlantNannyBot.class);

    private final TelegramBotProperties properties;
    private final MessageRoutingService messageRoutingService;

    /**
     * Creates a new {@link PlantNannyBot} instance.
     *
     * @param properties            bot's properties
     * @param messageRoutingService {@link MessageRoutingService}
     */
    @Autowired
    public PlantNannyBot(final TelegramBotProperties properties,
                         final MessageRoutingService messageRoutingService) {
        super(properties.getToken());
        this.properties = properties;
        this.messageRoutingService = messageRoutingService;
    }

    @Override
    public String getBotUsername() {
        return properties.getName();
    }

    @Override
    public void onUpdateReceived(final Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            var incomingMessage = new IncomingMessage(
                    message.getChatId(),
                    message.getFrom(),
                    message.getText(),
                    message.isCommand());
            var replyMessage = messageRoutingService.routeMessage(incomingMessage);
            if (StringUtils.isNotBlank(replyMessage.text())) {
                sendMessage(replyMessage);
            }
        }
    }

    private void sendMessage(final ReplyMessage message) {
        var sendMessage = SendMessage.builder()
                .chatId(message.chatId())
                .text(message.text())
                .build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Failed to send message to chatId {}", message.chatId(), e);
        }
    }

}
