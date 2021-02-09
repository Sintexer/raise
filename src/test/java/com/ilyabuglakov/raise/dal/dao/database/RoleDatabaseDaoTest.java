package com.ilyabuglakov.raise.dal.dao.database;

import static org.mockito.Mockito.*;

import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.domain.Role;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.domain.structure.Tables;
import com.ilyabuglakov.raise.domain.structure.columns.EntityColumns;
import com.ilyabuglakov.raise.domain.structure.columns.RoleColumns;
import com.ilyabuglakov.raise.domain.structure.columns.RolePermissionsColumns;
import com.ilyabuglakov.raise.domain.type.UserRole;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;

import static org.testng.Assert.*;

public class RoleDatabaseDaoTest {

    Connection connection;
    PreparedStatement statement;
    ResultSet resultSet;

    RoleDatabaseDao roleDatabaseDao;

    @BeforeMethod
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        roleDatabaseDao = mock(RoleDatabaseDao.class);
        when(connection.prepareStatement(any(), (int[]) any())).thenReturn(statement);
        when(statement.getResultSet()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        roleDatabaseDao = new RoleDatabaseDao(connection);
    }

    @Test
    public void testRead() throws SQLException, DaoOperationException {
        when(resultSet.wasNull()).thenReturn(false);
        when(resultSet.getString(RoleColumns.NAME.name())).thenReturn("USER");
        when(resultSet.getString(RolePermissionsColumns.PERMISSION.name())).thenReturn("test:get");

        Role roleExpected = Role.builder()
                .name("USER")
                .permissions(Collections.emptySet())
                .build();
        Role role = roleDatabaseDao.read(1).get();
        assertEquals(role.getName(), roleExpected.getName());

    }

    @Test
    public void testFindUserRoles() throws SQLException, DaoOperationException {
        when(resultSet.wasNull()).thenReturn(false);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(RoleColumns.NAME.name())).thenReturn(UserRole.USER.name());

        Set<UserRole> roles = roleDatabaseDao.findUserRoles(1);
        assertTrue(roles.contains(UserRole.USER));
    }

    @Test
    public void testFindPermissions() throws DaoOperationException, SQLException {

        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString(any())).thenReturn("test:create").thenReturn("user:get");

        Set<String> permissionsExpected = Set.of("test:create", "user:get");
        Set<String> permissions = roleDatabaseDao.findPermissions(2);
        assertTrue(permissions.containsAll(permissionsExpected));
    }
}