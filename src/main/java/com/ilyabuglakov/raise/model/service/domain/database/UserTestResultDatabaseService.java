package com.ilyabuglakov.raise.model.service.domain.database;

import com.ilyabuglakov.raise.dal.dao.interfaces.TestDao;
import com.ilyabuglakov.raise.dal.dao.interfaces.UserDao;
import com.ilyabuglakov.raise.dal.dao.interfaces.UserTestResultDao;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.dal.transaction.Transaction;
import com.ilyabuglakov.raise.domain.Test;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.domain.UserTestResult;
import com.ilyabuglakov.raise.domain.type.Characteristic;
import com.ilyabuglakov.raise.model.DaoType;
import com.ilyabuglakov.raise.model.dto.TestResultDto;
import com.ilyabuglakov.raise.model.dto.UserCharacteristic;
import com.ilyabuglakov.raise.model.service.domain.UserTestResultService;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class UserTestResultDatabaseService extends DatabaseService implements UserTestResultService {
    public UserTestResultDatabaseService(Transaction transaction) {
        super(transaction);
    }

    @Override
    public void saveResult(TestResultDto testResultDto, String email) throws PersistentException {
        UserDao userDao = (UserDao) transaction.createDao(DaoType.USER);
        TestDao testDao = (TestDao) transaction.createDao(DaoType.TEST);
        User user = userDao.findByEmail(email).orElseThrow(PersistentException::new);
        Test test = testDao.read(testResultDto.getTestId()).orElseThrow(PersistentException::new);
        UserTestResult userTestResult = UserTestResult.builder()
                .result(testResultDto.getResult() * test.getDifficulty())
                .user(user)
                .test(test)
                .build();
        UserTestResultDao userTestResultDao = (UserTestResultDao) transaction.createDao(DaoType.USER_TEST_RESULT);
        Optional<UserTestResult> prevResult =
                userTestResultDao.findByUserIdAndTestId(userTestResult.getUser().getId(),
                        userTestResult.getTest().getId());
        if (prevResult.isPresent()) {
            userTestResult.setId(prevResult.get().getId());
            if (prevResult.get().getResult() < userTestResult.getResult()) {
                userTestResultDao.update(userTestResult);
            }
        } else {
            userTestResultDao.create(userTestResult);
        }
        transaction.commit();
    }

    @Override
    public int getResultsAmount(Integer userId) throws PersistentException {
        UserTestResultDao userTestResultDao = (UserTestResultDao) transaction.createDao(DaoType.USER_TEST_RESULT);
        return userTestResultDao.findResultAmount(userId);
    }

    @Override
    public List<UserCharacteristic> getUserCharacteristics(Integer userId) throws PersistentException {
        UserTestResultDao userTestResultDao = (UserTestResultDao) transaction.createDao(DaoType.USER_TEST_RESULT);
        TestDao testDao = (TestDao) transaction.createDao(DaoType.TEST);

        List<UserTestResult> userTestResults = userTestResultDao.findUserTestResults(userId);
        Map<Characteristic, Double> characteristicResults = Stream.of(Characteristic.values())
                .collect(Collectors.toMap(Function.identity(), characteristic -> 0.0));

        for (UserTestResult utr : userTestResults) {
            testDao.findCharacteristics(utr.getTest().getId())
                    .forEach(characteristic ->
                            characteristicResults.merge(characteristic, (double) utr.getResult(), Double::sum));
        }
        log.info("User characteristics values: " + characteristicResults);

        return characteristicResults.entrySet().stream()
                .map(entry -> new UserCharacteristic(entry.getKey(), entry.getValue() / 100))
                .collect(Collectors.toList());
    }
}
