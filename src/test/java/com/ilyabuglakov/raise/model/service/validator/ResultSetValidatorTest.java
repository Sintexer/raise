package com.ilyabuglakov.raise.model.service.validator;

import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.testng.Assert.*;

public class ResultSetValidatorTest {

    @Test
    public void testHasAllValues() throws SQLException {
        ResultSetValidator validator = new ResultSetValidator();
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getObject(Mockito.anyString())).thenReturn(new Object());
        assertTrue(validator.hasAllValues(resultSet, "str"));
    }

    @Test
    public void testFalseHasAllValues() throws SQLException {
        ResultSetValidator validator = new ResultSetValidator();
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getObject(Mockito.anyString())).thenReturn(null);
        Mockito.when(resultSet.wasNull()).thenReturn(true);
        assertFalse(validator.hasAllValues(resultSet, "str"));
    }
}