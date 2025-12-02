package com.hotel.management.view.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ViewReportsPanel extends JPanel {

    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color DARK_BG = new Color(30, 30, 30);
    private final Color PANEL_BG = new Color(40, 40, 40);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color FIELD_BORDER_COLOR = GOLD_COLOR.darker().darker(); // Darker gold for fields

    public ViewReportsPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- Title ---
        JLabel titleLabel = new JLabel("View Reports");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(GOLD_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // --- Tabbed Pane for Report Types ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setForeground(TEXT_COLOR); // Fixed: Ensure tab headers are visible white
        tabbedPane.setBackground(PANEL_BG);
        tabbedPane.setOpaque(false);

        // Custom styling for tabs
        // Note: UIManager properties are tricky with different LaF. We ensure text is visible.
        UIManager.put("TabbedPane.contentAreaColor", PANEL_BG);
        UIManager.put("TabbedPane.selectedBackground", GOLD_COLOR);
        UIManager.put("TabbedPane.shadow", DARK_BG);
        UIManager.put("TabbedPane.darkShadow", DARK_BG);
        UIManager.put("TabbedPane.light", DARK_BG);
        UIManager.put("TabbedPane.highlight", DARK_BG);
        UIManager.put("TabbedPane.tabAreaBackground", DARK_BG);
        // Ensure selected text color is black for contrast against GOLD_COLOR selected background
        UIManager.put("TabbedPane.selectedForeground", Color.BLACK);


        // 1. Occupancy Report
        tabbedPane.addTab("Occupancy & Status", createOccupancyReport());

        // 2. Revenue Report
        tabbedPane.addTab("Revenue & Pricing", createRevenueReport());

        // 3. Staff Efficiency Report
        tabbedPane.addTab("Staff Efficiency", createStaffEfficiencyReport());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // --- Report Panel Builders ---

    private JPanel createOccupancyReport() {
        JPanel panel = createStyledReportPanel();
        panel.setLayout(new GridLayout(3, 1, 15, 15));

        // Filter options (UPDATED to use Date Picker)
        panel.add(createReportFilter("Filter by Date Range:", true));

        // Key Metrics
        panel.add(createMetricBox("Current Occupancy Rate", "75%", new Color(100, 255, 100)));
        panel.add(createMetricBox("Rooms Needing Service", "12 Units", new Color(255, 100, 100)));

        return panel;
    }

    private JPanel createRevenueReport() {
        JPanel panel = createStyledReportPanel();
        panel.setLayout(new GridLayout(3, 1, 15, 15));

        // Filter options (UPDATED to use styled ComboBox)
        panel.add(createReportFilter("Filter by Quarter:", false));

        // Key Metrics
        panel.add(createMetricBox("Total Revenue (YTD)", "$950,000", GOLD_COLOR));
        panel.add(createMetricBox("Average Daily Rate (ADR)", "$185.50", new Color(100, 200, 255)));

        return panel;
    }

    private JPanel createStaffEfficiencyReport() {
        JPanel panel = createStyledReportPanel();
        panel.setLayout(new GridLayout(3, 1, 15, 15));

        // Filter options (UPDATED to use styled ComboBox)
        panel.add(createReportFilter("Filter by Role:", false));

        // Key Metrics
        panel.add(createMetricBox("Avg Cleaning Time", "35 Minutes", new Color(200, 200, 255)));
        panel.add(createMetricBox("Maintenance Tickets Open", "3 Open", new Color(255, 150, 100)));

        return panel;
    }

    // --- General Styling Components ---

    private JPanel createStyledReportPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL_BG);
        panel.setForeground(TEXT_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        return panel;
    }

    private JPanel createMetricBox(String title, String value, Color color) {
        JPanel box = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        box.setBackground(DARK_BG);
        box.setBorder(BorderFactory.createLineBorder(color, 2));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(TEXT_COLOR);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);

        box.setLayout(new BorderLayout());

        // Content container to hold title and value
        JPanel content = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        content.setOpaque(false);
        content.add(titleLabel);
        content.add(valueLabel);

        box.add(content, BorderLayout.CENTER);
        box.setBorder(new EmptyBorder(10, 15, 10, 15));

        return box;
    }

    private JPanel createReportFilter(String labelText, boolean isDateRange) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(label);

        if (isDateRange) {
            // Replaced JTextField with DatePicker functionality
            JPanel startDatePanel = new JPanel(new BorderLayout());
            startDatePanel.setOpaque(false);
            addDatePicker(startDatePanel);

            JPanel endDatePanel = new JPanel(new BorderLayout());
            endDatePanel.setOpaque(false);
            addDatePicker(endDatePanel);

            panel.add(startDatePanel);
            panel.add(new JLabel("-"));
            panel.add(endDatePanel);
        } else {
            JComboBox<String> selector;
            if (labelText.contains("Role")) {
                selector = createStyledComboBox(new String[]{"All Staff", "Housekeeping", "Maintenance"});
            } else {
                selector = createStyledComboBox(new String[]{"Q1 2024", "Q2 2024", "Q3 2024"});
            }
            panel.add(selector);
        }

        JButton generate = new JButton("Generate Report");
        generate.setBackground(GOLD_COLOR.darker());
        generate.setForeground(Color.BLACK);
        generate.setFocusPainted(false);
        panel.add(generate);

        return panel;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setOpaque(false);
        comboBox.setBackground(DARK_BG); // Sets background for the component itself

        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton();
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setContentAreaFilled(false);
                btn.setFocusPainted(false);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

                btn.setIcon(new Icon() {
                    @Override
                    public void paintIcon(Component c, Graphics g, int x, int y) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(GOLD_COLOR);
                        int w = 8; int h = 5;
                        int mx = c.getWidth() / 2 - w/2;
                        int my = c.getHeight() / 2 - h/2;
                        int[] xPoints = {mx, mx + w, mx + w / 2};
                        int[] yPoints = {my, my, my + h};
                        g2.fillPolygon(xPoints, yPoints, 3);
                        g2.dispose();
                    }
                    @Override
                    public int getIconWidth() { return 10; }
                    @Override
                    public int getIconHeight() { return 10; }
                });
                return btn;
            }

            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popup = new BasicComboPopup(comboBox) {
                    @Override
                    protected JScrollPane createScroller() {
                        JScrollPane scroller = super.createScroller();
                        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                        return scroller;
                    }
                };
                // Style popup
                popup.setBorder(BorderFactory.createLineBorder(GOLD_COLOR, 1));
                popup.setBackground(DARK_BG);
                return popup;
            }
        });

        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (index == -1) {
                    // Displayed Item in ComboBox field
                    setOpaque(false);
                    setForeground(TEXT_COLOR);
                    setBackground(DARK_BG); // Try to set dark background for transparency effect
                } else {
                    // Dropdown List Item
                    setOpaque(true);
                    if (isSelected) {
                        setBackground(GOLD_COLOR);
                        setForeground(Color.BLACK);
                    } else {
                        setBackground(DARK_BG); // Dark background for list items
                        setForeground(Color.WHITE);
                    }
                }
                setBorder(new EmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
        return comboBox;
    }

    // --- Calendar Implementation (Copied from AddStaffPanel) ---

    private void addDatePicker(JPanel parent) {
        // The field that displays the selected date
        final JTextField dateField = new JTextField("DD/MM/YYYY") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        dateField.setEditable(false);
        dateField.setOpaque(false);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateField.setForeground(TEXT_COLOR);
        dateField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        dateField.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // The Calendar Popup
        JPopupMenu calendarPopup = new JPopupMenu();
        calendarPopup.setBorder(BorderFactory.createLineBorder(GOLD_COLOR));
        calendarPopup.setBackground(DARK_BG);

        // Create the custom calendar panel
        CalendarPanel calendarPanel = new CalendarPanel(dateField, calendarPopup);
        calendarPopup.add(calendarPanel);

        // Toggle popup on click
        dateField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!calendarPopup.isVisible()) {
                    calendarPopup.show(dateField, 0, dateField.getHeight());
                } else {
                    calendarPopup.setVisible(false);
                }
            }
        });

        parent.add(dateField);
    }

    private class CalendarPanel extends JPanel {
        private Calendar currentCalendar = Calendar.getInstance();
        private JLabel monthLabel;
        private JPanel daysPanel;
        private JTextField targetField;
        private JPopupMenu parentPopup;

        public CalendarPanel(JTextField target, JPopupMenu popup) {
            this.targetField = target;
            this.parentPopup = popup;
            setLayout(new BorderLayout());
            setBackground(DARK_BG);
            setPreferredSize(new Dimension(300, 250));

            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(DARK_BG);
            header.setBorder(new EmptyBorder(5, 5, 5, 5));

            JButton prevBtn = createArrowButton("<");
            prevBtn.addActionListener(e -> navigateMonth(-1));

            JButton nextBtn = createArrowButton(">");
            nextBtn.addActionListener(e -> navigateMonth(1));

            monthLabel = new JLabel("", JLabel.CENTER);
            monthLabel.setForeground(GOLD_COLOR);
            monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

            header.add(prevBtn, BorderLayout.WEST);
            header.add(monthLabel, BorderLayout.CENTER);
            header.add(nextBtn, BorderLayout.EAST);

            add(header, BorderLayout.NORTH);

            daysPanel = new JPanel(new GridLayout(0, 7, 5, 5));
            daysPanel.setBackground(DARK_BG);
            daysPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
            add(daysPanel, BorderLayout.CENTER);

            updateCalendar();
        }

        private void navigateMonth(int offset) {
            currentCalendar.add(Calendar.MONTH, offset);
            updateCalendar();
        }

        private void updateCalendar() {
            daysPanel.removeAll();
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
            monthLabel.setText(sdf.format(currentCalendar.getTime()));

            String[] weekDays = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
            for (String day : weekDays) {
                JLabel lbl = new JLabel(day, JLabel.CENTER);
                lbl.setForeground(Color.GRAY);
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                daysPanel.add(lbl);
            }

            Calendar temp = (Calendar) currentCalendar.clone();
            temp.set(Calendar.DAY_OF_MONTH, 1);
            int startDay = temp.get(Calendar.DAY_OF_WEEK);
            int maxDays = temp.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int i = 1; i < startDay; i++) daysPanel.add(new JLabel(""));

            for (int i = 1; i <= maxDays; i++) {
                final int day = i;
                JButton dayBtn = new JButton(String.valueOf(day));
                dayBtn.setContentAreaFilled(false);
                dayBtn.setBorderPainted(false);
                dayBtn.setFocusPainted(false);
                dayBtn.setForeground(TEXT_COLOR);
                dayBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                dayBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                dayBtn.setMargin(new Insets(0,0,0,0));

                if (isToday(temp, day)) {
                    dayBtn.setForeground(GOLD_COLOR);
                    dayBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    dayBtn.setBorder(BorderFactory.createLineBorder(GOLD_COLOR));
                }

                dayBtn.addActionListener(e -> {
                    selectDate(day);
                });

                daysPanel.add(dayBtn);
            }
            daysPanel.revalidate();
            daysPanel.repaint();
        }

        private void selectDate(int day) {
            Calendar selectedDate = (Calendar) currentCalendar.clone();
            selectedDate.set(Calendar.DAY_OF_MONTH, day);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            targetField.setText(sdf.format(selectedDate.getTime()));
            targetField.setForeground(TEXT_COLOR);
            parentPopup.setVisible(false);
        }

        private boolean isToday(Calendar cal, int day) {
            Calendar today = Calendar.getInstance();
            return today.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                    today.get(Calendar.MONTH) == cal.get(Calendar.MONTH) &&
                    today.get(Calendar.DAY_OF_MONTH) == day;
        }

        private JButton createArrowButton(String text) {
            JButton btn = new JButton(text);
            btn.setForeground(GOLD_COLOR);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return btn;
        }
    }
}