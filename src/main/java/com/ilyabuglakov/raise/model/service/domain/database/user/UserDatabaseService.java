package com.ilyabuglakov.raise.model.service.domain.database.user;

import com.ilyabuglakov.raise.dal.dao.database.UserDatabaseDao;
import com.ilyabuglakov.raise.dal.dao.interfaces.TestDao;
import com.ilyabuglakov.raise.dal.dao.interfaces.UserDao;
import com.ilyabuglakov.raise.dal.dao.interfaces.UserTestResultDao;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.dal.transaction.Transaction;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.domain.UserTestResult;
import com.ilyabuglakov.raise.domain.type.Characteristic;
import com.ilyabuglakov.raise.model.DaoType;
import com.ilyabuglakov.raise.model.dto.UserCharacteristic;
import com.ilyabuglakov.raise.model.dto.UserInfoDto;
import com.ilyabuglakov.raise.model.dto.UserParametersDto;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.domain.TestCommentService;
import com.ilyabuglakov.raise.model.service.domain.TestService;
import com.ilyabuglakov.raise.model.service.domain.UserService;
import com.ilyabuglakov.raise.model.service.domain.database.DatabaseService;
import com.ilyabuglakov.raise.model.service.domain.database.TestCommentDatabaseService;
import com.ilyabuglakov.raise.model.service.domain.database.test.TestDatabaseService;
import com.ilyabuglakov.raise.model.service.user.UserInfoChangeService;
import com.ilyabuglakov.raise.model.service.validator.UserCredentialsValidator;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class UserDatabaseService extends DatabaseService implements UserService {
    public UserDatabaseService(Transaction transaction) {
        super(transaction);
    }

    @Override
    public Optional<User> getUser(String email) throws PersistentException {
        UserDatabaseDao dao = (UserDatabaseDao) transaction.createDao(DaoType.USER);
        return dao.findByEmail(email);
    }

    @Override
    public Optional<User> getUser(Integer id) throws PersistentException {
        UserDatabaseDao dao = (UserDatabaseDao) transaction.createDao(DaoType.USER);
        return dao.read(id);
    }

    @Override
    public void updateUser(User user) throws PersistentException {
        UserDao userDao = (UserDao) transaction.createDao(DaoType.USER);
        userDao.update(user);
        transaction.commit();
    }

    @Override
    public UserParametersDto getUserParameters(Integer userId) throws PersistentException {
        Optional<User> userOptional = getUser(userId);
        if (!userOptional.isPresent())
            return null;

        return createUserParameters(userOptional.get());
    }

    @Override
    public UserParametersDto getUserParameters(String email) throws PersistentException {
        Optional<User> userOptional = getUser(email);
        if (!userOptional.isPresent())
            return null;

        return createUserParameters(userOptional.get());
    }

    private UserParametersDto createUserParameters(User user) throws PersistentException {
        UserParametersDto userParametersDto = null;
        int answeredTestsAmount = getResultsAmount(user.getId());
        TestService testService = new TestDatabaseService(transaction);
        int postedTestsAmount = testService.getTestAmountByUser(user.getId());
        TestCommentService testCommentService = new TestCommentDatabaseService(transaction);
        int commentsAmount = testCommentService.getCommentsAmount(user.getEmail());
        List<UserCharacteristic> characteristics = getUserCharacteristics(user.getId());

        userParametersDto = UserParametersDto.builder()
                .user(user)
                .userCharacteristics(characteristics)
                .testsSolved(answeredTestsAmount)
                .testsCreated(postedTestsAmount)
                .commentsPosted(commentsAmount)
                .build();
        return userParametersDto;
    }

    @Override
    public ResponseEntity changeUserInfo(UserInfoDto userInfoDto) throws PersistentException {
        UserInfoChangeService userInfoChangeService = new UserInfoChangeService();
        ResponseEntity responseEntity = new ResponseEntity();
        boolean somethingChanged = false;
        boolean somethingWrong = false;
        if (!userInfoDto.getName().isEmpty()
                && !userInfoDto.getUser().getName().equals(userInfoDto.getName())) {
            responseEntity.setAttribute("nameChanged",
                    userInfoChangeService.changeName(userInfoDto.getUser(), userInfoDto.getName()));
            somethingChanged = true;
        }
        if (!userInfoDto.getSurname().isEmpty()
                && !userInfoDto.getUser().getSurname().equals(userInfoDto.getSurname())) {
            responseEntity.setAttribute("surnameChanged",
                    userInfoChangeService.changeSurname(userInfoDto.getUser(), userInfoDto.getSurname()));
            somethingChanged = true;
        }

        if (!userInfoDto.getOldPassword().isEmpty()
                && !userInfoDto.getNewPassword().isEmpty()
                && !userInfoDto.getNewPasswordRepeat().isEmpty()) {
            UserCredentialsValidator userCredentialsValidator = new UserCredentialsValidator();
            if (userCredentialsValidator.isCorrectOldPassword(userInfoDto.getUser(), userInfoDto.getOldPassword())) {
                boolean changed = userInfoChangeService.changePassword(
                        userInfoDto.getUser(),
                        userInfoDto.getNewPassword(),
                        userInfoDto.getNewPasswordRepeat());
                if (changed) {
                    responseEntity.setAttribute("passwordChanged", true);
                    somethingChanged = true;
                } else {
                    responseEntity.setAttribute("incorrectNewPassword", true);
                    somethingWrong = true;
                }
            } else {
                somethingWrong = true;
                responseEntity.setAttribute("incorrectOldPassword", true);
            }
        } else {
            responseEntity.setAttribute("incorrectOldPassword", true);
        }

        if (somethingChanged) {
            updateUser(userInfoDto.getUser());
            transaction.commit();
        }
        if (somethingWrong) {
            responseEntity.setErrorOccurred(true);
        }

        responseEntity.setAttribute("userParameters", getUserParameters(userInfoDto.getUser().getId()));
        responseEntity.setAttribute("somethingChanged", somethingChanged);
        responseEntity.setAttribute("somethingWrong", somethingWrong);
        return responseEntity;
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

    @Override
    public int getResultsAmount(Integer userId) throws PersistentException {
        UserTestResultDao userTestResultDao = (UserTestResultDao) transaction.createDao(DaoType.USER_TEST_RESULT);
        return userTestResultDao.findResultAmount(userId);
    }
}
