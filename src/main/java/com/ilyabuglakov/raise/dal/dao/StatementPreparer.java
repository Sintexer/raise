package com.ilyabuglakov.raise.dal.dao;

import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The interface Statement preparer.
 * This functional interface is used to specify the way of preparing the statement and delegate the exception
 * handling to the place, where statement will be executed
 */
@FunctionalInterface
public interface StatementPreparer {
    /**
     * Prepares statement.
     *
     * @return the prepared statement
     * @throws SQLException          the sql exception
     * @throws DaoOperationException the dao operation exception
     */
    PreparedStatement prepareStatement() throws SQLException, DaoOperationException;
}
