package com.hotel.management.view.admin;

import com.hotel.management.model.GuestActivity;
import com.hotel.management.service.GuestActivityManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * GuestActivityPanel (Option B) - View + Add guest activities
 * Use null-layout style if you prefer; this implementation uses absolute positioning which
 * matches the typical style in your project.
 */
public class GuestActivityPanel extends JPanel {

    private final GuestActivityManagement backend;
    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JTextField txtGuestId;
    private final JComboBox<String> cmbType;
    private final JTextField txtDescription;

    public GuestActivityPanel(GuestActivityManagement backend) {
        this.backend = backend;
        setLayout(null);

        JLabel title = new JLabel("Guest Activity");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBounds(20, 10, 400, 30);
        add(title);

        // --- add form area (left)
        JLabel lblGuestId = new JLabel("Guest ID:");
        lblGuestId.setForeground(Color.WHITE);
        lblGuestId.setBounds(20, 60, 80, 25);
        add(lblGuestId);

        txtGuestId = new JTextField();
        txtGuestId.setBounds(100, 60, 160, 25);
        add(txtGuestId);

        JLabel lblType = new JLabel("Type:");
        lblType.setForeground(Color.WHITE);
        lblType.setBounds(20, 100, 80, 25);
        add(lblType);

        cmbType = new JComboBox<>(new String[] {"Check-in", "Check-out", "Purchase", "Complaint", "Other"});
        cmbType.setBounds(100, 100, 160, 25);
        add(cmbType);

        JLabel lblDesc = new JLabel("Description:");
        lblDesc.setForeground(Color.WHITE);
        lblDesc.setBounds(20, 140, 80, 25);
        add(lblDesc);

        txtDescription = new JTextField();
        txtDescription.setBounds(100, 140, 400, 25);
        add(txtDescription);

        JButton btnAdd = new JButton("Add Activity");
        btnAdd.setBounds(100, 180, 140, 30);
        add(btnAdd);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBounds(260, 180, 100, 30);
        add(btnRefresh);

        btnAdd.addActionListener(e -> onAddActivity());
        btnRefresh.addActionListener(e -> refreshTable());

        // --- table area (right / below)
        String[] cols = {"ID", "Guest ID", "Type", "Description", "Timestamp"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 230, 880, 320);
        add(sp);

        // delete button
        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.setBounds(20, 560, 150, 30);
        add(btnDelete);
        btnDelete.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel == -1) {
                JOptionPane.showMessageDialog(this, "Select a row to delete.");
                return;
            }
            int id = Integer.parseInt(tableModel.getValueAt(sel, 0).toString());
            boolean ok = backend.removeActivity(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Deleted activity id=" + id);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete id=" + id);
            }
        });

        // initial load
        refreshTable();

        // set background to match dashboard theme if needed
        setBackground(new Color(26, 26, 26));
    }

    private void onAddActivity() {
        String guestId = txtGuestId.getText().trim();
        String type = (String) cmbType.getSelectedItem();
        String desc = txtDescription.getText().trim();

        if (guestId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Guest ID is required");
            return;
        }
        if (desc.isEmpty()) {
            int res = JOptionPane.showConfirmDialog(this, "Description is empty. Continue?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (res != JOptionPane.YES_OPTION) return;
        }

        backend.addActivity(guestId, type, desc);
        txtDescription.setText("");
        txtGuestId.setText("");
        JOptionPane.showMessageDialog(this, "Activity added");
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<GuestActivity> rows = backend.getAllActivities();
        for (GuestActivity a : rows) {
            tableModel.addRow(new Object[] {
                    a.getId(),
                    a.getGuestId(),
                    a.getType(),
                    a.getDescription(),
                    GuestActivityManagement.formatDate(a.getTimestamp())
            });
        }
    }
}
