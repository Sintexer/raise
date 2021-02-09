package com.ilyabuglakov.raise.model.service.validator;

import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.domain.type.Status;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class UserValidatorTest {

    public User user;

    @BeforeMethod
    public void setUp(){
        user = User.builder()
                .name("Ilya")
                .surname("Buglakov")
                .password("121212")
                .status(Status.ACTIVE)
                .email("sample@gmail.com")
                .build();
    }

    @Test
    public void testIsValidEmail() {
        UserValidator validator = new UserValidator();
        assertTrue(validator.isValidEmail(user.getEmail()));
    }

    @Test
    public void testIsValidName() {
        UserValidator validator = new UserValidator();
        assertTrue(validator.isValidName(user.getName()));
    }

    @Test
    public void testIsValidSurname() {
        UserValidator validator = new UserValidator();
        assertTrue(validator.isValidSurname(user.getSurname()));
    }

    @Test
    public void testIsValidPassword() {
        UserValidator validator = new UserValidator();
        assertTrue(validator.isValidPassword(user.getPassword()));
    }

    @Test
    public void testIsValid() {
        UserValidator validator = new UserValidator();
        assertTrue(validator.isValid(user));
    }

    @Test
    public void testisValidUserParameters() {
        UserValidator validator = new UserValidator();
        assertTrue(validator.isValidUserParameters(user.getEmail(), user.getName(), user.getSurname(), user.getPassword()));
    }

    @Test
    public void testFailedValidEmail() {
        UserValidator validator = new UserValidator();
        assertFalse(validator.isValidEmail("123"));
    }

    @Test
    public void testFailedValidName() {
        UserValidator validator = new UserValidator();
        assertFalse(validator.isValidName(""));
    }

    @Test
    public void testFailedValidSurname() {
        UserValidator validator = new UserValidator();
        assertFalse(validator.isValidSurname(""));
    }

    @Test
    public void testFailedValidPassword() {
        UserValidator validator = new UserValidator();
        assertFalse(validator.isValidPassword("123"));
    }

    @Test
    public void testFailedValid() {
        UserValidator validator = new UserValidator();
        assertFalse(validator.isValid(User.builder().email("").name("").surname("").password("").build()));
    }

    @Test
    public void testFailedValidUserParameters() {
        UserValidator validator = new UserValidator();
        assertFalse(validator.isValidUserParameters("", "", "", ""));
    }
}