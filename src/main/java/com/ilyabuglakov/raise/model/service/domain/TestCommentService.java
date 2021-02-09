package com.ilyabuglakov.raise.model.service.domain;

import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.domain.TestComment;

import java.util.List;

/**
 * The interface Test comment service.
 */
public interface TestCommentService extends Service {
    /**
     * Save comment.
     *
     * @param comment     the comment
     * @param testId      the test id
     * @param authorEmail the author email
     * @throws PersistentException the persistent exception
     */
    void saveComment(String comment, Integer testId, String authorEmail) throws PersistentException;

    /**
     * Gets comments amount.
     *
     * @param testId the test id
     * @return the comments amount
     * @throws DaoOperationException the dao operation exception
     */
    Integer getCommentsAmount(Integer testId) throws DaoOperationException;

    /**
     * Gets comments.
     *
     * @param testId       the test id
     * @param pageStart    the page start
     * @param itemsPerPage the items per page
     * @return the comments
     * @throws PersistentException the persistent exception
     */
    List<TestComment> getComments(Integer testId, int pageStart, int itemsPerPage) throws PersistentException;

    /**
     * Gets comments amount.
     *
     * @param email the email
     * @return the comments amount
     * @throws DaoOperationException the dao operation exception
     */
    int getCommentsAmount(String email) throws DaoOperationException;

    /**
     * Delete comment.
     *
     * @param commentId the comment id
     * @throws PersistentException the persistent exception
     */
    void deleteComment(Integer commentId) throws PersistentException;
}
