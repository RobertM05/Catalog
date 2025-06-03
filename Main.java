package org.example;

import org.example.ui.LoginFrame;
import org.example.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting Catalog Application...");


        try {
            logger.debug("Testing database connection....");
            Connection testConnection = DatabaseConnection.getConnection();
            testConnection.close();
            logger.info("Database connection test succesful");
        } catch (SQLException e) {
            logger.error("Failed to connect to database: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(null,
                    "Failed to connect to database: " + e.getMessage(),
                    "Database error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                logger.debug("Initializing login frame....");
                new LoginFrame();
                logger.info("Application UI initialized succesfully");
            } catch (Exception e) {
                logger.error("Failed to initialize UI: {}", e.getMessage(),e);
                JOptionPane.showMessageDialog(null,
                        "Failed to initialize application UI: " + e.getMessage(),
                        "Application error",
                        JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
            }
        });
    }
}