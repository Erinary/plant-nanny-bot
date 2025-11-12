package ru.erinary.plantnannybot.service.exceptions;

/**
 * A generic exception class, that represents business logic errors.
 */
public class BusinessException extends RuntimeException {

    /**
     * Creates a new {@link BusinessException} instance.
     *
     * @param message the detail message
     */
    public BusinessException(final String message) {
        super(message);
    }
}
