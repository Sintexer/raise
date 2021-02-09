package com.ilyabuglakov.raise.model.service.servlet;

import javax.servlet.http.Cookie;

/**
 * The type Cookie service.
 */
public class CookieService {

    /**
     * Create locale cookie.
     *
     * @param locale the locale
     * @return the cookie
     */
    public static Cookie createLocaleCookie(String locale) {
        Cookie cookie = new Cookie("userLocale", locale);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

}
