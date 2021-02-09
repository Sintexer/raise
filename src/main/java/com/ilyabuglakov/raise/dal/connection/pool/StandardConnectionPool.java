package com.ilyabuglakov.raise.dal.connection.pool;

import com.ilyabuglakov.raise.dal.connection.ConnectionProxy;
import com.ilyabuglakov.raise.dal.connection.exception.ThreadPoolLimitException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The type Standard connection pool.
 */
public class StandardConnectionPool implements ConnectionPool {

    private static final Logger logger = LogManager.getLogger(StandardConnectionPool.class);

    private String url;
    private String user;
    private String password;
    private int maxPoolSize;

    private BlockingQueue<ConnectionProxy> connections;
    private Set<ConnectionProxy> busyConnections;

    private final Lock lock = new ReentrantLock();

    @Override
    public void init(String driverClass, String url, String user, String password, int initPoolSize, int maxPoolSize,
                     int checkConnectionTimeout) {
        lock.lock();
        try {
            Class.forName(driverClass);

            this.url = url;
            this.user = user;
            this.password = password;
            this.maxPoolSize = maxPoolSize;
            connections = new ArrayBlockingQueue<>(maxPoolSize);
            busyConnections = new CopyOnWriteArraySet<>();
            for (int i = 0; i < initPoolSize; ++i) {
                connections.add(createConnection());
            }
        } catch (ClassNotFoundException e) {
            logger.fatal("Cant init thread pool", e);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private ConnectionProxy createConnection() throws SQLException {
        return new ConnectionProxy(DriverManager.getConnection(url, user, password));
    }

    @Override
    public ConnectionProxy getConnection() {
        ConnectionProxy connection = null;
        lock.lock();
        while (connection == null) {
            try {
                if (!connections.isEmpty()) {
                    connection = connections.poll();
                } else if (busyConnections.size() < maxPoolSize) {
                    connection = createConnection();
                } else {
                    logger.error("The limit of database connections has been reached");
                    throw new ThreadPoolLimitException("Reached limit of database connections");
                }
            } catch (SQLException e) {
                logger.error("SQL exception while creating connection", e);
            }
        }
        busyConnections.add(connection);
        logger.info(() -> String.format("Connection taken, freeConnections: %d, busyConnections: %d",
                connections.size(), busyConnections.size()));
        lock.unlock();
        return connection;
    }

    @Override
    public void releaseConnection(ConnectionProxy connection) {
        lock.lock();
        try {
            connection.clearWarnings();
            connection.setAutoCommit(true);
            busyConnections.remove(connection);
            connections.put(connection);
            logger.info(() -> String.format("Connection released, freeConnections: %d, busyConnections: %d",
                    connections.size(), busyConnections.size()));
        } catch (SQLException | InterruptedException e) {
            logger.error("Error while releasing connection", e);
            try {
                connection.getConnection().close();
            } catch (SQLException ex) {
                logger.error("Error while closing connection", e);
            }
        }
        lock.unlock();
    }

    @Override
    public void destroy() {
        lock.lock();
        busyConnections.addAll(connections);
        connections.clear();
        for (ConnectionProxy connection : busyConnections) {
            try {
                connection.getConnection().close();
            } catch (SQLException e) {
                logger.error("Error while closing connection", e);
            }
        }
        lock.unlock();
    }

    @Override
    protected void finalize() throws Throwable {
        destroy();
    }
}
