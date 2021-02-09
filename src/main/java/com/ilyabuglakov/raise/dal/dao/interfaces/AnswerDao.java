package com.ilyabuglakov.raise.dal.dao.interfaces;

import com.ilyabuglakov.raise.dal.dao.Dao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.domain.Answer;

import java.util.Collection;
import java.util.Set;

/**
 * The interface Answer dao.
 */
public interface AnswerDao extends Dao<Answer> {
    /**
     * Find by question id set.
     *
     * @param questionId the question id
     * @return the set
     * @throws DaoOperationException the dao operation exception
     */
    Set<Answer> findByQuestionId(Integer questionId) throws DaoOperationException;

    /**
     * Create all.
     *
     * @param answers the answers
     * @throws DaoOperationException the dao operation exception
     */
    void createAll(Collection<Answer> answers) throws DaoOperationException;
}
