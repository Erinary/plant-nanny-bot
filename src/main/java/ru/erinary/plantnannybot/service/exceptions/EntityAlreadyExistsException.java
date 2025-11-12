package ru.erinary.plantnannybot.service.exceptions;

public class EntityAlreadyExistsException extends BusinessException {

    /**
     * Creates a new {@link EntityAlreadyExistsException} instance.
     */
    public EntityAlreadyExistsException() {
        super("Entity already exists");
    }
}
