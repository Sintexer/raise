package com.ilyabuglakov.raise.command.impl.registration;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.model.FormConstants;
import com.ilyabuglakov.raise.model.Patterns;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.auth.AuthServiceFactory;
import com.ilyabuglakov.raise.storage.PropertiesStorage;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The type Registration get command.<br>
 *
 * Returns the registration page with specified attributes
 */
@Log4j2
public class RegistrationGetCommand extends Command {
    /**
     * @param request  http request
     * @param response http response
     * @return the response entity
     */
    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response) {
        log.info(() -> "Entered registration command");
        ResponseEntity responseEntity = new ResponseEntity();
        if (AuthServiceFactory.getAuthService().isAuthenticated()) {
            responseEntity.setRedirect(true);
            responseEntity.setLink(PropertiesStorage.getInstance().getLinks().getProperty("root"));
        } else {
            responseEntity.setAttribute("namePattern", Patterns.NAME.getPattern());
            responseEntity.setAttribute("passwordPattern", Patterns.PASSWORD.getPattern());
            responseEntity.setAttribute("emailLength", FormConstants.EMAIL_LENGTH.getValue());
            responseEntity.setAttribute("nameLength", FormConstants.NAME_LENGTH.getValue());
            responseEntity.setAttribute("surnameLength", FormConstants.SURNAME_LENGTH.getValue());
            responseEntity.setAttribute("passwordMin", FormConstants.PASSWORD_MIN.getValue());
            responseEntity.setAttribute("passwordMax", FormConstants.PASSWORD_MAX.getValue());
            responseEntity.setLink(PropertiesStorage.getInstance().getPages().getProperty("registration"));
        }

        return responseEntity;
    }

}
