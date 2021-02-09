package com.ilyabuglakov.raise.model.service.domain.database;

import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.dao.interfaces.TestCommentDao;
import com.ilyabuglakov.raise.dal.dao.interfaces.UserDao;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.dal.transaction.Transaction;
import com.ilyabuglakov.raise.domain.Test;
import com.ilyabuglakov.raise.domain.TestComment;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.model.DaoType;
import com.ilyabuglakov.raise.model.service.domain.TestCommentService;
import com.ilyabuglakov.raise.model.service.domain.UserService;
import com.ilyabuglakov.raise.model.service.domain.database.user.UserDatabaseService;

import java.time.LocalDateTime;
import java.util.List;

public class TestCommentDatabaseService extends DatabaseService implements TestCommentService {
    public TestCommentDatabaseService(Transaction transaction) {
        super(transaction);
    }

    @Override
    public void saveComment(String comment, Integer testId, String authorEmail)
            throws PersistentException {
        TestCommentDao testCommentDao = (TestCommentDao) transaction.createDao(DaoType.TEST_COMMENT);
        UserService userService = new UserDatabaseService(transaction);
        User user = userService.getUser(authorEmail).orElseThrow(DaoOperationException::new);
        testCommentDao.create(TestComment.builder()
                .test(Test.builder().id(testId).build())
                .user(user)
                .content(comment)
                .timestamp(LocalDateTime.now())
                .build());
        transaction.commit();
    }

    @Override
    public List<TestComment> getComments(Integer testId, int pageStart, int itemsPerPage) throws PersistentException {
        UserDao userDao = (UserDao) transaction.createDao(DaoType.USER);
        List<TestComment> testComments = ((TestCommentDao) transaction.createDao(DaoType.TEST_COMMENT))
                .findComments(testId, pageStart * itemsPerPage, itemsPerPage);
        for (TestComment testComment : testComments) {
            testComment.setUser(userDao.read(testComment.getUser().getId()).orElseThrow(PersistentException::new));
        }
        return testComments;
    }

    @Override
    public Integer getCommentsAmount(Integer testId) throws DaoOperationException {
        return ((TestCommentDao) transaction.createDao(DaoType.TEST_COMMENT))
                .findCommentsAmount(testId);
    }

    @Override
    public int getCommentsAmount(String email) throws DaoOperationException {
        return ((TestCommentDao) transaction.createDao(DaoType.TEST_COMMENT))
                .findUserCommentAmount(email);
    }

    @Override
    public void deleteComment(Integer commentId) throws PersistentException {
        ((TestCommentDao) transaction.createDao(DaoType.TEST_COMMENT))
                .delete(TestComment.builder().id(commentId).build());
    }
}
