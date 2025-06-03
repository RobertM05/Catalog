package org.example.dao;

import org.example.model.User;
import org.example.util.TransactionManager;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    public Optional<User> authenticate(String username, String password) throws SQLException {
        logger.debug("Incercare autentificare pentru utilizator: {}", username);
        String sql = "SELECT * FROM users WHERE username = ?";

        return TransactionManager.executeInTransaction(connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String hashedPassword = rs.getString("password");
                        if (BCrypt.checkpw(password, hashedPassword)) {
                            User user = extractUserFromResultSet(rs);
                            logger.info("Autentificare reusita pentru utilizator: {}", username);
                            return Optional.of(user);
                        }
                    }
                    logger.warn("Autentificare esuata pentru utilizator: {}", username);
                    return Optional.empty();
                }
            }
        });
    }

    public void createUser(User user, String plainPassword) throws SQLException {
        logger.debug("Creare utilizator nou: {}", user.getUsername());
        String sql = "INSERT INTO users (id, username, password, nume, prenume, email, role) VALUES (?, ?, ?, ?, ?, ?, ?)";

        TransactionManager.executeInTransaction(connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, user.getId().toString());
                stmt.setString(2, user.getUsername());
                stmt.setString(3, BCrypt.hashpw(plainPassword, BCrypt.gensalt()));
                stmt.setString(4, user.getNume());
                stmt.setString(5, user.getPrenume());
                stmt.setString(6, user.getEmail());
                stmt.setString(7, user.getRole());

                stmt.executeUpdate();
                logger.info("Utilizator creat cu succes: {}", user.getUsername());
                return null;
            }
        });
    }

    public void updatePassword(UUID userId, String newPassword) throws SQLException {
        logger.debug("Actualizare parola pentru utilizator ID: {}", userId);
        String sql = "UPDATE users SET password = ? WHERE id = ?";

        TransactionManager.executeInTransaction(connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
                stmt.setString(2, userId.toString());
                
                int rowsAffected = stmt.executeUpdate();
                logger.info("Parola actualizata pentru utilizator ID: {}. Randuri afectate: {}", userId, rowsAffected);
                return null;
            }
        });
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(
            UUID.fromString(rs.getString("id")),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("nume"),
            rs.getString("prenume"),
            rs.getString("email"),
            rs.getString("role")
        );
    }
} 