package com.ilyabuglakov.raise.dal.dao.interfaces;

import com.ilyabuglakov.raise.dal.dao.Dao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.domain.Question;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The interface Question dao.
 */
public interface QuestionDao extends Dao<Question> {
    /**
     * Create all.
     *
     * @param questions the questions
     * @throws DaoOperationException the dao operation exception
     */
    void createAll(Collection<Question> questions) throws DaoOperationException;

    /**
     * Find question amount optional.
     *
     * @param testId the test id
     * @return the optional
     * @throws DaoOperationException the dao operation exception
     */
    Optional<Integer> findQuestionAmount(Integer testId) throws DaoOperationException;

    /**
     * Find by test id set.
     *
     * @param testId the test id
     * @return the set
     * @throws DaoOperationException the dao operation exception
     */
    Set<Question> findByTestId(Integer testId) throws DaoOperationException;

    /**
     * Find questions names list.
     *
     * @param testId the test id
     * @return the list
     * @throws DaoOperationException the dao operation exception
     */
    List<String> findQuestionsNames(Integer testId) throws DaoOperationException;
}
