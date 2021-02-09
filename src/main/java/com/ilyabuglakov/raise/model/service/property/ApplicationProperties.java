package com.ilyabuglakov.raise.model.service.property;

import com.ilyabuglakov.raise.model.service.property.exception.PropertyFileException;
import lombok.extern.log4j.Log4j2;

/**
 * The type Application properties. Holder for application.properties
 */
@Log4j2
public class ApplicationProperties {

    private final PropertyParser propertyParser;

    private static class InstanceHolder {
        /**
         * The constant INSTANCE.
         */
        public static ApplicationProperties INSTANCE;

        static {
            try {
                INSTANCE = new ApplicationProperties();
            } catch (PropertyFileException e) {
                log.info("Application works without initialisation from property file");
            }
        }
    }

    private ApplicationProperties() throws PropertyFileException {
        propertyParser = new PropertyParser("application.properties");
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ApplicationProperties getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Gets property.
     *
     * @param propertyName the property name
     * @return the property
     */
    public static String getProperty(String propertyName) {
        return getInstance().propertyParser.getProperty(propertyName);
    }

}
