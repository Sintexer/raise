package com.ilyabuglakov.raise.dal.dao.interfaces;

import com.ilyabuglakov.raise.dal.dao.Dao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.domain.UserTestResult;

import java.util.List;
import java.util.Optional;

/**
 * The interface User test result dao.
 */
public interface UserTestResultDao extends Dao<UserTestResult> {
    /**
     * Find by user id and test id optional.
     *
     * @param userId the user id
     * @param testId the test id
     * @return the optional
     * @throws DaoOperationException the dao operation exception
     */
    Optional<UserTestResult> findByUserIdAndTestId(Integer userId, Integer testId) throws DaoOperationException;

    /**
     * Find result amount int.
     *
     * @param userId the user id
     * @return the int
     * @throws DaoOperationException the dao operation exception
     */
    int findResultAmount(Integer userId) throws DaoOperationException;

    /**
     * Find user test results list.
     *
     * @param userId the user id
     * @return the list
     * @throws DaoOperationException the dao operation exception
     */
    List<UserTestResult> findUserTestResults(Integer userId) throws DaoOperationException;
}
