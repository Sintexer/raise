package com.ilyabuglakov.raise.dal.dao.database;

import com.ilyabuglakov.raise.dal.dao.DatabaseDao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.dao.interfaces.AnswerDao;
import com.ilyabuglakov.raise.domain.Answer;
import com.ilyabuglakov.raise.domain.structure.Tables;
import com.ilyabuglakov.raise.domain.structure.columns.AnswerColumns;
import com.ilyabuglakov.raise.domain.structure.columns.EntityColumns;
import com.ilyabuglakov.raise.model.service.validator.ResultSetValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AnswerDao is the Dao implementation specifically for Answer class.
 * Based on DatabaseDao abstract class.
 */
public class AnswerDatabaseDao extends DatabaseDao implements AnswerDao {

    private static final String INSERT_ANSWER = String.format(
            "INSERT INTO %s(%s, %s, %s) VALUES(?, ?, ?)",
            Tables.ANSWER.name(),
            AnswerColumns.CONTENT.name(), AnswerColumns.CORRECT.name(), AnswerColumns.QUESTION_ID.name());

    private static final String SELECT_WHERE_ID = String.format(
            "SELECT %s, %s, %s FROM %s WHERE %s = ?",
            EntityColumns.ID.name(), AnswerColumns.CONTENT.name(), AnswerColumns.CORRECT.name(),
            Tables.ANSWER.name(),
            EntityColumns.ID.name());

    private static final String UPDATE_BY_ID = String.format(
            "UPDATE %s SET %s=?, %s=?, %s=? WHERE %s = ?",
            Tables.ANSWER.name(),
            AnswerColumns.CONTENT.name(), AnswerColumns.CORRECT.name(), AnswerColumns.QUESTION_ID.name(),
            EntityColumns.ID.name());

    private static final String DELETE_BY_ID = String.format(
            "DELETE FROM %s WHERE %s = ?",
            Tables.ANSWER.name(),
            EntityColumns.ID.name());

    private static final String SELECT_WHERE_QUESTION_ID = String.format(
            "SELECT %s, %s, %s FROM %s WHERE %s = ?",
            EntityColumns.ID.name(), AnswerColumns.CONTENT.name(), AnswerColumns.CORRECT.name(),
            Tables.ANSWER.name(),
            AnswerColumns.QUESTION_ID.name());

    public AnswerDatabaseDao(Connection connection) {
        super(connection);
    }

    @Override
    public Integer create(Answer answer) throws DaoOperationException {
        PreparedStatement preparedStatement = prepareStatementReturnKeys(INSERT_ANSWER);
        setAllStatementParameters(answer, preparedStatement);
        return executeReturnId(preparedStatement);
    }

    @Override
    public Optional<Answer> read(Integer id) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_WHERE_ID);
        setIdStatementParameters(id, statement);
        Optional<ResultSet> resultSet = unpackResultSet(createResultSet(statement));
        Optional<Answer> answer = Optional.empty();
        if (resultSet.isPresent()) {
            answer = buildAnswer(resultSet.get());
            closeResultSet(resultSet.get());
        }
        return answer;
    }

    @Override
    public void update(Answer answer) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(UPDATE_BY_ID);
        setAllStatementParameters(answer, statement);
        try {
            statement.setInt(4, answer.getId());
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't change result set parameters", e);
        }

        executeUpdateQuery(statement);
    }

    @Override
    public void delete(Answer answer) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(DELETE_BY_ID);
        setIdStatementParameters(answer.getId(), statement);

        executeUpdateQuery(statement);
    }

    @Override
    public Set<Answer> findByQuestionId(Integer questionId) throws DaoOperationException {
        Set<Optional<Answer>> answers = new HashSet<>();

        PreparedStatement preparedStatement = prepareStatement(SELECT_WHERE_QUESTION_ID);
        ResultSet resultSet = null;
        try {
            preparedStatement.setInt(1, questionId);
            resultSet = createResultSet(preparedStatement);
            while (resultSet.next()) {
                Optional<Answer> answer = buildAnswer(resultSet);
                answers.add(answer);
            }
            return answers.stream()
                    .flatMap(Optional::stream)
                    .collect(Collectors.toSet());
        } catch (SQLException e) {
            throw new DaoOperationException("Bad result set after executing query. Can't build entities", e);
        } finally {
            closeResultSet(resultSet);
        }
    }

    @Override
    public void createAll(Collection<Answer> answers) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(INSERT_ANSWER);
        try {
            for (Answer answer : answers) {
                setAllStatementParameters(answer, statement);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DaoOperationException("Can't save batch", e);
        } finally {
            closeStatement(statement);
        }
    }

    private void setAllStatementParameters(Answer answer, PreparedStatement statement) throws DaoOperationException {
        try {
            statement.setString(1, answer.getContent());
            statement.setBoolean(2, answer.isCorrect());
            statement.setInt(3, answer.getQuestion().getId());
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }
    }

    /**
     * This operation won't close resultSet in success case, but will
     * in case of exception thrown
     * <p>
     * Will build Optional-Answer only if resultSet has values of all user fields,
     * otherwise will return Optional.empty()
     *
     * @param resultSet input result set parameters, taken from sql query execution
     * @return Answer from resultSet
     */
    private Optional<Answer> buildAnswer(ResultSet resultSet) throws DaoOperationException {
        try {
            ResultSetValidator validator = new ResultSetValidator();
            if (validator.hasAllValues(resultSet,
                    AnswerColumns.CONTENT.name(),
                    AnswerColumns.CORRECT.name(),
                    EntityColumns.ID.name())) {
                Answer answer = Answer.builder()
                        .content(resultSet.getString(AnswerColumns.CONTENT.name()))
                        .correct(resultSet.getBoolean(AnswerColumns.CORRECT.name()))
                        .build();
                answer.setId(resultSet.getInt(EntityColumns.ID.name()));
                return Optional.of(answer);
            }
            return Optional.empty();
        } catch (SQLException e) {
            closeResultSet(resultSet);
            throw createBadResultSetException(e);
        }
    }
}
