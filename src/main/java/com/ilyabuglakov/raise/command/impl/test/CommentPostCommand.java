package com.ilyabuglakov.raise.command.impl.test;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.auth.AuthService;
import com.ilyabuglakov.raise.model.service.auth.AuthServiceFactory;
import com.ilyabuglakov.raise.model.service.domain.ServiceType;
import com.ilyabuglakov.raise.model.service.domain.TestCommentService;
import com.ilyabuglakov.raise.model.service.servlet.RequestService;
import com.ilyabuglakov.raise.storage.PropertiesStorage;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * The type Comment post command.
 * <p>
 * Saves comment to storage.
 */
@Log4j2
public class CommentPostCommand extends Command {
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
        String comment = request.getParameter("comment");

        if (comment == null || comment.isEmpty() || !authService.isAuthenticated()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        Optional<Integer> testId = RequestService.getInstance().getIntParameter(request, "testId");
        if (!testId.isPresent()) {
            log.debug(() -> "testId is not present");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        TestCommentService testCommentService = (TestCommentService) serviceFactory.createService(ServiceType.TEST_COMMENT);
        testCommentService.saveComment(comment, testId.get(), authService.getEmail());

        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setLink(
                PropertiesStorage.getInstance().getLinks().getProperty("test.preview")
                        + "?testId=" + testId.get());
        responseEntity.setRedirect(true);
        return responseEntity;
    }
}
