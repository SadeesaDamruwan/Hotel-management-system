package com.hotel.management.view.receptionist;

import com.hotel.management.model.Booking;
import com.hotel.management.model.Room;
import com.hotel.management.model.Staff;
import com.hotel.management.service.GuestBookingService;
import com.hotel.management.service.RoomService;
import com.hotel.management.service.StaffService;
import com.hotel.management.view.LoginSelectionFrame;
import com.hotel.management.service.RoomCleaningMonitor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

public class ReceptionistDashboardFrame extends JFrame {

    private BufferedImage backgroundImage;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    
    // Track currently selected navigation button
    private JButton currentlySelectedButton;
    private JButton currentlySelectedSubMenuButton;
    
    // Track dropdown menus for collapse functionality
    private java.util.List<JPanel> dropdownSubMenus = new java.util.ArrayList<>();
    private java.util.List<JButton> dropdownMainButtons = new java.util.ArrayList<>();

    // --- Color Constants ---
    private final Color SELECTED_COLOR = new Color(255, 180, 60); // Gold
    private final Color SELECTED_TEXT_COLOR = new Color(30, 30, 30);
    private final Color DEFAULT_COLOR = new Color(45, 45, 48, 0);
    private final Color HOVER_COLOR = new Color(70, 70, 75, 180);
    private final Color SUB_ITEM_HOVER_COLOR = new Color(255, 180, 60);
    private final Color SUB_MENU_BG = new Color(25, 25, 28, 200);
    private final Color SIDEBAR_BG_TOP = new Color(28, 28, 32);
    private final Color SIDEBAR_BG_BOTTOM = new Color(20, 20, 24);
    private final Color SEPARATOR_COLOR = new Color(60, 60, 65, 100);
    private final Color BUTTON_TEXT_COLOR = new Color(255, 200, 100); // Lighter Gold for text

    public ReceptionistDashboardFrame() {
        // --- Frame Setup ---
        setTitle("Hotel Management System - Receptionist Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // --- Load Background Image ---
        try {
            // Using the same background as admin for consistency, or change to reception specific if available
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/a170b7e6b576d72403c665e6337322e1.jpg")));
        } catch (Exception e) {
            System.err.println("Dashboard background image not found.");
            backgroundImage = null;
        }

        // --- Main Background Panel ---
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(20, 20, 20));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // --- Header and Main Container ---
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);

        // 1. Top Bar
        container.add(createTopBar(), BorderLayout.NORTH);

        // 2. Main Panel (Sidebar + Content)
        JPanel mainPanel = new JPanel(new BorderLayout(10, 0));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 2a. Sidebar
        mainPanel.add(createSidebar(), BorderLayout.WEST);

        // 2b. Content Area
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setOpaque(false);

        // Add Dashboard Home View
        mainContentPanel.add(createDashboardHome(), "Dashboard");

        // Placeholders for other views
        mainContentPanel.add(createPlaceholderPanel("Settings & Profile"), "Settings");
        mainContentPanel.add(createPlaceholderPanel("Exams Section"), "Exams");
        mainContentPanel.add(new AddGuestPanel(), "AddGuest");
        mainContentPanel.add(new RemoveGuestPanel(), "RemoveGuest");
        mainContentPanel.add(new RoomStatusPanel(), "RoomStatus");
        mainContentPanel.add(new CreateTicketPanel(), "CreateTicket");
        mainContentPanel.add(new ViewTicketsPanel(), "ViewTickets");

        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        container.add(mainPanel, BorderLayout.CENTER);
        backgroundPanel.add(container, BorderLayout.CENTER);
        add(backgroundPanel);
        
        // Start the room cleaning monitor
        RoomCleaningMonitor.getInstance().start();

