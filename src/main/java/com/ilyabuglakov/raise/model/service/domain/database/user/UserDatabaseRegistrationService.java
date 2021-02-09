package com.ilyabuglakov.raise.model.service.domain.database.user;

import com.ilyabuglakov.raise.dal.dao.exception.DaoOperationException;
import com.ilyabuglakov.raise.dal.dao.interfaces.RoleDao;
import com.ilyabuglakov.raise.dal.dao.interfaces.UserDao;
import com.ilyabuglakov.raise.dal.transaction.Transaction;
import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.domain.UsrKey;
import com.ilyabuglakov.raise.domain.type.Status;
import com.ilyabuglakov.raise.domain.type.UserRole;
import com.ilyabuglakov.raise.model.DaoType;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.domain.UserRegistrationService;
import com.ilyabuglakov.raise.model.service.domain.database.DatabaseService;
import com.ilyabuglakov.raise.model.service.mail.MailSender;
import com.ilyabuglakov.raise.model.service.mail.MailSenderFactory;
import com.ilyabuglakov.raise.storage.PropertiesStorage;
import org.apache.shiro.crypto.hash.Sha256Hash;

import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public class UserDatabaseRegistrationService extends DatabaseService implements UserRegistrationService {
    public UserDatabaseRegistrationService(Transaction transaction) {
        super(transaction);
    }

    @Override
    public ResponseEntity registerUser(User user) throws DaoOperationException, MessagingException {
        UserDatabaseValidationService userValidationService = new UserDatabaseValidationService(transaction);
        ResponseEntity responseEntity = userValidationService.validateUser(user);
        if (responseEntity.isErrorOccurred())
            return responseEntity;
        if (!userValidationService.isUnique(user)) {
            responseEntity.setErrorOccurred(true);
            responseEntity.setAttribute("userEmailAlreadyExist", true);
            return responseEntity;
        }
        user.setPassword(new Sha256Hash(user.getPassword()).toHex());
        user.setStatus(Status.UNCONFIRMED);
        user.setRegistrationDate(LocalDate.now());
        UserDao userDao = (UserDao) transaction.createDao(DaoType.USER);

        String key;
        SecureRandom secureRandom = new SecureRandom();
        do {
            byte[] bytes = new byte[30];
            secureRandom.nextBytes(bytes);
            key = new Sha256Hash(bytes).toHex();
        } while (userDao.findKey(key).isPresent());

        Integer userId = userDao.create(user);
        userDao.createKey(key, userId, LocalDateTime.now());
        sendKeyMail(user.getEmail(), key);

        RoleDao roleDao = (RoleDao) transaction.createDao(DaoType.ROLE);
        roleDao.createUserRoles(userId, Set.of(UserRole.USER));
        transaction.commit();
        return responseEntity;
    }

    @Override
    public boolean tryConfirm(String key) throws DaoOperationException {
        UserDao userDao = (UserDao) transaction.createDao(DaoType.USER);
        Optional<UsrKey> usrKeyOptional = userDao.findKey(key);
        if (!usrKeyOptional.isPresent()) {
            return false;
        }
        UsrKey usrKey = usrKeyOptional.get();
        userDao.updateStatus(usrKey.getUserId(), Status.ACTIVE);
        userDao.deleteKey(usrKey.getKey());
        transaction.commit();
        return true;
    }

    private void sendKeyMail(String userEmail, String key) throws MessagingException {
        MailSender mailSender = new MailSenderFactory().createMailSender();
        String body = "To confirm tour account, please, follow the link bellow:\n" +
                "http://localhost:8080" +
                PropertiesStorage.getInstance().getLinks().getProperty("auth.confirm") +
                "?key=" + key;
        mailSender.sendMail(userEmail, "Raise account confirmation", body);
    }
}
