package ru.erinary.plantnannybot.service.plant;

import ru.erinary.plantnannybot.api.model.PlantModel;

import java.util.List;

/**
 * Interface for working with plants.
 */
public interface PlantService {

    /**
     * Get user's plants.
     *
     * @param tgUserId user's telegram id
     * @return list of user's plants
     */
    List<PlantModel> getUserPlants(Long tgUserId);

    /**
     * Save a plant.
     *
     * @param model plant model
     * @return saved plant model
     */
    PlantModel save(PlantModel model);
}
