package com.ilyabuglakov.raise.dal.dao.interfaces;

import com.ilyabuglakov.raise.dal.dao.Dao;
import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.domain.TestCategory;

import java.util.List;

/**
 * The interface Test category dao.
 */
public interface TestCategoryDao extends Dao<TestCategory> {
    /**
     * Find all list.
     *
     * @return the list
     * @throws DaoOperationException the dao operation exception
     */
    List<TestCategory> findAll() throws DaoOperationException;
}
