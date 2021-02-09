package com.ilyabuglakov.raise.model.service.domain;

import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.model.dto.TestResultDto;
import com.ilyabuglakov.raise.model.dto.UserCharacteristic;

import java.util.List;

/**
 * The interface User test result service.
 */
public interface UserTestResultService extends Service {
    /**
     * Save result.
     *
     * @param testResultDto the test result dto
     * @param email         the email
     * @throws PersistentException the persistent exception
     */
    void saveResult(TestResultDto testResultDto, String email) throws PersistentException;

    /**
     * Gets results amount.
     *
     * @param userId the user id
     * @return the results amount
     * @throws PersistentException the persistent exception
     */
    int getResultsAmount(Integer userId) throws PersistentException;

    /**
     * Gets user characteristics.
     *
     * @param userId the user id
     * @return the user characteristics
     * @throws PersistentException the persistent exception
     */
    List<UserCharacteristic> getUserCharacteristics(Integer userId) throws PersistentException;
}
