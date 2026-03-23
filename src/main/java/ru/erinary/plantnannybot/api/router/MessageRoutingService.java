package ru.erinary.plantnannybot.api.router;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.erinary.plantnannybot.api.BotMessages;
import ru.erinary.plantnannybot.api.model.PlantModel;
import ru.erinary.plantnannybot.api.model.UserModel;
import ru.erinary.plantnannybot.api.router.dto.IncomingMessage;
import ru.erinary.plantnannybot.api.router.dto.ReplyMessage;
import ru.erinary.plantnannybot.api.wizard.ConversationMode;
import ru.erinary.plantnannybot.api.wizard.ConversationState;
import ru.erinary.plantnannybot.api.wizard.ConversationWizard;
import ru.erinary.plantnannybot.api.wizard.store.ConversationStateStore;
import ru.erinary.plantnannybot.service.exceptions.EntityAlreadyExistsException;
import ru.erinary.plantnannybot.service.plant.PlantService;
import ru.erinary.plantnannybot.service.user.UserService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A service for routing messages to appropriate conversation wizards.
 */
@Service
public class MessageRoutingService {

    private static final Logger logger = LoggerFactory.getLogger(MessageRoutingService.class);

    private final ConversationStateStore conversationStateStore;
    private final Map<ConversationMode, ConversationWizard> wizards;
    private final UserService userService;
    private final PlantService plantService;

    /**
     * Creates a new {@link MessageRoutingService} instance.
     *
     * @param conversationStateStore {@link ConversationStateStore}
     * @param wizards                list of {@link ConversationWizard}
     */
    public MessageRoutingService(final ConversationStateStore conversationStateStore,
                                 final List<ConversationWizard> wizards,
                                 final UserService userService,
                                 final PlantService plantService) {
        this.conversationStateStore = conversationStateStore;
        this.wizards = wizards.stream()
                .collect(Collectors.toMap(ConversationWizard::supportedMode, Function.identity()));
        this.userService = userService;
        this.plantService = plantService;
    }

    /**
     * Handles an incoming message from Telegram.
     *
     * @param message incoming message {@link IncomingMessage}
     * @return reply message {@link ReplyMessage}
     */
    public ReplyMessage routeMessage(final IncomingMessage message) {
        var chatId = message.chatId();
        ReplyMessage replyMessage = new ReplyMessage(chatId, StringUtils.EMPTY);
        if (message.isCommand()) {
            var command = Command.fromText(message.text());
            if (command != null) {
                switch (command) {
                    case START -> replyMessage = handleStart(message);
                    case REGISTER -> replyMessage = handleRegister(message);
                    case PLANTS -> replyMessage = handlePlants(message);
                    case ADD_PLANT -> replyMessage = handleAddPlant(message);
                    default -> logger.warn("Unsupported command: {}", message.text());
                }
            } else {
                replyMessage = handleUnknownCommand(message);
            }
        } else {
            var conversationState = conversationStateStore.get(chatId);
            if (conversationState.isPresent()) {
                replyMessage = continueConversation(message, conversationState.get());
            } else {
                logger.warn("No conversation nextState found for chatId {}", chatId);
                replyMessage = new ReplyMessage(chatId, BotMessages.INPUT_ERROR);
            }
        }
        return replyMessage;
    }

    private ReplyMessage handleStart(final IncomingMessage message) {
        var tgUser = message.user();
        logger.info("User {} started the bot", tgUser.getId());
        return new ReplyMessage(message.chatId(), BotMessages.INIT);
    }

    private ReplyMessage handleRegister(final IncomingMessage message) {
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

    private ReplyMessage handlePlants(final IncomingMessage message) {
        var tgUser = message.user();
        logger.info("User {} requested plants", tgUser.getId());
        var plants = plantService.getUserPlants(tgUser.getId());
        if (plants.isEmpty()) {
            return new ReplyMessage(message.chatId(), BotMessages.EMPTY_PLANT_LIST);
        } else {
            var stringBuilder = new StringBuilder();
            for (PlantModel plant : plants) {
                stringBuilder.append("• ").append(plant.name()).append(System.lineSeparator());
            }
            return new ReplyMessage(message.chatId(), stringBuilder.toString());
        }
    }

    private ReplyMessage handleAddPlant(final IncomingMessage message) {
        var chatId = message.chatId();
        var wizard = wizards.get(ConversationMode.ADD_PLANT);
        var wizardStepResult = wizard.start(chatId);
        conversationStateStore.put(chatId, wizardStepResult.nextState());
        return new ReplyMessage(chatId, wizardStepResult.replyText());
    }

    private ReplyMessage handleUnknownCommand(final IncomingMessage msg) {
        logger.warn("Unknown command: {}", msg.text());
        return new ReplyMessage(msg.chatId(), BotMessages.UNKNOWN_COMMAND_ERROR);
    }

    private ReplyMessage continueConversation(final IncomingMessage message, final ConversationState state) {
        var wizard = wizards.get(state.getMode());
        var wizardStepResult = wizard.nextStep(state, message.text());
        if (wizardStepResult.nextState() != null) {
            conversationStateStore.put(message.chatId(), wizardStepResult.nextState());
        } else {
            logger.info("Conversation {} with chatId {} is finished", state.getMode().name(), message.chatId());
            conversationStateStore.clear(message.chatId());
        }
        return new ReplyMessage(message.chatId(), wizardStepResult.replyText());
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
        PLANTS("/plants"),

        /**
         * Starts a new conversation for adding a new plant.
         */
        ADD_PLANT("/addplant");

        private final String text;
        private static final Map<String, Command> COMMAND_MAP;

        static {
            Map<String, Command> map = new HashMap<>();
            for (Command command : values()) {
                map.put(command.text, command);
            }
            COMMAND_MAP = Collections.unmodifiableMap(map);
        }

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

        /**
         * Returns a {@link Command} instance from a text value.
         *
         * @param text text value of the command
         * @return a {@link Command} instance
         */
        public static Command fromText(final String text) {
            return COMMAND_MAP.get(text);
        }
    }
}
