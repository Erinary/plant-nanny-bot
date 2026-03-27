package ru.erinary.plantnannybot.service.user;

import ru.erinary.plantnannybot.api.model.UserModel;

/**
 * Interface for working with users.
 */
public interface UserService {

    /**
     * Saves new user.
     *
     * @param model user's model
     */
    UserModel save(UserModel model);

    /**
     * Checks if a user is registered.
     * @param tgUserId user's telegram id
     * @return true if a user is registered, false otherwise
     */
    boolean isUserRegistered(Long tgUserId);
}
