package ru.erinary.plantnannybot.api.command.plants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.erinary.plantnannybot.api.BotMessages;
import ru.erinary.plantnannybot.api.command.CommandHandler;
import ru.erinary.plantnannybot.api.model.PlantModel;
import ru.erinary.plantnannybot.api.router.MessageRoutingService;
import ru.erinary.plantnannybot.api.router.dto.IncomingMessage;
import ru.erinary.plantnannybot.api.router.dto.ReplyMessage;
import ru.erinary.plantnannybot.service.plant.PlantService;

@Component
public class PlantsCommandHandler implements CommandHandler {

    private final Logger logger = LoggerFactory.getLogger(PlantsCommandHandler.class);
    private final PlantService plantService;

    /**
     * Creates a new {@link PlantsCommandHandler} instance.
     *
     * @param plantService {@link PlantService}
     */
    public PlantsCommandHandler(final PlantService plantService) {
        this.plantService = plantService;
    }

    @Override
    public ReplyMessage handle(final IncomingMessage message) {
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

    @Override
    public MessageRoutingService.Command command() {
        return MessageRoutingService.Command.PLANTS;
    }
}
