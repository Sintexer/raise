package com.ilyabuglakov.raise.model.service.auth;

import javax.servlet.http.HttpServletRequest;

/**
 * The interface Auth service. Auth service operates authorization.
 */
public interface AuthService {
    /**
     * Return if subject is authenticated or not.
     *
     * @return the boolean
     */
    boolean isAuthenticated();

    /**
     * Login subject.
     *
     * @param username the username
     * @param password the password
     * @return the boolean
     */
    boolean login(String username, String password);

    /**
     * Gets previous url.
     *
     * @param request    the request
     * @param defaultUrl the default url
     * @return the previous url
     */
    String getPreviousUrl(HttpServletRequest request, String defaultUrl);

    /**
     * Gets email.
     *
     * @return the email
     */
    String getEmail();

    /**
     * Is permitted boolean.
     *
     * @param permission the permission
     * @return the boolean
     */
    boolean isPermitted(String permission);
}
