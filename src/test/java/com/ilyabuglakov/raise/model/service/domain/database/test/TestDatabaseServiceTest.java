package com.ilyabuglakov.raise.model.service.domain.database.test;

import com.google.gson.Gson;
import com.ilyabuglakov.raise.dal.dao.database.AnswerDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.database.QuestionDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.database.TestDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.database.UserDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.dal.transaction.Transaction;
import com.ilyabuglakov.raise.domain.Answer;
import com.ilyabuglakov.raise.domain.Question;
import com.ilyabuglakov.raise.domain.Test;
import com.ilyabuglakov.raise.domain.TestCategory;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.domain.type.Characteristic;
import com.ilyabuglakov.raise.domain.type.Status;
import com.ilyabuglakov.raise.domain.type.TestStatus;
import com.ilyabuglakov.raise.model.DaoType;
import com.ilyabuglakov.raise.model.dto.QuestionDto;
import com.ilyabuglakov.raise.model.dto.TestDto;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners(MockitoTestNGListener.class)
public class TestDatabaseServiceTest {

    @Mock
    Transaction transaction;
    @Mock
    TestDatabaseDao testDao;
    @Mock
    QuestionDatabaseDao questionDao;
    @Mock
    AnswerDatabaseDao answerDao;
    @Mock
    UserDatabaseDao userDao;

    TestDatabaseService testService;

    User sampleUser;

    Test sampleTest;

    @BeforeClass
    public void setup() throws DaoOperationException {
        sampleUser = User.builder()
                .id(1)
                .status(Status.ACTIVE)
                .email("sample@m.com")
                .password("121212")
                .name("Name")
                .surname("Surname")
                .registrationDate(LocalDate.of(2021, Month.JANUARY, 2))
                .build();

        Set<Question> questions = Set.of(
                Question.builder().name("question1")
                        .answers(Set.of(Answer.builder().correct(true).content("smt").build()))
                        .build());

        sampleTest = Test.builder()
                .author(User.builder().id(1).build())
                .testName("test")
                .category(TestCategory.builder().id(1).build())
                .characteristics(Set.of(Characteristic.LOGIC))
                .difficulty(1)
                .status(TestStatus.NEW)
                .questions(questions)
                .build();
    }

    @BeforeMethod
    public void setTransaction() {
        testService = new TestDatabaseService(transaction);
    }

    @org.testng.annotations.Test
    public void testCreateFromJson() {
        Test testToJson = Test.builder()
                .author(User.builder().id(1).build())
                .testName("test")
                .category(TestCategory.builder().id(1).build())
                .characteristics(Set.of(Characteristic.LOGIC))
                .difficulty(1)
                .status(TestStatus.NEW)
                .build();
        String json = new Gson().toJson(testToJson);

        Optional<Test> actual = testService.createFromJson(json);
        assertTrue(actual.isPresent());
        Test test = actual.get();
        assertEquals(test, testToJson);
    }

    @org.testng.annotations.Test
    public void testCreateDtoFromJson() {
        TestDto testToJson = TestDto.builder()
                .id(1)
                .questions(Set.of(QuestionDto.builder().id(1).build(), QuestionDto.builder().id(2).build()))
                .build();
        String json = new Gson().toJson(testToJson);

        Optional<TestDto> actual = testService.createDtoFromJson(json);
        assertEquals(actual.get(), testToJson);
    }

    @org.testng.annotations.Test
    public void testSave() throws PersistentException {
        when(userDao.findByEmail(any())).thenReturn(Optional.of(sampleUser));
        when(testDao.create(any())).thenReturn(2);
        when(questionDao.create(any())).thenReturn(3);
        when(transaction.createDao(DaoType.TEST)).thenReturn(testDao);
        when(transaction.createDao(DaoType.USER)).thenReturn(userDao);
        when(transaction.createDao(DaoType.QUESTION)).thenReturn(questionDao);
        when(transaction.createDao(DaoType.ANSWER)).thenReturn(answerDao);
        testService.save(sampleTest, "");
    }

    @org.testng.annotations.Test
    public void testChangeTestStatus() throws PersistentException {
        when(transaction.createDao(DaoType.TEST)).thenReturn(testDao);
        testService.changeTestStatus(1, TestStatus.CONFIRMED);
    }


//    @Test
//    public void testFindBySearchParameters() {
//    }
//
//    @Test
//    public void testGetTestInfosByStatus() {
//    }
//
//    @Test
//    public void testGetTestInfosByStatusAndPage() {
//    }
//
//    @Test
//    public void testGetAdvancedTestInfosByStatus() {
//    }
//
//    @Test
//    public void testGetAdvancedTestInfosByStatusAndPage() {
//    }
//
//    @Test
//    public void testGetTestAmountByStatus() {
//    }
}