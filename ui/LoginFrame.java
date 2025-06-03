package org.example.ui;

import org.example.model.User;
import org.example.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.JOptionPane;
import java.awt.*;
import java.sql.SQLException;

public class LoginFrame extends JFrame{
    private static final Logger logger = LoggerFactory.getLogger(LoginFrame.class);
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private static User currentUser;

    public LoginFrame(){
        setTitle("Autentificare Catalog");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,200);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"),gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField = new JTextField(15);
        add(usernameField,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password:"),gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        passwordField = new JPasswordField(15);
        add(passwordField,gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        add(loginButton,gbc);

        getRootPane().setDefaultButton(loginButton);

        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Va rugam completati toate campurile!");
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();
            userDAO.authenticate(username, password)
                    .ifPresentOrElse(
                            user -> {
                                currentUser = user;
                                logger.info("Utilzator autentificat: {}", user.getUsername());
                                openMainApplication();
                            },
                            () -> {
                                logger.warn("Autentificare esuata pentru: {}", username);
                                JOptionPane.showMessageDialog(this,
                                        "Username sau parola incorecta!",
                                        "Eroare Autentificare",
                                        JOptionPane.ERROR_MESSAGE);
                                passwordField.setText("");
                            }
                    );
        } catch (SQLException ex) {
            logger.error("Eroare la autentificare: {}", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this,
                    "Eroare la conectarea la baza de date: " + ex.getMessage(),
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
        private void openMainApplication(){
            SwingUtilities.invokeLater(() -> {
                try {
                    new CatalogFrame();
                    this.dispose();
                } catch (Exception ex) {
                    logger.error("Eroare la deschiderea aplicatiei principale: {}", ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this,
                            "Eroare la deschiderea aplicatiei principale: {}" + ex.getMessage(),
                            "Eroare",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        public static User getCurrentUser() {
            return currentUser;
        }

    public static void logout() {
            currentUser = null;
        }
    }

