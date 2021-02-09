package com.ilyabuglakov.raise.dal.connection.pool;

import com.ilyabuglakov.raise.dal.connection.ConnectionProxy;

/**
 * The interface Connection pool.
 * <p>
 * Connection pool creates connections and manages access to them. It helps not to create new connections every time,
 * but reuse already created connections. <p>Thread safe</p>
 */
public interface ConnectionPool {

    /**
     * Init.
     *
     * @param driverClass            the driver class
     * @param url                    the database url
     * @param user                   the user
     * @param password               the password
     * @param initPoolSize           the init pool size
     * @param maxPoolSize            the max pool size
     * @param checkConnectionTimeout the check connection timeout
     */
    void init(String driverClass, String url, String user, String password,
              int initPoolSize, int maxPoolSize, int checkConnectionTimeout);

    /**
     * Gets connection.
     *
     * @return the connection
     */
    ConnectionProxy getConnection();

    /**
     * Release connection boolean.
     *
     * @param connection the connection
     */
    void releaseConnection(ConnectionProxy connection);

    /**
     * Destroy. Will close all connections.
     */
    void destroy();

}
