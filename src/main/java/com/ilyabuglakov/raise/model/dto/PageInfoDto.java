package com.ilyabuglakov.raise.model.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

@Data
@Builder
public class PageInfoDto {
    private Integer currentPage;
    private Integer itemsAmount;
    private Integer itemsPerPage;
    private Integer maxPage;

    public int getCurrentPageIndex() {
        return currentPage - 1;
    }

    public boolean isIllegal() {
        return ObjectUtils.anyNull(currentPage, maxPage)
                || currentPage <= 0 || maxPage <= 0 || currentPage > maxPage;
    }

}
