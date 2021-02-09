package com.ilyabuglakov.raise.storage;

import com.ilyabuglakov.raise.model.service.property.PropertyParser;
import com.ilyabuglakov.raise.model.service.property.exception.PropertyCantInitException;
import com.ilyabuglakov.raise.model.service.property.exception.PropertyFileException;
import lombok.Getter;

/**
 * The type Properties storage. Stores information from properties files
 */
@Getter
public class PropertiesStorage {

    private final PropertyParser pages;
    private final PropertyParser links;

    private static class InstanceHolder {
        /**
         * The constant INSTANCE.
         */
        public static PropertiesStorage INSTANCE = new PropertiesStorage();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static PropertiesStorage getInstance() {
        return PropertiesStorage.InstanceHolder.INSTANCE;
    }

    private PropertiesStorage() {
        pages = initProperties("pages.properties");
        pages.setPrefix("/template");
        links = initProperties("links.properties");
    }

    private PropertyParser initProperties(String filename) {
        try {
            return new PropertyParser(filename);
        } catch (PropertyFileException e) {
            throw new PropertyCantInitException(e);
        }
    }

}
