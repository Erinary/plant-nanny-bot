package ru.erinary.plantnannybot.service.mapper;

/**
 * Defines a contract for converting between entities and DTOs.
 *
 * @param <E> entity
 * @param <D> DTO
 */
public interface Mapper<E, D> {

    /**
     * Converts an entity to a DTO.
     *
     * @param e entity
     * @return DTO
     */
    D map(E e);
}
