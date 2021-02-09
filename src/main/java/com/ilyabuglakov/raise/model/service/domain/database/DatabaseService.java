package com.ilyabuglakov.raise.model.service.domain.database;

import com.ilyabuglakov.raise.dal.transaction.Transaction;
import com.ilyabuglakov.raise.model.service.domain.Service;

/**
 * The type Database service. Parent class for services that work with database connection.
 */
public abstract class DatabaseService implements Service {
    /**
     * The Transaction.
     */
    protected final Transaction transaction;

    /**
     * Instantiates a new Database service.
     *
     * @param transaction the transaction
     */
    public DatabaseService(Transaction transaction) {
        this.transaction = transaction;
    }
}
