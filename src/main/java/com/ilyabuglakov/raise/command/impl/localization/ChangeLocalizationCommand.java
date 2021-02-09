package com.ilyabuglakov.raise.command.impl.localization;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.servlet.CookieService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Change localization command.
 * <p>
 * Adds locale cookie to response
 */
@Log4j2
public class ChangeLocalizationCommand extends Command {
    /**
     * @param request  http request
     * @param response http response
     * @return nothing (null)
     */
    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response) {
        String locale = request.getParameter("userLocale");
        response.addCookie(CookieService.createLocaleCookie(locale));
        log.debug(() -> "Cookie created for locale " + locale);
        return null;
    }
}
