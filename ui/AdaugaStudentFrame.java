package org.example.ui;

import org.example.dao.StudentDAO;
import org.example.model.Student;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class AdaugaStudentFrame extends BaseFrame {
    private final JTextField numeField;
    private final JTextField prenumeField;
    private final JTextField emailField;
    private final JTextField grupaField;
    private final StudentDAO studentDAO;

    public AdaugaStudentFrame() {
        super("Adauga Student", 6, 2);
        this.studentDAO = new StudentDAO();
        setSize(400, 300);

        numeField = new JTextField();
        prenumeField = new JTextField();
        emailField = new JTextField();
        grupaField = new JTextField();

        add(new JLabel("Nume:"));
        add(numeField);
        add(new JLabel("Prenume:"));
        add(prenumeField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Grupa:"));
        add(grupaField);

        JButton salveazaBtn = new JButton("Salveaza");
        salveazaBtn.addActionListener(e -> salveazaStudent());

        add(new JLabel());
        add(salveazaBtn);
    }

    private void salveazaStudent() {
        try {
            if (!validateAllFields(numeField, prenumeField, emailField, grupaField)) {
                return;
            }

            Student student = new Student(
                UUID.randomUUID(),
                numeField.getText().trim(),
                prenumeField.getText().trim(),
                emailField.getText().trim(),
                grupaField.getText().trim()
            );

            studentDAO.adaugaStudent(student);
            
            showInfo("Studentul a fost adaugat cu succes!");
            
            clearFields(numeField, prenumeField, emailField, grupaField);
            
        } catch (Exception ex) {
            showError("Eroare la salvarea studentului: " + ex.getMessage());
        }
    }
} 