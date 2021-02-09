package com.ilyabuglakov.raise.dal.dao;

import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * The type Database dao.
 */
@Log4j2
public abstract class DatabaseDao {

    /**
     * The Connection.
     */
    protected Connection connection;

    /**
     * Instantiates a new Database dao.
     *
     * @param connection the connection
     */
    protected DatabaseDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Gets count from result set.
     *
     * @param resultSet the result set
     * @return the count
     * @throws DaoOperationException the dao operation exception
     */
    protected Integer getCount(ResultSet resultSet) throws DaoOperationException {

        Optional<ResultSet> rs = unpackResultSet(resultSet);
        int count = 0;
        if (rs.isPresent()) {
            try {
                count = rs.get().getInt("count");
            } catch (SQLException e) {
                throw new DaoOperationException("Can't get row count", e);
            } finally {
                closeResultSet(rs.get());
            }
        }

        return count;
    }

    /**
     * Execute result set and returns created id integer.
     *
     * @param statement the statement
     * @return the integer
     * @throws DaoOperationException the dao operation exception
     */
    protected Integer executeReturnId(PreparedStatement statement) throws DaoOperationException {
        ResultSet resultSet = null;
        try {
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next())
                return resultSet.getInt(1);
            else
                throw new DaoOperationException();
        } catch (SQLException e) {
            throw new DaoOperationException(e);
        } finally {
            closeResultSet(resultSet);
        }
    }

    /**
     * Execute update query.
     *
     * @param statement the statement
     * @throws DaoOperationException the dao operation exception
     */
    protected void executeUpdateQuery(PreparedStatement statement) throws DaoOperationException {
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoOperationException("Bad update query: " + statement, e);
        } finally {
            closeStatement(statement);
        }
    }

    /**
     * Execute statement and get Result set.
     *
     * @param statement the statement
     * @return the result set
     * @throws DaoOperationException the dao operation exception
     */
    protected ResultSet createResultSet(PreparedStatement statement) throws DaoOperationException {
        ResultSet resultSet = null;
        try {
            statement.executeQuery();
            resultSet = statement.getResultSet();
            return resultSet;
        } catch (SQLException e) {
            throw new DaoOperationException(e);
        }
    }

    /**
     * Create and configure Prepared statement.
     *
     * @param query               the query
     * @param statementParameters the statement parameters
     * @return the prepared statement
     * @throws DaoOperationException the dao operation exception
     */
    protected PreparedStatement prepareStatement(String query, int... statementParameters) throws DaoOperationException {
        try {
            PreparedStatement statement = connection.prepareStatement(query, statementParameters);
            statement.closeOnCompletion();
            return statement;
        } catch (SQLException e) {
            throw new DaoOperationException("Can't prepare statement", e);
        }
    }

    /**
     * Prepare statement that return keys.
     *
     * @param query the query
     * @return the prepared statement
     * @throws DaoOperationException the dao operation exception
     */
    protected PreparedStatement prepareStatementReturnKeys(String query) throws DaoOperationException {
        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.closeOnCompletion();
            return statement;
        } catch (SQLException e) {
            throw new DaoOperationException("Can't prepare statement", e);
        }
    }

    /**
     * Unpack result will call next() method on result set and return Optional empty  if result is false.
     *
     * @param resultSet the result set
     * @return the optional
     * @throws DaoOperationException the dao operation exception
     */
    protected Optional<ResultSet> unpackResultSet(ResultSet resultSet) throws DaoOperationException {
        try {
            if (resultSet.next())
                return Optional.of(resultSet);
            closeResultSet(resultSet);
        } catch (SQLException e) {
            throw new DaoOperationException("Can't access ResultSet", e);
        }
        return Optional.empty();
    }

    /**
     * Close statement.
     *
     * @param statement the statement
     */
    protected void closeStatement(Statement statement) {
        if (statement != null)
            try {
                statement.close();
            } catch (SQLException e) {
                log.error("Exception while closing statement", e);
            }
    }

    /**
     * Close result set.
     *
     * @param resultSet the result set
     */
    protected void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.error("Exception while closing result set", e);
            }
        }
    }

    /**
     * Create bad result set dao operation exception.
     *
     * @param parent the parent
     * @return the dao operation exception
     */
    protected DaoOperationException createBadResultSetException(Exception parent) {
        return new DaoOperationException("Bad result set after executing query. Some fields aren't present", parent);
    }

    /**
     * Sets id statement parameters.
     *
     * @param id        the id
     * @param statement the statement
     * @throws DaoOperationException the dao operation exception
     */
    protected void setIdStatementParameters(Integer id, PreparedStatement statement) throws DaoOperationException {
        try {
            statement.setInt(1, id);
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }
    }

    /**
     * Sets all int statement parameters.
     *
     * @param statement the statement
     * @param ids       the ids
     * @throws DaoOperationException the dao operation exception
     */
    protected void setAllIntStatementParameters(PreparedStatement statement, Integer... ids) throws DaoOperationException {
        try {
            for (int i = 1; i - 1 < ids.length; ++i)
                statement.setInt(i, ids[i - 1]);
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }
    }

}
