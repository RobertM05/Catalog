package org.example.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public abstract class BaseFrame extends JFrame {
    protected final Logger logger;
    protected static final int DEFAULT_WIDTH = 300;
    protected static final int DEFAULT_HEIGHT = 200;
    protected static final int DEFAULT_GAP = 5;
    protected final JPanel contentPane;

    public BaseFrame(String title, int rows, int cols) {
        this.logger = LoggerFactory.getLogger(getClass());
        
        setTitle(title);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(rows, cols, DEFAULT_GAP, DEFAULT_GAP));
        setContentPane(contentPane);
    }

    protected void showError(String message) {
        logger.error(message);
        JOptionPane.showMessageDialog(this,
                message,
                "Eroare",
                JOptionPane.ERROR_MESSAGE);
    }

    protected void showInfo(String message) {
        logger.info(message);
        JOptionPane.showMessageDialog(this,
                message,
                "Informatie",
                JOptionPane.INFORMATION_MESSAGE);
    }

    protected void showWarning(String message) {
        logger.warn(message);
        JOptionPane.showMessageDialog(this,
                message,
                "Atentie",
                JOptionPane.WARNING_MESSAGE);
    }

    protected boolean validateAllFields(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                showWarning("Toate campurile sunt obligatorii!");
                return false;
            }
        }
        return true;
    }

    protected void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }
} 