package com.hotel.management.view.receptionist;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ReceptionistCheckins extends JFrame {

    // === GOLD BUTTON CLASS ===
class GoldButton extends JButton {

    public GoldButton(String text) {
        super(text);

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);  // We paint the button manually
        setForeground(Color.BLACK);   // Text color

        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setBackground(new Color(255, 180, 60));  // GOLD
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill rounded rectangle
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

        super.paintComponent(g2);
        g2.dispose();
    }
}

    // === BACKGROUND PANEL FIXED ===
    class BackgroundPanel extends JPanel {
        private Image img;

        public BackgroundPanel(String imgPath) {
            java.net.URL imgURL = getClass().getResource(imgPath);
            if (imgURL != null) {
                img = new ImageIcon(imgURL).getImage();
            } else {
                System.out.println("⚠ Image not found: " + imgPath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // === MAIN UI CONSTRUCTOR ===
    public ReceptionistCheckins() {

        setTitle("Check IN");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === SIDEBAR ===
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 700));
        sidebar.setLayout(null);
        sidebar.setBackground(new Color(20, 20, 20)); // dark background

        // === LOGO ===
        JLabel logo = new JLabel();
        java.net.URL logoURL = getClass().getResource("/images/logo.png");
        if (logoURL != null) {
            logo.setIcon(new ImageIcon(logoURL));
        }
        logo.setBounds(50, 20, 120, 100);
        sidebar.add(logo);

        // --- GOLD BUTTONS ---
        sidebar.add(createSidebarButton("Dashboard", 140));
        sidebar.add(createSidebarButton("Settings and Profile", 200));
        sidebar.add(createSidebarButton("Exams", 260));
        sidebar.add(createSidebarButton("Features  NEW", 600));

        add(sidebar, BorderLayout.WEST);

        // === MAIN BACKGROUND ===
        BackgroundPanel mainPanel = new BackgroundPanel("/images/1303755c519208ef762b4077b243707e.jpg");
        mainPanel.setLayout(null);

        JLabel title = new JLabel("Today’s Check-Ins");
        title.setFont(new Font("Serif", Font.BOLD, 38));
        title.setForeground(new Color(255, 215, 0));
        title.setBounds(360, 25, 500, 40);
        mainPanel.add(title);

        // === TABLE ===
        String[] columns = {"Guest Name", "ID / Passport No.", "Contact Number",
                "Room Number / Room Type", "Check-In Date & Time", "No. of Guests", "Status"};

        Object[][] data = {
                {"Maya Perera", "N1234567", "077 458 3254", "302 – Deluxe", "17/11/2025 2:30 PM", "2 Guests", "Checked-In"},
                {"John Silva", "901254789V", "071 225 8941", "118 – Standard", "17/11/2025 11:00 AM", "1 Guest", "Checked-In"},
                {"Hiruni Jayasinghe", "N4423568", "075 663 2104", "501 – Suite", "17/11/2025 6:45 PM", "3 Guests", "Upcoming"},
                {"Kevin Rodrigo", "200534789V", "076 889 3441", "207 – Superior", "18/11/2025 1:00 PM", "2 Guests", "Upcoming"},
                {"Amaya Fernando", "N8845261", "078 556 9032", "412 – Premium", "18/11/2025 3:15 PM", "1 Guest", "Upcoming"},
                {"Shun Tanaka", "P91234567", "+81 90 5522 3344", "620 – Executive", "17/11/2025 10:10 PM", "2 Guests", "Checked-In"}
        };

        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(100, 350, 840, 3000);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);

        mainPanel.add(sp);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // === GOLD SIDEBAR BUTTON CREATOR ===
    private JButton createSidebarButton(String text, int y) {
        GoldButton b = new GoldButton(text);
        b.setBounds(20, y, 180, 45);

        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b.setForeground(Color.BLACK);
            }
        });

        return b;
    }

    // === MAIN METHOD ===
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReceptionistCheckins::new);
    }
}
