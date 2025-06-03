package org.example.ui;

import org.example.dao.DisciplinaDAO;
import org.example.dao.NotaDAO;
import org.example.model.Disciplina;
import org.example.model.Nota;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class VizualizareMediiDetaliiFrame extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(VizualizareMediiDetaliiFrame.class);
    private final NotaDAO notaDAO;
    private final DisciplinaDAO disciplinaDAO;
    private final UUID studentId;
    private final String numeStudent;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public VizualizareMediiDetaliiFrame(UUID studentId, String numeStudent) {
        this.studentId = studentId;
        this.numeStudent = numeStudent;
        this.notaDAO = new NotaDAO();
        this.disciplinaDAO = new DisciplinaDAO();

        setTitle("Medii Student: " + numeStudent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        String[] columnNames = {"Disciplina", "Medie", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(300);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (column == 1 || column == 2) {
                    double medie = Double.parseDouble(tableModel.getValueAt(row, 1).toString());
                    if (medie < 5.0) {
                        c.setForeground(new Color(220, 53, 69));
                    } else {
                        c.setForeground(new Color(40, 167, 69));
                    }
                } else {
                    c.setForeground(table.getForeground());
                }
                
                return c;
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel medieGeneralaLabel = new JLabel();
        medieGeneralaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        medieGeneralaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(medieGeneralaLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(mainPanel);

        loadData(medieGeneralaLabel);
    }

    private void loadData(JLabel medieGeneralaLabel) {
        try {
            while (tableModel.getRowCount() > 0) {
                tableModel.removeRow(0);
            }

            List<Nota> note = notaDAO.getNoteStudent(studentId);
            
            Map<UUID, List<Nota>> notePerDisciplina = note.stream()
                .collect(Collectors.groupingBy(Nota::getDisciplinaId));
            
            double sumaMediaTotala = 0.0;
            int numarDiscipline = 0;

            for (Map.Entry<UUID, List<Nota>> entry : notePerDisciplina.entrySet()) {
                Disciplina disciplina = disciplinaDAO.getDisciplinaDupaId(entry.getKey());
                if (disciplina == null) continue;

                double medie = entry.getValue().stream()
                    .mapToInt(Nota::getNota)
                    .average()
                    .orElse(0.0);
                
                String status = medie >= 5.0 ? "Promovat" : "Nepromovat";
                
                tableModel.addRow(new Object[]{
                    disciplina.getNume(),
                    String.format("%.2f", medie),
                    status
                });

                sumaMediaTotala += medie;
                numarDiscipline++;
            }

            if (numarDiscipline > 0) {
                double medieGenerala = sumaMediaTotala / numarDiscipline;
                String statusGeneral = medieGenerala >= 5.0 ? "Promovat" : "Nepromovat";
                Color culoareStatus = medieGenerala >= 5.0 ? 
                    new Color(40, 167, 69) :
                    new Color(220, 53, 69);
                
                medieGeneralaLabel.setText(String.format("Media generala: %.2f - %s", 
                    medieGenerala, statusGeneral));
                medieGeneralaLabel.setForeground(culoareStatus);
            } else {
                medieGeneralaLabel.setText("Nu exista note inregistrate");
                medieGeneralaLabel.setForeground(Color.BLACK);
            }

            logger.info("Au fost calculate mediile pentru studentul {}", numeStudent);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Eroare la incarcarea mediilor: " + ex.getMessage(),
                "Eroare",
                JOptionPane.ERROR_MESSAGE
            );
            logger.error("Eroare la calcularea mediilor pentru studentul {}: {}", 
                numeStudent, ex.getMessage());
        }
    }
} 