package ru.erinary.plantnannybot.service.user;

import org.springframework.stereotype.Service;
import ru.erinary.plantnannybot.api.model.UserModel;
import ru.erinary.plantnannybot.dao.UserRepository;
import ru.erinary.plantnannybot.entity.User;
import ru.erinary.plantnannybot.service.exceptions.EntityAlreadyExistsException;
import ru.erinary.plantnannybot.service.user.mapper.UserMapper;

/**
 * Realisation of {@link UserService}.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    /**
     * Creates a new {@link UserServiceImpl} instance.
     *
     * @param repository {@link UserRepository}
     * @param mapper     {@link UserMapper}
     */
    public UserServiceImpl(final UserRepository repository, final UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UserModel save(final UserModel model) {
        var existing = repository.findByTgUserId(model.tgUserId());
        if (existing.isPresent()) {
            throw new EntityAlreadyExistsException();
        }
        var user = new User(model.tgUserId(), model.chatId());
        return mapper.map(repository.save(user));
    }

    @Override
    public boolean isUserRegistered(final Long tgUserId) {
        return repository.existsByTgUserId(tgUserId);
    }
}
