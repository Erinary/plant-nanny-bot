package ru.erinary.plantnannybot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.erinary.plantnannybot.entity.User;

import java.util.UUID;

/**
 * Repository interface for {@link User}.
 */
public interface UserRepository extends JpaRepository<User, UUID> {
}
