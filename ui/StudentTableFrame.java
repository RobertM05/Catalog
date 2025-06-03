package org.example.ui;

import org.example.dao.StudentDAO;
import org.example.model.Student;
import org.example.util.CatalogExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class StudentTableFrame extends BaseTableFrame {
    private static final Logger logger = LoggerFactory.getLogger(StudentTableFrame.class);
    private static final String[] COLUMN_NAMES = {"ID", "Nume", "Prenume", "Email", "Grupa"};
    private final StudentDAO studentDAO;

    public StudentTableFrame() {
        super("Lista Studentilor", COLUMN_NAMES);
        this.studentDAO = new StudentDAO();
        setSize(1000, 600);
    }

    @Override
    protected JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        
        JButton adaugaNotaBtn = new JButton("Adauga Nota");
        JButton vizualizareNoteBtn = new JButton("Vizualizare Note");
        JButton vizualizareMediiBtn = new JButton("Vizualizare Medii");
        JButton editStudentBtn = new JButton("Editeaza Student");
        JButton stergeStudentBtn = new JButton("Sterge Student");
        JButton exportaBtn = new JButton("Exporta Date");
        
        editStudentBtn.setBackground(new Color(255, 193, 7));
        editStudentBtn.setForeground(Color.BLACK);
        editStudentBtn.setFont(new Font("Arial", Font.BOLD, 12));
        editStudentBtn.setBorder(BorderFactory.createLineBorder(new Color(215, 153, 7), 2));
        editStudentBtn.setOpaque(true);
        editStudentBtn.setFocusPainted(false);

        editStudentBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                editStudentBtn.setBackground(new Color(215, 153, 7));
                editStudentBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                editStudentBtn.setBackground(new Color(255, 193, 7));
                editStudentBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        stergeStudentBtn.setBackground(new Color(220, 53, 69));
        stergeStudentBtn.setForeground(Color.WHITE);
        stergeStudentBtn.setFont(new Font("Arial", Font.BOLD, 12));
        stergeStudentBtn.setBorder(BorderFactory.createLineBorder(new Color(180, 43, 59), 2));
        stergeStudentBtn.setOpaque(true);
        stergeStudentBtn.setFocusPainted(false);

        stergeStudentBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                stergeStudentBtn.setBackground(new Color(180, 43, 59));
                stergeStudentBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                stergeStudentBtn.setBackground(new Color(220, 53, 69));
                stergeStudentBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        adaugaNotaBtn.addActionListener(e -> adaugaNotaPentruStudentSelectat());
        vizualizareNoteBtn.addActionListener(e -> vizualizeazaNotePentruStudentSelectat());
        vizualizareMediiBtn.addActionListener(e -> vizualizeazaMediiPentruStudentSelectat());
        editStudentBtn.addActionListener(e -> editeazaStudentSelectat());
        stergeStudentBtn.addActionListener(e -> stergeStudentSelectat());
        exportaBtn.addActionListener(e -> exportaDate());
        
        buttonPanel.add(adaugaNotaBtn);
        buttonPanel.add(vizualizareNoteBtn);
        buttonPanel.add(vizualizareMediiBtn);
        buttonPanel.add(editStudentBtn);
        buttonPanel.add(stergeStudentBtn);
        buttonPanel.add(exportaBtn);
        
        return buttonPanel;
    }

    @Override
    protected void loadData() {
        try {
            clearTable();
            List<Student> studenti = studentDAO.selectAllStudents();
            
            for(Student s: studenti) {
                tableModel.addRow(new Object[]{
                    s.getId().toString(),
                    s.getNume(),
                    s.getPrenume(),
                    s.getEmail(),
                    s.getGrupa()
                });
            }
            logger.info("Au fost incarcati {} studenti in tabel", studenti.size());
        } catch (SQLException ex) {
            showError("Eroare la incarcarea listei de studenti: " + ex.getMessage());
        }
    }

    private void adaugaNotaPentruStudentSelectat() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Va rugam sa selectati un student!");
            return;
        }

        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        String numeComplet = tableModel.getValueAt(selectedRow, 1) + " " + tableModel.getValueAt(selectedRow, 2);
        
        AdaugaNotaStudentFrame frame = new AdaugaNotaStudentFrame(UUID.fromString(studentId), numeComplet);
        frame.setVisible(true);
    }

    private void vizualizeazaNotePentruStudentSelectat() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Va rugam sa selectati un student!");
            return;
        }

        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        String numeComplet = tableModel.getValueAt(selectedRow, 1) + " " + tableModel.getValueAt(selectedRow, 2);
        
        VizualizareNoteDetaliiFrame frame = new VizualizareNoteDetaliiFrame(UUID.fromString(studentId), numeComplet);
        frame.setVisible(true);
    }

    private void vizualizeazaMediiPentruStudentSelectat() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Va rugam sa selectati un student!");
            return;
        }

        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        String numeComplet = tableModel.getValueAt(selectedRow, 1) + " " + tableModel.getValueAt(selectedRow, 2);
        
        VizualizareMediiDetaliiFrame frame = new VizualizareMediiDetaliiFrame(
            UUID.fromString(studentId), 
            numeComplet
        );
        frame.setVisible(true);
    }

    private void stergeStudentSelectat() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Va rugam sa selectati un student!");
            return;
        }

        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        String numeComplet = tableModel.getValueAt(selectedRow, 1) + " " + tableModel.getValueAt(selectedRow, 2);

        int result = JOptionPane.showConfirmDialog(
            this,
            "Sunteti sigur ca doriti sa stergeti studentul " + numeComplet + "?\n" +
            "Aceasta actiune va sterge si toate notele asociate studentului!",
            "Confirmare Stergere",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            try {
                studentDAO.stergeStudent(UUID.fromString(studentId));
                showInfo("Studentul a fost sters cu succes!");
                loadData();
            } catch (SQLException ex) {
                showError("Eroare la stergerea studentului: " + ex.getMessage());
            }
        }
    }

    private void exportaDate() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvare fisier CSV");
            fileChooser.setSelectedFile(new File("catalog_export.csv"));
            
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                new CatalogExporter().exportToCSV(file.getAbsolutePath());
                showInfo("Datele au fost exportate cu succes in: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            showError("Eroare la exportul datelor: " + e.getMessage());
        }
    }

    private void editeazaStudentSelectat() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Va rugam sa selectati un student!");
            return;
        }

        try {
            String studentId = (String) tableModel.getValueAt(selectedRow, 0);
            Student student = studentDAO.getStudentDupaId(UUID.fromString(studentId));
            if (student != null) {
                deschideFormularEditare(student);
            } else {
                showError("Studentul selectat nu a fost gasit in baza de date!");
            }
        } catch (SQLException ex) {
            showError("Eroare la incarcarea studentului: " + ex.getMessage());
        }
    }

    private void deschideFormularEditare(Student student) {
        JDialog dialog = new JDialog(this, "Editeaza Student", true);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JTextField numeField = new JTextField(student.getNume());
        JTextField prenumeField = new JTextField(student.getPrenume());
        JTextField emailField = new JTextField(student.getEmail());
        JTextField grupaField = new JTextField(student.getGrupa());

        JButton salveazaBtn = new JButton("Salveaza");
        salveazaBtn.addActionListener(e -> {
            try {
                if (!validateAllFields(numeField, prenumeField, emailField, grupaField)) {
                    return;
                }

                if (!emailField.getText().trim().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    showError("Adresa de email nu este valida!");
                    return;
                }

                Student s = new Student(
                    student.getId(),
                    numeField.getText().trim(),
                    prenumeField.getText().trim(),
                    emailField.getText().trim(),
                    grupaField.getText().trim()
                );

                studentDAO.actualizeazaStudent(s);
                showInfo("Studentul a fost actualizat cu succes!");
                dialog.dispose();
                loadData();
            } catch (Exception ex) {
                showError("Eroare la actualizarea studentului: " + ex.getMessage());
            }
        });

        dialog.add(new JLabel("Nume:"));
        dialog.add(numeField);
        dialog.add(new JLabel("Prenume:"));
        dialog.add(prenumeField);
        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);
        dialog.add(new JLabel("Grupa:"));
        dialog.add(grupaField);
        dialog.add(new JLabel());
        dialog.add(salveazaBtn);

        dialog.setVisible(true);
    }
}