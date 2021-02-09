package com.ilyabuglakov.raise.dal.dao.interfaces;

import com.ilyabuglakov.raise.dal.dao.Dao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.domain.UsrKey;
import com.ilyabuglakov.raise.domain.type.Status;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * The interface User dao.
 */
public interface UserDao extends Dao<User> {
    /**
     * Find by email optional.
     *
     * @param email the email
     * @return the optional
     * @throws DaoOperationException the dao operation exception
     */
    Optional<User> findByEmail(String email) throws DaoOperationException;

    /**
     * Find user info optional.
     *
     * @param id the id
     * @return the optional
     * @throws DaoOperationException the dao operation exception
     */
    Optional<User> findUserInfo(Integer id) throws DaoOperationException;

    /**
     * Create key.
     *
     * @param key       the key
     * @param userId    the user id
     * @param timestamp the timestamp
     * @throws DaoOperationException the dao operation exception
     */
    void createKey(String key, Integer userId, LocalDateTime timestamp) throws DaoOperationException;

    /**
     * Find key optional.
     *
     * @param key the key
     * @return the optional
     * @throws DaoOperationException the dao operation exception
     */
    Optional<UsrKey> findKey(String key) throws DaoOperationException;

    /**
     * Update status.
     *
     * @param id     the id
     * @param status the status
     * @throws DaoOperationException the dao operation exception
     */
    void updateStatus(Integer id, Status status) throws DaoOperationException;

    /**
     * Delete key.
     *
     * @param key the key
     * @throws DaoOperationException the dao operation exception
     */
    void deleteKey(String key) throws DaoOperationException;
}
