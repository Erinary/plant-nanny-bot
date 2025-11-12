package ru.erinary.plantnannybot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.erinary.plantnannybot.entity.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for {@link User}.
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Searches a user by Telegram id.
     *
     * @param tgUserId user's telegram id
     * @return a user
     */
    Optional<User> findByTgUserId(Long tgUserId);
}
