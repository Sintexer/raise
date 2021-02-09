package com.ilyabuglakov.raise.dal.transaction;

import com.ilyabuglakov.raise.dal.dao.DatabaseDao;
import com.ilyabuglakov.raise.model.DaoType;

/**
 * Transaction is interface for classes, that let you to perform
 * datasource operations atomically.
 * Transaction can be committed via commit() method, or rolled back by rollback() method.
 */
public interface Transaction extends AutoCloseable {

    /**
     * Create dao database dao.
     *
     * @param daoType the dao type
     * @return the database dao
     */
    DatabaseDao createDao(DaoType daoType);

    /**
     * Commit transaction.
     */
    void commit();

    /**
     * Rollback transaction.
     */
    void rollback();

    void close();
}
