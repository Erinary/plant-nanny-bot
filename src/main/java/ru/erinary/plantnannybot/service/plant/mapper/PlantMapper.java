package ru.erinary.plantnannybot.service.plant.mapper;

import org.springframework.stereotype.Component;
import ru.erinary.plantnannybot.api.model.PlantModel;
import ru.erinary.plantnannybot.entity.Plant;
import ru.erinary.plantnannybot.service.mapper.Mapper;

/**
 * Mapper for conversion {@link Plant} domain entity to {@link PlantModel} DTO.
 */
@Component
public class PlantMapper implements Mapper<Plant, PlantModel> {

    @Override
    public PlantModel map(final Plant plant) {
        return new PlantModel(plant.getName(), plant.getUser().getTgUserId(), plant.getNotes());
    }
}
