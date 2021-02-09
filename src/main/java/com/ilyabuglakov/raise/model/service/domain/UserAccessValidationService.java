package com.ilyabuglakov.raise.model.service.domain;

import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.model.response.ResponseEntity;

/**
 * The interface User access validation service.
 */
public interface UserAccessValidationService {
    /**
     * Check if user is allowed to create test.
     *
     * @param email the email
     * @return the response entity
     * @throws DaoOperationException the dao operation exception
     */
    ResponseEntity isAllowedToCreateTest(String email) throws DaoOperationException;
}
