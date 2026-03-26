package ru.erinary.plantnannybot.api.wizard.addplant;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.erinary.plantnannybot.api.BotMessages;
import ru.erinary.plantnannybot.api.model.PlantModel;
import ru.erinary.plantnannybot.api.router.dto.IncomingMessage;
import ru.erinary.plantnannybot.api.wizard.ConversationMode;
import ru.erinary.plantnannybot.api.wizard.ConversationState;
import ru.erinary.plantnannybot.api.wizard.ConversationWizard;
import ru.erinary.plantnannybot.api.wizard.WizardStepResult;
import ru.erinary.plantnannybot.service.plant.PlantService;

/**
 * Wizard for adding a new plant.
 */
@Component
public class AddPlantWizard implements ConversationWizard {

    private final PlantService plantService;

    /**
     * Creates a new {@link AddPlantWizard} instance.
     *
     * @param plantService {@link PlantService}
     */
    public AddPlantWizard(final PlantService plantService) {
        this.plantService = plantService;
    }

    @Override
    public WizardStepResult start(final IncomingMessage message) {
        var state = new ConversationState(message.chatId(),
                ConversationMode.ADD_PLANT,
                AddPlantStep.ASK_NAME.name(),
                new AddPlantDraft());
        return new WizardStepResult(BotMessages.ADD_PLANT_ASK_NAME, state);
    }

    @Override
    public WizardStepResult nextStep(final ConversationState state, final IncomingMessage message) {
        WizardStepResult result;
        var step = AddPlantStep.fromString(state.getStep());
        switch (step) {
            case null -> throw new IllegalArgumentException("Invalid step: " + state.getStep());
            case ASK_NAME -> result = handleAskName(state, message);
            case ASK_NOTES -> result = handleAskNotes(state, message);
            default -> throw new IllegalStateException("Unexpected value: " + step);
        }
        return result;
    }

    private WizardStepResult handleAskName(final ConversationState state, final IncomingMessage message) {
        String replyText;
        ConversationState newState;
        if (StringUtils.isBlank(message.text())) {
            replyText = BotMessages.ADD_PLANT_NAME_NOT_EMPTY;
            newState = state;
        } else {
            var draft = (AddPlantDraft) state.getDraft();
            draft.setName(message.text());
            replyText = BotMessages.ADD_PLANT_ASK_NOTES;
            newState = new ConversationState(state.getChatId(), ConversationMode.ADD_PLANT,
                    AddPlantStep.ASK_NOTES.toString(), draft);
        }
        return new WizardStepResult(replyText, newState);
    }

    private WizardStepResult handleAskNotes(final ConversationState state, final IncomingMessage message) {
        var draft = (AddPlantDraft) state.getDraft();
        draft.setNotes(message.text());
        var replyText = BotMessages.ADD_PLANT_SUCCESS;

        var newPlant = new PlantModel(draft.getName(), message.user().getId(), draft.getNotes());
        plantService.save(newPlant);
        return new WizardStepResult(replyText, null);
    }

    @Override
    public ConversationMode supportedMode() {
        return ConversationMode.ADD_PLANT;
    }

    /**
     * Defines the internal steps of the ADD_PLANT wizard.
     */
    public enum AddPlantStep {
        /**
         * Waiting for plant name.
         */
        ASK_NAME,

        /**
         * Waiting for additional notes.
         */
        ASK_NOTES;

        static AddPlantStep fromString(final String step) {
            for (var value : values()) {
                if (value.name().equals(step)) {
                    return value;
                }
            }
            return null;
        }
    }
}
