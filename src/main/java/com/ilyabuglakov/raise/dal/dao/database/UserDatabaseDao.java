package com.ilyabuglakov.raise.dal.dao.database;

import com.ilyabuglakov.raise.dal.dao.DatabaseDao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.dao.interfaces.UserDao;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.domain.UsrKey;
import com.ilyabuglakov.raise.domain.structure.Tables;
import com.ilyabuglakov.raise.domain.structure.columns.EntityColumns;
import com.ilyabuglakov.raise.domain.structure.columns.UserColumns;
import com.ilyabuglakov.raise.domain.structure.columns.UsrKeyColumns;
import com.ilyabuglakov.raise.domain.type.Status;
import com.ilyabuglakov.raise.model.service.validator.ResultSetValidator;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * UserDao is the Dao implementation specifically for User class
 * Based on DatabaseDao abstract class.
 */
public class UserDatabaseDao extends DatabaseDao implements UserDao {

    public static final String INSERT_USER = String.format(
            "INSERT INTO %s(%s, %s, %s, %s, %s, %s) VALUES(?, ?, ?, ?, ?, ?)",
            Tables.USR.name(),
            UserColumns.EMAIL.name(), UserColumns.NAME.name(), UserColumns.SURNAME.name(),
            UserColumns.REGISTRATION_DATE.name(), UserColumns.STATUS.name(), UserColumns.PASSWORD.name());

    public static final String SELECT_BY_ID = String.format(
            "SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s = ?",
            EntityColumns.ID.name(), UserColumns.EMAIL.name(), UserColumns.NAME.name(), UserColumns.SURNAME.name(),
            UserColumns.REGISTRATION_DATE.name(), UserColumns.STATUS.name(), UserColumns.PASSWORD.name(),
            Tables.USR.name(),
            EntityColumns.ID.name());

    public static final String UPDATE_BY_ID = String.format(
            "UPDATE %s SET %s=?, %s=?, %s=?, %s=? WHERE %s = ?",
            Tables.USR.name(),
            UserColumns.NAME.name(), UserColumns.SURNAME.name(), UserColumns.STATUS.name(), UserColumns.PASSWORD,
            EntityColumns.ID.name());

    public static final String DELETE_BY_ID = String.format(
            "DELETE FROM %s WHERE %s=?",
            Tables.USR.name(),
            EntityColumns.ID.name());

    public static final String SELECT_BY_EMAIL = String.format(
            "SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s = ?",
            EntityColumns.ID.name(), UserColumns.EMAIL.name(), UserColumns.NAME.name(), UserColumns.SURNAME.name(),
            UserColumns.REGISTRATION_DATE.name(), UserColumns.STATUS.name(), UserColumns.PASSWORD.name(),
            Tables.USR.name(),
            UserColumns.EMAIL.name());

    public static final String SELECT_USER_INFO_BY_ID = String.format(
            "SELECT %s, %s, %s FROM %s WHERE %s = ?",
            EntityColumns.ID.name(), UserColumns.NAME.name(), UserColumns.SURNAME.name(),
            Tables.USR.name(),
            EntityColumns.ID.name());

    public static final String INSERT_KEY = String.format(
            "INSERT INTO %s(%s, %s, %s) VALUES(?, ?, ?)",
            Tables.USR_KEY.name(),
            UsrKeyColumns.KEY.name(), UsrKeyColumns.USER_ID.name(), UsrKeyColumns.TIMESTAMP.name());

    public static final String SELECT_KEY = String.format(
            "SELECT %s, %s, %s FROM %s WHERE %s = ?",
            UsrKeyColumns.KEY.name(), UsrKeyColumns.USER_ID.name(), UsrKeyColumns.TIMESTAMP.name(),
            Tables.USR_KEY.name(),
            UsrKeyColumns.KEY.name());

    public static final String UPDATE_STATUS_BY_ID = String.format(
            "UPDATE %s SET %s = ? WHERE %s = ?",
            Tables.USR.name(),
            UserColumns.STATUS.name(),
            EntityColumns.ID.name());

    public static final String DELETE_KEY = String.format(
            "DELETE FROM %s WHERE %s = ?",
            Tables.USR_KEY.name(),
            UsrKeyColumns.KEY.name());

    public UserDatabaseDao(Connection connection) {
        super(connection);
    }

    @Override
    public Integer create(User user) throws DaoOperationException {
        PreparedStatement statement = prepareStatementReturnKeys(INSERT_USER);
        setAllStatementParameters(user, statement);

        return executeReturnId(statement);
    }

