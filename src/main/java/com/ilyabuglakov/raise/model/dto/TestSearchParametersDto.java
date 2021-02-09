package com.ilyabuglakov.raise.model.dto;

import com.ilyabuglakov.raise.domain.TestCategory;
import com.ilyabuglakov.raise.domain.type.TestStatus;
import com.ilyabuglakov.raise.model.service.test.CatalogService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSearchParametersDto {
    private String testName;
    private TestCategory category;
    private Integer page;
    private int limit;
    private int itemsAmount;
    private Integer maxPage;
    private TestStatus status;
    private Integer categoryId;

    public boolean hasSearchParameters() {
        return category != null || testName != null && !testName.isEmpty();
    }

    public int getFrom() {
        return (page - 1) * limit;
    }

    public PageInfoDto getPageInfoDto() {
        return PageInfoDto.builder()
                .currentPage(page)
                .itemsPerPage(limit)
                .maxPage(CatalogService.getMaxPage(itemsAmount, limit))
                .build();
    }
}
