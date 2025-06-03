package org.example.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public abstract class BaseTableFrame extends BaseFrame {
    protected JTable table;
    protected DefaultTableModel tableModel;
    protected JTextField searchField;
    protected static final Color SUCCESS_COLOR = new Color(144, 238, 144);
    protected static final Color ERROR_COLOR = new Color(255, 182, 193);
    protected final JPanel mainPanel;

    public BaseTableFrame(String title, String[] columnNames) {
        super(title, 1, 1);
        mainPanel = new JPanel(new BorderLayout(10, 10));
        setContentPane(mainPanel);
        
        initializeTable(columnNames);
        setupSearchPanel();
        setupMainPanel();
        
        SwingUtilities.invokeLater(this::loadData);
    }

    private void initializeTable(String[] columnNames) {
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        
        // Setare renderer implicit pentru celule
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        
        // Permite sortarea
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
    }

    private void setupSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });
        
        searchPanel.add(new JLabel("Cauta:"));
        searchPanel.add(searchField);
        mainPanel.add(searchPanel, BorderLayout.NORTH);
    }

    private void setupMainPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = createButtonPanel();
        if (buttonPanel != null) {
            centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        }
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }

    protected void filter() {
        String searchText = searchField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
        
        if (searchText.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    protected void clearTable() {
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
    }

    protected void setCellRenderer(int column, DefaultTableCellRenderer renderer) {
        table.getColumnModel().getColumn(column).setCellRenderer(renderer);
    }

    protected abstract JPanel createButtonPanel();
    protected abstract void loadData();
} 