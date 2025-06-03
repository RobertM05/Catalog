package org.example.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static HikariDataSource dataSource;

    static {
        try {
            logger.info("Initializing database connection pool...");
            HikariConfig config = new HikariConfig();

            String jdbcUrl = System.getProperty("db.url", "jdbc:mysql://localhost:3306/catalog");
            String username = System.getProperty("db.username", "root");
            String password = System.getProperty("db.password", "");

            logger.debug("Database URL: {}", jdbcUrl);
            logger.debug("Database username: {}", username);

            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            config.setIdleTimeout(300000);
            config.setConnectionTimeout(20000);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
            logger.info("Database connection pool obtained successfully.");
        } catch (Exception e) {
            logger.error("Failed to obtain database connection: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to obtain database connection pool", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = dataSource.getConnection();
            logger.debug("Connection obtained successfully.");
            return connection;
        } catch (SQLException e) {
            logger.error("Failed to obtain database connection: {}", e.getMessage(), e);
            throw e;
        }
    }
}