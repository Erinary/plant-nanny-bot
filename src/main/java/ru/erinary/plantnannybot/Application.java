package ru.erinary.plantnannybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the PlantNannyBot application.
 * This is a Spring Boot application that runs a Telegram bot
 * to remind users to water their plants based on configurable schedules.
 */
@SpringBootApplication
public class Application {

    /**
     * Configures and starts the application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
