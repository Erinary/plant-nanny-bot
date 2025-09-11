package ru.erinary.plantnannybot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.erinary.plantnannybot.entity.Plant;

import java.util.UUID;

/**
 * Repository interface for {@link Plant}.
 */
public interface PlantRepository extends JpaRepository<Plant, UUID> {
}
