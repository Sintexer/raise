package com.ilyabuglakov.raise.dal.dao.database;

import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.dao.interfaces.AnswerDao;
import com.ilyabuglakov.raise.domain.Answer;
import com.ilyabuglakov.raise.domain.Question;
import com.ilyabuglakov.raise.model.service.path.PathService;
import com.ilyabuglakov.raise.model.service.property.PropertyParser;
import com.ilyabuglakov.raise.model.service.property.exception.PropertyFileException;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import static org.testng.Assert.*;

public class AnswerDatabaseDaoTest {

    static final int DEFAULT_PARENT_QUESTION_ID = 22;

    private AnswerDao answerDao;

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
        answerDao = new AnswerDatabaseDao(connection);
    }

    @AfterMethod
    public void loseConnection() throws SQLException {
        connection.close();
    }

    @DataProvider(name = "createValues")
    public Object[][] produceCreateValues(){
        Question parent = Question.builder().id(DEFAULT_PARENT_QUESTION_ID).build();
        String longString = StringUtils.repeat("*", 255);
        String longString2 = StringUtils.repeat("*", 256);
        return new Object[][] {
                {Answer.builder().content("Sample answer").correct(true).question(parent).build()},
                {Answer.builder().content("<>>>%#(*@&&*%(#FSSVKIVJ^2364713892>").correct(false).question(parent).build()},
                {Answer.builder().content("").correct(false).question(parent).build()},
                {Answer.builder().content("Default 1213123").correct(true).question(parent).build()},
                {Answer.builder().content("233548Starts with numbers").correct(false).question(parent).build()},
                {Answer.builder().content("false").correct(true).question(parent).build()},
                {Answer.builder().content("False").correct(true).question(parent).build()},
                {Answer.builder().content("True").correct(true).question(parent).build()},
                {Answer.builder().content("true").correct(false).question(parent).build()},
                {Answer.builder().content("00.000.012").correct(false).question(parent).build()},
                {Answer.builder().content("0.1").correct(true).question(parent).build()},
                {Answer.builder().content("NULL").correct(true).question(parent).build()},
                {Answer.builder().content(longString).correct(true).question(parent).build()},
                {Answer.builder().content(longString2).correct(true).question(parent).build()},
                {Answer.builder().content("null").correct(false).question(parent).build()}
        };
    }

    @DataProvider(name = "createFailValues")
    public Object[][] produceFailValues(){
        String tooLongContent = StringUtils.repeat("*", 257);
        Question parent = Question.builder().id(DEFAULT_PARENT_QUESTION_ID).build();
        return new Object[][] {
                {Answer.builder().content(null).correct(true).question(parent).build()},
                {Answer.builder().content("no set parent").correct(false).build()},
                {Answer.builder().content("null question").correct(false).question(null).build()},
                {Answer.builder().content(tooLongContent).correct(true).question(parent).build()},
                {Answer.builder().correct(true).question(parent).build()},
        };
    }

    @DataProvider(name = "updateValues")
    public Object[][] produceUpdate(){
        return new Object[][] {
                {Answer.builder().id(1).content("Sample answer").correct(true).question(Question.builder().id(1).build()).build()},
                {Answer.builder().id(2).content("<>>>%#(*@&&*%(#FSSVKIVJ^2364713892>").correct(false).question(Question.builder().id(1).build()).build()},
                {Answer.builder().id(3).content("").correct(false).question(Question.builder().id(1).build()).build()},
                {Answer.builder().id(4).content("Default 1213123").correct(true).question(Question.builder().id(1).build()).build()},
                {Answer.builder().id(5).content("233548Starts with numbers").correct(false).question(Question.builder().id(1).build()).build()},
                {Answer.builder().id(6).content("false").correct(true).question(Question.builder().id(1).build()).build()},
                {Answer.builder().id(7).content("False").correct(true).question(Question.builder().id(2).build()).build()},
                {Answer.builder().id(8).content("True").correct(true).question(Question.builder().id(2).build()).build()},
                {Answer.builder().id(9).content("true").correct(false).question(Question.builder().id(3).build()).build()},
                {Answer.builder().id(10).content("00.000.012").correct(false).question(Question.builder().id(4).build()).build()},
                {Answer.builder().id(11).content("0.1").correct(true).question(Question.builder().id(4).build()).build()},
                {Answer.builder().id(12).content("NULL").correct(true).question(Question.builder().id(4).build()).build()},
                {Answer.builder().id(13).content("null").correct(false).question(Question.builder().id(4).build()).build(),}
        };
    }

    @DataProvider(name = "deleteValues")
    public Object[][] produceValuesToDelete(){
        return new Object[][] {
                {Answer.builder().id(1).build()},
                {Answer.builder().id(2).build()},
                {Answer.builder().id(3).build()},
                {Answer.builder().id(4).build()},
        };
    }

    @DataProvider(name = "createAllValues")
    public Object[][] produceCollectionsOfValues(){
        Question parent = Question.builder().id(DEFAULT_PARENT_QUESTION_ID).build();
        Set<Answer> defaultSet = Set.of(
                Answer.builder().content("Sample answer 1").correct(true).question(parent).build(),
                Answer.builder().content("Sample answer 2").correct(true).question(parent).build(),
                Answer.builder().content("Sample answer 3").correct(true).question(parent).build(),
                Answer.builder().content("Sample answer 4").correct(true).question(parent).build(),
                Answer.builder().content("Sample answer 5").correct(true).question(parent).build());
        return new Object[][] {
                {new HashSet<>(defaultSet)},
                {new LinkedHashSet<>(defaultSet)},
                {new ArrayList<>(defaultSet)},
                {new LinkedList<>(defaultSet)},
                {Collections.emptySet()},
        };
    }

    @DataProvider(name = "findByQuestionIdValues")
    public Object[][] produceFindValues(){
        return new Object[][] {
                {5},
                {6},
                {7},
                {8},
                {9},
        };
    }

    @Test(dataProvider = "createValues")
    public void checkInsert(Answer answer) throws DaoOperationException {
        Integer id = answerDao.create(answer);
        assertNotNull(id);
    }

    @Test(dataProvider = "createFailValues", expectedExceptions = {
            DaoOperationException.class,
            NullPointerException.class
    })
    public void createFail(Answer answer) throws DaoOperationException {
        answerDao.create(answer);
        fail("Exception wasn't thrown");
    }

    @Test(dataProvider = "updateValues")
    public void checkUpdate(Answer answer) throws DaoOperationException {
        answerDao.update(answer);
    }

    @Test(dataProvider = "deleteValues")
    public void checkDelete(Answer answer) throws DaoOperationException {
        answerDao.delete(answer);
    }

    @Test(dataProvider = "createAllValues")
    public void checkCreateAll(Collection<Answer> answers) throws DaoOperationException {
        answerDao.createAll(answers);
    }

    @Test(dataProvider = "findByQuestionIdValues")
    public void checkFindByQuestionId(Integer id) throws DaoOperationException{
        assertFalse(answerDao.findByQuestionId(id).isEmpty(), "Result is empty");
    }

}