package com.ilyabuglakov.raise.model.service.user;

import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.model.service.validator.UserCredentialsValidator;
import com.ilyabuglakov.raise.model.service.validator.UserValidator;
import org.apache.shiro.crypto.hash.Sha256Hash;

/**
 * The type User info change service. Changes user info if new info is valid.
 */
public class UserInfoChangeService {

    /**
     * Change name.
     *
     * @param user the user
     * @param name the name
     * @return the boolean
     */
    public boolean changeName(User user, String name) {
        UserValidator userValidator = new UserValidator();
        if (userValidator.isValidName(name)) {
            user.setName(name);
            return true;
        }
        return false;
    }

    /**
     * Change surname.
     *
     * @param user    the user
     * @param surname the surname
     * @return the boolean
     */
    public boolean changeSurname(User user, String surname) {
        UserValidator userValidator = new UserValidator();
        if (userValidator.isValidSurname(surname)) {
            user.setSurname(surname);
            return true;
        }
        return false;
    }

    /**
     * Change password.
     *
     * @param user              the user
     * @param newPassword       the new password
     * @param newPasswordRepeat the new password repeat
     * @return the boolean
     */
    public boolean changePassword(User user, String newPassword, String newPasswordRepeat) {
        UserCredentialsValidator userCredentialsValidator = new UserCredentialsValidator();
        if (userCredentialsValidator.isValidNewPassword(newPassword, newPasswordRepeat)) {
            user.setPassword(new Sha256Hash(newPassword).toHex());
            return true;
        }
        return false;
    }

}
