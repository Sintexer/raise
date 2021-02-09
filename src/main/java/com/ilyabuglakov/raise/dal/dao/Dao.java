package com.ilyabuglakov.raise.dal.dao;

import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.domain.Entity;

import java.util.Optional;


/**
 * The interface Dao.
 *
 * @param <T> the type parameter extends Entity
 */
public interface Dao<T extends Entity> {
    /**
     * Create entity and return id.
     *
     * @param entity the entity
     * @return the integer
     * @throws DaoOperationException the dao operation exception
     */
    Integer create(T entity) throws DaoOperationException;

    /**
     * Read optional entity.
     *
     * @param id the id
     * @return the optional
     * @throws DaoOperationException the dao operation exception
     */
    Optional<T> read(Integer id) throws DaoOperationException;

    /**
     * Update entity.
     *
     * @param entity the entity
     * @throws DaoOperationException the dao operation exception
     */
    void update(T entity) throws DaoOperationException;

    /**
     * Delete entity.
     *
     * @param entity the entity
     * @throws DaoOperationException the dao operation exception
     */
    void delete(T entity) throws DaoOperationException;
}
