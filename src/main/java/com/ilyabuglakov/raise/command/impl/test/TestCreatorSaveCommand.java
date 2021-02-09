package com.ilyabuglakov.raise.command.impl.test;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.domain.Test;
import com.ilyabuglakov.raise.domain.type.TestStatus;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.auth.AuthService;
import com.ilyabuglakov.raise.model.service.auth.AuthServiceFactory;
import com.ilyabuglakov.raise.model.service.domain.ServiceType;
import com.ilyabuglakov.raise.model.service.domain.TestService;
import com.ilyabuglakov.raise.model.service.domain.UserAccessValidationService;
import com.ilyabuglakov.raise.storage.PropertiesStorage;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * The type Test creator save command.
 * <p>
 * Saves test to storage
 */
@Log4j2
public class TestCreatorSaveCommand extends Command {
    /**
     * @param request  http request
     * @param response http response
     * @return the response entity or null if bad request
     * @throws IOException         by request/response
     * @throws PersistentException datasource error
     */
    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, PersistentException {

        AuthService authService = AuthServiceFactory.getAuthService();
        if (!authService.isAuthenticated()) {
            response.sendError(401);
            return null;
        }

        TestService testService = (TestService) serviceFactory.createService(ServiceType.TEST);
        Optional<Test> optionalTest = testService.createFromJson(request.getParameter("testJson"));
        if (!optionalTest.isPresent()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//400
            return null;
        }
        Test test = optionalTest.get();

        if (authService.isPermitted("confirm:test")) {
            test.setStatus(TestStatus.CONFIRMED);
        }

        UserAccessValidationService userAccessValidationService =
                (UserAccessValidationService) serviceFactory.createService(ServiceType.USER_ACCESS_VALIDATION);
        ResponseEntity accessResponse = userAccessValidationService.isAllowedToCreateTest(authService.getEmail());
        ResponseEntity responseEntity = testService.validateTest(test);
        if (responseEntity.isErrorOccurred() || accessResponse.isErrorOccurred()) {
            responseEntity.setAttribute("testWasntCreated", true);
            responseEntity.setAttribute("testWasntPosted", true);
            if (accessResponse.isErrorOccurred()) {
                responseEntity.setAttribute("testLimitReached", true);
            }
        } else {
            testService.save(test, authService.getEmail());
            responseEntity.setAttribute("testName", test.getTestName());
            responseEntity.setLink(PropertiesStorage.getInstance()
                    .getPages()
                    .getProperty("test.creator.save.success"));
        }

        return responseEntity;
    }


}
