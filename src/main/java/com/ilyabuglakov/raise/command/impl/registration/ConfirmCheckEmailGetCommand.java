package com.ilyabuglakov.raise.command.impl.registration;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.storage.PropertiesStorage;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The type Confirm check email get command.
 * <p>
 * Command returns page with invitation to check email for confirm url. Used for redirect after login
 */
@Log4j2
public class ConfirmCheckEmailGetCommand extends Command {
    /**
     * @param request  http request
     * @param response http response
     * @return the response entity
     * @throws PersistentException by datasource
     */
    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response)
            throws PersistentException {
        log.info("Entered checkEmailGet command");

        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setLink(PropertiesStorage.getInstance().getPages().getProperty("auth.confirm.email"));
        return responseEntity;
    }
}