package com.ilyabuglakov.raise.dal.transaction.impl;

import com.ilyabuglakov.raise.dal.dao.DatabaseDao;
import com.ilyabuglakov.raise.dal.dao.database.AnswerDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.database.QuestionDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.database.RoleDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.database.TestCategoryDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.database.TestCommentDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.database.TestDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.database.UserDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.database.UserTestResultDatabaseDao;
import com.ilyabuglakov.raise.dal.transaction.Transaction;
import com.ilyabuglakov.raise.model.DaoType;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.EnumMap;

/**
 * An implementation of Transaction Interface, specified to work with database connection
 */
@Log4j2
public class DatabaseTransaction implements Transaction {

    private final Connection connection;

    private final EnumMap<DaoType, DatabaseDao> daoMap;

    /**
     * Instantiates a new Database transaction.
     *
     * @param connection the connection
     */
    public DatabaseTransaction(Connection connection) {
        this.connection = connection;
        daoMap = new EnumMap<>(DaoType.class);
        daoMap.put(DaoType.USER, new UserDatabaseDao(connection));
        daoMap.put(DaoType.TEST_COMMENT, new TestCommentDatabaseDao(connection));
        daoMap.put(DaoType.TEST, new TestDatabaseDao(connection));
        daoMap.put(DaoType.ANSWER, new AnswerDatabaseDao(connection));
        daoMap.put(DaoType.QUESTION, new QuestionDatabaseDao(connection));
        daoMap.put(DaoType.USER_TEST_RESULT, new UserTestResultDatabaseDao(connection));
        daoMap.put(DaoType.ROLE, new RoleDatabaseDao(connection));
        daoMap.put(DaoType.TEST_CATEGORY, new TestCategoryDatabaseDao(connection));
    }

    @Override
    public DatabaseDao createDao(DaoType daoType) {
        return daoMap.get(daoType);
    }

    @Override
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            log.error("Can't commit transaction", e);
        }
    }

    @Override
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            log.error("Can't rollback transaction", e);
        }
    }

    @Override
    public void close() {
        try {
            connection.setAutoCommit(true);
            connection.close();
        } catch (SQLException e) {
            log.error("Can't close transaction", e);
        }
    }
}
