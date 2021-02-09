package com.ilyabuglakov.raise.model.service.domain.database.test;


import com.ilyabuglakov.raise.dal.dao.database.AnswerDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.database.QuestionDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.database.TestCategoryDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.database.TestDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.database.UserDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
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
import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestInfoDatabaseServiceTest {

    public static User sampleUser = User.builder()
            .id(1)
            .email("sample@email.com")
            .name("name")
            .surname("surname")
            .build();

    public static Question sampleQuestion = Question.builder()
            .id(2)
            .name("qName")
            .content("qContent")
            .build();

    public static Answer sampleAnswer = Answer.builder()
            .id(3)
            .content("aContent")
            .correct(true)
            .build();


    Transaction transaction;

    TestInfoDatabaseService testInfoDatabaseService;

    @BeforeMethod
    public void setUp() {
        transaction = mock(Transaction.class);
        testInfoDatabaseService = new TestInfoDatabaseService(transaction);
    }

    public void prepareForTestInfos(){

    }

    @org.testng.annotations.Test
    public void testFillTest() throws DaoOperationException {
        Optional<Test> testOptional = Optional.of(Test.builder()
                .testName("test")
                .author(sampleUser)
                .build());

        TestDatabaseDao testDatabaseDao = mock(TestDatabaseDao.class);
        QuestionDatabaseDao questionDatabaseDao = mock(QuestionDatabaseDao.class);
        AnswerDatabaseDao answerDatabaseDao = mock(AnswerDatabaseDao.class);

        when(transaction.createDao(DaoType.QUESTION)).thenReturn(questionDatabaseDao);
        when(transaction.createDao(DaoType.ANSWER)).thenReturn(answerDatabaseDao);
        when(questionDatabaseDao.findByTestId(any())).thenReturn(Set.of(sampleQuestion));
        when(answerDatabaseDao.findByQuestionId(any())).thenReturn(Set.of(sampleAnswer));

        Test test = testInfoDatabaseService.fillTest(testOptional).get();

        Assert.assertTrue(test.getQuestions().size()>0);
        Assert.assertTrue(test.getQuestions().stream().flatMap(q -> q.getAnswers().stream()).count() > 0);
    }

    public void testGetTestInfos() throws DaoOperationException {
        Test test1 = Test.builder()
                .id(1)
                .testName("test1")
                .author(sampleUser)
                .category(TestCategory.builder().id(1).build())
                .build();

        Test test2 = Test.builder()
                .id(2)
                .testName("test2")
                .author(sampleUser)
                .category(TestCategory.builder().id(1).build())
                .build();

        List<Test> tests = List.of(test1, test2);

        UserDatabaseDao userDatabaseDao = mock(UserDatabaseDao.class);
        TestDatabaseDao testDatabaseDao = mock(TestDatabaseDao.class);
        QuestionDatabaseDao questionDatabaseDao = mock(QuestionDatabaseDao.class);
        AnswerDatabaseDao answerDatabaseDao = mock(AnswerDatabaseDao.class);
        TestCategoryDatabaseDao testCategoryDatabaseDao = mock(TestCategoryDatabaseDao.class);
        when(transaction.createDao(DaoType.USER)).thenReturn(userDatabaseDao);
        when(transaction.createDao(DaoType.TEST)).thenReturn(testDatabaseDao);
        when(transaction.createDao(DaoType.QUESTION)).thenReturn(questionDatabaseDao);
        when(transaction.createDao(DaoType.ANSWER)).thenReturn(answerDatabaseDao);
        when(transaction.createDao(DaoType.TEST_CATEGORY)).thenReturn(testCategoryDatabaseDao);
        when(testDatabaseDao.findCharacteristics(any())).thenReturn(Set.of(Characteristic.LOGIC, Characteristic.MEMORY));
        when(questionDatabaseDao.findQuestionAmount(any())).thenReturn(Optional.of(4));
        when(questionDatabaseDao.findByTestId(any())).thenReturn(Set.of(sampleQuestion));
        when(answerDatabaseDao.findByQuestionId(any())).thenReturn(Set.of(sampleAnswer));
        when(testCategoryDatabaseDao.read(any())).thenReturn(
                Optional.of(TestCategory.builder().id(1).category("Cat").build()));
        when(userDatabaseDao.read(any())).thenReturn(Optional.of(sampleUser));

        List<TestInfo> testInfos = testInfoDatabaseService.getTestInfos(tests);
        Assert.assertEquals(testInfos.get(0).getCategory().getCategory(), "Cat");
        Assert.assertEquals(testInfos.get(0).getCharacteristics(), Set.of(Characteristic.MEMORY, Characteristic.LOGIC));
    }

    @org.testng.annotations.Test
    public void testGetAdvancedTestInfo() throws DaoOperationException {
        Test test1 = Test.builder()
                .id(1)
                .testName("test1")
                .author(sampleUser)
                .category(TestCategory.builder().id(1).build())
                .build();

        Test test2 = Test.builder()
                .id(2)
                .testName("test2")
                .author(sampleUser)
                .category(TestCategory.builder().id(1).build())
                .build();

        List<Test> tests = List.of(test1, test2);
        List<String> questionNames = List.of("q1", "q2", "q3", "q4");

        UserDatabaseDao userDatabaseDao = mock(UserDatabaseDao.class);
        TestDatabaseDao testDatabaseDao = mock(TestDatabaseDao.class);
        QuestionDatabaseDao questionDatabaseDao = mock(QuestionDatabaseDao.class);
        AnswerDatabaseDao answerDatabaseDao = mock(AnswerDatabaseDao.class);
        TestCategoryDatabaseDao testCategoryDatabaseDao = mock(TestCategoryDatabaseDao.class);
        when(transaction.createDao(DaoType.USER)).thenReturn(userDatabaseDao);
        when(transaction.createDao(DaoType.TEST)).thenReturn(testDatabaseDao);
        when(transaction.createDao(DaoType.QUESTION)).thenReturn(questionDatabaseDao);
        when(transaction.createDao(DaoType.ANSWER)).thenReturn(answerDatabaseDao);
        when(transaction.createDao(DaoType.TEST_CATEGORY)).thenReturn(testCategoryDatabaseDao);
        when(testDatabaseDao.findCharacteristics(any())).thenReturn(Set.of(Characteristic.LOGIC, Characteristic.MEMORY));
        when(questionDatabaseDao.findQuestionAmount(any())).thenReturn(Optional.of(4));
        when(questionDatabaseDao.findByTestId(any())).thenReturn(Set.of(sampleQuestion));
        when(answerDatabaseDao.findByQuestionId(any())).thenReturn(Set.of(sampleAnswer));
        when(testCategoryDatabaseDao.read(any())).thenReturn(
                Optional.of(TestCategory.builder().id(1).category("Cat").build()));
        when(questionDatabaseDao.findQuestionsNames(any())).thenReturn(questionNames);
        when(userDatabaseDao.read(any())).thenReturn(Optional.of(sampleUser));

        List<AdvancedTestInfo> testInfos = testInfoDatabaseService.getAdvancedTestInfo(tests);
        Assert.assertEquals(testInfos.get(0).getCategory().getCategory(), "Cat");
        Assert.assertEquals(testInfos.get(0).getCharacteristics(), Set.of(Characteristic.MEMORY, Characteristic.LOGIC));
        Assert.assertEquals(testInfos.get(0).getQuestionNames(), "q1; q2; q3; q4");
    }
}