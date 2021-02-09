package com.ilyabuglakov.raise.model.service.request.extractor;

import com.ilyabuglakov.raise.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
 * The type User extractor.
 */
public class UserExtractor implements RequestDataExtractor<User> {
    @Override
    public User extractFrom(HttpServletRequest request) {
        String email = request.getParameter("username");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String password = request.getParameter("password");

        return User.builder()
                .email(email.toLowerCase())
                .name(name)
                .surname(surname)
                .password(password)
                .build();
    }
}
