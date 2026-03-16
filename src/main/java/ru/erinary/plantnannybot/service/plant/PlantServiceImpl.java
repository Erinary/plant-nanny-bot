package ru.erinary.plantnannybot.service.plant;

import org.springframework.stereotype.Service;
import ru.erinary.plantnannybot.api.model.PlantModel;
import ru.erinary.plantnannybot.dao.PlantRepository;
import ru.erinary.plantnannybot.service.plant.mapper.PlantMapper;

import java.util.List;

/**
 * Realisation of {@link PlantService}.
 */
@Service
public class PlantServiceImpl implements PlantService {

    private final PlantRepository repository;
    private final PlantMapper mapper;

    /**
     * Creates a new {@link PlantServiceImpl} instance.
     *
     * @param repository {@link PlantRepository}
     * @param mapper     {@link PlantMapper}
     */
    public PlantServiceImpl(final PlantRepository repository, final PlantMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<PlantModel> getUserPlants(final Long tgUserId) {
        return repository.findByUserTgUserId(tgUserId).stream().map(mapper::map).toList();
    }
}
