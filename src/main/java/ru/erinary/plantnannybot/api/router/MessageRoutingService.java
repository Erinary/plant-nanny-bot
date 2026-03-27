package ru.erinary.plantnannybot.api.router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.erinary.plantnannybot.api.BotMessages;
import ru.erinary.plantnannybot.api.command.CommandHandler;
import ru.erinary.plantnannybot.api.router.dto.IncomingMessage;
import ru.erinary.plantnannybot.api.router.dto.ReplyMessage;
import ru.erinary.plantnannybot.api.wizard.ConversationMode;
import ru.erinary.plantnannybot.api.wizard.ConversationState;
import ru.erinary.plantnannybot.api.wizard.ConversationWizard;
import ru.erinary.plantnannybot.api.wizard.store.ConversationStateStore;

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
    private final Map<Command, CommandHandler> handlers;

    /**
     * Creates a new {@link MessageRoutingService} instance.
     *
     * @param conversationStateStore {@link ConversationStateStore}
     * @param wizards                list of {@link ConversationWizard}
     * @param handlers               list of {@link CommandHandler}
     */
    public MessageRoutingService(final ConversationStateStore conversationStateStore,
                                 final List<ConversationWizard> wizards,
                                 final List<CommandHandler> handlers) {
        this.conversationStateStore = conversationStateStore;
        this.wizards = wizards.stream()
                .collect(Collectors.toMap(ConversationWizard::supportedMode, Function.identity()));
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(CommandHandler::command, Function.identity()));
    }

    /**
     * Handles an incoming message from Telegram.
     *
     * @param message incoming message {@link IncomingMessage}
     * @return reply message {@link ReplyMessage}
     */
    public ReplyMessage routeMessage(final IncomingMessage message) {
        if (message.isCommand()) {
            return routeCommand(message);
        } else {
            return routeConversation(message);
        }
    }

    private ReplyMessage routeCommand(final IncomingMessage message) {
        var command = Command.fromText(message.text());
        if (command == null) {
            return handleUnknownCommand(message);
        }
        var handler = handlers.get(command);
        if (handler != null) {
            return handler.handle(message);
        } else {
            //command is not handled by any handler, so it's a conversation
            //noinspection SwitchStatementWithTooFewBranches
            switch (command) {
                case ADD_PLANT -> {
                    return handleAddPlant(message);
                }
                default -> {
                    logger.warn("Unsupported command: {}", message.text());
                    return new ReplyMessage(message.chatId(), BotMessages.UNSUPPORTED_COMMAND_ERROR);
                }
            }
        }

    }

    private ReplyMessage routeConversation(final IncomingMessage message) {
        var chatId = message.chatId();
        var conversationState = conversationStateStore.get(chatId);
        if (conversationState.isPresent()) {
            return continueConversation(message, conversationState.get());
        } else {
            logger.warn("No active conversation state found for chatId {}", chatId);
            return new ReplyMessage(chatId, BotMessages.INPUT_ERROR);
        }
    }

    private ReplyMessage handleAddPlant(final IncomingMessage message) {
        var chatId = message.chatId();
        var wizard = wizards.get(ConversationMode.ADD_PLANT);
        var wizardStepResult = wizard.start(message);
        conversationStateStore.put(chatId, wizardStepResult.nextState());
        return new ReplyMessage(chatId, wizardStepResult.replyText());
    }

    private ReplyMessage handleUnknownCommand(final IncomingMessage msg) {
        logger.warn("Unknown command: {}", msg.text());
        return new ReplyMessage(msg.chatId(), BotMessages.UNKNOWN_COMMAND_ERROR);
    }

    private ReplyMessage continueConversation(final IncomingMessage message, final ConversationState state) {
        var wizard = wizards.get(state.getMode());
        var wizardStepResult = wizard.nextStep(state, message);
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
         * Starts a new conversation about adding a new plant.
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
