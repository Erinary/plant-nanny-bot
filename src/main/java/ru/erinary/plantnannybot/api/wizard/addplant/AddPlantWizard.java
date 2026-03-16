package ru.erinary.plantnannybot.api.wizard.addplant;

import org.springframework.stereotype.Component;
import ru.erinary.plantnannybot.api.wizard.ConversationMode;
import ru.erinary.plantnannybot.api.wizard.ConversationState;
import ru.erinary.plantnannybot.api.wizard.ConversationWizard;

/**
 * Wizard for adding a new plant.
 */
@Component
public class AddPlantWizard implements ConversationWizard {

    @Override
    public ConversationState start(final Long chatId) {
        return new ConversationState(chatId,
                ConversationMode.ADD_PLANT,
                AddPlantStep.ASK_NAME.toString(),
                new AddPlantDraft());
    }

    @Override
    public ConversationState nextStep(final ConversationState state, final String message) {
        var step = AddPlantStep.fromString(state.getStep());
        switch (step) {
            case null -> throw new IllegalArgumentException("Invalid step: " + state.getStep());
            case ASK_NAME -> {
                var draft = (AddPlantDraft) state.getDraft();
                draft.setName(message);
            }
            case ASK_NOTES -> {
                var draft = (AddPlantDraft) state.getDraft();
                draft.setNotes(message);
            }
            default -> throw new IllegalStateException("Unexpected value: " + step);
        }
        return state;
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
                if (value.toString().equals(step)) {
                    return value;
                }
            }
            return null;
        }
    }

}
