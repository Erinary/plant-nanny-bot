package ru.erinary.plantnannybot.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.erinary.plantnannybot.dao.PlantRepository;
import ru.erinary.plantnannybot.dao.UserRepository;
import ru.erinary.plantnannybot.entity.Plant;
import ru.erinary.plantnannybot.entity.User;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
class MigrationIntegrationTest {

    @Container
    private final static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.1");

    @DynamicPropertySource
    static void configureProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlantRepository plantRepository;

    @Test
    void testContextLoads_and_canStoreUserAndPlant() {
        var user = new User(1L);
        userRepository.save(user);
        var users = userRepository.findAll();
        assertNotNull(users);

        var plant = new Plant("Basil", Instant.now(), user);
        plantRepository.save(plant);
        var plants = plantRepository.findAll();
        assertNotNull(plants);
        assertTrue(plants.stream().map(Plant::getName).toList().contains(plant.getName()));
    }
}
