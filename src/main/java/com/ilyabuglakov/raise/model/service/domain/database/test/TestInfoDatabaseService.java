package com.ilyabuglakov.raise.model.service.domain.database.test;

import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.dao.interfaces.AnswerDao;
import com.ilyabuglakov.raise.dal.dao.interfaces.QuestionDao;
import com.ilyabuglakov.raise.dal.dao.interfaces.TestCategoryDao;
import com.ilyabuglakov.raise.dal.dao.interfaces.TestDao;
import com.ilyabuglakov.raise.dal.dao.interfaces.UserDao;
import com.ilyabuglakov.raise.dal.transaction.Transaction;
import com.ilyabuglakov.raise.domain.Answer;
import com.ilyabuglakov.raise.domain.Question;
import com.ilyabuglakov.raise.domain.Test;
import com.ilyabuglakov.raise.domain.TestCategory;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.domain.type.Characteristic;
import com.ilyabuglakov.raise.model.DaoType;
import com.ilyabuglakov.raise.model.dto.AdvancedTestInfo;
import com.ilyabuglakov.raise.model.dto.TestInfo;
import com.ilyabuglakov.raise.model.service.domain.database.DatabaseService;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
public class TestInfoDatabaseService extends DatabaseService {

    public TestInfoDatabaseService(Transaction transaction) {
        super(transaction);
    }

    public Optional<Test> fillTest(Optional<Test> test) throws DaoOperationException {
        if (test.isPresent()) {
            QuestionDao questionDao = (QuestionDao) transaction.createDao(DaoType.QUESTION);
            AnswerDao answerDao = (AnswerDao) transaction.createDao(DaoType.ANSWER);
            Set<Question> questions = questionDao.findByTestId(test.get().getId());
            for (Question question : questions) {
                question.setAnswers(answerDao.findByQuestionId(question.getId()));
                question.setCorrectAmount((int) question.getAnswers().stream().filter(Answer::isCorrect).count());
            }
            test.get().setQuestions(questions);
        }
        return test;
    }

    public List<TestInfo> getTestInfos(List<Test> tests) throws DaoOperationException {
        TestDao testDao = (TestDao) transaction.createDao(DaoType.TEST);
        QuestionDao questionDao = (QuestionDao) transaction.createDao(DaoType.QUESTION);
        UserDao userDao = (UserDao) transaction.createDao(DaoType.USER);
        TestCategoryDao testCategoryDao = (TestCategoryDao) transaction.createDao(DaoType.TEST_CATEGORY);
        List<TestInfo> testInfos = new ArrayList<>();
        for (Test test : tests) {
            Set<Characteristic> characteristicSet = testDao.findCharacteristics(test.getId());
            int questionsAmount = questionDao.findQuestionAmount(test.getId()).orElseThrow(DaoOperationException::new);
            TestCategory testCategory = testCategoryDao.read(test.getCategory().getId()).orElseThrow(DaoOperationException::new);
            User authorInfo = userDao.findUserInfo(test.getAuthor().getId()).orElse(null);
            testInfos.add(TestInfo.builder()
                    .testName(test.getTestName())
                    .author(authorInfo)
                    .characteristics(characteristicSet)
                    .difficulty(test.getDifficulty())
                    .category(testCategory)
                    .id(test.getId())
                    .questionsAmount(questionsAmount)
                    .build());
        }
        return testInfos;
    }

    public List<AdvancedTestInfo> getAdvancedTestInfo(List<Test> tests) throws DaoOperationException {
        TestDao testDao = (TestDao) transaction.createDao(DaoType.TEST);
        QuestionDao questionDao = (QuestionDao) transaction.createDao(DaoType.QUESTION);
        UserDao userDao = (UserDao) transaction.createDao(DaoType.USER);
        TestCategoryDao testCategoryDao = (TestCategoryDao) transaction.createDao(DaoType.TEST_CATEGORY);

        List<AdvancedTestInfo> testInfos = new ArrayList<>();
        for (Test test : tests) {
            Set<Characteristic> characteristicSet = testDao.findCharacteristics(test.getId());
            int questionsAmount = questionDao.findQuestionAmount(test.getId()).orElseThrow(DaoOperationException::new);
            User author = userDao.read(test.getAuthor().getId()).orElse(null);
            String questionNames = String.join("; ", questionDao.findQuestionsNames(test.getId()));
            TestCategory testCategory = testCategoryDao.read(test.getCategory().getId()).orElseThrow(DaoOperationException::new);
            log.debug(test.getAuthor().getId() + ": " + author);
            testInfos.add(AdvancedTestInfo.builder()
                    .testName(test.getTestName())
                    .author(author)
                    .characteristics(characteristicSet)
                    .difficulty(test.getDifficulty())
                    .category(testCategory)
                    .id(test.getId())
                    .questionNames(questionNames)
                    .questionsAmount(questionsAmount)
                    .build());
        }
        return testInfos;
    }

}
