package com.ilyabuglakov.raise.command.impl.test;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.domain.type.Characteristic;
import com.ilyabuglakov.raise.model.Patterns;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.auth.AuthServiceFactory;
import com.ilyabuglakov.raise.model.service.domain.ServiceType;
import com.ilyabuglakov.raise.model.service.domain.TestCategoryService;
import com.ilyabuglakov.raise.model.service.domain.UserAccessValidationService;
import com.ilyabuglakov.raise.storage.PropertiesStorage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type Test creator get command.
 * <p>
 * Returns the page with test creator
 */
public class TestCreatorGetCommand extends Command {

    /**
     * @param request  http request
     * @param response http response
     * @return the response entity with attributes and error messages if they happened
     * @throws IOException         by request/response
     * @throws PersistentException datasource error
     */
    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, PersistentException {
        UserAccessValidationService accessValidationService =
                (UserAccessValidationService) serviceFactory.createService(ServiceType.USER_ACCESS_VALIDATION);
        ResponseEntity responseEntity =
                accessValidationService.isAllowedToCreateTest(AuthServiceFactory.getAuthService().getEmail());
        if (responseEntity.isErrorOccurred()) {
            responseEntity.setAttribute("testLimitReached", true);
            responseEntity.setAttribute("testWasntCreated", true);
            responseEntity.setAttribute("testWasntPosted", true);
            responseEntity.setLink(PropertiesStorage.getInstance().getPages().getProperty("test.creator.save.failure"));
            return responseEntity;
        }
        responseEntity.setAttribute("namePattern", Patterns.NAME.getPattern());
        responseEntity.setAttribute("passwordPattern", Patterns.PASSWORD.getPattern());
        responseEntity.setAttribute("characteristics", Characteristic.values());
        TestCategoryService testCategoryService = (TestCategoryService) serviceFactory.createService(ServiceType.TEST_CATEGORY);
        responseEntity.setAttribute("categories", testCategoryService.getCategoryMap());
        responseEntity.setLink(PropertiesStorage.getInstance().getPages().getProperty("test.creator"));

        return responseEntity;
    }

}
