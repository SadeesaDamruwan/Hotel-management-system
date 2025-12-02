package com.hotel.management.view.receptionist;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class GR extends JFrame {

    private Image backgroundImage;
    private DefaultTableModel tableModel;

    public GR() {

        // Load background image
        backgroundImage = new ImageIcon(getClass().getResource("/reseption.png")).getImage();

        setTitle("Guest Requests");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ---------- BACKGROUND PANEL ----------
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        bgPanel.setLayout(null);
        setContentPane(bgPanel);

        // ---------- TITLE ----------
        JLabel title = new JLabel("Guest Requests");
        title.setFont(new Font("Serif", Font.BOLD, 42));
        title.setForeground(new Color(212, 175, 55)); // GOLD
        title.setBounds(430, 40, 500, 60);
        bgPanel.add(title);

        // ---------- WHITE PANEL (TABLE AREA) ----------
        JPanel tableContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 120)); // semi-transparent black
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 35, 35));
            }
        };

        tableContainer.setBounds(100, 300, 1000, 340);
        tableContainer.setOpaque(false);
        tableContainer.setLayout(null);
        bgPanel.add(tableContainer);

        // ---------- TABLE ----------
        String[] columns = {"Request ID", "Guest Name", "Room", "Type", "Description", "Request Time", "Priority", "Assigned Staff", "Status"};
        Object[][] data = {
                {"R001", "Alice Smith", "101", "Housekeeping", "Extra Towel", "10:00 AM", "Normal", "John", "Pending"},
                {"R002", "Bob Johnson", "102", "Food", "Coffee", "11:30 AM", "Urgent", "Mary", "Completed"},
                {"R003", "Charlie Lee", "103", "Housekeeping", "Room Cleaning", "12:15 PM", "Normal", "Alex", "Pending"}
        };

        tableModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(tableModel);

        // Scroll pane for table with updated height
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 80, 960, 200); // Updated size
        tableContainer.add(scrollPane);

        // ---------- NEW REQUEST BUTTON ----------
        JButton newRequestBtn = new JButton("+ New Request");
        newRequestBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        newRequestBtn.setBackground(new Color(212, 175, 55)); // GOLD
        newRequestBtn.setForeground(Color.BLACK);
        newRequestBtn.setFocusPainted(false);
        newRequestBtn.setBounds(720, 20, 160, 38);
        tableContainer.add(newRequestBtn);

        // ---------- BUTTON ACTION ----------
        newRequestBtn.addActionListener(e -> {
            // Add a new row with empty/default values
            tableModel.addRow(new Object[]{"R" + String.format("%03d", tableModel.getRowCount() + 1),
                    "New Guest", "", "", "", "HH:MM", "Normal", "", "Pending"});
        });
    }

    public static void main(String[] args) {
        new GR().setVisible(true);
    }
}
