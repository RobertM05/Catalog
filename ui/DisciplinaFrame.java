package org.example.ui;

import org.example.dao.DisciplinaDAO;
import org.example.model.Disciplina;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DisciplinaFrame extends BaseTableFrame {
    private static final Logger logger = LoggerFactory.getLogger(DisciplinaFrame.class);
    private static final String[] COLUMN_NAMES = {"ID", "Nume", "Acronim", "Tip Evaluare"};
    private final DisciplinaDAO disciplinaDAO;

    public DisciplinaFrame() {
        super("Lista Discipline", COLUMN_NAMES);
        this.disciplinaDAO = new DisciplinaDAO();
        setSize(800, 500);
    }

    @Override
    protected JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        
        JButton adaugaBtn = new JButton("Adauga Disciplina");
        JButton editBtn = new JButton("Editeaza Disciplina");
        JButton stergeBtn = new JButton("Sterge Disciplina");
        
        editBtn.setBackground(new Color(255, 193, 7));
        editBtn.setForeground(Color.BLACK);
        editBtn.setFont(new Font("Arial", Font.BOLD, 12));
        editBtn.setBorder(BorderFactory.createLineBorder(new Color(215, 153, 7), 2));
        editBtn.setOpaque(true);
        editBtn.setFocusPainted(false);

        editBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                editBtn.setBackground(new Color(215, 153, 7));
                editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                editBtn.setBackground(new Color(255, 193, 7));
                editBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        stergeBtn.setBackground(new Color(220, 53, 69));
        stergeBtn.setForeground(Color.WHITE);
        stergeBtn.setFont(new Font("Arial", Font.BOLD, 12));
        stergeBtn.setBorder(BorderFactory.createLineBorder(new Color(180, 43, 59), 2));
        stergeBtn.setOpaque(true);
        stergeBtn.setFocusPainted(false);

        stergeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                stergeBtn.setBackground(new Color(180, 43, 59));
                stergeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                stergeBtn.setBackground(new Color(220, 53, 69));
                stergeBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        adaugaBtn.addActionListener(e -> deschideFormularAdaugare());
        editBtn.addActionListener(e -> deschideFormularEditare());
        stergeBtn.addActionListener(e -> stergeDisciplinaSelectata());
        
        buttonPanel.add(adaugaBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(stergeBtn);
        
        return buttonPanel;
    }

    @Override
    protected void loadData() {
        try {
            clearTable();
            List<Disciplina> discipline = disciplinaDAO.getAllDiscipline();
            
            for(Disciplina d : discipline) {
                tableModel.addRow(new Object[]{
                    d.getId().toString(),
                    d.getNume(),
                    d.getAcronim(),
                    d.getTipEvaluare()
                });
            }
            logger.info("Au fost incarcate {} discipline in tabel", discipline.size());
        } catch (SQLException ex) {
            showError("Eroare la incarcarea listei de discipline: " + ex.getMessage());
        }
    }

    private void deschideFormularAdaugare() {
        deschideFormularDisciplina(null);
    }

    private void deschideFormularEditare() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Va rugam sa selectati o disciplina!");
            return;
        }

        try {
            String disciplinaId = (String) tableModel.getValueAt(selectedRow, 0);
            Disciplina disciplina = disciplinaDAO.getDisciplinaDupaId(UUID.fromString(disciplinaId));
            if (disciplina != null) {
                deschideFormularDisciplina(disciplina);
            } else {
                showError("Disciplina selectata nu a fost gasita in baza de date!");
            }
        } catch (SQLException ex) {
            showError("Eroare la incarcarea disciplinei: " + ex.getMessage());
        }
    }

    private void deschideFormularDisciplina(Disciplina disciplinaExistenta) {
        String titluDialog = disciplinaExistenta == null ? "Adauga Disciplina" : "Editeaza Disciplina";
        JDialog dialog = new JDialog(this, titluDialog, true);
        dialog.setLayout(new GridLayout(5, 2, 5, 5));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JTextField numeField = new JTextField();
        JTextField acronimField = new JTextField();
        JTextField tipEvaluareField = new JTextField();

        if (disciplinaExistenta != null) {
            numeField.setText(disciplinaExistenta.getNume());
            acronimField.setText(disciplinaExistenta.getAcronim());
            tipEvaluareField.setText(disciplinaExistenta.getTipEvaluare());
        }

        JButton salveazaBtn = new JButton("Salveaza");
        salveazaBtn.addActionListener(e -> {
            try {
                if (!validateAllFields(numeField, acronimField, tipEvaluareField)) {
                    return;
                }

                Disciplina d = disciplinaExistenta == null ? 
                    new Disciplina(
                        UUID.randomUUID(),
                        numeField.getText().trim(),
                        acronimField.getText().trim(),
                        tipEvaluareField.getText().trim()
                    ) :
                    new Disciplina(
                        disciplinaExistenta.getId(),
                        numeField.getText().trim(),
                        acronimField.getText().trim(),
                        tipEvaluareField.getText().trim()
                    );

                if (disciplinaExistenta == null) {
                    disciplinaDAO.adaugaDisciplina(d);
                    showInfo("Disciplina a fost adaugata cu succes!");
                } else {
                    disciplinaDAO.actualizeazaDisciplina(d);
                    showInfo("Disciplina a fost actualizata cu succes!");
                }
                
                dialog.dispose();
                loadData();
            } catch (Exception ex) {
                String operatie = disciplinaExistenta == null ? "salvarea" : "actualizarea";
                showError("Eroare la " + operatie + " disciplinei: " + ex.getMessage());
            }
        });

        dialog.add(new JLabel("Nume:"));
        dialog.add(numeField);
        dialog.add(new JLabel("Acronim:"));
        dialog.add(acronimField);
        dialog.add(new JLabel("Tip Evaluare:"));
        dialog.add(tipEvaluareField);
        dialog.add(new JLabel());
        dialog.add(salveazaBtn);

        dialog.setVisible(true);
    }

    private void stergeDisciplinaSelectata() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Va rugam sa selectati o disciplina!");
            return;
        }

        String disciplinaId = (String) tableModel.getValueAt(selectedRow, 0);
        String numeDisciplina = (String) tableModel.getValueAt(selectedRow, 1);
        String acronim = (String) tableModel.getValueAt(selectedRow, 2);

        int result = JOptionPane.showConfirmDialog(
            this,
            "Sunteti sigur ca doriti sa stergeti disciplina " + numeDisciplina + " (" + acronim + ")?\n" +
            "Aceasta actiune va sterge si toate notele asociate disciplinei!",
            "Confirmare Stergere",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            try {
                disciplinaDAO.stergeDisciplina(UUID.fromString(disciplinaId));
                showInfo("Disciplina a fost stearsa cu succes!");
                loadData();
            } catch (SQLException ex) {
                showError("Eroare la stergerea disciplinei: " + ex.getMessage());
            }
        }
    }
}
