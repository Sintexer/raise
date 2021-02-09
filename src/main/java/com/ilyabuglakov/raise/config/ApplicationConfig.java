package com.ilyabuglakov.raise.config;

import com.ilyabuglakov.raise.config.exception.PoolConfigurationException;
import com.ilyabuglakov.raise.dal.connection.pool.ConnectionPoolFactory;
import com.ilyabuglakov.raise.model.service.property.ApplicationProperties;
import com.ilyabuglakov.raise.model.service.property.PropertyParser;
import com.ilyabuglakov.raise.model.service.property.exception.PropertyFileException;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;

/**
 * The type Application config.
 * <p>
 * Configures main application classes.
 */
@Log4j2
public class ApplicationConfig {

    @Getter
    private final PropertyParser linksProperties;

    private ApplicationConfig() throws PropertyFileException {
        linksProperties = new PropertyParser("pages.properties");
    }

    private static class InstanceHolder {
        /**
         * The constant INSTANCE.
         */
        public static ApplicationConfig INSTANCE;

        static {
            try {
                INSTANCE = new ApplicationConfig();
            } catch (PropertyFileException e) {
                log.error("Can't init pages.properties parser");
            }
        }
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ApplicationConfig getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Init connection pool by parameters from application.properties.
     *
     * @throws PoolConfigurationException the pool configuration exception
     */
    public static void initConnectionPool() throws PoolConfigurationException {
        try {
            String dbDriver = ApplicationProperties.getProperty("db.driver.name");
            String dbUrl = ApplicationProperties.getProperty("db.url");
            String dbUser = ApplicationProperties.getProperty("db.user.name");
            String dbPassword = ApplicationProperties.getProperty("db.user.password");
            String dbInitPoolSizeProperty = ApplicationProperties.getProperty("connectionPool.size.init");
            String dbMaxPoolSizeProperty = ApplicationProperties.getProperty("connectionPool.size.max");
            String dbCheckConnectionTimeoutProperty = ApplicationProperties.getProperty("connectionPool.checkConnection.timeout");

            if (ObjectUtils.anyNull(dbDriver, dbUrl, dbUser, dbPassword))
                throw new PoolConfigurationException("Any of required db init fields is null");

            int dbInitPoolSize;
            int dbMaxPoolSize;
            int dbCheckConnectionTimeout;

            if (dbInitPoolSizeProperty == null)
                dbInitPoolSize = 10;
            else
                dbInitPoolSize = Integer.parseInt(dbInitPoolSizeProperty);

            if (dbMaxPoolSizeProperty == null)
                dbMaxPoolSize = dbInitPoolSize;
            else {
                dbMaxPoolSize = Integer.parseInt(dbMaxPoolSizeProperty);
                if (dbMaxPoolSize < dbInitPoolSize)
                    throw new PoolConfigurationException("Max pool size is lesser, than init size");
            }

            if (dbCheckConnectionTimeoutProperty == null) {
                dbCheckConnectionTimeout = 0;
            } else
                dbCheckConnectionTimeout = Integer.parseInt(dbCheckConnectionTimeoutProperty);

            ConnectionPoolFactory.getConnectionPool().init(
                    dbDriver,
                    dbUrl,
                    dbUser,
                    dbPassword,
                    dbInitPoolSize,
                    dbMaxPoolSize,
                    dbCheckConnectionTimeout
            );
        } catch (NumberFormatException e) {
            throw new PoolConfigurationException("Wrong pool properties format");
        }
    }

    /**
     * Close connection pool.
     */
    public static void closeConnectionPool() {
        ConnectionPoolFactory.close();
    }

}
