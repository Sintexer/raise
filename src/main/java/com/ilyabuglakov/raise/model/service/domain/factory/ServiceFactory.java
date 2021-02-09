package com.ilyabuglakov.raise.model.service.domain.factory;

import com.ilyabuglakov.raise.model.service.domain.Service;
import com.ilyabuglakov.raise.model.service.domain.ServiceType;

/**
 * The interface Service factory. Service factory creates services and initializes them.
 */
public interface ServiceFactory {

    /**
     * Create service.
     *
     * @param serviceType the service type
     * @return the service
     */
    Service createService(ServiceType serviceType);

    /**
     * Close.
     */
    void close();

    /**
     * Rollback.
     */
    void rollback();
}