        setVisible(true);
    }

    private JPanel createTopBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 30, 0, 30));
        panel.setOpaque(false);

        JLabel title = new JLabel("Receptionist Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controls.setOpaque(false);
        
        // Logout Button
        JButton logoutButton = new JButton("Logout") {
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
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBackground(SELECTED_COLOR);
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(255, 200, 100));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(SELECTED_COLOR);
            }
        });
        
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginSelectionFrame().setVisible(true);
        });
        
        controls.add(logoutButton);

        panel.add(title, BorderLayout.WEST);
        panel.add(controls, BorderLayout.EAST);
        return panel;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, SIDEBAR_BG_TOP,
                    0, getHeight(), SIDEBAR_BG_BOTTOM
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Subtle right border with shadow effect
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRect(getWidth() - 2, 0, 2, getHeight());
                
                g2.dispose();
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 700));
        sidebar.setMinimumSize(new Dimension(220, 700));
        sidebar.setMaximumSize(new Dimension(220, Integer.MAX_VALUE));
        sidebar.setOpaque(false);

        // --- Logo Area ---
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(new EmptyBorder(30, 0, 30, 0));

        try {
            BufferedImage logoImg = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/logo.png")));
            Image scaledLogo = logoImg.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
        } catch (Exception e) {
            logoLabel.setText("<html><div style='text-align:center;'><span style='color:#FFB43C; font-size:24pt;'>innova</span><br><span style='color:gray; font-size:8pt;'>RECEPTION</span></div></html>");
        }
        sidebar.add(logoLabel);
        sidebar.add(createSeparator());
        sidebar.add(Box.createVerticalStrut(10));

        // Dashboard Button
        JButton dashboardButton = createNavButton("Dashboard", true);
        currentlySelectedButton = dashboardButton;
        dashboardButton.addActionListener(e -> {
            collapseAllDropdowns();
            setSelectedButton(dashboardButton);
            cardLayout.show(mainContentPanel, "Dashboard");
        });
        sidebar.add(dashboardButton);
        sidebar.add(Box.createVerticalStrut(5));

        // Manage Guest Dropdown
        sidebar.add(createDropdownMenu("Manage Guest", 
                new String[]{"Add Guest", "Remove Guest"},
                new String[]{"AddGuest", "RemoveGuest"}));
        sidebar.add(Box.createVerticalStrut(5));

        // Room Status Button
        JButton roomStatusBtn = createNavButton("Room Status", false);
        roomStatusBtn.addActionListener(e -> {
            collapseAllDropdowns();
            setSelectedButton(roomStatusBtn);
            cardLayout.show(mainContentPanel, "RoomStatus");
        });
        sidebar.add(roomStatusBtn);
        sidebar.add(Box.createVerticalStrut(5));

        // Support Tickets Dropdown
        sidebar.add(createDropdownMenu("Support Tickets", 
                new String[]{"Create Ticket", "View Tickets"},
                new String[]{"CreateTicket", "ViewTickets"}));
        sidebar.add(Box.createVerticalStrut(10));
        
        sidebar.add(createSeparator());
        sidebar.add(Box.createVerticalStrut(10));

        return sidebar;
    }
    
    private JPanel createDropdownMenu(String title, String[] subItemTexts, String[] subItemCardNames) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton mainButton = createNavButton(title, false);

        JPanel subMenuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(SUB_MENU_BG);
                g2.fillRoundRect(10, 0, getWidth() - 20, getHeight(), 8, 8);
                
                g2.setColor(new Color(SELECTED_COLOR.getRed(), SELECTED_COLOR.getGreen(), SELECTED_COLOR.getBlue(), 100));
                g2.fillRoundRect(15, 5, 3, getHeight() - 10, 3, 3);
                
                g2.dispose();
            }
        };
        subMenuPanel.setLayout(new BoxLayout(subMenuPanel, BoxLayout.Y_AXIS));
        subMenuPanel.setOpaque(true);
        subMenuPanel.setBackground(new Color(25, 25, 28));
        subMenuPanel.setVisible(false);
        subMenuPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subMenuPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        for (int i = 0; i < subItemTexts.length; i++) {
            String itemText = subItemTexts[i];
            String cardName = (subItemCardNames != null && i < subItemCardNames.length) ? subItemCardNames[i] : null;

            JButton subBtn = new JButton(itemText) {
                @Override
                protected void paintComponent(Graphics g) {
                    if (getForeground().equals(SUB_ITEM_HOVER_COLOR)) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(SUB_ITEM_HOVER_COLOR.getRed(), SUB_ITEM_HOVER_COLOR.getGreen(), SUB_ITEM_HOVER_COLOR.getBlue(), 30));
                        g2.fillRoundRect(10, 0, getWidth() - 20, getHeight(), 5, 5);
                        g2.dispose();
                    }
                    super.paintComponent(g);
                }
            };
            subBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            subBtn.setForeground(new Color(200, 200, 200));
            subBtn.setOpaque(false);
            subBtn.setContentAreaFilled(false);
            subBtn.setBorderPainted(false);
            subBtn.setFocusPainted(false);
            subBtn.setHorizontalAlignment(SwingConstants.LEFT);
            subBtn.setBorder(new EmptyBorder(6, 55, 6, 10));
            subBtn.setMaximumSize(new Dimension(200, 32));
            subBtn.setPreferredSize(new Dimension(200, 32));
            subBtn.setMinimumSize(new Dimension(200, 32));
            subBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

            subBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    subBtn.setForeground(SUB_ITEM_HOVER_COLOR);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (currentlySelectedSubMenuButton != subBtn) {
                        subBtn.setForeground(new Color(200, 200, 200));
                    }
                }
            });

            if (cardName != null) {
                subBtn.addActionListener(e -> {
                    setSelectedSubMenuButton(subBtn);
                    cardLayout.show(mainContentPanel, cardName);
                });
            }
            subMenuPanel.add(subBtn);
        }

        subMenuPanel.add(Box.createVerticalStrut(5));

        dropdownSubMenus.add(subMenuPanel);
        dropdownMainButtons.add(mainButton);

        mainButton.addActionListener(e -> {
            boolean isVisible = subMenuPanel.isVisible();
            
            setSelectedButton(null);
            
            for (int i = 0; i < dropdownSubMenus.size(); i++) {
                JPanel otherSubMenu = dropdownSubMenus.get(i);
                JButton otherMainBtn = dropdownMainButtons.get(i);
                
                if (otherSubMenu != subMenuPanel && otherSubMenu.isVisible()) {
                    otherSubMenu.setVisible(false);
                    otherMainBtn.setBackground(DEFAULT_COLOR);
                    otherMainBtn.setForeground(new Color(220, 220, 220));
                }
            }
            
            subMenuPanel.setVisible(!isVisible);

            if (!isVisible) {
                mainButton.setBackground(SELECTED_COLOR);
                mainButton.setForeground(SELECTED_TEXT_COLOR);
            } else {
                mainButton.setBackground(DEFAULT_COLOR);
                mainButton.setForeground(new Color(220, 220, 220));
            }
            
            SwingUtilities.invokeLater(() -> {
                container.revalidate();
                Component root = SwingUtilities.getRoot(container);
                if (root != null) {
                    root.repaint();
                }
            });
        });

        container.add(mainButton);
        container.add(subMenuPanel);
        return container;
    }
    
    private void setSelectedButton(JButton newSelectedButton) {
        if (currentlySelectedButton != null) {
            currentlySelectedButton.setBackground(DEFAULT_COLOR);
            currentlySelectedButton.setForeground(new Color(220, 220, 220));
            currentlySelectedButton.repaint();
        }
        
        if (newSelectedButton != null) {
            newSelectedButton.setBackground(SELECTED_COLOR);
            newSelectedButton.setForeground(SELECTED_TEXT_COLOR);
            newSelectedButton.repaint();
        }
        
        currentlySelectedButton = newSelectedButton;
        
        if (newSelectedButton != null) {
            setSelectedSubMenuButton(null);
        }
    }

    private void setSelectedSubMenuButton(JButton newSelectedSubMenuButton) {
        if (currentlySelectedSubMenuButton != null) {
            currentlySelectedSubMenuButton.setForeground(new Color(200, 200, 200));
            currentlySelectedSubMenuButton.repaint();
        }
        
        if (newSelectedSubMenuButton != null) {
            newSelectedSubMenuButton.setForeground(SUB_ITEM_HOVER_COLOR);
            newSelectedSubMenuButton.repaint();
        }
        
        currentlySelectedSubMenuButton = newSelectedSubMenuButton;
    }

    private void collapseAllDropdowns() {
        for (int i = 0; i < dropdownSubMenus.size(); i++) {
            JPanel subMenu = dropdownSubMenus.get(i);
            JButton mainBtn = dropdownMainButtons.get(i);
            
            if (subMenu.isVisible()) {
                subMenu.setVisible(false);
                mainBtn.setBackground(DEFAULT_COLOR);
                mainBtn.setForeground(new Color(220, 220, 220));
            }
        }
    }

    private JPanel createSeparator() {
        JPanel separator = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int margin = 25;
                GradientPaint gradient = new GradientPaint(
                    margin, 0, new Color(60, 60, 65, 0),
                    getWidth() / 2, 0, SEPARATOR_COLOR,
                    true
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(margin, 0, getWidth() - (margin * 2), 1, 1, 1);
                
                g2.dispose();
            }
        };
        separator.setOpaque(false);
        separator.setMaximumSize(new Dimension(220, 1));
        separator.setPreferredSize(new Dimension(220, 1));
        return separator;
    }

    // --- DASHBOARD HOME ---
    private JPanel createDashboardHome() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Initialize services
        GuestBookingService bookingService = new GuestBookingService();
        RoomService roomService = new RoomService();
        StaffService staffService = new StaffService();

        // Calculate statistics
        List<Booking> allBookings = bookingService.getAllBookings();
        List<Room> allRooms = roomService.getAllRooms();
        List<Staff> allStaff = staffService.getAllStaff();
        
        LocalDate today = LocalDate.now();
        
        // Today's check-ins (bookings scheduled for today)
        long todayCheckIns = allBookings.stream()
            .filter(b -> {
                if (b.getCheckInDate() == null) return false;
                LocalDate checkInDate = b.getCheckInDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
                return checkInDate.equals(today) && "CONFIRMED".equals(b.getBookingStatus());
            })
            .count();
        
        // Today's check-outs (bookings ending today)
        long todayCheckOuts = allBookings.stream()
            .filter(b -> {
                if (b.getCheckOutDate() == null) return false;
                LocalDate checkOutDate = b.getCheckOutDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
                return checkOutDate.equals(today) && "CHECKED_IN".equals(b.getBookingStatus());
            })
            .count();
        
        // Room counts
        long occupiedRooms = allRooms.stream()
            .filter(r -> "Occupied".equals(r.getStatus()))
            .count();
        long availableRooms = allRooms.stream()
            .filter(r -> "Available".equals(r.getStatus()))
            .count();
        long needsCleaningRooms = allRooms.stream()
            .filter(r -> "Needs Cleaning".equals(r.getStatus()))
            .count();
        long maintenanceRooms = allRooms.stream()
            .filter(r -> "Under Maintenance".equals(r.getStatus()))
            .count();
        
        // Staff counts (available = not currently assigned)
        Set<String> assignedStaff = allRooms.stream()
            .filter(r -> r.getAssignedStaff() != null && !r.getAssignedStaff().isEmpty())
            .map(Room::getAssignedStaff)
            .collect(java.util.stream.Collectors.toSet());
        
        long totalCleaners = allStaff.stream()
            .filter(s -> "Cleaning Staff".equals(s.getRole()))
            .count();
        long availableCleaners = allStaff.stream()
            .filter(s -> "Cleaning Staff".equals(s.getRole()) && !assignedStaff.contains(s.getId()))
            .count();
        
        long totalMaintenance = allStaff.stream()
            .filter(s -> "Maintenance Staff".equals(s.getRole()))
            .count();
        long availableMaintenance = allStaff.stream()
            .filter(s -> "Maintenance Staff".equals(s.getRole()) && !assignedStaff.contains(s.getId()))
            .count();

        // 1. TOP SECTION - 8 STAT CARDS (2 rows x 4 cols)
        JPanel statsSection = new JPanel(new GridLayout(2, 4, 15, 15));
        statsSection.setOpaque(false);
        statsSection.setMaximumSize(new Dimension(1200, 200));
        
        // Row 1 - Guest Operations
        statsSection.add(createStatCard("Today's Check-ins", String.valueOf(todayCheckIns), "📥", new Color(46, 204, 113)));
        statsSection.add(createStatCard("Today's Check-outs", String.valueOf(todayCheckOuts), "📤", new Color(52, 152, 219)));
        statsSection.add(createStatCard("Occupied Rooms", String.valueOf(occupiedRooms), "🏨", new Color(155, 89, 182)));
        statsSection.add(createStatCard("Available Rooms", String.valueOf(availableRooms), "✅", new Color(46, 204, 113)));
        
        // Row 2 - Operational Status
        statsSection.add(createStatCard("Needs Cleaning", String.valueOf(needsCleaningRooms), "🧹", new Color(230, 126, 34)));
        statsSection.add(createStatCard("Under Maintenance", String.valueOf(maintenanceRooms), "🔧", new Color(231, 76, 60)));
        statsSection.add(createStatCard("Available Cleaners", availableCleaners + "/" + totalCleaners, "👤", new Color(52, 152, 219)));
        statsSection.add(createStatCard("Available Maintenance", availableMaintenance + "/" + totalMaintenance, "👷", new Color(155, 89, 182)));
        
        content.add(statsSection);
        content.add(Box.createVerticalStrut(20));

        // 2. MIDDLE SECTION - Charts (2 columns)
        JPanel middleSection = new JPanel(new GridLayout(1, 2, 20, 0));
        middleSection.setOpaque(false);
        middleSection.setMaximumSize(new Dimension(1200, 280));
        
        middleSection.add(createRoomStatusPanel(availableRooms, occupiedRooms, needsCleaningRooms, maintenanceRooms));
        middleSection.add(createActivityPanel(bookingService));
        
        content.add(middleSection);
        content.add(Box.createVerticalStrut(20));

        // 3. BOTTOM SECTION - Quick Actions + Recent Activity
        JPanel bottomSection = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomSection.setOpaque(false);
        bottomSection.setMaximumSize(new Dimension(1200, 200));
        
        bottomSection.add(createQuickActionsPanel());
        bottomSection.add(createRecentActivityPanel(bookingService));
        
        content.add(bottomSection);
        content.add(Box.createVerticalGlue());

        return content;
    }
    
    private JPanel createStatCard(String title, String value, String emoji, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 25, 28, 240),
                    0, getHeight(), new Color(20, 20, 24, 240)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 80));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setLayout(new BorderLayout(5, 5));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(12, 15, 12, 15));

        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        card.add(emojiLabel, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        titleLabel.setForeground(new Color(160, 160, 160));
        textPanel.add(titleLabel);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(accentColor);
        textPanel.add(valueLabel);
        
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }
    
    private JPanel createRoomStatusPanel(long available, long occupied, long cleaning, long maintenance) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 25, 28, 240),
                    0, getHeight(), new Color(20, 20, 24, 240)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Room Status Distribution");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(SELECTED_COLOR);
        panel.add(title, BorderLayout.NORTH);

        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                long total = available + occupied + cleaning + maintenance;
                if (total == 0) total = 1;
                
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int radius = Math.min(getWidth(), getHeight()) / 3;
                
                double availableAngle = (available * 360.0) / total;
                double occupiedAngle = (occupied * 360.0) / total;
                double cleaningAngle = (cleaning * 360.0) / total;
                double maintenanceAngle = (maintenance * 360.0) / total;
                
                int startAngle = 0;
                
                // Available (Green)
                g2.setColor(new Color(46, 204, 113));
                g2.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, (int)availableAngle);
                startAngle += (int)availableAngle;
                
                // Occupied (Blue)
                g2.setColor(new Color(52, 152, 219));
                g2.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, (int)occupiedAngle);
                startAngle += (int)occupiedAngle;
                
                // Cleaning (Orange)
                g2.setColor(new Color(230, 126, 34));
                g2.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, (int)cleaningAngle);
                startAngle += (int)cleaningAngle;
                
                // Maintenance (Red)
                g2.setColor(new Color(231, 76, 60));
                g2.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, (int)maintenanceAngle);
                
                // Center hole for donut chart
                g2.setColor(new Color(25, 25, 28));
                int innerRadius = radius / 2;
                g2.fillOval(centerX - innerRadius, centerY - innerRadius, innerRadius * 2, innerRadius * 2);
                
                // Total in center
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
                String totalStr = String.valueOf(total);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(totalStr, centerX - fm.stringWidth(totalStr) / 2, centerY + fm.getAscent() / 2);
                
                g2.dispose();
            }
        };
        chartPanel.setOpaque(false);
        panel.add(chartPanel, BorderLayout.CENTER);
        
        // Legend
        JPanel legend = new JPanel(new GridLayout(4, 1, 5, 5));
        legend.setOpaque(false);
        legend.add(createLegendItem("Available", new Color(46, 204, 113), available));
        legend.add(createLegendItem("Occupied", new Color(52, 152, 219), occupied));
        legend.add(createLegendItem("Needs Cleaning", new Color(230, 126, 34), cleaning));
        legend.add(createLegendItem("Maintenance", new Color(231, 76, 60), maintenance));
        panel.add(legend, BorderLayout.SOUTH);

        return panel;
    }
    
    private JPanel createLegendItem(String label, Color color, long count) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        item.setOpaque(false);
        
        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(15, 15));
        item.add(colorBox);
        
        JLabel textLabel = new JLabel(label + ": " + count);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        textLabel.setForeground(new Color(200, 200, 200));
        item.add(textLabel);
        
        return item;
    }
    
    private JPanel createActivityPanel(GuestBookingService bookingService) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 25, 28, 240),
                    0, getHeight(), new Color(20, 20, 24, 240)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Today's Activity");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(SELECTED_COLOR);
        panel.add(title, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(20, 10, 10, 10));
        
        LocalDate today = LocalDate.now();
        List<Booking> allBookings = bookingService.getAllBookings();
        
        long checkInsToday = allBookings.stream()
            .filter(b -> b.getBookingDate() != null && 
                b.getBookingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(today) &&
                "CHECKED_IN".equals(b.getBookingStatus()))
            .count();
        long checkOutsToday = allBookings.stream()
            .filter(b -> b.getActualCheckOutDate() != null && 
                b.getActualCheckOutDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(today))
            .count();
        long currentlyCheckedIn = allBookings.stream()
            .filter(b -> "CHECKED_IN".equals(b.getBookingStatus()))
            .count();
        
        statsPanel.add(createActivityStat("Checked In", String.valueOf(checkInsToday), new Color(46, 204, 113)));
        statsPanel.add(createActivityStat("Checked Out", String.valueOf(checkOutsToday), new Color(52, 152, 219)));
        statsPanel.add(createActivityStat("Currently Staying", String.valueOf(currentlyCheckedIn), new Color(155, 89, 182)));
        statsPanel.add(createActivityStat("Pending Check-ins", "0", new Color(230, 126, 34)));
        
        panel.add(statsPanel, BorderLayout.CENTER);

        return panel;
    }
    
    private JPanel createActivityStat(String label, String value, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(valueLabel);
        
        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        labelText.setForeground(new Color(160, 160, 160));
        labelText.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelText);
        
        return panel;
    }
    
    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 25, 28, 240),
                    0, getHeight(), new Color(20, 20, 24, 240)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Quick Actions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(SELECTED_COLOR);
        panel.add(title, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        buttonsPanel.add(createQuickActionButton("Check-in", "AddGuest"));
        buttonsPanel.add(createQuickActionButton("Check-out", "RemoveGuest"));
        buttonsPanel.add(createQuickActionButton("Room Status", "RoomStatus"));
        buttonsPanel.add(createQuickActionButton("New Ticket", "CreateTicket"));
        
        panel.add(buttonsPanel, BorderLayout.CENTER);

        return panel;
    }
    
    private JButton createQuickActionButton(String text, String cardName) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(255, 180, 60, 200));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 180, 60, 120));
                } else {
                    g2.setColor(new Color(60, 60, 65, 150));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            collapseAllDropdowns();
            setSelectedButton(null);
            cardLayout.show(mainContentPanel, cardName);
        });
        
        return button;
    }
    
    private JPanel createRecentActivityPanel(GuestBookingService bookingService) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 25, 28, 240),
                    0, getHeight(), new Color(20, 20, 24, 240)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Recent Activity");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(SELECTED_COLOR);
        panel.add(title, BorderLayout.NORTH);

        JPanel activityList = new JPanel();
        activityList.setLayout(new BoxLayout(activityList, BoxLayout.Y_AXIS));
        activityList.setOpaque(false);
        activityList.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        List<Booking> recentBookings = bookingService.getAllBookings();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm");
        
        int count = 0;
        for (int i = recentBookings.size() - 1; i >= 0 && count < 4; i--) {
            Booking booking = recentBookings.get(i);
            String status = booking.getBookingStatus();
            String icon = "CHECKED_OUT".equals(status) ? "✓" : "CHECKED_IN".equals(status) ? "🔑" : "📅";
            Date date = booking.getBookingDate();
            String dateStr = date != null ? sdf.format(date) : "N/A";
            
            activityList.add(createActivityItem(icon, booking.getGuestName(), "Room " + booking.getRoomNumber(), dateStr));
            count++;
        }
        
        if (count == 0) {
            JLabel noActivity = new JLabel("No recent activity");
            noActivity.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            noActivity.setForeground(new Color(120, 120, 120));
            activityList.add(noActivity);
        }

        panel.add(activityList, BorderLayout.CENTER);

        return panel;
    }
    
    private JPanel createActivityItem(String icon, String name, String room, String time) {
        JPanel item = new JPanel(new BorderLayout(8, 0));
        item.setOpaque(false);
        item.setMaximumSize(new Dimension(500, 35));
        item.setBorder(new EmptyBorder(5, 8, 5, 8));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        item.add(iconLabel, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        nameLabel.setForeground(Color.WHITE);
        textPanel.add(nameLabel);
        
        JLabel roomLabel = new JLabel(room);
        roomLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        roomLabel.setForeground(new Color(160, 160, 160));
        textPanel.add(roomLabel);
        
        item.add(textPanel, BorderLayout.CENTER);

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        timeLabel.setForeground(new Color(120, 120, 120));
        item.add(timeLabel, BorderLayout.EAST);

        return item;
    }

    private JButton createActionCard(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dark translucent background
                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Draw Border
                if (getModel().isRollover()) {
                    g2.setColor(SELECTED_COLOR);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 30, 30);
                }

                super.paintComponent(g2);
                g2.dispose();
            }
        };

        button.setPreferredSize(new Dimension(300, 80));
        button.setFont(new Font("Serif", Font.BOLD, 20));
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    // --- Helper Components (Reused from Admin) ---

    private JButton createNavButton(String text, boolean isSelected) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                int margin = 8;
                int width = getWidth() - (margin * 2);
                int height = getHeight();
                
                if (getBackground().getAlpha() > 0 || getBackground().equals(SELECTED_COLOR)) {
                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.fillRoundRect(margin + 2, 2, width, height, 12, 12);
                }
                
                g2.setColor(getBackground());
                g2.fillRoundRect(margin, 0, width, height, 12, 12);
                
                if (getBackground().equals(SELECTED_COLOR)) {
                    g2.setColor(new Color(SELECTED_TEXT_COLOR.getRed(), SELECTED_TEXT_COLOR.getGreen(), SELECTED_TEXT_COLOR.getBlue(), 150));
                    g2.fillRoundRect(margin + 5, 8, 3, height - 16, 3, 3);
                }

                super.paintComponent(g2);
                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 44));
        button.setPreferredSize(new Dimension(200, 44));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        if (isSelected) {
            button.setBackground(SELECTED_COLOR);
            button.setForeground(SELECTED_TEXT_COLOR);
        } else {
            button.setBackground(DEFAULT_COLOR);
            button.setForeground(new Color(220, 220, 220));
        }

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(SELECTED_COLOR)) {
                    button.setBackground(HOVER_COLOR);
                    button.repaint();
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(SELECTED_COLOR)) {
                    button.setBackground(DEFAULT_COLOR);
                    button.repaint();
                }
            }
        });

        return button;
    }

    private JButton createSmallButton(String text, Color bgColor) {
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 10));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        return button;
    }

    private JPanel createPlaceholderPanel(String text) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 24));
        l.setForeground(Color.WHITE);
        p.add(l);
        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReceptionistDashboardFrame::new);
    }
}