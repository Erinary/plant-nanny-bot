package ru.erinary.plantnannybot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.erinary.plantnannybot.api.PlantNannyBot;

/**
 * An initializer that registers a Telegram bot.
 */
@Component
public class BotInitializer {

    private static final Logger logger = LoggerFactory.getLogger(BotInitializer.class);

    private final PlantNannyBot plantNannyBot;

    /**
     * Creates a new {@link BotInitializer} instance.
     *
     * @param plantNannyBot bot to register
     */
    @Autowired
    public BotInitializer(final PlantNannyBot plantNannyBot) {
        this.plantNannyBot = plantNannyBot;
    }

    /**
     * Registers the bot when the application context is refreshed.
     */
    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(plantNannyBot);
        } catch (TelegramApiException e) {
            logger.error("Failed to initialize Telegram bot", e);
            throw new IllegalStateException("Failed to initialize Telegram bot", e);
        }
    }
}
