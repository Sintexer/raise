package com.ilyabuglakov.raise.command.impl.index;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.domain.type.TestStatus;
import com.ilyabuglakov.raise.model.dto.TestInfo;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.domain.ServiceType;
import com.ilyabuglakov.raise.model.service.domain.TestService;
import com.ilyabuglakov.raise.storage.PropertiesStorage;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Index get command.
 * <p>
 * Command adds all needed for index jsp page to request attributes
 */
@Log4j2
public class IndexGetCommand extends Command {
    /**
     * @param request  http request
     * @param response http response
     * @return Index page
     * @throws PersistentException datasource error
     */
    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response)
            throws PersistentException {
        log.info("Entered index command");
        TestService testService = (TestService) serviceFactory.createService(ServiceType.TEST);
        List<TestInfo> tests = testService.getTestInfosByStatus(TestStatus.CONFIRMED, 3, 0);

        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setAttribute("newTests", tests);
        responseEntity.setLink(PropertiesStorage.getInstance().getPages().getProperty("index"));
        return responseEntity;
    }
}
