package ru.erinary.plantnannybot.api.wizard.addplant;

import ru.erinary.plantnannybot.api.wizard.ConversationDraft;

/**
 * Temporary data container used during the ADD_PLANT wizard.
 */
public class AddPlantDraft implements ConversationDraft {

    private String name;
    private String notes;

    /**
     * Creates a new {@link AddPlantDraft} instance.
     */
    public AddPlantDraft() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }
}
