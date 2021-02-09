package com.ilyabuglakov.raise.model.service.domain.database;

import com.ilyabuglakov.raise.dal.dao.interfaces.TestCategoryDao;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.dal.transaction.Transaction;
import com.ilyabuglakov.raise.domain.TestCategory;
import com.ilyabuglakov.raise.model.DaoType;
import com.ilyabuglakov.raise.model.service.domain.TestCategoryService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestCategoryDatabaseService extends DatabaseService implements TestCategoryService {
    public TestCategoryDatabaseService(Transaction transaction) {
        super(transaction);
    }

    @Override
    public Map<TestCategory, List<TestCategory>> getCategoryMap() throws PersistentException {
        TestCategoryDao testCategoryDao = (TestCategoryDao) transaction.createDao(DaoType.TEST_CATEGORY);
        List<TestCategory> testCategories = testCategoryDao.findAll();
        List<TestCategory> parentCategories = testCategories.stream()
                .filter(tc -> tc.getParent() == null)
                .collect(Collectors.toList());
        Map<Integer, List<TestCategory>> testCategoryMap = testCategories.stream()
                .filter(tc -> tc.getParent() != null)
                .collect(Collectors.groupingBy(tc -> tc.getParent().getId()));
        return parentCategories.stream()
                .filter(pc -> testCategoryMap.get(pc.getId()) != null)
                .collect(Collectors.toMap(Function.identity(), pc -> testCategoryMap.get(pc.getId())));
    }

    @Override
    public Optional<TestCategory> getCategory(Integer id) throws PersistentException {
        return ((TestCategoryDao) transaction.createDao(DaoType.TEST_CATEGORY))
                .read(id);
    }
}
