package ru.erinary.plantnannybot.api.router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.erinary.plantnannybot.api.BotCommand;
import ru.erinary.plantnannybot.api.BotMessages;
import ru.erinary.plantnannybot.api.command.CommandHandler;
import ru.erinary.plantnannybot.api.router.dto.IncomingMessage;
import ru.erinary.plantnannybot.api.router.dto.ReplyMessage;
import ru.erinary.plantnannybot.api.wizard.ConversationMode;
import ru.erinary.plantnannybot.api.wizard.ConversationState;
import ru.erinary.plantnannybot.api.wizard.ConversationWizard;
import ru.erinary.plantnannybot.api.wizard.store.ConversationStateStore;
import ru.erinary.plantnannybot.service.user.UserService;

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
    private final Map<BotCommand, CommandHandler> handlers;
    private final UserService userService;

    /**
     * Creates a new {@link MessageRoutingService} instance.
     *
     * @param conversationStateStore {@link ConversationStateStore}
     * @param wizards                list of {@link ConversationWizard}
     * @param handlers               list of {@link CommandHandler}
     * @param userService            {@link UserService}
     */
    public MessageRoutingService(final ConversationStateStore conversationStateStore,
                                 final List<ConversationWizard> wizards,
                                 final List<CommandHandler> handlers,
                                 final UserService userService) {
        this.conversationStateStore = conversationStateStore;
        this.wizards = wizards.stream()
                .collect(Collectors.toMap(ConversationWizard::supportedMode, Function.identity()));
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(CommandHandler::command, Function.identity()));
        this.userService = userService;
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
        var command = BotCommand.fromText(message.text());
        if (command == null) {
            return handleUnknownCommand(message);
        }
        if (command.isRequiresRegistration() && !userService.isUserRegistered(message.user().getId())) {
            return new ReplyMessage(message.chatId(), BotMessages.USER_MUST_BE_REGISTERED_ERROR);
        }
        var handler = handlers.get(command);
        if (handler != null) {
            return handler.handle(message);
        } else {
            //command is not handled by any handler, so it's a conversation control command
            switch (command) {
                case BotCommand.ADD_PLANT -> {
                    return handleAddPlant(message);
                }
                case BotCommand.CANCEL -> {
                    return handleCancel(message);
                }
                case BotCommand.SKIP -> {
                    return handleSkip(message);
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

    private ReplyMessage handleUnknownCommand(final IncomingMessage msg) {
        logger.warn("Unknown command: {}", msg.text());
        return new ReplyMessage(msg.chatId(), BotMessages.UNKNOWN_COMMAND_ERROR);
    }

    private ReplyMessage handleAddPlant(final IncomingMessage message) {
        var chatId = message.chatId();
        if (hasActiveConversation(chatId)) {
            return new ReplyMessage(chatId, BotMessages.ACTION_IN_PROGRESS_ERROR);
        }
        var wizard = wizards.get(ConversationMode.ADD_PLANT);
        var wizardStepResult = wizard.start(message);
        conversationStateStore.put(chatId, wizardStepResult.nextState());
        return new ReplyMessage(chatId, wizardStepResult.replyText());
    }

    private ReplyMessage handleCancel(final IncomingMessage message) {
        var chatId = message.chatId();
        var conversationState = conversationStateStore.get(chatId);
        if (conversationState.isPresent()) {
            conversationStateStore.clear(chatId);
            logger.info("Conversation canceled for chatId {}", chatId);
            return new ReplyMessage(chatId, BotMessages.CANCEL_ACTION);
        } else {
            logger.warn("No active conversation state to cancel for chatId {}", chatId);
            return new ReplyMessage(chatId, BotMessages.NO_ACTIVE_ACTION_ERROR);
        }
    }

    private ReplyMessage handleSkip(final IncomingMessage message) {
        var chatId = message.chatId();
        var conversationState = conversationStateStore.get(chatId);
        if (conversationState.isEmpty()) {
            logger.warn("No active conversation state to skip for chatId {}", chatId);
            return new ReplyMessage(chatId, BotMessages.NO_ACTIVE_CONVERSATION_STEP_ERROR);
        }
        return continueConversation(message, conversationState.get());
    }

    private boolean hasActiveConversation(final Long chatId) {
        var conversationState = conversationStateStore.get(chatId);
        if (conversationState.isPresent()) {
            logger.warn("Conversation already in progress for chatId {}", chatId);
            return true;
        }
        return false;
    }
}
