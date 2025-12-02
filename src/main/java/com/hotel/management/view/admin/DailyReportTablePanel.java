package com.hotel.management.view.admin;

import com.hotel.management.model.FinancialRecord;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class DailyReportTablePanel extends JPanel {

    private BufferedImage backgroundImage;
    private final DefaultTableModel model;
    private final JTable table;
    private final List<FinancialRecord> records;

    // --- UPDATED COLUMN HEADERS ---
    private static final String[] COLUMNS = {
            "Date", "Type", "Category", "Amount (Rs.)",
            "Client/Vendor", "Ref. ID", "Paid Via", "Description"
    };


    public DailyReportTablePanel() {
        // Load Background Image
        try {
            final URL imageUrl = getClass().getResource("/images/1303755c519208ef762b4077b243707e.jpg");
            if (imageUrl != null) {
                backgroundImage = ImageIO.read(imageUrl);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }

        records = new ArrayList<>();
        records.add(new FinancialRecord("28/11/2025", "Revenue", "Invoicing", 22500.00,
                "Client: Hotel Chain | Ref: INV-001 | Tax: 10% | Via: Bank Transfer | Note: Initial payment for services"));
        records.add(new FinancialRecord("28/11/2025", "Expense", "Salary", 15000.00,
                "Vendor: Staffing Agency | Ref: P-1125 | Tax: 0% | Via: Check | Note: Monthly Staff Payout"));

        setOpaque(false);
        setLayout(new BorderLayout());

        //  TOP PANEL
        final JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        final JLabel title = new JLabel("💰 Daily Financial Reports", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(255, 180, 60));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        //  TABLE
        model = new DefaultTableModel(COLUMNS, 0);
        loadTableData();

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(212, 160, 0));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        javax.swing.table.TableColumnModel columnModel = table.getColumnModel();

        // Define widths (in pixels)
        columnModel.getColumn(0).setPreferredWidth(80);
        columnModel.getColumn(1).setPreferredWidth(70);
        columnModel.getColumn(2).setPreferredWidth(120);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(150);
        columnModel.getColumn(5).setPreferredWidth(90);
        columnModel.getColumn(6).setPreferredWidth(100);
        columnModel.getColumn(7).setPreferredWidth(350);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }


    private String getFieldValue(String description, String key) {
        // Searches for "key: value |" pattern
        String search = key + ": ";
        int start = description.indexOf(search);
        if (start == -1) return "";

        start += search.length();
        int end = description.indexOf(" | ", start);
        if (end == -1) {
            end = description.length();
            if(key.equals("Note")) return description.substring(start).trim();
        }
        return description.substring(start, end).trim();
    }


    private void loadTableData() {
        model.setRowCount(0);
        for (final FinancialRecord record : records) {
            String fullDesc = record.getDescription();

            // --- PARSING EXTENDED FIELDS ---
            String clientVendor = getFieldValue(fullDesc, record.getType().equals("Revenue") ? "Client" : "Vendor");
            String refId = getFieldValue(fullDesc, "Ref");
            String paidVia = getFieldValue(fullDesc, "Via");
            String note = getFieldValue(fullDesc, "Note");

            model.addRow(new Object[]{
                    record.getDate(),
                    record.getType(),
                    record.getCategory(),
                    String.format("%,.2f", record.getAmount()),
                    clientVendor,
                    refId,
                    paidVia,
                    note
            });
        }
    }


    public void addRecord(final FinancialRecord record) {
        records.add(record);

        String fullDesc = record.getDescription();
        String clientVendor = getFieldValue(fullDesc, record.getType().equals("Revenue") ? "Client" : "Vendor");
        String refId = getFieldValue(fullDesc, "Ref");
        String paidVia = getFieldValue(fullDesc, "Via");
        String note = getFieldValue(fullDesc, "Note");

        model.addRow(new Object[]{
                record.getDate(),
                record.getType(),
                record.getCategory(),
                String.format("%,.2f", record.getAmount()),
                clientVendor,
                refId,
                paidVia,
                note
        });
    }


    public List<FinancialRecord> getRecords() {
        return records;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
    }
}