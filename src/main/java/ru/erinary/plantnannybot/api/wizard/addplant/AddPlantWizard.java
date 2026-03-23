package ru.erinary.plantnannybot.api.wizard.addplant;

import org.springframework.stereotype.Component;
import ru.erinary.plantnannybot.api.BotMessages;
import ru.erinary.plantnannybot.api.wizard.ConversationMode;
import ru.erinary.plantnannybot.api.wizard.ConversationState;
import ru.erinary.plantnannybot.api.wizard.ConversationWizard;
import ru.erinary.plantnannybot.api.wizard.WizardStepResult;

/**
 * Wizard for adding a new plant.
 */
@Component
public class AddPlantWizard implements ConversationWizard {

    @Override
    public WizardStepResult start(final Long chatId) {
        var state = new ConversationState(chatId,
                ConversationMode.ADD_PLANT,
                AddPlantStep.ASK_NAME.toString(),
                new AddPlantDraft());
        return new WizardStepResult(BotMessages.ADD_PLANT_ASK_NAME, state);
    }

    @Override
    public WizardStepResult nextStep(final ConversationState state, final String message) {
        String replyText;
        ConversationState newState;
        var step = AddPlantStep.fromString(state.getStep());
        switch (step) {
            case null -> throw new IllegalArgumentException("Invalid step: " + state.getStep());
            case ASK_NAME -> {
                var draft = (AddPlantDraft) state.getDraft();
                draft.setName(message);
                replyText = BotMessages.ADD_PLANT_ASK_NOTES;
                newState = new ConversationState(state.getChatId(), ConversationMode.ADD_PLANT,
                        AddPlantStep.ASK_NOTES.toString(), draft);
            }
            case ASK_NOTES -> {
                var draft = (AddPlantDraft) state.getDraft();
                draft.setNotes(message);
                replyText = BotMessages.ADD_PLANT_SUCCESS;
                newState = null;
                //TODO save the plant
            }
            default -> throw new IllegalStateException("Unexpected value: " + step);
        }
        return new WizardStepResult(replyText, newState);
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
