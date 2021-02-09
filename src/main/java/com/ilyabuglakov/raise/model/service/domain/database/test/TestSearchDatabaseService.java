package com.ilyabuglakov.raise.model.service.domain.database.test;

import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.dao.interfaces.AnswerDao;
import com.ilyabuglakov.raise.dal.dao.interfaces.QuestionDao;
import com.ilyabuglakov.raise.dal.dao.interfaces.TestDao;
import com.ilyabuglakov.raise.dal.transaction.Transaction;
import com.ilyabuglakov.raise.domain.Question;
import com.ilyabuglakov.raise.domain.Test;
import com.ilyabuglakov.raise.domain.type.TestStatus;
import com.ilyabuglakov.raise.model.DaoType;
import com.ilyabuglakov.raise.model.service.domain.database.DatabaseService;

import java.util.List;
import java.util.Set;

public class TestSearchDatabaseService extends DatabaseService {
    public TestSearchDatabaseService(Transaction transaction) {
        super(transaction);
    }

    public List<Test> getTests(TestStatus status, int limit, int from)
            throws DaoOperationException {
        TestDao testDao = (TestDao) transaction.createDao(DaoType.TEST);
        List<Test> tests;
        tests = testDao.findTests(status, limit, from);
        return tests;
    }

    public List<Test> fillTests(List<Test> tests) throws DaoOperationException {
        List<Test> filledTests = List.copyOf(tests);
        QuestionDao questionDao = (QuestionDao) transaction.createDao(DaoType.QUESTION);
        AnswerDao answerDao = (AnswerDao) transaction.createDao(DaoType.ANSWER);
        for (Test test : filledTests) {
            Set<Question> questions = questionDao.findByTestId(test.getId());
            for (Question question : questions) {
                question.setAnswers(answerDao.findByQuestionId(question.getId()));
            }
            test.setQuestions(questions);
        }
        return filledTests;
    }
}
