package com.ilyabuglakov.raise.command.manager;

import com.ilyabuglakov.raise.model.service.domain.factory.ServiceFactory;

/**
 * The type Command manager factory.
 * <p>
 * Used to create Command managers
 */
public class CommandManagerFactory {

    /**
     * Gets command manager.
     *
     * @param serviceFactory the service factory
     * @return the command manager
     */
    public static CommandManager getCommandManager(ServiceFactory serviceFactory) {
        return new DatabaseCommandManager(serviceFactory);
    }

}
