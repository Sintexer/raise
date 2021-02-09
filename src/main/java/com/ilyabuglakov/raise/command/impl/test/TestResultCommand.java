package com.ilyabuglakov.raise.command.impl.test;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.model.dto.TestDto;
import com.ilyabuglakov.raise.model.dto.TestResultDto;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.auth.AuthService;
import com.ilyabuglakov.raise.model.service.auth.AuthServiceFactory;
import com.ilyabuglakov.raise.model.service.domain.ServiceType;
import com.ilyabuglakov.raise.model.service.domain.TestService;
import com.ilyabuglakov.raise.model.service.domain.UserTestResultService;
import com.ilyabuglakov.raise.storage.PropertiesStorage;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * The type Test result command.
 * <p>
 * Returns the page with test result and incorrectly answered questions.
 */
@Log4j2
public class TestResultCommand extends Command {
    /**
     * @param request  http request
     * @param response http response
     * @return the response entity
     * @throws IOException         by request/response
     * @throws PersistentException datasource error
     */
    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, PersistentException {

        TestService testService = (TestService) serviceFactory.createService(ServiceType.TEST);
        Optional<TestDto> testDtoOptional = testService.createDtoFromJson(request.getParameter("testJson"));
        if (!testDtoOptional.isPresent()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        AuthService authService = AuthServiceFactory.getAuthService();
        ResponseEntity responseEntity = testService.createResult(testDtoOptional.get());
        if (responseEntity.isErrorOccurred()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        if (authService.isAuthenticated()) {
            UserTestResultService userTestResultService =
                    (UserTestResultService) serviceFactory.createService(ServiceType.USER_TEST_RESULT);
            userTestResultService.saveResult(
                    (TestResultDto) responseEntity.getAttributes().get("testResult"),
                    authService.getEmail());
        }
        responseEntity.setLink(PropertiesStorage.getInstance().getPages().getProperty("test.testing.result"));
        return responseEntity;

    }
}
