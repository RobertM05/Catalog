package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class CatalogFrame extends JFrame {
    public CatalogFrame() {
        setTitle("Catalog Universitar");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JButton adaugaStudentBtn = new JButton("Adauga Student");
        JButton studentiTableBtn = new JButton("Lista Studenti");
        JButton disciplineBtn = new JButton("Gestioneaza Discipline");
        JButton logoutBtn = new JButton("Deconectare");

        Dimension buttonSize = new Dimension(250, 50);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        configureButton(adaugaStudentBtn, buttonSize, buttonFont);
        configureButton(studentiTableBtn, buttonSize, buttonFont);
        configureButton(disciplineBtn, buttonSize, buttonFont);
        configureButton(logoutBtn, buttonSize, buttonFont);

        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.BLACK);
        logoutBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        logoutBtn.setOpaque(true);
        logoutBtn.setFocusPainted(false);

        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(200, 35, 51));
                logoutBtn.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(220, 53, 69));
                logoutBtn.setForeground(Color.BLACK);
            }
        });

        adaugaStudentBtn.addActionListener(e -> {
            AdaugaStudentFrame frame = new AdaugaStudentFrame();
            frame.setVisible(true);
        });

        studentiTableBtn.addActionListener(e -> {
            StudentTableFrame frame = new StudentTableFrame();
            frame.setVisible(true);
        });
        
        disciplineBtn.addActionListener(e -> {
            DisciplinaFrame frame = new DisciplinaFrame();
            frame.setVisible(true);
        });

        logoutBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                this,
                "Sunteti sigur ca doriti sa va deconectati?",
                "Confirmare Deconectare",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (result == JOptionPane.YES_OPTION) {
                LoginFrame.logout();
                dispose();
                new LoginFrame();
            }
        });

        buttonPanel.add(Box.createVerticalStrut(10));
        addButtonWithSpacing(buttonPanel, studentiTableBtn);
        addButtonWithSpacing(buttonPanel, adaugaStudentBtn);
        addButtonWithSpacing(buttonPanel, disciplineBtn);
        addButtonWithSpacing(buttonPanel, logoutBtn);
        buttonPanel.add(Box.createVerticalStrut(10));

        JLabel titleLabel = new JLabel("Catalog Universitar", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel userLabel = new JLabel("Utilizator: " + LoginFrame.getCurrentUser().getUsername(),
                                    SwingConstants.RIGHT);
        userLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        mainPanel.add(userLabel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(buttonPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void configureButton(JButton button, Dimension size, Font font) {
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
        button.setFont(font);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void addButtonWithSpacing(JPanel panel, JButton button) {
        panel.add(button);
        panel.add(Box.createVerticalStrut(20));
    }
}
