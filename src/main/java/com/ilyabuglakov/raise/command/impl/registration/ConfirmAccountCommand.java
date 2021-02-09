package com.ilyabuglakov.raise.command.impl.registration;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.domain.ServiceType;
import com.ilyabuglakov.raise.model.service.domain.UserRegistrationService;
import com.ilyabuglakov.raise.storage.PropertiesStorage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The type Confirm account command.
 * <p>
 * Confirms account by link from email. Will change user status to ACTIVE.
 * User will be found by associated key.
 */
public class ConfirmAccountCommand extends Command {
    /**
     * @param request  http request
     * @param response http response
     * @return the response entity with attributes
     * @throws PersistentException by datasource
     */
    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response)
            throws PersistentException {
        String key = request.getParameter("key");
        UserRegistrationService registrationService =
                (UserRegistrationService) serviceFactory.createService(ServiceType.USER_REGISTRATION);

        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setLink(PropertiesStorage.getInstance().getPages().getProperty("auth.confirm"));
        if (!registrationService.tryConfirm(key)) {
            responseEntity.setAttribute("failed", true);
        }
        return responseEntity;
    }
}
