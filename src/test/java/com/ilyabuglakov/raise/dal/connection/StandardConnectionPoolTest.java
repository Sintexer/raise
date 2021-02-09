package com.ilyabuglakov.raise.dal.connection;

import com.ilyabuglakov.raise.dal.connection.pool.ConnectionPool;
import com.ilyabuglakov.raise.dal.connection.pool.ConnectionPoolFactory;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Log4j2
public class StandardConnectionPoolTest {

    ConnectionPool connectionPool = ConnectionPoolFactory.getConnectionPool();
    private static final String driver = "org.postgresql.Driver";
    private static final String url = "jdbc:postgresql://localhost:5432/raise_db_test";
    private static final String username = "postgres";
    private static final String password = "admin";

    @Test
    public void testConnection() throws SQLException {
        connectionPool.init(driver, url, username, password, 2, 10, 0);
        Connection connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM usr order by id");
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        if (resultSet.next())
            log.info(resultSet.getString("role"));
        connection.close();
        connectionPool.destroy();
    }

}