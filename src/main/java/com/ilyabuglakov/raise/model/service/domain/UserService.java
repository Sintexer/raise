package com.ilyabuglakov.raise.model.service.domain;

import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.model.dto.UserCharacteristic;
import com.ilyabuglakov.raise.model.dto.UserInfoDto;
import com.ilyabuglakov.raise.model.dto.UserParametersDto;
import com.ilyabuglakov.raise.model.response.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * The interface User service.
 */
public interface UserService extends Service {
    /**
     * Gets user parameters by id.
     *
     * @param userId the user id
     * @return the user parameters
     * @throws PersistentException exception fom datasource
     */
    UserParametersDto getUserParameters(Integer userId) throws PersistentException;

    /**
     * Gets user parameters by email.
     *
     * @param email the email
     * @return the user parameters
     * @throws PersistentException exception fom datasource
     */
    UserParametersDto getUserParameters(String email) throws PersistentException;

    /**
     * Gets user by email.
     *
     * @param email the email
     * @return the user
     * @throws PersistentException exception fom datasource
     */
    Optional<User> getUser(String email) throws PersistentException;

    /**
     * Gets user by id.
     *
     * @param id the id
     * @return the user
     * @throws PersistentException exception fom datasource
     */
    Optional<User> getUser(Integer id) throws PersistentException;

    /**
     * Update user.
     *
     * @param user the user
     * @throws PersistentException exception fom datasource
     */
    void updateUser(User user) throws PersistentException;

    /**
     * Change user info response entity.
     *
     * @param userInfoDto the user info dto
     * @return the response entity
     * @throws PersistentException exception fom datasource
     */
    ResponseEntity changeUserInfo(UserInfoDto userInfoDto) throws PersistentException;

    /**
     * Gets user characteristics.
     *
     * @param userId the user id
     * @return the user characteristics
     * @throws PersistentException exception fom datasource
     */
    List<UserCharacteristic> getUserCharacteristics(Integer userId) throws PersistentException;

    /**
     * Gets results amount by user id.
     *
     * @param userId the user id
     * @return the results amount
     * @throws PersistentException exception fom datasource
     */
    int getResultsAmount(Integer userId) throws PersistentException;
}
