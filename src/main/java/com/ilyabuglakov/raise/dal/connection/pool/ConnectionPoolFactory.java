package com.ilyabuglakov.raise.dal.connection.pool;

/**
 * The type Connection pool factory. Singleton.
 */
public class ConnectionPoolFactory {

    private final ConnectionPool connectionPool;

    private ConnectionPoolFactory() {
        connectionPool = new StandardConnectionPool();
    }

    private static class InstanceHolder {
        /**
         * The constant INSTANCE.
         */
        public static ConnectionPoolFactory INSTANCE = new ConnectionPoolFactory();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ConnectionPoolFactory getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Gets connection pool.
     *
     * @return the connection pool
     */
    public static ConnectionPool getConnectionPool() {
        return getInstance().connectionPool;
    }

    /**
     * Close. Will destroy connection pool
     */
    public static void close() {
        getConnectionPool().destroy();
    }

}
