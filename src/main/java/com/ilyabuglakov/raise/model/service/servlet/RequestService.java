package com.ilyabuglakov.raise.model.service.servlet;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * The type Request service.
 */
public class RequestService {

    private static class InstanceHolder {
        /**
         * The constant INSTANCE.
         */
        public static RequestService INSTANCE = new RequestService();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static RequestService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Gets int parameter fom request by name.
     *
     * @param request       the request
     * @param parameterName the parameter name
     * @return the int parameter
     */
    public Optional<Integer> getIntParameter(HttpServletRequest request, String parameterName) {
        Optional<Integer> optionalInteger = Optional.empty();
        String parameter = request.getParameter(parameterName);
        if (parameter != null) {
            try {
                optionalInteger = Optional.of(Integer.parseInt(parameter));
            } catch (NumberFormatException e) {
                //Exception ignored because method will return Optional.empty anyway
            }
        }
        return optionalInteger;
    }

}
