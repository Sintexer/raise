package com.ilyabuglakov.raise.dal.dao.database;

import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.dao.interfaces.TestDao;
import com.ilyabuglakov.raise.domain.Test;
import com.ilyabuglakov.raise.domain.TestCategory;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.domain.type.Characteristic;
import com.ilyabuglakov.raise.domain.type.TestStatus;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class TestDatabaseDaoTest {

    private final static int TEST_CATEGORY_ID = 8;

    private TestDao testDao;

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
        testDao = new TestDatabaseDao(connection);
    }

    @AfterMethod
    public void closeConnection() throws SQLException {
        connection.close();
    }

    @DataProvider(name = "createValues")
    public Object[][] produceCreateValues() {
        String longString = StringUtils.repeat("*", 255);
        String longString2 = StringUtils.repeat("*", 256);
        User superAuthor = User.builder().id(1).build();
        User author = User.builder().id(2).build();
        return new Object[][]{
                {Test.builder().testName("Sample test name")
                        .category(TestCategory.builder().id(TEST_CATEGORY_ID).build())
                        .status(TestStatus.CONFIRMED)
                        .author(superAuthor)
                        .difficulty(1)
                        .characteristics(Set.of(Characteristic.LOGIC))
                        .build()
                },
                {Test.builder().testName("")
                        .category(TestCategory.builder().id(TEST_CATEGORY_ID).build())
                        .status(TestStatus.NEW)
                        .author(author)
                        .difficulty(2)
                        .characteristics(Set.of(Characteristic.LOGIC, Characteristic.CALCULATIONS))
                        .build()
                },
                {Test.builder().testName("Sample test name")
                        .category(TestCategory.builder().id(TEST_CATEGORY_ID).build())
                        .status(TestStatus.CONFIRMED)
                        .author(author)
                        .difficulty(1)
                        .characteristics(Set.of(Characteristic.values()))
                        .build()
                },
                {Test.builder().testName(longString)
                        .category(TestCategory.builder().id(TEST_CATEGORY_ID).build())
                        .status(TestStatus.NEW)
                        .author(superAuthor)
                        .difficulty(1)
                        .characteristics(Set.of(Characteristic.LOGIC))
                        .build()
                },
                {Test.builder().testName(longString2)
                        .category(TestCategory.builder().id(TEST_CATEGORY_ID).build())
                        .status(TestStatus.CONFIRMED)
                        .author(author)
                        .difficulty(1)
                        .characteristics(Set.of(Characteristic.LOGIC))
                        .build()
                },
        };
    }

    @DataProvider(name = "readValues")
    public Object[][] produceReadValues() {
        User author = User.builder().id(1).build();
        return new Object[][]{
                {1, Test.builder().testName("Math test 1")
                        .id(1)
                        .category(TestCategory.builder().id(6).build())
                        .status(TestStatus.CONFIRMED)
                        .author(author)
                        .difficulty(1)
                        .build()
                },
                {2, Test.builder().testName("Math test 2")
                        .id(2)
                        .category(TestCategory.builder().id(6).build())
                        .status(TestStatus.CONFIRMED)
                        .author(author)
                        .difficulty(1)
                        .build()
                },
                {3, Test.builder().testName("Math test 3")
                        .id(3)
                        .category(TestCategory.builder().id(6).build())
                        .status(TestStatus.CONFIRMED)
                        .author(author)
                        .difficulty(1)
                        .build()
                },
                {4, Test.builder().testName("Animals 1")
                        .id(4)
                        .category(TestCategory.builder().id(8).build())
                        .status(TestStatus.CONFIRMED)
                        .author(author)
                        .difficulty(1)
                        .build()
                },
                {5, Test.builder().testName("Java 1")
                        .id(5)
                        .category(TestCategory.builder().id(4).build())
                        .status(TestStatus.CONFIRMED)
                        .author(author)
                        .difficulty(1)
                        .build()
                },
                {7, Test.builder().testName("Java 2")
                        .id(7)
                        .category(TestCategory.builder().id(4).build())
                        .status(TestStatus.NEW)
                        .author(author)
                        .difficulty(1)
                        .build()
                },
        };
    }

    @DataProvider(name = "updateValues")
    public Object[][] produceUpdateValues() {
        User superAuthor = User.builder().id(1).build();
        User author = User.builder().id(2).build();
        return new Object[][]{
                {Test.builder().id(12)
                        .testName("After update")
                        .category(TestCategory.builder().id(TEST_CATEGORY_ID).build())
                        .status(TestStatus.CONFIRMED)
                        .author(superAuthor)
                        .difficulty(1)
                        .characteristics(Set.of(Characteristic.LOGIC))
                        .build()
                },
                {Test.builder()
                        .id(13)
                        .testName("After update")
                        .category(TestCategory.builder().id(TEST_CATEGORY_ID).build())
                        .status(TestStatus.NEW)
                        .author(author)
                        .difficulty(2)
                        .characteristics(Set.of(Characteristic.LOGIC, Characteristic.CALCULATIONS))
                        .build()
                },
        };
    }

    @DataProvider(name = "deleteValues")
    public Object[][] produceDeleteValues() {
        return new Object[][]{
                {9},
                {10},
                {11},
                {12},
        };
    }

    @org.testng.annotations.Test(dataProvider = "createValues")
    public void testCreate(Test test) throws DaoOperationException {
        Integer id = testDao.create(test);
        assertNotNull(id);
        assertNotEquals(id, 0);
    }

    @org.testng.annotations.Test(dataProvider = "readValues")
    public void testRead(Integer id, Test expected) throws DaoOperationException {
        Test actual = testDao.read(id).get();
        assertEquals(actual, expected, "Actual test :" + actual + " is not equal to actual");
    }

    @org.testng.annotations.Test(dataProvider = "updateValues")
    public void testUpdate(Test test) throws DaoOperationException {
        testDao.update(test);
    }

    @org.testng.annotations.Test(dataProvider = "deleteValues")
    public void testDelete(Integer id) throws DaoOperationException {
        testDao.delete(Test.builder().id(id).build());
    }

    @org.testng.annotations.Test
    public void testFindByNameAndCategoryAndStatus() throws DaoOperationException {
        List<Test> tests =
                testDao.findByNameAndCategoryAndStatus(
                        "Math",
                        TestCategory.builder().id(6).build(),
                        TestStatus.CONFIRMED, 2, 0);
        assertTrue(tests.size() > 0);
    }

    @org.testng.annotations.Test
    public void testFindByNameAndParentCategoryAndStatus() throws DaoOperationException {
        List<Test> tests =
                testDao.findByNameAndParentCategoryAndStatus(
                        "Math",
                        TestCategory.builder().id(2).build(),
                        TestStatus.CONFIRMED,
                        2, 0);
        assertTrue(tests.size() > 0);
    }

    @org.testng.annotations.Test
    public void testFindByNameAndStatus() throws DaoOperationException {
        List<Test> tests =
                testDao.findByNameAndStatus(
                        "Math",
                        TestStatus.CONFIRMED,
                        2, 0);
        assertTrue(tests.size() > 0);
    }

    @org.testng.annotations.Test
    public void testFindByCategoryAndStatus() throws DaoOperationException {
        List<Test> tests =
                testDao.findByCategoryAndStatus(
                        TestCategory.builder().id(2).build(),
                        TestStatus.CONFIRMED,
                        2, 0);
    }

    @org.testng.annotations.Test
    public void testFindByParentCategoryAndStatus() throws DaoOperationException {
        List<Test> tests =
                testDao.findByParentCategoryAndStatus(
                        TestCategory.builder().id(2).build(),
                        TestStatus.CONFIRMED,
                        2, 0);
    }

    @org.testng.annotations.Test
    public void testSaveCharacteristics() throws DaoOperationException {
        testDao.saveCharacteristics(List.of(Characteristic.LOGIC, Characteristic.MEMORY), 13);
    }

    @org.testng.annotations.Test
    public void testGetCharacteristics() throws DaoOperationException {
        Set<Characteristic> characteristics = testDao.findCharacteristics(1);
        assertEquals(characteristics.size(), 2);
    }

    @org.testng.annotations.Test
    public void testGetTestAmountByStatus() throws DaoOperationException {
        int amount = testDao.findTestAmountByStatus(TestStatus.CONFIRMED, 1);
        assertTrue(amount>0);
    }

    @org.testng.annotations.Test
    public void testGetTestAmount() throws DaoOperationException {
        int amount = testDao.findTestAmount(1);
        assertTrue(amount>0);
    }

    @org.testng.annotations.Test
    public void testGetTestAmountByStatusAndAuthor() throws DaoOperationException {
        int amount = testDao.findTestAmountByStatus(TestStatus.CONFIRMED, 1);
        assertTrue(amount>0);
    }

    @org.testng.annotations.Test
    public void testGetTestAmountByAuthor() throws DaoOperationException {
        int amount = testDao.findTestAmount(1);
        assertTrue(amount>0);
    }

    @org.testng.annotations.Test
    public void testFindTests() throws DaoOperationException {
        List<Test> tests =
                testDao.findTests(
                        TestStatus.CONFIRMED,
                        2, 0);
        System.out.println(tests);
    }

    @org.testng.annotations.Test
    public void testUpdateStatus() throws DaoOperationException {
        testDao.updateStatus(13, TestStatus.NEW);
    }
}