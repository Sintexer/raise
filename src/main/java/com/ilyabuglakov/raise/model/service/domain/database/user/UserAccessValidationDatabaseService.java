package com.ilyabuglakov.raise.model.service.domain.database.user;

import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.dao.interfaces.TestDao;
import com.ilyabuglakov.raise.dal.dao.interfaces.UserDao;
import com.ilyabuglakov.raise.dal.transaction.Transaction;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.domain.type.TestStatus;
import com.ilyabuglakov.raise.model.DaoType;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.domain.UserAccessValidationService;
import com.ilyabuglakov.raise.model.service.domain.database.DatabaseService;
import com.ilyabuglakov.raise.model.service.property.ApplicationProperties;

import java.util.Optional;

public class UserAccessValidationDatabaseService extends DatabaseService implements UserAccessValidationService {
    public UserAccessValidationDatabaseService(Transaction transaction) {
        super(transaction);
    }

    @Override
    public ResponseEntity isAllowedToCreateTest(String email) throws DaoOperationException {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setErrorOccurred(true);
        UserDao userDao = (UserDao) transaction.createDao(DaoType.USER);
        Optional<User> userOptional = userDao.findByEmail(email);
        if (!userOptional.isPresent()) {
            return responseEntity;
        }
        TestDao testDao = (TestDao) transaction.createDao(DaoType.TEST);
        int testAmount = testDao.findTestAmountByStatus(TestStatus.NEW, userOptional.get().getId());
        if (testAmount >= Integer.parseInt(ApplicationProperties.getProperty("user.max.new.tests"))) {
            return responseEntity;
        }
        responseEntity.setErrorOccurred(false);
        return responseEntity;
    }
}
