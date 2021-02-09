package com.ilyabuglakov.raise.command.manager;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.model.response.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The interface Command manager.
 */
public interface CommandManager {
    /**
     * Sets command ServiceFactory and executes it.
     *
     * @param command  the command to execute
     * @param request  http request
     * @param response http response
     * @return the response entity
     * @throws PersistentException the persistent exception
     * @throws IOException         the io exception
     */
    ResponseEntity execute(Command command, HttpServletRequest request, HttpServletResponse response)
            throws PersistentException, IOException;

    /**
     * Close service factory and transactions.
     */
    void close();

    /**
     * Rollback transactions.
     */
    void rollback();
}
