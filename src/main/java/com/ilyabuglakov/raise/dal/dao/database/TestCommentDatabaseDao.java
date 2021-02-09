package com.ilyabuglakov.raise.dal.dao.database;

import com.ilyabuglakov.raise.dal.dao.DatabaseDao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.dao.interfaces.TestCommentDao;
import com.ilyabuglakov.raise.domain.Test;
import com.ilyabuglakov.raise.domain.TestComment;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.domain.structure.Tables;
import com.ilyabuglakov.raise.domain.structure.columns.EntityColumns;
import com.ilyabuglakov.raise.domain.structure.columns.TestCommentColumns;
import com.ilyabuglakov.raise.domain.structure.columns.UserColumns;
import com.ilyabuglakov.raise.model.service.validator.ResultSetValidator;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TestCommentDatabaseDao is the Dao implementation specifically for TestComment class
 * Based on DatabaseDao abstract class.
 */
@Log4j2
public class TestCommentDatabaseDao extends DatabaseDao implements TestCommentDao {

    public static final String INSERT_COMMENT = String.format(
            "INSERT INTO %s(%s, %s, %s, %s) VALUES(?, ?, ?, ?)",
            Tables.TEST_COMMENT.name(),
            TestCommentColumns.USER_ID.name(), TestCommentColumns.TEST_ID.name(),
            TestCommentColumns.TIMESTAMP.name(), TestCommentColumns.CONTENT.name());

    public static final String SELECT_BY_ID = String.format(
            "SELECT %s, %s, %s, %s, %s FROM %s WHERE %s = ?",
            EntityColumns.ID.name(), TestCommentColumns.USER_ID.name(), TestCommentColumns.TEST_ID.name(),
            TestCommentColumns.TIMESTAMP.name(), TestCommentColumns.CONTENT.name(),
            Tables.TEST_COMMENT.name(),
            EntityColumns.ID.name());

    public static final String UPDATE_BY_ID = String.format(
            "UPDATE %s SET %s=?, %s=?, %s=?, %s=? WHERE %s = ?",
            Tables.TEST_COMMENT.name(),
            TestCommentColumns.USER_ID.name(), TestCommentColumns.TEST_ID.name(),
            TestCommentColumns.TIMESTAMP.name(), TestCommentColumns.CONTENT.name(),
            EntityColumns.ID.name());

    public static final String DELETE_BY_ID = String.format(
            "DELETE FROM %s WHERE %s = ?",
            Tables.TEST_COMMENT.name(),
            EntityColumns.ID.name());

    public static final String SELECT_COUNT = String.format(
            "SELECT COUNT(*) FROM %s",
            Tables.TEST_COMMENT.name());

    public static final String SELECT_COUNT_BY_TEST_ID = String.format(
            "SELECT COUNT(*) FROM %s WHERE %s = ?",
            Tables.TEST_COMMENT.name(),
            TestCommentColumns.TEST_ID.name());

    public static final String SELECT_BY_TEST_ID_LIMIT_OFFSET = String.format(
            "SELECT %s, %s, %s, %s, %s FROM %s WHERE %s = ? ORDER BY %s DESC LIMIT ? OFFSET ?",
            EntityColumns.ID.name(), TestCommentColumns.USER_ID.name(), TestCommentColumns.TEST_ID.name(),
            TestCommentColumns.TIMESTAMP.name(), TestCommentColumns.CONTENT.name(),
            Tables.TEST_COMMENT.name(),
            TestCommentColumns.TEST_ID.name(),
            TestCommentColumns.TIMESTAMP);

    public static final String SELECT_USER_COMMENT_COUNT = String.format(
            "SELECT COUNT(*) FROM %s WHERE %s = (SELECT %s FROM %s WHERE %s = ?)",
            Tables.TEST_COMMENT.name(),
            TestCommentColumns.USER_ID.name(),
            EntityColumns.ID.name(),
            Tables.USR.name(),
            UserColumns.EMAIL.name());

    public TestCommentDatabaseDao(Connection connection) {
        super(connection);
    }

