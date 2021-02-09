package com.ilyabuglakov.raise.command;

import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.domain.factory.ServiceFactory;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type Command.
 * <p>
 * Base class for servlet commands
 */
public abstract class Command {
    /**
     * The Service factory.
     */
    @Setter
    protected ServiceFactory serviceFactory;

    /**
     * Execute command.
     *
     * @param request  http request
     * @param response http response
     * @return the response entity
     * @throws IOException         the io exception
     * @throws PersistentException datasource
     */
    public abstract ResponseEntity execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, PersistentException;
}
