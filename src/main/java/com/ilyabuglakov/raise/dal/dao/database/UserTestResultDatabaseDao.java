package com.ilyabuglakov.raise.dal.dao.database;

import com.ilyabuglakov.raise.dal.dao.DatabaseDao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.dao.interfaces.UserTestResultDao;
import com.ilyabuglakov.raise.domain.Test;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.domain.UserTestResult;
import com.ilyabuglakov.raise.domain.structure.Tables;
import com.ilyabuglakov.raise.domain.structure.columns.EntityColumns;
import com.ilyabuglakov.raise.domain.structure.columns.UserTestResultColumns;
import com.ilyabuglakov.raise.model.service.validator.ResultSetValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * UserTestResultDao is the Dao implementation specifically for UserTestResult class
 * Based on DatabaseDao abstract class.
 */
public class UserTestResultDatabaseDao extends DatabaseDao implements UserTestResultDao {

    public static final String INSERT_USER_TEST_RESULT = String.format(
            "INSERT INTO %s(%s, %s, %s) VALUES(?, ?, ?)",
            Tables.USER_TEST_RESULT.name(),
            UserTestResultColumns.USER_ID.name(), UserTestResultColumns.TEST_ID.name(),
            UserTestResultColumns.RESULT.name());

    public static final String SELECT_BY_ID = String.format(
            "SELECT %s, %s, %s, %s FROM %s WHERE %s = ?",
            UserTestResultColumns.USER_ID.name(), UserTestResultColumns.TEST_ID.name(),
            UserTestResultColumns.RESULT.name(), EntityColumns.ID.name(),
            Tables.USER_TEST_RESULT.name(),
            EntityColumns.ID.name());

    public static final String UPDATE_BY_ID = String.format(
            "UPDATE %s SET %s=?, %s=?, %s=? WHERE %s = ?",
            Tables.USER_TEST_RESULT.name(),
            UserTestResultColumns.USER_ID.name(), UserTestResultColumns.TEST_ID.name(),
            UserTestResultColumns.RESULT.name(),
            EntityColumns.ID.name());

    public static final String DELETE_BY_ID = String.format(
            "DELETE FROM %s WHERE %s = ?",
            Tables.USER_TEST_RESULT.name(),
            EntityColumns.ID.name());

    public static final String SELECT_BY_USER_ID_TEST_ID = String.format(
            "SELECT %s, %s, %s, %s FROM %s WHERE %s=? AND %s=?",
            UserTestResultColumns.USER_ID.name(), UserTestResultColumns.TEST_ID.name(),
            UserTestResultColumns.RESULT.name(), EntityColumns.ID.name(),
            Tables.USER_TEST_RESULT.name(),
            UserTestResultColumns.USER_ID.name(), UserTestResultColumns.TEST_ID.name());

    public static final String SELECT_RESULT_COUNT_BY_USER_ID = String.format(
            "SELECT COUNT(*) FROM %s WHERE %s = ?",
            Tables.USER_TEST_RESULT.name(),
            UserTestResultColumns.USER_ID.name());

    public static final String SELECT_BY_USER_ID = String.format(
            "SELECT %s, %s, %s, %s FROM %s WHERE %s = ?",
            UserTestResultColumns.USER_ID.name(), UserTestResultColumns.TEST_ID.name(),
            UserTestResultColumns.RESULT.name(), EntityColumns.ID.name(),
            Tables.USER_TEST_RESULT.name(),
            UserTestResultColumns.USER_ID.name());

    public UserTestResultDatabaseDao(Connection connection) {
        super(connection);
    }

    @Override
    public Integer create(UserTestResult userTestResult) throws DaoOperationException {
        PreparedStatement statement = prepareStatementReturnKeys(INSERT_USER_TEST_RESULT);
        setAllStatementParameters(userTestResult, statement);

        return executeReturnId(statement);
    }

