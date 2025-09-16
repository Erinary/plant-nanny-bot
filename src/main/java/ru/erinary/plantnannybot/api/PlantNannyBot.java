package ru.erinary.plantnannybot.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.erinary.plantnannybot.config.TelegramBotProperties;

@Component
public class PlantNannyBot extends TelegramLongPollingBot {

    private final TelegramBotProperties properties;

    @Autowired
    public PlantNannyBot(final TelegramBotProperties properties) {
        super(properties.getToken());
        this.properties = properties;
    }

    @Override
    public String getBotUsername() {
        return properties.getName();
    }

    @Override
    public void onUpdateReceived(final Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        System.out.println(user.getFirstName() + " wrote " + msg.getText());
    }
}
