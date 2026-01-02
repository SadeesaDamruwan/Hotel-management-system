package com.hotel.management.view.admin;

import com.hotel.management.model.Ticket;
import com.hotel.management.service.TicketService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SupportTicketsPanel extends JPanel {

    private TicketService ticketService;
    
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusFilterCombo;
    private JComboBox<String> priorityFilterCombo;
    private JTextField searchField;
    
    private final Color BG_COLOR = new Color(30, 30, 35);
    private final Color CARD_BG = new Color(40, 40, 45, 230);
    private final Color TABLE_BG = new Color(30, 30, 35, 200);
    private final Color HEADER_BG = new Color(50, 50, 55);
    private final Color ROW_EVEN = new Color(45, 45, 50, 180);
    private final Color ROW_ODD = new Color(35, 35, 40, 180);
    
    private final Color URGENT_COLOR = new Color(231, 76, 60);
    private final Color HIGH_COLOR = new Color(230, 126, 34);
    private final Color MEDIUM_COLOR = new Color(241, 196, 15);
    private final Color LOW_COLOR = new Color(46, 204, 113);
    
    public SupportTicketsPanel() {
        this.ticketService = new TicketService();
        
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        setBorder(new EmptyBorder(20, 30, 20, 30));
        
        initComponents();
        loadTickets();
    }
    
    private void initComponents() {
        JPanel mainContainer = new JPanel(new BorderLayout(15, 15));
        mainContainer.setOpaque(false);
        
        mainContainer.add(createTopSection(), BorderLayout.NORTH);
        
        mainContainer.add(createTableSection(), BorderLayout.CENTER);
        
        mainContainer.add(createBottomActions(), BorderLayout.SOUTH);
        
        add(mainContainer, BorderLayout.CENTER);
    }
    
    private JPanel createTopSection() {
        JPanel topPanel = new JPanel(new BorderLayout(15, 15));
        topPanel.setOpaque(false);
        
        topPanel.add(createTitlePanel(), BorderLayout.NORTH);
        
        topPanel.add(createFiltersPanel(), BorderLayout.CENTER);
        
        return topPanel;
    }
    
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Support Tickets Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        long openCount = ticketService.getTicketCountByStatus("Open");
        long inProgressCount = ticketService.getTicketCountByStatus("In Progress");
        long resolvedCount = ticketService.getTicketCountByStatus("Resolved");
        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        statsPanel.setOpaque(false);
        statsPanel.add(createStatBadge("Open: " + openCount, new Color(52, 152, 219)));
        statsPanel.add(createStatBadge("In Progress: " + inProgressCount, new Color(241, 196, 15)));
        statsPanel.add(createStatBadge("Resolved: " + resolvedCount, new Color(46, 204, 113)));
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createHorizontalStrut(20));
        titlePanel.add(statsPanel);
        
        return titlePanel;
    }
    
    private JLabel createStatBadge(String text, Color color) {
        JLabel badge = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setForeground(Color.WHITE);
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(5, 15, 5, 15));
        return badge;
    }
    
    private JPanel createFiltersPanel() {
        JPanel filtersPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        filtersPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        filtersPanel.setOpaque(false);
        
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setForeground(Color.WHITE);
        
        statusFilterCombo = new JComboBox<>(new String[]{
                "All Status", "Open", "In Progress", "Resolved", "Closed"
        });
        statusFilterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusFilterCombo.setPreferredSize(new Dimension(140, 35));
        statusFilterCombo.addActionListener(e -> applyFilter());
        
        JLabel priorityLabel = new JLabel("Priority:");
        priorityLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        priorityLabel.setForeground(Color.WHITE);
        
        priorityFilterCombo = new JComboBox<>(new String[]{
                "All Priority", "Urgent", "High", "Medium", "Low"
        });
        priorityFilterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        priorityFilterCombo.setPreferredSize(new Dimension(140, 35));
        priorityFilterCombo.addActionListener(e -> applyFilter());
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLabel.setForeground(Color.WHITE);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setPreferredSize(new Dimension(200, 35));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                applyFilter();
            }
        });
        
        JButton refreshBtn = createStyledButton("Refresh", new Color(52, 152, 219));
        refreshBtn.addActionListener(e -> {
            loadTickets();
            JOptionPane.showMessageDialog(this, "Tickets refreshed!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        
        filtersPanel.add(statusLabel);
        filtersPanel.add(statusFilterCombo);
        filtersPanel.add(Box.createHorizontalStrut(5));
        filtersPanel.add(priorityLabel);
        filtersPanel.add(priorityFilterCombo);
        filtersPanel.add(Box.createHorizontalStrut(5));
        filtersPanel.add(searchLabel);
        filtersPanel.add(searchField);
        filtersPanel.add(Box.createHorizontalStrut(5));
        filtersPanel.add(refreshBtn);
        
        return filtersPanel;
    }
    
    private JScrollPane createTableSection() {
        String[] columnNames = {"Ticket ID", "Title", "Category", "Priority", "Status", "Room", "Guest", "Created By", "Created", "Assigned To"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        ticketTable = new JTable(tableModel);
        ticketTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ticketTable.setRowHeight(45);
        ticketTable.setBackground(TABLE_BG);
        ticketTable.setForeground(Color.WHITE);
        ticketTable.setGridColor(new Color(60, 60, 65));
        ticketTable.setSelectionBackground(new Color(255, 180, 60, 100));
        ticketTable.setSelectionForeground(Color.WHITE);
        
        ticketTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        ticketTable.getTableHeader().setBackground(HEADER_BG);
        ticketTable.getTableHeader().setForeground(new Color(255, 200, 100));
        ticketTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        ticketTable.getColumnModel().getColumn(0).setPreferredWidth(110);
        ticketTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        ticketTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        ticketTable.getColumnModel().getColumn(3).setPreferredWidth(90);
        ticketTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        ticketTable.getColumnModel().getColumn(5).setPreferredWidth(70);
        ticketTable.getColumnModel().getColumn(6).setPreferredWidth(120);
        ticketTable.getColumnModel().getColumn(7).setPreferredWidth(120);
        ticketTable.getColumnModel().getColumn(8).setPreferredWidth(140);
        ticketTable.getColumnModel().getColumn(9).setPreferredWidth(120);
        
        ticketTable.getColumnModel().getColumn(3).setCellRenderer(new PriorityCellRenderer());
        
        ticketTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(ROW_EVEN);
                    } else {
                        c.setBackground(ROW_ODD);
                    }
                    c.setForeground(Color.WHITE);
                }
                
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                
                return c;
            }
        });
        
        ticketTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = ticketTable.getSelectedRow();
                    if (row != -1) {
                        manageTicket(row);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(ticketTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        return scrollPane;
    }
    
    private JPanel createBottomActions() {
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        actionsPanel.setOpaque(false);
        
        JButton manageBtn = createStyledButton("Manage Selected", new Color(52, 152, 219));
        manageBtn.addActionListener(e -> {
            int row = ticketTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a ticket first!", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            manageTicket(row);
        });
        
        JButton deleteBtn = createStyledButton("Delete Selected", new Color(231, 76, 60));
        deleteBtn.addActionListener(e -> deleteTicket());
        
        actionsPanel.add(manageBtn);
        actionsPanel.add(deleteBtn);
        
        return actionsPanel;
    }
    
    private void loadTickets() {
        tableModel.setRowCount(0);
        List<Ticket> tickets = ticketService.getAllTickets();
        
        for (Ticket ticket : tickets) {
            Object[] row = {
                    ticket.getTicketId(),
                    ticket.getTitle(),
                    ticket.getCategory(),
                    ticket.getPriority(),
                    ticket.getStatus(),
                    ticket.getRoomNumber() != null && !ticket.getRoomNumber().isEmpty() ? ticket.getRoomNumber() : "N/A",
                    ticket.getGuestName() != null && !ticket.getGuestName().isEmpty() ? ticket.getGuestName() : "N/A",
                    ticket.getCreatedBy(),
                    TicketService.formatDate(ticket.getCreatedAt()),
                    ticket.getAssignedTo() != null ? ticket.getAssignedTo() : "Unassigned"
            };
            tableModel.addRow(row);
        }
    }
    
    private void applyFilter() {
        String statusFilter = (String) statusFilterCombo.getSelectedItem();
        String priorityFilter = (String) priorityFilterCombo.getSelectedItem();
        String searchText = searchField.getText().trim().toLowerCase();
        
        tableModel.setRowCount(0);
        List<Ticket> tickets = ticketService.getAllTickets();
        
        for (Ticket ticket : tickets) {
            
            if (!statusFilter.equals("All Status") && !ticket.getStatus().equals(statusFilter)) {
                continue;
            }
            
            if (!priorityFilter.equals("All Priority") && !ticket.getPriority().equals(priorityFilter)) {
                continue;
            }
            
            if (!searchText.isEmpty()) {
                boolean matches = ticket.getTicketId().toLowerCase().contains(searchText) ||
                                ticket.getTitle().toLowerCase().contains(searchText) ||
                                ticket.getCategory().toLowerCase().contains(searchText) ||
                                (ticket.getRoomNumber() != null && ticket.getRoomNumber().toLowerCase().contains(searchText)) ||
                                (ticket.getGuestName() != null && ticket.getGuestName().toLowerCase().contains(searchText));
                if (!matches) continue;
            }
            
            Object[] row = {
                    ticket.getTicketId(),
                    ticket.getTitle(),
                    ticket.getCategory(),
                    ticket.getPriority(),
                    ticket.getStatus(),
                    ticket.getRoomNumber() != null && !ticket.getRoomNumber().isEmpty() ? ticket.getRoomNumber() : "N/A",
                    ticket.getGuestName() != null && !ticket.getGuestName().isEmpty() ? ticket.getGuestName() : "N/A",
                    ticket.getCreatedBy(),
                    TicketService.formatDate(ticket.getCreatedAt()),
                    ticket.getAssignedTo() != null ? ticket.getAssignedTo() : "Unassigned"
            };
            tableModel.addRow(row);
        }
    }
    
    private void manageTicket(int row) {
        String ticketId = (String) tableModel.getValueAt(row, 0);
        Ticket ticket = ticketService.getTicketById(ticketId);
        
        if (ticket != null) {
            showManageDialog(ticket);
        }
    }
    
    private void showManageDialog(Ticket ticket) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Manage Ticket: " + ticket.getTicketId(), true);
        dialog.setSize(700, 750);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(new Color(30, 30, 35));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.weightx = 1.0;
        
        int row = 0;
        
        JLabel sectionTitle = new JLabel("Ticket Information");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(new Color(255, 180, 60));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(sectionTitle, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 0, 5, 15);
        
        mainPanel.add(createInfoLabelForDialog("Ticket ID:", 0), setGBC(gbc, 0, row, 0.3));
        mainPanel.add(createInfoValueForDialog(ticket.getTicketId()), setGBC(gbc, 1, row++, 0.7));
        
        mainPanel.add(createInfoLabelForDialog("Title:", 0), setGBC(gbc, 0, row, 0.3));
        mainPanel.add(createInfoValueForDialog(ticket.getTitle()), setGBC(gbc, 1, row++, 0.7));
        
        mainPanel.add(createInfoLabelForDialog("Description:", 0), setGBC(gbc, 0, row, 0.3));
        mainPanel.add(createInfoValueForDialog(ticket.getDescription()), setGBC(gbc, 1, row++, 0.7));
        
        mainPanel.add(createInfoLabelForDialog("Category:", 0), setGBC(gbc, 0, row, 0.3));
        mainPanel.add(createInfoValueForDialog(ticket.getCategory()), setGBC(gbc, 1, row++, 0.7));
        
        mainPanel.add(createInfoLabelForDialog("Priority:", 0), setGBC(gbc, 0, row, 0.3));
        mainPanel.add(createInfoValueForDialog(ticket.getPriority()), setGBC(gbc, 1, row++, 0.7));
        
        mainPanel.add(createInfoLabelForDialog("Room Number:", 0), setGBC(gbc, 0, row, 0.3));
        mainPanel.add(createInfoValueForDialog(ticket.getRoomNumber() != null ? ticket.getRoomNumber() : "N/A"), setGBC(gbc, 1, row++, 0.7));
        
        mainPanel.add(createInfoLabelForDialog("Guest Name:", 0), setGBC(gbc, 0, row, 0.3));
        mainPanel.add(createInfoValueForDialog(ticket.getGuestName() != null ? ticket.getGuestName() : "N/A"), setGBC(gbc, 1, row++, 0.7));
        
        mainPanel.add(createInfoLabelForDialog("Created By:", 0), setGBC(gbc, 0, row, 0.3));
        mainPanel.add(createInfoValueForDialog(ticket.getCreatedBy()), setGBC(gbc, 1, row++, 0.7));
        
        mainPanel.add(createInfoLabelForDialog("Created:", 0), setGBC(gbc, 0, row, 0.3));
        mainPanel.add(createInfoValueForDialog(TicketService.formatDate(ticket.getCreatedAt())), setGBC(gbc, 1, row++, 0.7));
        
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(60, 60, 65));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 20, 0);
        mainPanel.add(separator, gbc);
        
        JLabel managementTitle = new JLabel("Ticket Management");
        managementTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        managementTitle.setForeground(new Color(255, 180, 60));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(managementTitle, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 0, 5, 15);
        mainPanel.add(createInfoLabelForDialog("Change Status:", 0), setGBC(gbc, 0, row, 0.3));
        
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Open", "In Progress", "Resolved", "Closed"});
        statusCombo.setSelectedItem(ticket.getStatus());
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusCombo.setPreferredSize(new Dimension(200, 32));
        mainPanel.add(statusCombo, setGBC(gbc, 1, row++, 0.7));
        
        mainPanel.add(createInfoLabelForDialog("Assign To:", 0), setGBC(gbc, 0, row, 0.3));
        
        JTextField assignField = new JTextField(ticket.getAssignedTo() != null ? ticket.getAssignedTo() : "");
        assignField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        assignField.setPreferredSize(new Dimension(200, 32));
        mainPanel.add(assignField, setGBC(gbc, 1, row++, 0.7));
        
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 8, 0);
        JLabel notesLabel = new JLabel("Admin Notes / Response:");
        notesLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        notesLabel.setForeground(Color.WHITE);
        mainPanel.add(notesLabel, gbc);
        
        JTextArea notesArea = new JTextArea(ticket.getAdminNotes() != null ? ticket.getAdminNotes() : "", 6, 50);
        notesArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setMargin(new Insets(8, 8, 8, 8));
        JScrollPane notesScroll = new JScrollPane(notesArea);
        notesScroll.setPreferredSize(new Dimension(600, 150));
        
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(notesScroll, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        JButton saveBtn = createStyledButton("Save Changes", new Color(46, 204, 113));
        saveBtn.addActionListener(e -> {
            String newStatus = (String) statusCombo.getSelectedItem();
            String assignTo = assignField.getText().trim();
            String notes = notesArea.getText().trim();
            
            boolean success = ticketService.updateTicketStatus(ticket.getTicketId(), newStatus, 
                    assignTo.isEmpty() ? null : assignTo);
            
            if (success && !notes.isEmpty()) {
                ticketService.addAdminNotes(ticket.getTicketId(), notes);
            }
            
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Ticket updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                loadTickets();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to update ticket!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelBtn = createStyledButton("Cancel", new Color(127, 140, 141));
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);
        
        dialog.add(new JScrollPane(mainPanel));
        dialog.setVisible(true);
    }
    
    private GridBagConstraints setGBC(GridBagConstraints gbc, int x, int y, double weightx) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = weightx;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }
    
    private JLabel createInfoLabelForDialog(String text, int leftPadding) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(255, 200, 100));
        return label;
    }
    
    private JLabel createInfoValueForDialog(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(Color.WHITE);
        return label;
    }
    
    private void deleteTicket() {
        int row = ticketTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket first!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String ticketId = (String) tableModel.getValueAt(row, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete ticket " + ticketId + "?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = ticketService.deleteTicket(ticketId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Ticket deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadTickets();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete ticket!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    class PriorityCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            panel.setOpaque(true);
            
            if (!isSelected) {
                panel.setBackground(row % 2 == 0 ? ROW_EVEN : ROW_ODD);
            } else {
                panel.setBackground(table.getSelectionBackground());
            }
            
            String priority = (String) value;
            Color badgeColor;
            
            switch (priority) {
                case "Urgent":
                    badgeColor = URGENT_COLOR;
                    break;
                case "High":
                    badgeColor = HIGH_COLOR;
                    break;
                case "Medium":
                    badgeColor = MEDIUM_COLOR;
                    break;
                case "Low":
                    badgeColor = LOW_COLOR;
                    break;
                default:
                    badgeColor = Color.GRAY;
            }
            
            JLabel badge = new JLabel(priority) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(badgeColor);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
            badge.setForeground(Color.WHITE);
            badge.setOpaque(false);
            badge.setBorder(new EmptyBorder(3, 10, 3, 10));
            
            panel.add(badge);
            return panel;
        }
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = bgColor;
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
        
        return button;
    }
}
