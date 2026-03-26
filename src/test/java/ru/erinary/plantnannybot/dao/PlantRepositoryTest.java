package ru.erinary.plantnannybot.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.erinary.plantnannybot.entity.Plant;
import ru.erinary.plantnannybot.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Testcontainers
class PlantRepositoryTest {

    @Container
    private final static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.1");

    @DynamicPropertySource
    static void configureProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PlantRepository plantRepository;

    @Test
    void testFindPlantsByTgUserId() {
        var user = new User(1L, 2L);
        entityManager.persist(user);
        var basil = new Plant("Basil", user, "Some notes");
        entityManager.persist(basil);
        var pepper = new Plant("Pepper", user, "Some notes");
        entityManager.persist(pepper);

        var plants = plantRepository.findByUserTgUserId(user.getTgUserId());
        assertEquals(2, plants.size());
    }
}