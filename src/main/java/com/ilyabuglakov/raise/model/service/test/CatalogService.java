package com.ilyabuglakov.raise.model.service.test;

import com.ilyabuglakov.raise.model.dto.PageInfoDto;
import lombok.extern.log4j.Log4j2;

/**
 * The type Catalog service. Works with catalog parameters such as current page number, max page, etc.
 */
@Log4j2
public class CatalogService {

    private CatalogService() {
    }

    /**
     * Gets max page.
     *
     * @param testAmount   the test amount
     * @param itemsPerPage the items per page
     * @return the max page
     */
    public static int getMaxPage(int testAmount, int itemsPerPage) {
        testAmount = Math.max(testAmount, 1);
        return (int) Math.ceil((double) (testAmount) / itemsPerPage);
    }

    /**
     * Gets page number
     *
     * @param stringPageNumber the string page number
     * @return the page number or -1 if illegal page number
     */
    public static int getPageNumber(String stringPageNumber) {
        int page = 1;
        if (stringPageNumber != null && !stringPageNumber.isEmpty()) {
            try {
                page = Integer.parseInt(stringPageNumber);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return page;
    }

    /**
     * Gets page info.
     *
     * @param currentPage  the current page
     * @param itemsAmount  the items amount
     * @param itemsPerPage the items per page
     * @return the page info
     */
    public static PageInfoDto getPageInfo(String currentPage, int itemsAmount, int itemsPerPage) {
        try {
            return getPageInfo(getPageNumber(currentPage), itemsAmount, itemsPerPage);
        } catch (NumberFormatException e) {
            return PageInfoDto.builder().build();
        }
    }

    /**
     * Gets page info.
     *
     * @param currentPage  the current page
     * @param itemsAmount  the items amount
     * @param itemsPerPage the items per page
     * @return the page info
     */
    public static PageInfoDto getPageInfo(int currentPage, int itemsAmount, int itemsPerPage) {
        int maxPage = getMaxPage(itemsAmount, itemsPerPage);
        return PageInfoDto.builder()
                .currentPage(currentPage)
                .itemsAmount(itemsAmount)
                .itemsPerPage(itemsPerPage)
                .maxPage(maxPage)
                .build();
    }
}
