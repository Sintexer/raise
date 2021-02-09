package com.ilyabuglakov.raise.command.impl.user;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.model.FormConstants;
import com.ilyabuglakov.raise.model.dto.UserParametersDto;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.auth.AuthService;
import com.ilyabuglakov.raise.model.service.auth.AuthServiceFactory;
import com.ilyabuglakov.raise.model.service.domain.ServiceType;
import com.ilyabuglakov.raise.model.service.domain.UserService;
import com.ilyabuglakov.raise.storage.PropertiesStorage;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type User profile change get command.
 * <p>
 * Returns the user profile change page and results of user info change
 */
@Log4j2
public class UserProfileChangeGetCommand extends Command {
    /**
     * @param request  http request
     * @param response http response
     * @return the response entity or null if page not found
     * @throws IOException         by request/response
     * @throws PersistentException datasource error
     */
    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, PersistentException {
        log.info("Entered profile change command");

        AuthService authService = AuthServiceFactory.getAuthService();
        if (!authService.isAuthenticated()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        UserService userService = (UserService) serviceFactory.createService(ServiceType.USER);
        UserParametersDto userParametersDto = userService.getUserParameters(authService.getEmail());

        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setAttribute("userParameters", userParametersDto);
        responseEntity.getAttributes().put("nameLength", FormConstants.NAME_LENGTH.getValue());
        responseEntity.getAttributes().put("surnameLength", FormConstants.SURNAME_LENGTH.getValue());
        responseEntity.getAttributes().put("passwordMin", FormConstants.PASSWORD_MIN.getValue());
        responseEntity.getAttributes().put("passwordMax", FormConstants.PASSWORD_MAX.getValue());
        responseEntity.setLink(PropertiesStorage.getInstance().getPages().getProperty("user.profile.change"));

        return responseEntity;
    }
}
