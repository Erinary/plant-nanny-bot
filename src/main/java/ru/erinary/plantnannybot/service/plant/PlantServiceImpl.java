package ru.erinary.plantnannybot.service.plant;

import org.springframework.stereotype.Service;
import ru.erinary.plantnannybot.api.model.PlantModel;
import ru.erinary.plantnannybot.dao.PlantRepository;
import ru.erinary.plantnannybot.dao.UserRepository;
import ru.erinary.plantnannybot.entity.Plant;
import ru.erinary.plantnannybot.service.exceptions.UserNotFoundException;
import ru.erinary.plantnannybot.service.plant.mapper.PlantMapper;

import java.util.List;

/**
 * Realisation of {@link PlantService}.
 */
@Service
public class PlantServiceImpl implements PlantService {

    private final PlantRepository plantRepository;
    private final UserRepository userRepository;
    private final PlantMapper mapper;

    /**
     * Creates a new {@link PlantServiceImpl} instance.
     *
     * @param plantRepository {@link PlantRepository}
     * @param mapper          {@link PlantMapper}
     */
    public PlantServiceImpl(final PlantRepository plantRepository,
                            final UserRepository userRepository,
                            final PlantMapper mapper) {
        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public List<PlantModel> getUserPlants(final Long tgUserId) {
        return plantRepository.findByUserTgUserId(tgUserId).stream().map(mapper::map).toList();
    }

    @Override
    public PlantModel save(final PlantModel model) {
        var user = userRepository.findByTgUserId(model.tgUserId());
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        var plant = new Plant(model.name(), user.get(), model.notes());
        return mapper.map(plantRepository.save(plant));
    }
}
