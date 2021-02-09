package com.ilyabuglakov.raise.dal.dao.database;

import com.ilyabuglakov.raise.dal.dao.DatabaseDao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.dao.interfaces.RoleDao;
import com.ilyabuglakov.raise.domain.Role;
import com.ilyabuglakov.raise.domain.structure.Tables;
import com.ilyabuglakov.raise.domain.structure.columns.EntityColumns;
import com.ilyabuglakov.raise.domain.structure.columns.RoleColumns;
import com.ilyabuglakov.raise.domain.structure.columns.RolePermissionsColumns;
import com.ilyabuglakov.raise.domain.structure.columns.UserRolesColumns;
import com.ilyabuglakov.raise.domain.type.UserRole;
import com.ilyabuglakov.raise.model.service.validator.ResultSetValidator;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.NotImplementedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * RoleDao is the Dao implementation specifically for Role class
 * Based on DatabaseDao abstract class.
 */
@Log4j2
public class RoleDatabaseDao extends DatabaseDao implements RoleDao {

    private static final String SELECT_WHERE_ID = String.format(
            "SELECT %s FROM %s WHERE %s = ?",
            RoleColumns.NAME.name(),
            Tables.ROLE.name(),
            EntityColumns.ID.name());


    private static final String SELECT_ROLE_BY_USER_ID = String.format(
            "SELECT %s FROM %s WHERE %s = (SELECT %s FROM %s WHERE %s = ?)",
            RoleColumns.NAME.name(),
            Tables.ROLE.name(),
            EntityColumns.ID.name(),
            UserRolesColumns.ROLE_ID.name(),
            Tables.USER_ROLES.name(),
            UserRolesColumns.USER_ID.name());

    public static final String INSERT_USER_ROLE_BY_USER_ID_AND_ROLE_NAME = String.format(
            "INSERT INTO %s(%s, %s) VALUES(?, (SELECT %s FROM %s WHERE %s = ?))",
            Tables.USER_ROLES.name(),
            UserRolesColumns.USER_ID.name(), UserRolesColumns.ROLE_ID.name(),
            EntityColumns.ID.name(),
            Tables.ROLE.name(),
            RoleColumns.NAME.name());

    public static final String SELECT_PERMISSIONS_BY_ROLE_ID = String.format(
            "SELECT %s FROM %s WHERE %s = ?",
            RolePermissionsColumns.PERMISSION.name(),
            Tables.ROLE_PERMISSIONS.name(),
            RolePermissionsColumns.ROLE_ID.name());

    public RoleDatabaseDao(Connection connection) {
        super(connection);
    }

    @Override
    public Integer create(Role entity) throws DaoOperationException {
        log.error("Operation \"create role\" not implemented");
        throw new NotImplementedException("Operation not implemented");
    }

    @Override
    public Optional<Role> read(Integer id) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_WHERE_ID);
        setIdStatementParameters(id, statement);

        Optional<ResultSet> optionalResultSet = unpackResultSet(createResultSet(statement));
        Optional<Role> optionalRole = Optional.empty();
        if (optionalResultSet.isPresent()) {
            ResultSet resultSet = optionalResultSet.get();

            Set<String> permissions = findPermissions(id);
            optionalRole = buildRole(resultSet, permissions);
        }
        return optionalRole;
    }

    @Override
    public void update(Role entity) throws DaoOperationException {
        log.error("Operation \"update role\" not implemented");
        throw new NotImplementedException("Operation not implemented");
    }

    @Override
    public void delete(Role entity) throws DaoOperationException {
        log.error("Operation \"delete role\" not implemented");
        throw new NotImplementedException("Operation not implemented");
    }

    @Override
    public Set<UserRole> findUserRoles(Integer userId) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_ROLE_BY_USER_ID);
        setIdStatementParameters(userId, statement);

        ResultSet resultSet = createResultSet(statement);
        Set<UserRole> userRoles = new HashSet<>();
        try {
            while (resultSet.next()) {
                userRoles.add(UserRole.valueOf(resultSet.getString(RoleColumns.NAME.name())));
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Can't read User roles", e);
        } finally {
            closeResultSet(resultSet);
        }

        return userRoles;
    }

    @Override
    public void createUserRoles(Integer userId, Set<UserRole> userRoles) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(INSERT_USER_ROLE_BY_USER_ID_AND_ROLE_NAME);
        setIdStatementParameters(userId, statement);

        try {
            for (UserRole role : userRoles) {
                statement.setString(2, role.name());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DaoOperationException("Can't change result set parameters", e);
        } finally {
            closeStatement(statement);
        }
    }

    @Override
    public Set<String> findPermissions(Integer id) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_PERMISSIONS_BY_ROLE_ID);
        setIdStatementParameters(id, statement);

        ResultSet permissionResultSet = createResultSet(statement);
        Set<String> permissions = new HashSet<>();

        try {
            while (permissionResultSet.next()) {
                permissions.add(permissionResultSet.getString(RolePermissionsColumns.PERMISSION.name()));
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Can't read role's permissions", e);
        } finally {
            closeResultSet(permissionResultSet);
        }
        return permissions;
    }

    /**
     * This operation won't close resultSet in success case, but will
     * in case of exception thrown
     * <p>
     * Will build Optional-Role only if resultSet has values of all user fields,
     * otherwise will return Optional.empty()
     *
     * @param resultSet input resultSet parameters, taken from sql query execution
     * @return Role from resultSet
     */
    private Optional<Role> buildRole(ResultSet resultSet, Set<String> permissions) throws DaoOperationException {
        try {
            ResultSetValidator validator = new ResultSetValidator();
            if (validator.hasAllValues(resultSet,
                    RoleColumns.NAME.name(),
                    EntityColumns.ID.name())) {
                Role role = Role.builder()
                        .name(resultSet.getString(RoleColumns.NAME.name()))
                        .permissions(permissions)
                        .build();
                role.setId(resultSet.getInt(EntityColumns.ID.name()));
                return Optional.of(role);
            }
            return Optional.empty();
        } catch (SQLException e) {
            closeResultSet(resultSet);
            throw createBadResultSetException(e);
        }
    }
}
