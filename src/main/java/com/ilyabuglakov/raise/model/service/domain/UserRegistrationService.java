package com.ilyabuglakov.raise.model.service.domain;

import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.model.response.ResponseEntity;

import javax.mail.MessagingException;

/**
 * The interface User registration service.
 */
public interface UserRegistrationService {

    /**
     * Register user.
     *
     * @param user the user
     * @return the response entity
     * @throws DaoOperationException the dao operation exception
     * @throws MessagingException    the messaging exception
     */
    ResponseEntity registerUser(User user) throws DaoOperationException, MessagingException;

    /**
     * Try confirm user account.
     *
     * @param key the key
     * @return the boolean
     * @throws DaoOperationException the dao operation exception
     */
    boolean tryConfirm(String key) throws DaoOperationException;

}
