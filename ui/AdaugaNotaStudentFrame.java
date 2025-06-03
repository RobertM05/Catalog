package org.example.ui;

import org.example.dao.DisciplinaDAO;
import org.example.dao.NotaDAO;
import org.example.model.Disciplina;
import org.example.model.Nota;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AdaugaNotaStudentFrame extends BaseFrame {
    private final UUID studentId;
    private final String numeStudent;
    private JComboBox<Disciplina> disciplinaComboBox;
    private JTextField notaField;

    public AdaugaNotaStudentFrame(UUID studentId, String numeStudent) {
        super("Adauga Nota pentru " + numeStudent, 4, 2);
        this.studentId = studentId;
        this.numeStudent = numeStudent;
        setSize(400, DEFAULT_HEIGHT);

        initializeComponents();
        loadDiscipline();
        setVisible(true);
    }

    private void initializeComponents() {
        disciplinaComboBox = new JComboBox<>();
        notaField = new JTextField();

        JButton salveazaBtn = new JButton("Salveaza");
        salveazaBtn.addActionListener(e -> salveazaNota());

        add(new JLabel("Student:"));
        add(new JLabel(numeStudent));
        add(new JLabel("Disciplina:"));
        add(disciplinaComboBox);
        add(new JLabel("Nota:"));
        add(notaField);
        add(new JLabel());
        add(salveazaBtn);
    }

    private void loadDiscipline() {
        try {
            List<Disciplina> discipline = new DisciplinaDAO().getAllDiscipline();
            for (Disciplina d : discipline) {
                disciplinaComboBox.addItem(d);
            }
        } catch (Exception e) {
            showError("Eroare la incarcarea disciplinelor: " + e.getMessage());
        }
    }

    private void salveazaNota() {
        try {
            if (!validateAllFields(notaField)) {
                return;
            }

            int notaVal = Integer.parseInt(notaField.getText().trim());
            if (notaVal < 1 || notaVal > 10) {
                showWarning("Nota trebuie sa fie intre 1 si 10!");
                return;
            }

            Disciplina disciplinaSelectata = (Disciplina) disciplinaComboBox.getSelectedItem();
            if (disciplinaSelectata == null) {
                showWarning("Va rugam selectati o disciplina!");
                return;
            }

            Nota nota = new Nota(
                    UUID.randomUUID(),
                    studentId,
                    disciplinaSelectata.getId(),
                    notaVal,
                    LocalDateTime.now()
            );

            new NotaDAO().adaugaNota(nota);
            showInfo("Nota a fost adaugata cu succes!");
            clearFields(notaField);
            
        } catch (NumberFormatException e) {
            showWarning("Va rugam introduceti un numar valid pentru nota!");
        } catch (Exception e) {
            showError("Eroare la salvarea notei: " + e.getMessage());
        }
    }
} 
