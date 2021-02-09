package com.ilyabuglakov.raise.command.impl.login;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.auth.AuthService;
import com.ilyabuglakov.raise.model.service.auth.AuthServiceFactory;
import com.ilyabuglakov.raise.storage.PropertiesStorage;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The type Login post command.
 * <p>
 * Checks provided credentials and logs subject if they are correct, or returns login page with alert
 * Will redirect to previous url, or to home page
 */
@Log4j2
public class LoginPostCommand extends Command {
    /**
     * @param request  http request
     * @param response http response
     * @return the response entity with redirect link or with login page
     */
    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response) {
        log.info("posted to Login");

        Subject currentUser = SecurityUtils.getSubject();
        log.debug(currentUser);

        ResponseEntity responseEntity = new ResponseEntity();
        AuthService authService = AuthServiceFactory.getAuthService();
        if (!authService.isAuthenticated()) {
            boolean success = authService.login(request.getParameter("username"), request.getParameter("password"));
            if (success) {
                String previousUrl = authService.getPreviousUrl(request,
                        PropertiesStorage.getInstance().getLinks().getProperty("root"));
                responseEntity.setRedirect(true);
                responseEntity.setLink(previousUrl);
            } else {
                responseEntity.getAttributes().put("loginFailed", true);
                responseEntity.setLink(PropertiesStorage.getInstance().getPages().getProperty("login"));
            }
        } else {
            responseEntity.setRedirect(true);
            responseEntity.setLink(PropertiesStorage.getInstance().getPages().getProperty("root"));
        }
        return responseEntity;

    }
}
