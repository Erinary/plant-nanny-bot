package ru.erinary.plantnannybot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.erinary.plantnannybot.api.PlantNannyBot;

@Component
public class BotInitializer {

    private final PlantNannyBot plantNannyBot;

    @Autowired
    public BotInitializer(final PlantNannyBot plantNannyBot) {
        this.plantNannyBot = plantNannyBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(plantNannyBot);
        } catch (TelegramApiException e) {
            //TODO handle properly
            throw new RuntimeException(e);
        }
    }
}
