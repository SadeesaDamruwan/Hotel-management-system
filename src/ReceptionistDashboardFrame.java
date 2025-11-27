import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class ReceptionistDashboardFrame extends JFrame {

    private BufferedImage backgroundImage;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    // --- Color Constants ---
    private final Color SELECTED_COLOR = new Color(255, 180, 60); // Gold
    private final Color DEFAULT_COLOR = new Color(40, 40, 40);    // Dark Gray
    private final Color HOVER_COLOR = new Color(60, 60, 60);
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
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/Rescources/a170b7e6b576d72403c665e6337322e1.jpg")));
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

        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        container.add(mainPanel, BorderLayout.CENTER);
        backgroundPanel.add(container, BorderLayout.CENTER);
        add(backgroundPanel);

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
        // Add profile circle or similar controls if needed
        JLabel profile = new JLabel("<html><div style='background-color:#444; color:white; padding:5px 10px; border-radius:15px;'>RW</div></html>");
        controls.add(profile);

        panel.add(title, BorderLayout.WEST);
        panel.add(controls, BorderLayout.EAST);
        return panel;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, 700));
        sidebar.setBackground(new Color(30, 30, 30, 200));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.DARK_GRAY));

        // --- Logo Area ---
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(new EmptyBorder(30, 0, 30, 0));

        try {
            BufferedImage logoImg = ImageIO.read(Objects.requireNonNull(getClass().getResource("/Rescources/logo.png")));
            Image scaledLogo = logoImg.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
        } catch (Exception e) {
            logoLabel.setText("<html><div style='text-align:center;'><span style='color:#FFB43C; font-size:24pt;'>innova</span><br><span style='color:gray; font-size:8pt;'>RECEPTION</span></div></html>");
        }
        sidebar.add(logoLabel);

        // --- Menu Buttons ---
        // Dashboard
        JButton dashBtn = createNavButton("Dashboard", true);
        dashBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "Dashboard"));
        sidebar.add(dashBtn);

        // Settings and Profile
        JButton settingsBtn = createNavButton("Settings and profile", false);
        settingsBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "Settings"));
        sidebar.add(settingsBtn);

        // Exams
        JButton examsBtn = createNavButton("Exams", false);
        examsBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "Exams"));
        sidebar.add(examsBtn);

        // Footer
        sidebar.add(Box.createVerticalGlue());
        JPanel featuresPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        featuresPanel.setOpaque(false);
        featuresPanel.add(createSmallButton("Features", new Color(255, 180, 60)));
        featuresPanel.add(createSmallButton("NEW", new Color(255, 100, 100)));
        featuresPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        sidebar.add(featuresPanel);

        return sidebar;
    }

    // --- DASHBOARD HOME (Grid of Actions) ---
    private JPanel createDashboardHome() {
        JPanel content = new JPanel(new GridBagLayout()); // Center the grid
        content.setOpaque(false);

        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 40, 30)); // 3 Rows, 2 Cols, Gaps
        gridPanel.setOpaque(false);

        // Add the 6 main action buttons
        gridPanel.add(createActionCard("Today's Check-Ins"));
        gridPanel.add(createActionCard("Today's Check-Outs"));
        gridPanel.add(createActionCard("Room Status Overview"));
        gridPanel.add(createActionCard("Guest Requests"));
        gridPanel.add(createActionCard("Add New Guest"));
        gridPanel.add(createActionCard("Billing & Payments"));

        content.add(gridPanel);
        return content;
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
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 45));
        button.setPreferredSize(new Dimension(180, 45));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        // Use the custom icon logic if you want icons here, simplified for text-only screenshot match
        // button.setIcon(new SidebarIcon("DEFAULT"));

        if (isSelected) {
            button.setBackground(SELECTED_COLOR);
            button.setForeground(Color.BLACK);
        } else {
            button.setBackground(DEFAULT_COLOR);
            button.setForeground(Color.WHITE);
        }

        // Hover Effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(SELECTED_COLOR)) {
                    button.setBackground(HOVER_COLOR);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(SELECTED_COLOR)) {
                    button.setBackground(DEFAULT_COLOR);
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