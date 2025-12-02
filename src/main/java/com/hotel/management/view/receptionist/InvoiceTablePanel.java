package com.hotel.management.view.receptionist;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class InvoiceTablePanel extends JPanel {

    private BufferedImage backgroundImage;
    private DefaultTableModel model;
    private JTable table;

    public InvoiceTablePanel() {
        // Load Background Image
        try {
            URL imageUrl = getClass().getResource("/reception.jpg");
            if (imageUrl != null) {
                backgroundImage = ImageIO.read(imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        setOpaque(false);
        setLayout(new BorderLayout());

        // -------------------- TOP PANEL --------------------
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel title = new JLabel("📄 View All Payments", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(50, 50, 50)); // professional dark color
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // -------------------- TABLE --------------------
        String[] columns = {
                "Invoice ID", "Guest Name", "Room Number",
                "Total Amount", "Payment Method", "Payment Status", "Date Issued"
        };

        // Pre-saved 4 values
        Object[][] data = {
                {"INV-001", "John Silva", "118 – Standard", "Rs. 22,500", "Credit Card", "Paid", "18/11/2025"},
                {"INV-002", "Maya Perera", "302 – Deluxe", "Rs. 35,800", "Cash", "Paid", "17/11/2025"},
                {"INV-003", "Hiruni Jayasinghe", "501 – Suite", "Rs. 72,000", "Credit Card", "Paid", "17/11/2025"},
                {"INV-004", "Kevin Rodrigo", "207 – Superior", "Rs. 28,400", "Debit Card", "Pending", "18/11/2025"}
        };

        model = new DefaultTableModel(data, columns);
        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(212, 160, 0));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        // -------------------- SEARCH & FILTER --------------------
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        JTextField searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchPanel.add(searchField);

        JButton searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchBtn.setBackground(new Color(212, 160, 0));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.setOpaque(true);
        searchPanel.add(searchBtn);

        add(searchPanel, BorderLayout.SOUTH);

        // Search action
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim().toLowerCase();
            filterTable(keyword);
        });
    }

    // -------------------- FILTER METHOD --------------------
    private void filterTable(String keyword) {
        DefaultTableModel newModel = new DefaultTableModel(
                new Object[]{"Invoice ID", "Guest Name", "Room Number",
                        "Total Amount", "Payment Method", "Payment Status", "Date Issued"}, 0);

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean matches = false;
            for (int j = 0; j < model.getColumnCount(); j++) {
                String cell = model.getValueAt(i, j).toString().toLowerCase();
                if (cell.contains(keyword)) {
                    matches = true;
                    break;
                }
            }
            if (matches) {
                newModel.addRow(new Object[]{
                        model.getValueAt(i, 0),
                        model.getValueAt(i, 1),
                        model.getValueAt(i, 2),
                        model.getValueAt(i, 3),
                        model.getValueAt(i, 4),
                        model.getValueAt(i, 5),
                        model.getValueAt(i, 6)
                });
            }
        }

        table.setModel(newModel);
    }

    // -------------------- ADD INVOICE --------------------
    public void addInvoice(String invoiceID, String guestName, String roomNumber, int nights,
                           double total, String paymentMethod, String paymentStatus, String dateIssued) {
        model.addRow(new Object[]{
                invoiceID,
                guestName,
                roomNumber,
                nights,
                "Rs. " + String.format("%,.2f", total),
                paymentMethod,
                paymentStatus,
                dateIssued
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