    @Override
    public Integer create(TestComment testComment) throws DaoOperationException {
        PreparedStatement statement = prepareStatementReturnKeys(INSERT_COMMENT);
        setAllStatementParameters(testComment, statement);

        return executeReturnId(statement);
    }

    @Override
    public Optional<TestComment> read(Integer id) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_BY_ID);
        setIdStatementParameters(id, statement);

        Optional<ResultSet> resultSet = unpackResultSet(createResultSet(statement));
        Optional<TestComment> testComment = Optional.empty();
        if (resultSet.isPresent()) {
            testComment = buildTestComment(resultSet.get());
            closeResultSet(resultSet.get());
        }
        return testComment;
    }

    @Override
    public void update(TestComment testComment) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(UPDATE_BY_ID);
        setAllStatementParameters(testComment, statement);
        try {
            statement.setInt(5, testComment.getId());
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }

        executeUpdateQuery(statement);
    }

    @Override
    public void delete(TestComment testComment) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(DELETE_BY_ID);
        setIdStatementParameters(testComment.getId(), statement);

        executeUpdateQuery(statement);
    }

    @Override
    public Integer findCommentsAmount(Integer testId) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_COUNT_BY_TEST_ID);
        setIdStatementParameters(testId, statement);
        return getCount(createResultSet(statement));
    }

    @Override
    public List<TestComment> findComments(Integer testId, int offset, int items) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_BY_TEST_ID_LIMIT_OFFSET);
        log.debug("offset:" + offset + "; items: " + items);
        try {
            statement.setInt(1, testId);
            statement.setInt(2, items);
            statement.setInt(3, offset);
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }

        ResultSet resultSet = createResultSet(statement);
        List<Optional<TestComment>> testComments = new ArrayList<>();

        try {
            while (resultSet.next()) {
                testComments.add(buildTestComment(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Error while reading tests from resultSet", e);
        } finally {
            closeResultSet(resultSet);
        }
        return testComments.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    @Override
    public int findUserCommentAmount(String email) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_USER_COMMENT_COUNT);
        try {
            statement.setString(1, email);
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }
        return getCount(createResultSet(statement));
    }

    private void setAllStatementParameters(TestComment comment, PreparedStatement statement) throws DaoOperationException {
        try {
            statement.setInt(1, comment.getUser().getId());
            statement.setInt(2, comment.getTest().getId());
            statement.setTimestamp(3, Timestamp.valueOf(comment.getTimestamp()));
            statement.setString(4, comment.getContent());
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }
    }

    /**
     * This operation won't close resultSet in success case, but will
     * in case of exception thrown
     * <p>
     * Will build Optional-TestComment only if resultSet has values of all user fields,
     * otherwise will return Optional.empty()
     *
     * @param resultSet input resultSet parameters, taken from sql query execution
     * @return TestComment from resultSet
     */
    private Optional<TestComment> buildTestComment(ResultSet resultSet) throws DaoOperationException {
        try {
            ResultSetValidator validator = new ResultSetValidator();
            if (validator.hasAllValues(resultSet,
                    TestCommentColumns.CONTENT.name(),
                    TestCommentColumns.USER_ID.name(),
                    TestCommentColumns.TEST_ID.name(),
                    TestCommentColumns.TIMESTAMP.name(),
                    EntityColumns.ID.name())) {
                TestComment testComment = TestComment.builder()
                        .content(resultSet.getString(TestCommentColumns.CONTENT.name()))
                        .user(User.builder().id(resultSet.getInt(TestCommentColumns.USER_ID.name())).build())
                        .test(Test.builder().id(resultSet.getInt(TestCommentColumns.TEST_ID.name())).build())
                        .timestamp(resultSet.getTimestamp(TestCommentColumns.TIMESTAMP.name()).toLocalDateTime())
                        .build();
                testComment.setId(resultSet.getInt(EntityColumns.ID.name()));
                return Optional.of(testComment);
            }
            return Optional.empty();
        } catch (SQLException e) {
            closeResultSet(resultSet);
            throw createBadResultSetException(e);
        }
    }
}
