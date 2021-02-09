package com.ilyabuglakov.raise.model.service.validator;

import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.model.FormConstants;
import com.ilyabuglakov.raise.model.Patterns;

/**
 * UserValidator can validate email, name and surname by regex and check if password length is in bounds.
 * Can validate whole User object by separate fields
 */
public class UserValidator {

    /**
     * Is valid email boolean.
     *
     * @param email the email
     * @return the boolean
     */
    public boolean isValidEmail(String email) {
        return email.matches(Patterns.EMAIL.getPattern());
    }

    /**
     * Is valid name boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean isValidName(String name) {
        return name.matches(Patterns.NAME.getPattern());
    }

    /**
     * Is valid surname boolean.
     *
     * @param surname the surname
     * @return the boolean
     */
    public boolean isValidSurname(String surname) {
        return surname.matches(Patterns.NAME.getPattern());
    }

    /**
     * Is valid password boolean.
     *
     * @param password the password
     * @return the boolean
     */
    public boolean isValidPassword(String password) {
        return password.length() >= FormConstants.PASSWORD_MIN.getValue() &&
                password.length() < FormConstants.PASSWORD_MAX.getValue();
    }

    /**
     * Is valid boolean.
     *
     * @param user the user
     * @return the boolean
     */
    public boolean isValid(User user) {
        return isValidUserParameters(user.getEmail(), user.getName(), user.getSurname(), user.getPassword());
    }

    /**
     * Is valid user parameters boolean.
     *
     * @param email    the email
     * @param name     the name
     * @param surname  the surname
     * @param password the password
     * @return the boolean
     */
    public boolean isValidUserParameters(String email, String name, String surname, String password) {
        return isValidEmail(email) &&
                isValidName(name) &&
                isValidSurname(surname) &&
                isValidPassword(password);
    }

}
