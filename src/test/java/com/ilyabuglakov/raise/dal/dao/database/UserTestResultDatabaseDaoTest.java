package com.ilyabuglakov.raise.dal.dao.database;

import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.domain.Role;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.domain.UserTestResult;
import com.ilyabuglakov.raise.domain.structure.columns.RoleColumns;
import com.ilyabuglakov.raise.domain.structure.columns.RolePermissionsColumns;
import com.ilyabuglakov.raise.domain.structure.columns.UserTestResultColumns;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class UserTestResultDatabaseDaoTest {

    Connection connection;
    PreparedStatement statement;
    ResultSet resultSet;

    UserTestResultDatabaseDao userTestResultDatabaseDao;

    @BeforeMethod
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        when(connection.prepareStatement(any(), (int[]) any())).thenReturn(statement);
        when(connection.prepareStatement(any(), anyInt())).thenReturn(statement);
        when(statement.getResultSet()).thenReturn(resultSet);
        when(statement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        userTestResultDatabaseDao = new UserTestResultDatabaseDao(connection);
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test
    public void testCreate() throws SQLException, DaoOperationException {
        when(resultSet.getInt(1)).thenReturn(4);
        Integer id = userTestResultDatabaseDao.create(UserTestResult.builder()
                .test(com.ilyabuglakov.raise.domain.Test.builder().id(1).build())
                .user(User.builder().id(2).build())
                .result(1)
                .build());

        Assert.assertEquals((int) id, 4, "Returned id is not 4");
    }

    @Test
    public void testRead() throws SQLException, DaoOperationException {
        int result = 67;
        int userId = 2;
        int testId = 3;
        when(resultSet.wasNull()).thenReturn(false);
        when(resultSet.getInt(UserTestResultColumns.RESULT.name())).thenReturn(result);
        when(resultSet.getInt(UserTestResultColumns.USER_ID.name())).thenReturn(userId);
        when(resultSet.getInt(UserTestResultColumns.TEST_ID.name())).thenReturn(testId);

        UserTestResult expected = UserTestResult.builder()
                .user(User.builder().id(userId).build())
                .test(com.ilyabuglakov.raise.domain.Test.builder().id(testId).build())
                .result(result)
                .build();
        UserTestResult actual = userTestResultDatabaseDao.read(1).get();
        assertEquals(expected.getTest().getId(), actual.getTest().getId(), "Returned test result do not match expected by testId");
        assertEquals(expected.getUser().getId(), actual.getUser().getId(), "Returned test result do not match expected by userId");
        assertEquals(expected.getResult(), actual.getResult(), "Returned test result do not match expected by result");
    }

    @Test
    public void testFindByUserIdAndTestId() throws SQLException, DaoOperationException {
        int result = 67;
        int userId = 2;
        int testId = 3;
        when(resultSet.wasNull()).thenReturn(false);
        when(resultSet.getInt(UserTestResultColumns.RESULT.name())).thenReturn(result);
        when(resultSet.getInt(UserTestResultColumns.USER_ID.name())).thenReturn(userId);
        when(resultSet.getInt(UserTestResultColumns.TEST_ID.name())).thenReturn(testId);

        UserTestResult expected = UserTestResult.builder()
                .user(User.builder().id(userId).build())
                .test(com.ilyabuglakov.raise.domain.Test.builder().id(testId).build())
                .result(result)
                .build();
        UserTestResult actual = userTestResultDatabaseDao.findByUserIdAndTestId(userId, testId).get();
        assertEquals(expected.getTest().getId(), actual.getTest().getId(), "Returned test result do not match expected by testId");
        assertEquals(expected.getUser().getId(), actual.getUser().getId(), "Returned test result do not match expected by userId");
        assertEquals(expected.getResult(), actual.getResult(), "Returned test result do not match expected by result");
    }

    @Test
    public void testFindResultAmount() throws DaoOperationException, SQLException {
        int userId = 3;
        int amount = 50;
        when(resultSet.getInt("count")).thenReturn(amount);

        Integer count = userTestResultDatabaseDao.findResultAmount(userId);

        Assert.assertEquals((int)count, amount);
    }

}