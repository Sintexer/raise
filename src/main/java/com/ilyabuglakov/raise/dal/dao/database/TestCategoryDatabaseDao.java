package com.ilyabuglakov.raise.dal.dao.database;

import com.ilyabuglakov.raise.dal.dao.DatabaseDao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.dao.interfaces.TestCategoryDao;
import com.ilyabuglakov.raise.domain.TestCategory;
import com.ilyabuglakov.raise.domain.structure.Tables;
import com.ilyabuglakov.raise.domain.structure.columns.EntityColumns;
import com.ilyabuglakov.raise.domain.structure.columns.TestCategoryColumns;
import com.ilyabuglakov.raise.model.service.validator.ResultSetValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TestCategoryDatabaseDao is the Dao implementation specifically for TestCategory class.
 * Based on DatabaseDao abstract class.
 */
public class TestCategoryDatabaseDao extends DatabaseDao implements TestCategoryDao {

    private static final String INSERT_CATEGORY = String.format(
            "INSERT INTO %s(%s, %s) VALUES(?, ?)",
            Tables.TEST_CATEGORY.name(),
            TestCategoryColumns.CATEGORY.name(), TestCategoryColumns.PARENT_ID.name());

    private static final String SELECT_WHERE_ID = String.format(
            "SELECT %s, %s, %s FROM %s WHERE %s = ?",
            EntityColumns.ID.name(), TestCategoryColumns.CATEGORY.name(), TestCategoryColumns.PARENT_ID.name(),
            Tables.TEST_CATEGORY.name(),
            EntityColumns.ID.name());

    private static final String UPDATE_BY_ID = String.format(
            "UPDATE %s SET %s=?, %s=? WHERE %s = ?",
            Tables.TEST_CATEGORY.name(),
            TestCategoryColumns.CATEGORY.name(), TestCategoryColumns.PARENT_ID.name(),
            EntityColumns.ID.name());

    private static final String DELETE_BY_ID = String.format(
            "DELETE FROM %s WHERE %s = ?",
            Tables.TEST_CATEGORY.name(),
            EntityColumns.ID.name());

    private static final String SELECT_ALL = String.format(
            "SELECT %s, %s, %s FROM %s",
            EntityColumns.ID.name(), TestCategoryColumns.CATEGORY.name(), TestCategoryColumns.PARENT_ID.name(),
            Tables.TEST_CATEGORY.name());

    public TestCategoryDatabaseDao(Connection connection) {
        super(connection);
    }

    @Override
    public Integer create(TestCategory testCategory) throws DaoOperationException {
        PreparedStatement preparedStatement = prepareStatementReturnKeys(INSERT_CATEGORY);
        setAllStatementParameters(testCategory, preparedStatement);
        return executeReturnId(preparedStatement);
    }

    @Override
    public Optional<TestCategory> read(Integer id) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_WHERE_ID);
        setIdStatementParameters(id, statement);
        Optional<ResultSet> resultSet = unpackResultSet(createResultSet(statement));
        Optional<TestCategory> testCategory = Optional.empty();
        if (resultSet.isPresent()) {
            testCategory = buildTestCategory(resultSet.get());
            closeResultSet(resultSet.get());
        }
        return testCategory;
    }

    @Override
    public void update(TestCategory testCategory) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(UPDATE_BY_ID);
        setAllStatementParameters(testCategory, statement);
        try {
            statement.setInt(3, testCategory.getId());
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't change result set parameters", e);
        }

        executeUpdateQuery(statement);
    }

    @Override
    public void delete(TestCategory testCategory) throws DaoOperationException {
        PreparedStatement statement = prepareStatement(DELETE_BY_ID);
        setIdStatementParameters(testCategory.getId(), statement);

        executeUpdateQuery(statement);
    }

    @Override
    public List<TestCategory> findAll() throws DaoOperationException {
        PreparedStatement statement = prepareStatement(SELECT_ALL);

        ResultSet resultSet = createResultSet(statement);
        List<Optional<TestCategory>> testCategories = new ArrayList<>();
        try {
            while (resultSet.next()) {
                testCategories.add(buildTestCategory(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Can't build testCategory", e);
        }

        return testCategories.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private void setAllStatementParameters(TestCategory testCategory, PreparedStatement statement) throws DaoOperationException {
        try {
            statement.setString(1, testCategory.getCategory());
            if (testCategory.getParent() != null)
                statement.setInt(2, testCategory.getParent().getId());
            else
                statement.setNull(2, Types.INTEGER);
        } catch (SQLException e) {
            closeStatement(statement);
            throw new DaoOperationException("Can't set statement parameters", e);
        }
    }

    /**
     * This operation won't close resultSet in success case, but will
     * in case of exception thrown
     * <p>
     * Will build Optional-TestCategory only if resultSet has values of all user fields,
     * otherwise will return Optional.empty()
     *
     * @param resultSet input result set parameters, taken from sql query execution
     * @return TestCategory from resultSet
     */
    private Optional<TestCategory> buildTestCategory(ResultSet resultSet) throws DaoOperationException {
        try {
            ResultSetValidator validator = new ResultSetValidator();
            if (validator.hasAllValues(resultSet,
                    TestCategoryColumns.CATEGORY.name(),
                    EntityColumns.ID.name())) {
                TestCategory testCategory = TestCategory.builder()
                        .category(resultSet.getString(TestCategoryColumns.CATEGORY.name()))
                        .build();
                Integer parentId = resultSet.getInt(TestCategoryColumns.PARENT_ID.name());
                if (resultSet.wasNull()) {
                    testCategory.setParent(null);
                } else {
                    testCategory.setParent(TestCategory.builder().id(parentId).build());
                }
                testCategory.setId(resultSet.getInt(EntityColumns.ID.name()));
                return Optional.of(testCategory);
            }
            return Optional.empty();
        } catch (SQLException e) {
            closeResultSet(resultSet);
            throw createBadResultSetException(e);
        }
    }
}
