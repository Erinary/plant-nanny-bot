package ru.erinary.plantnannybot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.erinary.plantnannybot.entity.Plant;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for {@link Plant}.
 */
public interface PlantRepository extends JpaRepository<Plant, UUID> {

    /**
     * Searches plants by user's telegram id.
     *
     * @param tgUserId user's telegram id
     * @return list of user's plants
     */
    List<Plant> findByUserTgUserId(Long tgUserId);
}
