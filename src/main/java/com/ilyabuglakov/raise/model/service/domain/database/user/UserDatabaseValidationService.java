package com.ilyabuglakov.raise.model.service.domain.database.user;

import com.ilyabuglakov.raise.dal.dao.database.UserDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.transaction.Transaction;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.model.DaoType;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.domain.database.DatabaseService;
import com.ilyabuglakov.raise.model.service.validator.UserValidator;

import java.util.Optional;

public class UserDatabaseValidationService extends DatabaseService {
    public UserDatabaseValidationService(Transaction transaction) {
        super(transaction);
    }

    public ResponseEntity validateUser(User user) {
        ResponseEntity responseEntity = new ResponseEntity();
        UserValidator validator = new UserValidator();
        boolean isValidEmail = validator.isValidEmail(user.getEmail());
        boolean isValidName = validator.isValidName(user.getName());
        boolean isValidSurname = validator.isValidSurname(user.getSurname());
        boolean isValidPassword = validator.isValidPassword(user.getPassword());
        boolean isValid = isValidEmail && isValidName && isValidSurname && isValidPassword;

        if (!isValid) {
            responseEntity.setErrorOccurred(true);

            responseEntity.setAttribute("invalidEmail", !isValidEmail);
            responseEntity.setAttribute("invalidName", !isValidName);
            responseEntity.setAttribute("invalidSurname", !isValidSurname);
            responseEntity.setAttribute("invalidPassword", !isValidPassword);
        }
        return responseEntity;
    }

    public boolean isUnique(User user) throws DaoOperationException {
        UserDatabaseDao dao = (UserDatabaseDao) transaction.createDao(DaoType.USER);
        String email = user.getEmail();
        Optional<User> userOptional = dao.findByEmail(email);
        return !userOptional.isPresent();
    }
}
