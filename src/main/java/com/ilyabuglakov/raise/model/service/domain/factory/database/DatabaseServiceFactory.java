package com.ilyabuglakov.raise.model.service.domain.factory.database;

import com.ilyabuglakov.raise.dal.transaction.Transaction;
import com.ilyabuglakov.raise.dal.transaction.factory.TransactionFactory;
import com.ilyabuglakov.raise.dal.transaction.factory.impl.DatabaseTransactionFactory;
import com.ilyabuglakov.raise.model.service.domain.Service;
import com.ilyabuglakov.raise.model.service.domain.ServiceType;
import com.ilyabuglakov.raise.model.service.domain.database.TestCategoryDatabaseService;
import com.ilyabuglakov.raise.model.service.domain.database.TestCommentDatabaseService;
import com.ilyabuglakov.raise.model.service.domain.database.UserTestResultDatabaseService;
import com.ilyabuglakov.raise.model.service.domain.database.test.TestDatabaseService;
import com.ilyabuglakov.raise.model.service.domain.database.user.UserAccessValidationDatabaseService;
import com.ilyabuglakov.raise.model.service.domain.database.user.UserDatabaseRegistrationService;
import com.ilyabuglakov.raise.model.service.domain.database.user.UserDatabaseService;
import com.ilyabuglakov.raise.model.service.domain.factory.ServiceFactory;

import java.util.EnumMap;

/**
 * The type Database service factory.
 */
public class DatabaseServiceFactory implements ServiceFactory {

    private final EnumMap<ServiceType, DatabaseServiceProducer> serviceCreators;
    private final TransactionFactory transactionFactory;

    /**
     * Instantiates a new Database service factory.
     *
     * @param databaseTransactionFactory the database transaction factory
     */
    public DatabaseServiceFactory(DatabaseTransactionFactory databaseTransactionFactory) {
        this.transactionFactory = databaseTransactionFactory;
        serviceCreators = new EnumMap<>(ServiceType.class);
        serviceCreators.put(ServiceType.USER, UserDatabaseService::new);
        serviceCreators.put(ServiceType.USER_REGISTRATION, UserDatabaseRegistrationService::new);
        serviceCreators.put(ServiceType.USER_ACCESS_VALIDATION, UserAccessValidationDatabaseService::new);
        serviceCreators.put(ServiceType.USER_TEST_RESULT, UserTestResultDatabaseService::new);
        serviceCreators.put(ServiceType.TEST, TestDatabaseService::new);
        serviceCreators.put(ServiceType.TEST_COMMENT, TestCommentDatabaseService::new);
        serviceCreators.put(ServiceType.TEST_CATEGORY, TestCategoryDatabaseService::new);
    }

    public Service createService(ServiceType serviceType) {
        return serviceCreators.get(serviceType).create(transactionFactory.createTransaction());
    }

    @Override
    public void close() {
        transactionFactory.close();
    }

    @Override
    public void rollback() {
        transactionFactory.rollback();
    }

    @FunctionalInterface
    private interface DatabaseServiceProducer {
        /**
         * Create service.
         *
         * @param transaction the transaction
         * @return the service
         */
        Service create(Transaction transaction);
    }
}
