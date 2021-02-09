package com.ilyabuglakov.raise.model.service.domain;

import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.domain.TestCategory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The interface Test category service.
 */
public interface TestCategoryService {

    /**
     * Gets category map.
     *
     * @return the category map
     * @throws PersistentException the persistent exception
     */
    Map<TestCategory, List<TestCategory>> getCategoryMap() throws PersistentException;

    /**
     * Gets category.
     *
     * @param id the id
     * @return the category
     * @throws PersistentException the persistent exception
     */
    Optional<TestCategory> getCategory(Integer id) throws PersistentException;

}
