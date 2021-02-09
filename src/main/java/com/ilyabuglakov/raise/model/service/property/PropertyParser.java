package com.ilyabuglakov.raise.model.service.property;

import com.ilyabuglakov.raise.model.service.path.PathService;
import com.ilyabuglakov.raise.model.service.property.exception.PropertyFileException;
import lombok.Getter;
import lombok.Setter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The type Property parser.
 */
public class PropertyParser {

    private final Properties properties;

    @Getter
    @Setter
    private String prefix = "";

    /**
     * Instantiates a new Property parser.
     *
     * @param propertyFileName the property file name
     * @throws PropertyFileException the property file exception
     */
    public PropertyParser(String propertyFileName) throws PropertyFileException {
        try (InputStream inputStream =
                     new FileInputStream(PathService.getInstance().getResourcePath(propertyFileName))) {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            throw new PropertyFileException(e);
        }
    }

    /**
     * Gets property.
     *
     * @param propertyName the property name
     * @return the property
     */
    public String getProperty(String propertyName) {
        return prefix + properties.getProperty(propertyName);
    }
}
