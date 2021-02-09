package com.ilyabuglakov.raise.command.impl.test;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.domain.Test;
import com.ilyabuglakov.raise.domain.type.TestStatus;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.domain.ServiceType;
import com.ilyabuglakov.raise.model.service.domain.TestService;
import com.ilyabuglakov.raise.model.service.servlet.RequestService;
import com.ilyabuglakov.raise.storage.PropertiesStorage;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * The type Testing get command.
 * <p>
 * Returns the page with test
 */
@Log4j2
public class TestingGetCommand extends Command {
    /**
     * @param request  http request
     * @param response http response
     * @return the response entity or null if bad request or page not found
     * @throws IOException         by request/response
     * @throws PersistentException datasource exception
     */
    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, PersistentException {

        Optional<Integer> testId = RequestService.getInstance().getIntParameter(request, "testId");
        if (!testId.isPresent()) {
            log.debug(() -> "testId is not present");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        ResponseEntity responseEntity = new ResponseEntity();
        TestService testService = (TestService) serviceFactory.createService(ServiceType.TEST);
        Optional<Test> test = testService.getTest(testId.get());
        if (!test.isPresent()) {
            response.sendError(404);
            return null;
        } else if (test.get().getStatus() == TestStatus.CONFIRMED
                || SecurityUtils.getSubject().isPermitted("test:confirm")) {
            log.debug(test::get);
            responseEntity.setAttribute("test", test.get());
            responseEntity.setLink(PropertiesStorage.getInstance().getPages().getProperty("test.testing"));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return responseEntity;

    }
}
