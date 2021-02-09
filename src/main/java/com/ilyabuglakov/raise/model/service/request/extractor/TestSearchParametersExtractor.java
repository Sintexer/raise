package com.ilyabuglakov.raise.model.service.request.extractor;

import com.ilyabuglakov.raise.domain.type.TestStatus;
import com.ilyabuglakov.raise.model.dto.TestSearchParametersDto;
import com.ilyabuglakov.raise.model.service.servlet.RequestService;
import com.ilyabuglakov.raise.model.service.test.CatalogService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * The type Test search parameters extractor.
 */
public class TestSearchParametersExtractor implements RequestDataExtractor<TestSearchParametersDto> {
    @Override
    public TestSearchParametersDto extractFrom(HttpServletRequest request) {
        int page = CatalogService.getPageNumber(request.getParameter("pageNumber"));
        String testName = request.getParameter("testName");
        TestStatus status = TestStatus.CONFIRMED;
        Optional<Integer> categoryId = RequestService.getInstance().getIntParameter(request, "category");
        if (!categoryId.isPresent())
            categoryId = RequestService.getInstance().getIntParameter(request, "stashedCategory");
        return TestSearchParametersDto.builder()
                .testName(testName)
                .status(status)
                .categoryId(categoryId.orElse(null))
                .page(page).build();
    }
}
