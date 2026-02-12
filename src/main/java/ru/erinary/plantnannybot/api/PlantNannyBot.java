package ru.erinary.plantnannybot.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.erinary.plantnannybot.api.model.UserModel;
import ru.erinary.plantnannybot.config.TelegramBotProperties;
import ru.erinary.plantnannybot.service.exceptions.EntityAlreadyExistsException;
import ru.erinary.plantnannybot.service.plant.PlantService;
import ru.erinary.plantnannybot.service.user.UserService;

import java.util.Arrays;

/**
 * A realisation of a Telegram bot.
 */
@Component
public class PlantNannyBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(PlantNannyBot.class);

    private final TelegramBotProperties properties;

    private final UserService userService;

    private final PlantService plantService;

    /**
     * Creates a new {@link PlantNannyBot} instance.
     *
     * @param properties  bot's properties
     * @param userService {@link UserService}
     * @param plantService {@link PlantService}
     */
    @Autowired
    public PlantNannyBot(final TelegramBotProperties properties,
                         final UserService userService,
                         final PlantService plantService) {
        super(properties.getToken());
        this.properties = properties;
        this.userService = userService;
        this.plantService = plantService;
    }

    @Override
    public String getBotUsername() {
        return properties.getName();
    }

    @Override
    public void onUpdateReceived(final Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var msg = update.getMessage();
            if (msg.isCommand()) {
                var command = Arrays.stream(Command.values())
                        .filter(c -> c.text().equals(msg.getText()))
                        .findFirst()
                        .orElse(null);
                if (command != null) {
                    switch (command) {
                        case START -> handleStart(msg);
                        case REGISTER -> handleRegister(msg);
                        case PLANTS -> handlePlants(msg);
                        default -> logger.warn("Unknown command: {}", msg.getText());
                    }
                }
            }
        }
    }

    private void sendMessage(final Long chatId, final String text) {
        var sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Failed to send message to chatId {}", chatId, e);
        }
    }

    private void handleStart(final Message msg) {
        var tgUser = msg.getFrom();
        logger.info("User {} started the bot", tgUser.getId());
        sendMessage(msg.getChatId(), BotMessages.INIT);
    }

    private void handleRegister(final Message msg) {
        var model = new UserModel(msg.getFrom().getId(), msg.getChatId());
        try {
            userService.save(model);
            logger.info("User {} was registered", msg.getFrom().getId());
            sendMessage(msg.getChatId(), BotMessages.REGISTER_SUCCESS);
        } catch (EntityAlreadyExistsException e) {
            logger.warn("Repeated registration attempt by user {}", msg.getFrom().getId());
            sendMessage(msg.getChatId(), BotMessages.REGISTER_ALREADY_EXISTS);
        }
    }

    private void handlePlants(final Message msg) {
        var tgUser = msg.getFrom();
        logger.info("User {} requested plants", tgUser.getId());
        var plants = plantService.getUserPlants(tgUser.getId());
        if (plants.isEmpty()) {
            sendMessage(msg.getChatId(), BotMessages.EMPTY_PLANT_LIST);
        } else {
            //TODO
            sendMessage(msg.getChatId(), "It's under development!");
        }
    }

    /**
     * Commands of the bot.
     */
    public enum Command {

        /**
         * Initial command for an interaction with the bot.
         */
        START("/start"),

        /**
         * Creates and save a new user.
         */
        REGISTER("/register"),

        /**
         * Returns user's plants.
         */
        PLANTS("/plants");

        private final String text;

        Command(final String text) {
            this.text = text;
        }

        /**
         * Returns a text value of the command.
         *
         * @return a text value of the command
         */
        public String text() {
            return text;
        }
    }
}