    @Override
    public Optional<UserTestResult> read(Integer id) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_BY_ID);
        setIdStatementParameters(id, statement);

        Optional<ResultSet> resultSet = unpackResultSet(createResultSet(statement));
        Optional<UserTestResult> userTestResult = Optional.empty();
        if (resultSet.isPresent()) {
            userTestResult = buildUserTestResult(resultSet.get());
            closeResultSet(resultSet.get());
        }

        return userTestResult;
    }

    @Override
    public void update(UserTestResult userTestResult) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(UPDATE_BY_ID);
        setAllStatementParameters(userTestResult, statement);
        try {
            statement.setInt(4, userTestResult.getId());
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }

        executeUpdateQuery(statement);
    }

    @Override
    public void delete(UserTestResult userTestResult) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(DELETE_BY_ID);
        setIdStatementParameters(userTestResult.getId(), statement);

        executeUpdateQuery(statement);
    }

    @Override
    public Optional<UserTestResult> findByUserIdAndTestId(Integer userId, Integer testId) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_BY_USER_ID_TEST_ID);
        try {
            statement.setInt(1, userId);
            statement.setInt(2, testId);
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }
        Optional<ResultSet> resultSet = unpackResultSet(createResultSet(statement));
        Optional<UserTestResult> userTestResult = Optional.empty();
        if (resultSet.isPresent()) {
            userTestResult = buildUserTestResult(resultSet.get());
            closeResultSet(resultSet.get());
        }
        return userTestResult;
    }

    @Override
    public int findResultAmount(Integer userId) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_RESULT_COUNT_BY_USER_ID);
        setIdStatementParameters(userId, statement);

        return getCount(createResultSet(statement));
    }

    @Override
    public List<UserTestResult> findUserTestResults(Integer userId) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_BY_USER_ID);
        setIdStatementParameters(userId, statement);

        ResultSet resultSet = createResultSet(statement);
        List<Optional<UserTestResult>> userTestResults = new ArrayList<>();
        try {
            while (resultSet.next()) {
                userTestResults.add(buildUserTestResult(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Error while reading resultSet", e);
        } finally {
            closeResultSet(resultSet);
        }

        return userTestResults.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private void setAllStatementParameters(UserTestResult userTestResult, PreparedStatement statement) throws DaoOperationException {
        try {
            statement.setInt(1, userTestResult.getUser().getId());
            statement.setInt(2, userTestResult.getTest().getId());
            statement.setInt(3, userTestResult.getResult());
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }
    }

    /**
     * This operation won't close resultSet in success case, but will
     * in case of exception thrown
     * <p>
     * Will build Optional-UserTestResult only if resultSet has values of all UserTestResult fields,
     * otherwise will return Optional.empty()
     *
     * @param resultSet input resultSet parameters, taken from sql query execution
     * @return Optional UserTestResult from resultSet
     */
    private Optional<UserTestResult> buildUserTestResult(ResultSet resultSet) throws DaoOperationException {
        try {
            ResultSetValidator validator = new ResultSetValidator();
            if (validator.hasAllValues(resultSet,
                    UserTestResultColumns.RESULT.name(),
                    UserTestResultColumns.USER_ID.name(),
                    UserTestResultColumns.TEST_ID.name(),
                    EntityColumns.ID.name())) {
                UserTestResult userTestResult = UserTestResult.builder()
                        .result(resultSet.getInt(UserTestResultColumns.RESULT.name()))
                        .build();
                userTestResult.setUser(User.builder().id(resultSet.getInt(UserTestResultColumns.USER_ID.name())).build());
                userTestResult.setTest(Test.builder().id(resultSet.getInt(UserTestResultColumns.TEST_ID.name())).build());
                userTestResult.setId(resultSet.getInt(EntityColumns.ID.name()));
                return Optional.of(userTestResult);
            }
            return Optional.empty();
        } catch (SQLException e) {
            closeResultSet(resultSet);
            throw createBadResultSetException(e);
        }
    }
}