    @Override
    public Optional<User> read(Integer id) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_BY_ID);
        setIdStatementParameters(id, statement);

        Optional<ResultSet> resultSet = unpackResultSet(createResultSet(statement));
        Optional<User> user = Optional.empty();
        if (resultSet.isPresent()) {
            user = buildUser(resultSet.get());
            closeResultSet(resultSet.get());
        }
        return user;
    }

    @Override
    public void update(User user) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(UPDATE_BY_ID);
        try {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setObject(3, user.getStatus(), Types.OTHER);
            statement.setString(4, user.getPassword());
            statement.setInt(5, user.getId());
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }
        executeUpdateQuery(statement);
    }

    @Override
    public void delete(User user) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(DELETE_BY_ID);
        setIdStatementParameters(user.getId(), statement);

        executeUpdateQuery(statement);
    }

    @Override
    public Optional<User> findByEmail(String email) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_BY_EMAIL);
        try {
            statement.setString(1, email);
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }

        Optional<ResultSet> optionalResultSet = unpackResultSet(createResultSet(statement));
        if (optionalResultSet.isPresent())
            return buildUser(optionalResultSet.get());
        return Optional.empty();
    }


    @Override
    public Optional<User> findUserInfo(Integer id) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_USER_INFO_BY_ID);
        setIdStatementParameters(id, statement);
        Optional<ResultSet> optionalResultSet = unpackResultSet(createResultSet(statement));
        Optional<User> userOptional = Optional.empty();
        if (optionalResultSet.isPresent()) {
            try {
                ResultSet resultSet = optionalResultSet.get();
                userOptional = Optional.of(User.builder()
                        .name(resultSet.getString(UserColumns.NAME.name()))
                        .surname(resultSet.getString(UserColumns.SURNAME.name()))
                        .id(resultSet.getInt(EntityColumns.ID.name()))
                        .build());
            } catch (SQLException e) {
                throw new DaoOperationException("Can't build user info from result set by id", e);
            }
        }
        return userOptional;
    }

    @Override
    public void createKey(String key, Integer userId, LocalDateTime timestamp) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(INSERT_KEY);
        try {
            statement.setString(1, key);
            statement.setInt(2, userId);
            statement.setTimestamp(3, Timestamp.valueOf(timestamp));
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }
        executeUpdateQuery(statement);
    }

    @Override
    public Optional<UsrKey> findKey(String key) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_KEY);
        try {
            statement.setString(1, key);
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }
        Optional<ResultSet> resultSet = unpackResultSet(createResultSet(statement));
        Optional<UsrKey> optionalKey = Optional.empty();
        try {
            if (resultSet.isPresent()) {
                optionalKey = Optional.of(UsrKey.builder()
                        .key(resultSet.get().getString(UsrKeyColumns.KEY.name()))
                        .userId(resultSet.get().getInt(UsrKeyColumns.USER_ID.name()))
                        .timestamp(resultSet.get().getTimestamp(UsrKeyColumns.TIMESTAMP.name()).toLocalDateTime())
                        .build());
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Can't get key", e);
        }
        return optionalKey;
    }

    @Override
    public void updateStatus(Integer id, Status status) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(UPDATE_STATUS_BY_ID);
        try {
            statement.setObject(1, status, Types.OTHER);
            statement.setInt(2, id);
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }
        executeUpdateQuery(statement);
    }

    @Override
    public void deleteKey(String key) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(DELETE_KEY);
        try {
            statement.setString(1, key);
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }
        executeUpdateQuery(statement);
    }

    private void setAllStatementParameters(User user, PreparedStatement statement) throws DaoOperationException {
        try {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getName());
            statement.setString(3, user.getSurname());
            statement.setDate(4, Date.valueOf(user.getRegistrationDate()));
            statement.setObject(5, user.getStatus(), Types.OTHER);
            statement.setString(6, user.getPassword());
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }
    }

    /**
     * This operation won't close resultSet in success case, but will
     * in case of exception thrown
     * <p>
     * Will build Optional-User only if resultSet has values of all User fields,
     * otherwise will return Optional.empty()
     *
     * @param resultSet input resultSet parameters, taken from sql query execution
     * @return User from resultSet
     */
    private Optional<User> buildUser(ResultSet resultSet) throws DaoOperationException {
        try {
            ResultSetValidator validator = new ResultSetValidator();
            if (validator.hasAllValues(resultSet, UserColumns.EMAIL.name(),
                    UserColumns.NAME.name(),
                    UserColumns.SURNAME.name(),
                    UserColumns.REGISTRATION_DATE.name(),
                    UserColumns.STATUS.name(),
                    UserColumns.PASSWORD.name(),
                    EntityColumns.ID.name())) {
                User user = User.builder()
                        .email(resultSet.getString(UserColumns.EMAIL.name()))
                        .name(resultSet.getString(UserColumns.NAME.name()))
                        .surname(resultSet.getString(UserColumns.SURNAME.name()))
                        .registrationDate(LocalDate.parse(resultSet.getString(UserColumns.REGISTRATION_DATE.name())))
                        .status(Status.valueOf(resultSet.getString(UserColumns.STATUS.name())))
                        .password(resultSet.getString(UserColumns.PASSWORD.name()))
                        .build();
                user.setId(resultSet.getInt(EntityColumns.ID.name()));
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            closeResultSet(resultSet);
            throw createBadResultSetException(e);
        }
    }
}
