package org.example.dao;

import org.example.util.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class BaseDAO<T> {
    protected final Logger logger;

    protected BaseDAO() {
        this.logger = LoggerFactory.getLogger(getClass());
    }

    protected void executeUpdate(String sql, PreparedStatementSetter setter) throws SQLException {
        logger.debug("Executing update SQL: {}", sql);
        TransactionManager.executeInTransaction(connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                setter.setParameters(stmt);
                stmt.executeUpdate();
                return null;
            }
        });
    }

    protected <R> R executeQuery(String sql, PreparedStatementSetter setter, ResultSetMapper<R> mapper) throws SQLException {
        logger.debug("Executing query SQL: {}", sql);
        return TransactionManager.executeInTransaction(connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                setter.setParameters(stmt);
                try (ResultSet rs = stmt.executeQuery()) {
                    return mapper.mapResultSet(rs);
                }
            }
        });
    }

    protected List<T> executeQueryList(String sql, PreparedStatementSetter setter) throws SQLException {
        return executeQuery(sql, setter, rs -> {
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(extractFromResultSet(rs));
            }
            return results;
        });
    }

    protected Optional<T> executeQuerySingle(String sql, PreparedStatementSetter setter) throws SQLException {
        return executeQuery(sql, setter, rs -> {
            if (rs.next()) {
                return Optional.of(extractFromResultSet(rs));
            }
            return Optional.empty();
        });
    }

    protected abstract T extractFromResultSet(ResultSet rs) throws SQLException;

    @FunctionalInterface
    protected interface PreparedStatementSetter {
        void setParameters(PreparedStatement stmt) throws SQLException;
    }

    @FunctionalInterface
    protected interface ResultSetMapper<R> {
        R mapResultSet(ResultSet rs) throws SQLException;
    }

    protected void setUUID(PreparedStatement stmt, int index, UUID value) throws SQLException {
        if (value == null) {
            stmt.setNull(index, Types.VARCHAR);
        } else {
            stmt.setString(index, value.toString());
        }
    }

    protected UUID getUUID(ResultSet rs, String columnName) throws SQLException {
        String uuid = rs.getString(columnName);
        return uuid == null ? null : UUID.fromString(uuid);
    }
} 
