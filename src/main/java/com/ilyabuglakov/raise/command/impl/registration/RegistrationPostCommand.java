package com.ilyabuglakov.raise.command.impl.registration;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.model.FormConstants;
import com.ilyabuglakov.raise.model.Patterns;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.domain.ServiceType;
import com.ilyabuglakov.raise.model.service.domain.UserRegistrationService;
import com.ilyabuglakov.raise.model.service.request.extractor.UserExtractor;
import com.ilyabuglakov.raise.storage.PropertiesStorage;
import lombok.extern.log4j.Log4j2;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * The type Registration post command.
 * <p>
 * Validates registration parameters and saves user if they are valid
 * Else will return registration page with alert messages
 */
@Log4j2
public class RegistrationPostCommand extends Command {
    /**
     * @param request  http request
     * @param response http response
     * @return the response entity or null if email exception
     * @throws IOException           by request/response
     * @throws DaoOperationException the dao operation exception
     */
    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, DaoOperationException {
        log.debug(() -> "registration from posted");

        User user = new UserExtractor().extractFrom(request);

        UserRegistrationService userRegistrationService =
                (UserRegistrationService) serviceFactory.createService(ServiceType.USER_REGISTRATION);

        ResponseEntity responseEntity = null;
        try {
            responseEntity = userRegistrationService.registerUser(user);
        } catch (MessagingException e) {
            response.sendError(500);
            return null;
        }

        if (!responseEntity.isErrorOccurred()) {
            responseEntity.setRedirect(true);
            responseEntity.setLink(PropertiesStorage.getInstance().getLinks().getProperty("auth.confirm.email"));
        } else {
            responseEntity.setAttribute("namePattern", Patterns.NAME.getPattern());
            responseEntity.setAttribute("passwordPattern", Patterns.PASSWORD.getPattern());
            responseEntity.setAttribute("emailLength", FormConstants.EMAIL_LENGTH.getValue());
            responseEntity.setAttribute("nameLength", FormConstants.NAME_LENGTH.getValue());
            responseEntity.setAttribute("surnameLength", FormConstants.SURNAME_LENGTH.getValue());
            responseEntity.setAttribute("passwordMin", FormConstants.PASSWORD_MIN.getValue());
            responseEntity.setAttribute("passwordMax", FormConstants.PASSWORD_MAX.getValue());
            responseEntity.setAttribute("emailPrevVal", user.getEmail());
            responseEntity.setAttribute("namePrevVal", user.getName());
            responseEntity.setAttribute("surnamePrevVal", user.getSurname());
            responseEntity.setAttribute("registrationFailed", true);
            responseEntity.setLink(PropertiesStorage.getInstance().getPages().getProperty("registration"));
        }
        return responseEntity;
    }
}
