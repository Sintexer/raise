package com.ilyabuglakov.raise.command.impl.login;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.model.FormConstants;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.auth.AuthServiceFactory;
import com.ilyabuglakov.raise.storage.PropertiesStorage;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Login get command.
 * <p>
 * Collects all data needed for login page and returns it in ResponseEntity attributes
 */
@Log4j2
public class LoginGetCommand extends Command {
    /**
     * @param request  http request
     * @param response http response
     * @return ResponseEntity of login page with attributes
     */
    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response) {
        log.info(() -> "Entered login command");

        ResponseEntity responseEntity = new ResponseEntity();
        if (AuthServiceFactory.getAuthService().isAuthenticated()) {
            responseEntity.setRedirect(true);
            responseEntity.setLink(PropertiesStorage.getInstance().getLinks().getProperty("root"));
        } else {
            responseEntity.getAttributes().put("emailLength", FormConstants.EMAIL_LENGTH.getValue());
            responseEntity.getAttributes().put("passwordMax", FormConstants.PASSWORD_MAX.getValue());
            responseEntity.setLink(PropertiesStorage.getInstance().getPages().getProperty("login"));
        }

        return responseEntity;
    }

}
