package com.ilyabuglakov.raise.model.service.request.extractor;

import javax.servlet.http.HttpServletRequest;

/**
 * The interface Request data extractor.
 *
 * @param <T> the data type parameter
 */
public interface RequestDataExtractor<T> {
    /**
     * Extract data from request.
     *
     * @param request the request
     * @return the data type
     */
    T extractFrom(HttpServletRequest request);
}
