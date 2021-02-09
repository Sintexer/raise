package com.ilyabuglakov.raise.dal.dao.database;

import com.ilyabuglakov.raise.dal.dao.DatabaseDao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.dao.interfaces.QuestionDao;
import com.ilyabuglakov.raise.domain.Question;
import com.ilyabuglakov.raise.domain.structure.Tables;
import com.ilyabuglakov.raise.domain.structure.columns.EntityColumns;
import com.ilyabuglakov.raise.domain.structure.columns.QuestionColumns;
import com.ilyabuglakov.raise.model.service.validator.ResultSetValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * QuestionDao is the Dao implementation specifically for Question class
 * Based on DatabaseDao abstract class.
 */
public class QuestionDatabaseDao extends DatabaseDao implements QuestionDao {

    private static final String INSERT_QUESTION = String.format(
            "INSERT INTO %s(%s, %s, %s) VALUES(?, ?, ?)",
            Tables.QUESTION.name(),
            QuestionColumns.NAME.name(), QuestionColumns.CONTENT.name(), QuestionColumns.TEST_ID.name());

    private static final String SELECT_WHERE_ID = String.format(
            "SELECT %s, %s, %s FROM %s WHERE %s = ?",
            EntityColumns.ID.name(), QuestionColumns.NAME.name(), QuestionColumns.CONTENT.name(),
            Tables.QUESTION.name(),
            EntityColumns.ID.name());

    private static final String UPDATE_BY_ID = String.format(
            "UPDATE %s SET %s=?, %s=?, %s=? WHERE %s = ?",
            Tables.QUESTION.name(),
            QuestionColumns.NAME.name(), QuestionColumns.CONTENT.name(), QuestionColumns.TEST_ID.name(),
            EntityColumns.ID.name());

    private static final String DELETE_BY_ID = String.format(
            "DELETE FROM %s WHERE %s = ?",
            Tables.QUESTION.name(),
            EntityColumns.ID.name());

    private static final String SELECT_QUESTION_NAMES_BY_TEST_ID = String.format(
            "SELECT %s FROM %s WHERE %s = ?",
            QuestionColumns.NAME.name(),
            Tables.QUESTION.name(),
            QuestionColumns.TEST_ID.name());

    private static final String SELECT_QUESTION_COUNT_BY_TEST_ID = String.format(
            "SELECT COUNT(*) FROM %s WHERE %s = ?",
            Tables.QUESTION.name(),
            QuestionColumns.TEST_ID.name());

    private static final String SELECT_BY_TEST_ID = String.format(
            "SELECT %s, %s, %s FROM %s WHERE %s = ?",
            EntityColumns.ID.name(), QuestionColumns.NAME.name(), QuestionColumns.CONTENT.name(),
            Tables.QUESTION.name(),
            QuestionColumns.TEST_ID.name());

    public QuestionDatabaseDao(Connection connection) {
        super(connection);
    }

    @Override
    public Integer create(Question question) throws DaoOperationException {
        PreparedStatement statement = prepareStatementReturnKeys(INSERT_QUESTION);
        setAllStatementParameters(question, statement);

        return executeReturnId(statement);
    }

    @Override
    public Optional<Question> read(Integer id) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_WHERE_ID);
        setIdStatementParameters(id, statement);
        Optional<Question> question = Optional.empty();
        Optional<ResultSet> optionalResultSet = unpackResultSet(createResultSet(statement));
        if (optionalResultSet.isPresent()) {
            question = buildQuestion(optionalResultSet.get());
            closeResultSet(optionalResultSet.get());
        }

        return question;
    }

    @Override
    public void update(Question question) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(UPDATE_BY_ID);
        setAllStatementParameters(question, statement);
        try {
            statement.setInt(4, question.getId());
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't change result set parameters", e);
        }

        executeUpdateQuery(statement);
    }

    @Override
    public void delete(Question question) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(DELETE_BY_ID);
        setIdStatementParameters(question.getId(), statement);

        executeUpdateQuery(statement);
    }

    @Override
    public List<String> findQuestionsNames(Integer testId) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_QUESTION_NAMES_BY_TEST_ID);

        try {
            statement.setInt(1, testId);
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't change result set parameters", e);
        }

        ResultSet resultSet = createResultSet(statement);
        List<String> questionsNames = new ArrayList<>();
        try {
            while (resultSet.next()) {
                questionsNames.add(resultSet.getString(QuestionColumns.NAME.name()));
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Can't read resultSet", e);
        } finally {
            closeResultSet(resultSet);
        }
        return questionsNames;
    }

    @Override
    public Optional<Integer> findQuestionAmount(Integer testId) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_QUESTION_COUNT_BY_TEST_ID);
        setIdStatementParameters(testId, statement);
        Optional<ResultSet> resultSet = unpackResultSet(createResultSet(statement));
        Optional<Integer> count = Optional.empty();
        if (resultSet.isPresent()) {
            try {
                count = Optional.ofNullable(resultSet.get().getInt("count"));
            } catch (SQLException e) {
                throw new DaoOperationException("Can't get row count", e);
            } finally {
                closeResultSet(resultSet.get());
            }
        }

        return count;
    }

    @Override
    public Set<Question> findByTestId(Integer testId) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_BY_TEST_ID);
        setIdStatementParameters(testId, statement);

        Set<Optional<Question>> questions = new HashSet<>();
        ResultSet resultSet = createResultSet(statement);
        try {
            while (resultSet.next()) {
                Optional<Question> question = buildQuestion(resultSet);
                questions.add(question);
            }
            return questions.stream()
                    .flatMap(Optional::stream)
                    .collect(Collectors.toSet());
        } catch (SQLException e) {
            throw new DaoOperationException("Bad result set after executing query. Can't build entities", e);
        } finally {
            closeResultSet(resultSet);
        }
    }

    @Override
    public void createAll(Collection<Question> questions) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(INSERT_QUESTION);
        try {
            for (Question question : questions) {
                setAllStatementParameters(question, statement);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DaoOperationException("Can't save batch", e);
        } finally {
            closeStatement(statement);
        }
    }

    private void setAllStatementParameters(Question question, PreparedStatement statement) throws DaoOperationException {
        try {
            statement.setString(1, question.getName());
            statement.setString(2, question.getContent());
            statement.setInt(3, question.getTest().getId());
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }
    }

    /**
     * This operation won't close resultSet in success case, but will
     * in case of exception thrown
     * <p>
     * Will build Optional-Question only if resultSet has values of all user fields,
     * otherwise will return Optional.empty()
     *
     * @param resultSet input resultSet parameters, taken from sql query execution
     * @return Question from resultSet
     */
    private Optional<Question> buildQuestion(ResultSet resultSet) throws DaoOperationException {
        try {
            ResultSetValidator validator = new ResultSetValidator();
            if (validator.hasAllValues(resultSet,
                    QuestionColumns.CONTENT.name(),
                    QuestionColumns.NAME.name(),
                    EntityColumns.ID.name())) {
                Question question = Question.builder()
                        .content(resultSet.getString(QuestionColumns.CONTENT.name()))
                        .name(resultSet.getString(QuestionColumns.NAME.name()))
                        .build();
                question.setId(resultSet.getInt(EntityColumns.ID.name()));
                return Optional.of(question);
            }
            return Optional.empty();
        } catch (SQLException e) {
            closeResultSet(resultSet);
            throw createBadResultSetException(e);
        }
    }
}
