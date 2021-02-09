package com.ilyabuglakov.raise.command.impl.rest;


import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.domain.type.TestStatus;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.domain.ServiceType;
import com.ilyabuglakov.raise.model.service.domain.TestService;
import com.ilyabuglakov.raise.model.service.servlet.RequestService;
import com.ilyabuglakov.raise.storage.PropertiesStorage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * The type Confirm test command.
 * <p>
 * Changes test status to confirmed
 */
public class ConfirmTestCommand extends Command {
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

        ResponseEntity responseEntity = new ResponseEntity();

        Optional<Integer> optionalTestId = RequestService.getInstance().getIntParameter(request, "testId");
        if (!optionalTestId.isPresent()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        TestService testService = (TestService) serviceFactory.createService(ServiceType.TEST);
        testService.changeTestStatus(optionalTestId.get(), TestStatus.CONFIRMED);
        responseEntity.setLink(PropertiesStorage.getInstance().getLinks().getProperty("admin.test.catalog"));
        responseEntity.setRedirect(true);
        return responseEntity;
    }
}
