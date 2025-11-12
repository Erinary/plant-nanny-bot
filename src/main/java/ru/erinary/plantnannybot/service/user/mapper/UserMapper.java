package ru.erinary.plantnannybot.service.user.mapper;

import org.springframework.stereotype.Component;
import ru.erinary.plantnannybot.api.model.UserModel;
import ru.erinary.plantnannybot.entity.User;
import ru.erinary.plantnannybot.service.mapper.Mapper;

/**
 * Mapper for conversion {@link User} domain entity to {@link UserModel} DTO.
 */
@Component
public class UserMapper implements Mapper<User, UserModel> {

    @Override
    public UserModel map(final User user) {
        return new UserModel(user.getTgUserId(), user.getChatId());
    }
}
