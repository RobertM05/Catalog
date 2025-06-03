package org.example.ui;

import org.example.dao.DisciplinaDAO;
import org.example.dao.NotaDAO;
import org.example.model.Disciplina;
import org.example.model.Nota;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class VizualizareNoteDetaliiFrame extends BaseTableFrame {
    private static final String[] COLUMN_NAMES = {"Disciplina", "Nota", "Data Notare"};
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private final UUID studentId;
    private final NotaDAO notaDAO;
    private final DisciplinaDAO disciplinaDAO;

    public VizualizareNoteDetaliiFrame(UUID studentId, String numeStudent) {
        super("Note pentru " + numeStudent, COLUMN_NAMES);
        this.studentId = studentId;
        this.notaDAO = new NotaDAO();
        this.disciplinaDAO = new DisciplinaDAO();
        
        setSize(800, 500);
        setupCellRenderer();
    }

    private void setupCellRenderer() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (column == 1 && value != null) {
                    int nota = Integer.parseInt(value.toString());
                    c.setBackground(nota >= 5 ? SUCCESS_COLOR : ERROR_COLOR);
                }
                
                return c;
            }
        };
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        setCellRenderer(1, renderer);
    }

    @Override
    protected JPanel createButtonPanel() {
        return null; // No buttons needed for this frame
    }

    @Override
    protected void loadData() {
        try {
            clearTable();
            List<Nota> note = notaDAO.getNoteStudent(studentId);
            
            if (note.isEmpty()) {
                showInfo("Nu exista note inregistrate pentru acest student");
                return;
            }

            Map<UUID, List<Nota>> noteGrupate = note.stream()
                .collect(Collectors.groupingBy(Nota::getDisciplinaId));

            List<Disciplina> discipline = new ArrayList<>();
            for (UUID disciplinaId : noteGrupate.keySet()) {
                discipline.add(disciplinaDAO.getDisciplinaDupaId(disciplinaId));
            }
            discipline.sort(Comparator.comparing(Disciplina::getNume));

            for (Disciplina disciplina : discipline) {
                List<Nota> noteDisciplina = noteGrupate.get(disciplina.getId());
                noteDisciplina.sort(Comparator.comparing(Nota::getDataNotarii).reversed());
                
                for (Nota nota : noteDisciplina) {
                    tableModel.addRow(new Object[]{
                        disciplina.getNume(),
                        nota.getNota(),
                        nota.getDataNotarii().format(formatter)
                    });
                }
            }

        } catch (SQLException ex) {
            showError("Eroare la incarcarea notelor: " + ex.getMessage());
        }
    }
} 