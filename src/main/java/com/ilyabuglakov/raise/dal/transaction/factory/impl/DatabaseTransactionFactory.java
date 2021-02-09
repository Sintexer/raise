package com.ilyabuglakov.raise.dal.transaction.factory.impl;

import com.ilyabuglakov.raise.dal.connection.pool.ConnectionPoolFactory;
import com.ilyabuglakov.raise.dal.transaction.Transaction;
import com.ilyabuglakov.raise.dal.transaction.factory.TransactionFactory;
import com.ilyabuglakov.raise.dal.transaction.factory.exception.TransactionCreationException;
import com.ilyabuglakov.raise.dal.transaction.impl.DatabaseTransaction;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * DatabaseTransactionFactory uses ConnectionPool to create DatabaseTransaction object
 */
@Log4j2
public class DatabaseTransactionFactory implements TransactionFactory {

    private Connection connection;

    @Override
    public Transaction createTransaction() {
        if (connection == null) {
            connection = ConnectionPoolFactory.getConnectionPool().getConnection();
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                throw new TransactionCreationException("Can't turn connection autocommit off", e);
            }
        }
        return new DatabaseTransaction(connection);
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("Error while closing transaction", e);
            }
        }
    }

    @Override
    public void rollback() {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                log.error("Error while closing transaction", e);
            }
        }
    }
}
