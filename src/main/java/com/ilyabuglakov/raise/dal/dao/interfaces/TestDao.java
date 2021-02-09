package com.ilyabuglakov.raise.dal.dao.interfaces;

import com.ilyabuglakov.raise.dal.dao.Dao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.domain.Test;
import com.ilyabuglakov.raise.domain.TestCategory;
import com.ilyabuglakov.raise.domain.type.Characteristic;
import com.ilyabuglakov.raise.domain.type.TestStatus;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * The interface Test dao.
 */
public interface TestDao extends Dao<Test> {
    /**
     * Find by name and category and status list.
     *
     * @param name     the name
     * @param category the category
     * @param status   the status
     * @param limit    the limit
     * @param from     the from
     * @return the list
     * @throws DaoOperationException the dao operation exception
     */
    List<Test> findByNameAndCategoryAndStatus(String name, TestCategory category, TestStatus status, int limit, int from) throws DaoOperationException;

    /**
     * Find by name and parent category and status list.
     *
     * @param name     the name
     * @param category the category
     * @param status   the status
     * @param limit    the limit
     * @param from     the from
     * @return the list
     * @throws DaoOperationException the dao operation exception
     */
    List<Test> findByNameAndParentCategoryAndStatus(String name, TestCategory category, TestStatus status, int limit, int from) throws DaoOperationException;

    /**
     * Find by name and status list.
     *
     * @param name   the name
     * @param status the status
     * @param limit  the limit
     * @param from   the from
     * @return the list
     * @throws DaoOperationException the dao operation exception
     */
    List<Test> findByNameAndStatus(String name, TestStatus status, int limit, int from) throws DaoOperationException;

    /**
     * Find by category and status list.
     *
     * @param category the category
     * @param status   the status
     * @param limit    the limit
     * @param from     the from
     * @return the list
     * @throws DaoOperationException the dao operation exception
     */
    List<Test> findByCategoryAndStatus(TestCategory category, TestStatus status, int limit, int from) throws DaoOperationException;

    /**
     * Find by parent category and status list.
     *
     * @param category the category
     * @param status   the status
     * @param limit    the limit
     * @param from     the from
     * @return the list
     * @throws DaoOperationException the dao operation exception
     */
    List<Test> findByParentCategoryAndStatus(TestCategory category, TestStatus status, int limit, int from) throws DaoOperationException;

    /**
     * Find tests list.
     *
     * @param status the status
     * @param limit  the limit
     * @param from   the from
     * @return the list
     * @throws DaoOperationException the dao operation exception
     */
    List<Test> findTests(TestStatus status, int limit, int from) throws DaoOperationException;

    /**
     * Save characteristics.
     *
     * @param characteristics the characteristics
     * @param testId          the test id
     * @throws DaoOperationException the dao operation exception
     */
    void saveCharacteristics(Collection<Characteristic> characteristics, Integer testId) throws DaoOperationException;

    /**
     * Find characteristics set.
     *
     * @param testId the test id
     * @return the set
     * @throws DaoOperationException the dao operation exception
     */
    Set<Characteristic> findCharacteristics(Integer testId) throws DaoOperationException;

    /**
     * Find test amount by status int.
     *
     * @param status the status
     * @return the int
     * @throws DaoOperationException the dao operation exception
     */
    int findTestAmountByStatus(TestStatus status) throws DaoOperationException;

    /**
     * Find test amount by status int.
     *
     * @param status   the status
     * @param authorId the author id
     * @return the int
     * @throws DaoOperationException the dao operation exception
     */
    int findTestAmountByStatus(TestStatus status, Integer authorId) throws DaoOperationException;

    /**
     * Find test amount int.
     *
     * @param authorId the author id
     * @return the int
     * @throws DaoOperationException the dao operation exception
     */
    int findTestAmount(Integer authorId) throws DaoOperationException;

    /**
     * Update status.
     *
     * @param testId the test id
     * @param status the status
     * @throws DaoOperationException the dao operation exception
     */
    void updateStatus(Integer testId, TestStatus status) throws DaoOperationException;

    /**
     * Find amount by name and category and status int.
     *
     * @param name     the name
     * @param category the category
     * @param status   the status
     * @return the int
     * @throws DaoOperationException the dao operation exception
     */
    int findAmountByNameAndCategoryAndStatus(String name, TestCategory category, TestStatus status) throws DaoOperationException;

    /**
     * Find amount by name and parent category and status int.
     *
     * @param name     the name
     * @param category the category
     * @param status   the status
     * @return the int
     * @throws DaoOperationException the dao operation exception
     */
    int findAmountByNameAndParentCategoryAndStatus(String name, TestCategory category, TestStatus status) throws DaoOperationException;

    /**
     * Find amount by name and status int.
     *
     * @param name   the name
     * @param status the status
     * @return the int
     * @throws DaoOperationException the dao operation exception
     */
    int findAmountByNameAndStatus(String name, TestStatus status) throws DaoOperationException;

    /**
     * Find amount by category and status int.
     *
     * @param category the category
     * @param status   the status
     * @return the int
     * @throws DaoOperationException the dao operation exception
     */
    int findAmountByCategoryAndStatus(TestCategory category, TestStatus status) throws DaoOperationException;

    /**
     * Find amount by parent category and status int.
     *
     * @param category the category
     * @param status   the status
     * @return the int
     * @throws DaoOperationException the dao operation exception
     */
    int findAmountByParentCategoryAndStatus(TestCategory category, TestStatus status) throws DaoOperationException;
}
