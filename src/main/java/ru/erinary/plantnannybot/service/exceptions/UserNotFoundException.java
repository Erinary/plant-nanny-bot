package ru.erinary.plantnannybot.service.exceptions;

public class UserNotFoundException extends BusinessException {

    /**
     * Creates a new {@link UserNotFoundException} instance.
     */
    public UserNotFoundException() {
        super("User not found: ");
    }
}
