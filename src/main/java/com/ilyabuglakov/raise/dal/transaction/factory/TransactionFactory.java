package com.ilyabuglakov.raise.dal.transaction.factory;

import com.ilyabuglakov.raise.dal.transaction.Transaction;
import com.ilyabuglakov.raise.dal.transaction.factory.exception.TransactionCreationException;

/**
 * The interface Transaction factory.
 */
public interface TransactionFactory {
    /**
     * Create transaction transaction.
     *
     * @return the transaction
     * @throws TransactionCreationException the transaction creation exception
     */
    Transaction createTransaction() throws TransactionCreationException;

    /**
     * Close transaction.
     */
    void close();

    /**
     * Rollback transaction.
     */
    void rollback();
}
