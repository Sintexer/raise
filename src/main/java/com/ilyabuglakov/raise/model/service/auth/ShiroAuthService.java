package com.ilyabuglakov.raise.model.service.auth;

import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Shiro based Auth service implementation
 */
@Log4j2
public class ShiroAuthService implements AuthService {
    @Override
    public boolean isAuthenticated() {
        return SecurityUtils.getSubject().isAuthenticated();
    }

    @Override
    public boolean login(String username, String password) {
        password = new Sha256Hash(password).toHex();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.login(token);
            log.info(() -> "after login");
            return true;
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            return false;
        }
    }

    @Override
    public String getPreviousUrl(HttpServletRequest request, String defaultUrl) {
        SavedRequest savedRequest = WebUtils.getSavedRequest(request);
        if (savedRequest != null)
            return savedRequest.getRequestUrl();
        return defaultUrl;
    }

    @Override
    public String getEmail() {
        return (String) SecurityUtils.getSubject().getPrincipal();
    }

    @Override
    public boolean isPermitted(String permission) {
        return SecurityUtils.getSubject().isPermitted(permission);
    }
}
