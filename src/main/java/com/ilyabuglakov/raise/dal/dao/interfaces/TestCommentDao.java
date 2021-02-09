package com.ilyabuglakov.raise.dal.dao.interfaces;

import com.ilyabuglakov.raise.dal.dao.Dao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.domain.TestComment;

import java.util.List;

/**
 * The interface Test comment dao.
 */
public interface TestCommentDao extends Dao<TestComment> {
    /**
     * Find comments amount integer.
     *
     * @param testId the test id
     * @return the integer
     * @throws DaoOperationException the dao operation exception
     */
    Integer findCommentsAmount(Integer testId) throws DaoOperationException;

    /**
     * Find comments list.
     *
     * @param testId the test id
     * @param offset the offset
     * @param items  the items
     * @return the list
     * @throws DaoOperationException the dao operation exception
     */
    List<TestComment> findComments(Integer testId, int offset, int items) throws DaoOperationException;

    /**
     * Find user comment amount int.
     *
     * @param email the email
     * @return the int
     * @throws DaoOperationException the dao operation exception
     */
    int findUserCommentAmount(String email) throws DaoOperationException;
}
