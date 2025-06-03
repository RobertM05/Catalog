package org.example.dao;

import org.example.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Optional;

public class UserDAO extends BaseDAO<User> {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    public Optional<User> authenticate(String username, String password) throws SQLException {
        logger.debug("Incercare autentificare pentru utilizator: {}", username);
        
        return executeQuerySingle(
            "SELECT * FROM users WHERE username = ?",
            stmt -> stmt.setString(1, username)
        ).filter(user -> BCrypt.checkpw(password, user.getPassword()));
    }

    @Override
    protected User extractFromResultSet(ResultSet rs) throws SQLException {
        return new User(
            getUUID(rs, "id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("role")
        );
    }
}
