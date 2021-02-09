package com.ilyabuglakov.raise.model.service.auth;

/**
 * The type Auth service factory.
 */
public class AuthServiceFactory {

    private static class InstanceHolder {
        /**
         * The constant INSTANCE.
         */
        public static final AuthServiceFactory INSTANCE = new AuthServiceFactory();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static AuthServiceFactory getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Get auth service auth service.
     *
     * @return the auth service
     */
    public static AuthService getAuthService() {
        return getInstance().getDefaultAuthService();
    }

    private AuthService getDefaultAuthService() {
        return new ShiroAuthService();
    }

}
