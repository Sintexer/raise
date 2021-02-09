package com.ilyabuglakov.raise.command.manager;

import com.ilyabuglakov.raise.command.Command;
import com.ilyabuglakov.raise.dal.exception.PersistentException;
import com.ilyabuglakov.raise.model.response.ResponseEntity;
import com.ilyabuglakov.raise.model.service.domain.factory.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type Database command manager.
 * <p>
 * Creates Command manager with Database service factory
 */
public class DatabaseCommandManager implements CommandManager {

    private final ServiceFactory serviceFactory;

    /**
     * Instantiates a new Database command manager.
     *
     * @param serviceFactory the service factory
     */
    public DatabaseCommandManager(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    @Override
    public ResponseEntity execute(Command command, HttpServletRequest request, HttpServletResponse response)
            throws PersistentException, IOException {
        command.setServiceFactory(serviceFactory);
        return command.execute(request, response);
    }

    @Override
    public void close() {
        serviceFactory.close();
    }

    @Override
    public void rollback() {
        serviceFactory.rollback();
    }
}
