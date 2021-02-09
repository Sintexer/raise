package com.ilyabuglakov.raise.model.service.validator;

import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.model.Patterns;
import org.apache.shiro.crypto.hash.Sha256Hash;

/**
 * The type User credentials validator.
 */
public class UserCredentialsValidator {

    /**
     * Is correct old password boolean.
     *
     * @param user        the user
     * @param oldPassword the old password
     * @return the boolean
     */
    public boolean isCorrectOldPassword(User user, String oldPassword) {
        return user.getPassword().equals(new Sha256Hash(oldPassword).toHex());

    }

    /**
     * Is valid new password boolean.
     *
     * @param newPassword       the new password
     * @param newPasswordRepeat the new password repeat
     * @return the boolean
     */
    public boolean isValidNewPassword(String newPassword, String newPasswordRepeat) {
        if (!newPassword.equals(newPasswordRepeat))
            return false;
        return newPassword.matches(Patterns.PASSWORD.getPattern());
    }

}
