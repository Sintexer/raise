package com.ilyabuglakov.raise.dal.dao.database;

import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.dao.interfaces.AnswerDao;
import com.ilyabuglakov.raise.dal.dao.interfaces.QuestionDao;
import com.ilyabuglakov.raise.domain.Answer;
import com.ilyabuglakov.raise.domain.Question;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Set;

import static org.testng.Assert.*;

public class QuestionDatabaseDaoTest {

    static final int DEFAULT_PARENT_TEST_ID = 8;

    private QuestionDao questionDao;

    private Connection connection;

    private String url;
    private String name;
    private String password;


    @BeforeClass
    public void initParams() throws IOException {
        ResourceBundle properties;
        properties = ResourceBundle.getBundle("testDatabase");
        url = properties.getString("url");
        name = properties.getString("name");
        password = properties.getString("password");
    }

    @BeforeMethod
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection(url, name, password);
        questionDao = new QuestionDatabaseDao(connection);
    }

    @AfterMethod
    public void closeConnection() throws SQLException {
        connection.close();
    }

    @DataProvider(name = "createValues")
    public Object[][] produceCreateValues(){
        com.ilyabuglakov.raise.domain.Test parent = com.ilyabuglakov.raise.domain.Test.builder().id(DEFAULT_PARENT_TEST_ID).build();
        String longName = StringUtils.repeat("*", 255);
        String longName2 = StringUtils.repeat("*", 256);
        String longContent = StringUtils.repeat("*", 511);
        String longContent2 = StringUtils.repeat("*", 512);
        return new Object[][] {
                {Question.builder().name("Sample name").content("Sample content").test(parent).build()},
                {Question.builder().name("").content("Sample content").test(parent).build()},
                {Question.builder().name("Sample name").content("").test(parent).build()},
                {Question.builder().name(longName).content("Sample content").test(parent).build()},
                {Question.builder().name(longName2).content("Sample content").test(parent).build()},
                {Question.builder().name("Sample name").content(longContent).test(parent).build()},
                {Question.builder().name("Sample name").content(longContent2).test(parent).build()},
        };
    }

    @DataProvider(name = "createFailValues")
    public Object[][] produceFailValues(){
        String tooLongName = StringUtils.repeat("*", 257);
        String tooLongContent = StringUtils.repeat("*", 513);
        com.ilyabuglakov.raise.domain.Test parent = com.ilyabuglakov.raise.domain.Test.builder().id(DEFAULT_PARENT_TEST_ID).build();
        return new Object[][] {
                {Question.builder().name("Sample name").content(null).test(parent).build()},
                {Question.builder().name("Sample name").content("no set parent").build()},
                {Question.builder().name("Sample name").content("null question").test(null).build()},
                {Question.builder().name("Sample name").content(tooLongContent).test(parent).build()},
                {Question.builder().name("Sample name").test(parent).build()},
                {Question.builder().content("Sample content").test(parent).build()},
                {Question.builder().name(tooLongName).content("Sample content").test(parent).build()},
        };
    }

    @DataProvider(name = "updateValues")
    public Object[][] produceUpdateValues(){
        return new Object[][] {
                {Question.builder().id(1).name("Sample name").content("Sample answer").test(
                        com.ilyabuglakov.raise.domain.Test.builder().id(1).build()).build()},
                {Question.builder().id(6).name("Sample name").content("Sample answer").test(
                        com.ilyabuglakov.raise.domain.Test.builder().id(2).build()).build()},
        };
    }

    @DataProvider(name = "deleteValues")
    public Object[][] produceValuesToDelete(){
        return new Object[][] {
                {Question.builder().id(18).build()},
                {Question.builder().id(19).build()},
                {Question.builder().id(20).build()},
                {Question.builder().id(21).build()},
        };
    }

    @DataProvider(name = "createAllValues")
    public Object[][] produceCollectionsOfValues(){
        com.ilyabuglakov.raise.domain.Test parent = com.ilyabuglakov.raise.domain.Test.builder().id(DEFAULT_PARENT_TEST_ID).build();
        Set<Question> defaultSet = Set.of(
                Question.builder().name("Sample name1").content("Sample content 1").test(parent).build(),
                Question.builder().name("Sample name2").content("Sample content 2").test(parent).build(),
                Question.builder().name("Sample name3").content("Sample content 3").test(parent).build(),
                Question.builder().name("Sample name4").content("Sample content 4").test(parent).build(),
                Question.builder().name("Sample name5").content("Sample content 5").test(parent).build());

        return new Object[][] {
                {new HashSet<>(defaultSet)},
                {new LinkedHashSet<>(defaultSet)},
                {new ArrayList<>(defaultSet)},
                {new LinkedList<>(defaultSet)},
                {Collections.emptySet()},
        };
    }

    @DataProvider(name = "findByTestIdValues")
    public Object[][] produceFindValues(){
        return new Object[][] {
                {1},
                {2},
                {3},
                {4},
                {5},
        };
    }

    @Test(dataProvider = "createValues")
    public void createTest(Question question) throws DaoOperationException {
        Integer id = questionDao.create(question);
        assertNotNull(id);
    }

    @Test(dataProvider = "createFailValues", expectedExceptions = {
            DaoOperationException.class,
            NullPointerException.class
    })
    public void createFailTest(Question question) throws DaoOperationException {
        questionDao.create(question);
        fail("Exception wasn't thrown");
    }

    @Test(dataProvider = "updateValues")
    public void updateTest(Question question) throws DaoOperationException {
        questionDao.update(question);
    }

    @Test(dataProvider = "deleteValues")
    public void deleteTest(Question question) throws DaoOperationException {
        questionDao.delete(question);
    }

    @Test(dataProvider = "createAllValues")
    public void createAllTest(Collection<Question> questions) throws DaoOperationException {
        questionDao.createAll(questions);
    }

    @Test(dataProvider = "findByTestIdValues")
    public void findByTestIdTest(Integer id) throws DaoOperationException{
        assertFalse(questionDao.findByTestId(id).isEmpty(), "Result is empty");
    }
}