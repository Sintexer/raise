package com.ilyabuglakov.raise.command.impl.test;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.domain.type.TestStatus;
import com.ilyabuglakov.raise.model.dto.CatalogTestsDto;
import com.ilyabuglakov.raise.model.dto.PageInfoDto;
import com.ilyabuglakov.raise.model.dto.TestSearchParametersDto;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.domain.ServiceType;
import com.ilyabuglakov.raise.model.service.domain.TestCategoryService;
import com.ilyabuglakov.raise.model.service.domain.TestService;
import com.ilyabuglakov.raise.model.service.property.ApplicationProperties;
import com.ilyabuglakov.raise.model.service.request.extractor.TestSearchParametersExtractor;
import com.ilyabuglakov.raise.storage.PropertiesStorage;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type Test catalog page command.
 * <p>
 * Returns the test catalog to user
 */
@Log4j2
public class TestCatalogPageCommand extends Command {
    /**
     * @param request  http request
     * @param response http response
     * @return the response entity with tests or null if 404
     * @throws IOException         by request/response
     * @throws PersistentException datasource error
     */
    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, PersistentException {

        ResponseEntity responseEntity = new ResponseEntity();
        TestService testService = (TestService) serviceFactory.createService(ServiceType.TEST);
        TestCategoryService testCategoryService = (TestCategoryService) serviceFactory.createService(ServiceType.TEST_CATEGORY);

        TestSearchParametersDto searchParametersDto = new TestSearchParametersExtractor().extractFrom(request);
        searchParametersDto.setItemsAmount(testService.getTestAmountByStatus(TestStatus.CONFIRMED));
        searchParametersDto.setLimit(Integer.parseInt(ApplicationProperties.getProperty("catalog.page.items")));
        PageInfoDto pageInfoDto = searchParametersDto.getPageInfoDto();
        if (pageInfoDto.isIllegal()) {
            log.error("Illegal page" + pageInfoDto);
            response.sendError(404);
            return null;
        }
        if (searchParametersDto.getCategoryId() != null) {
            searchParametersDto.setCategory(
                    testCategoryService.getCategory(searchParametersDto.getCategoryId()).orElse(null));
        }

        CatalogTestsDto testsDto = testService.findBySearchParameters(searchParametersDto);
        searchParametersDto.setItemsAmount(testsDto.getItemsAmount());
        pageInfoDto = searchParametersDto.getPageInfoDto();

        log.info(testsDto.getTests());
        log.debug(pageInfoDto);

        responseEntity.setAttribute("stashedCategory", searchParametersDto.getCategory());
        responseEntity.setAttribute("stashedTestName", searchParametersDto.getTestName());
        responseEntity.setAttribute("tests", testsDto.getTests());
        responseEntity.setAttribute("categories", testCategoryService.getCategoryMap());
        responseEntity.setAttribute("currentPage", pageInfoDto.getCurrentPage());
        responseEntity.setAttribute("maxPage", pageInfoDto.getMaxPage());
        responseEntity.setLink(PropertiesStorage.getInstance().getPages().getProperty("test.catalog"));
        return responseEntity;
    }
}
