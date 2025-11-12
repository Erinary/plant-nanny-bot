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
}
