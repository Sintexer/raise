package com.ilyabuglakov.raise.model.service.domain;

import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.domain.Test;
import com.ilyabuglakov.raise.domain.type.TestStatus;
import com.ilyabuglakov.raise.model.dto.AdvancedTestInfo;
import com.ilyabuglakov.raise.model.dto.CatalogTestsDto;
import com.ilyabuglakov.raise.model.dto.TestDto;
import com.ilyabuglakov.raise.model.dto.TestInfo;
import com.ilyabuglakov.raise.model.dto.TestSearchParametersDto;
import com.ilyabuglakov.raise.model.response.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * The interface Test service.
 */
public interface TestService extends Service {

    /**
     * Validate test.
     *
     * @param test the test
     * @return the response entity
     */
    ResponseEntity validateTest(Test test);

    /**
     * Create result.
     *
     * @param testDto the test dto
     * @return the response entity
     * @throws PersistentException the persistent exception
     */
    ResponseEntity createResult(TestDto testDto) throws PersistentException;

    /**
     * Create from json optional.
     *
     * @param json the json
     * @return the optional
     */
    Optional<Test> createFromJson(String json);

    /**
     * Create dto from json optional.
     *
     * @param json the json
     * @return the optional
     */
    Optional<TestDto> createDtoFromJson(String json);

    /**
     * Save.
     *
     * @param test        the test
     * @param authorEmail the author email
     * @throws PersistentException the persistent exception
     */
    void save(Test test, String authorEmail) throws PersistentException;

    /**
     * Change test status.
     *
     * @param testId the test id
     * @param status the status
     * @throws PersistentException the persistent exception
     */
    void changeTestStatus(Integer testId, TestStatus status) throws PersistentException;

    /**
     * Gets test.
     *
     * @param id the id
     * @return the test
     * @throws PersistentException the persistent exception
     */
    Optional<Test> getTest(Integer id) throws PersistentException;

    /**
     * Find by search parameters.
     *
     * @param searchParametersDto the search parameters dto
     * @return the catalog tests dto
     * @throws PersistentException the persistent exception
     */
    CatalogTestsDto findBySearchParameters(TestSearchParametersDto searchParametersDto) throws PersistentException;

    /**
     * Gets test infos by status.
     *
     * @param status the status
     * @param limit  the limit
     * @param from   the from
     * @return the test infos by status
     * @throws PersistentException the persistent exception
     */
    List<TestInfo> getTestInfosByStatus(TestStatus status, int limit, int from) throws PersistentException;

    /**
     * Gets test infos by status and page.
     *
     * @param status the status
     * @param limit  the limit
     * @param from   the from
     * @return the test infos by status and page
     * @throws PersistentException the persistent exception
     */
    List<TestInfo> getTestInfosByStatusAndPage(TestStatus status, int limit, int from) throws PersistentException;

    /**
     * Gets advanced test infos by status.
     *
     * @param status the status
     * @param limit  the limit
     * @param from   the from
     * @return the advanced test infos by status
     * @throws PersistentException the persistent exception
     */
    List<AdvancedTestInfo> getAdvancedTestInfosByStatus(TestStatus status, int limit, int from) throws PersistentException;

    /**
     * Gets advanced test infos by status and page.
     *
     * @param status the status
     * @param limit  the limit
     * @param from   the from
     * @return the advanced test infos by status and page
     * @throws PersistentException the persistent exception
     */
    List<AdvancedTestInfo> getAdvancedTestInfosByStatusAndPage(TestStatus status, int limit, int from) throws PersistentException;

    /**
     * Gets test amount by status.
     *
     * @param status the status
     * @return the test amount by status
     * @throws PersistentException the persistent exception
     */
    int getTestAmountByStatus(TestStatus status) throws PersistentException;

    /**
     * Gets test amount by user.
     *
     * @param userId the user id
     * @return the test amount by user
     * @throws PersistentException the persistent exception
     */
    int getTestAmountByUser(Integer userId) throws PersistentException;

}
