import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.text.JTextComponent;

// NOTE: Ensure ReceptionistDashboardFrame.java is in the same folder.

public class ReceptionistLoginFrame extends JFrame implements ActionListener {

    // --- Credentials (Temporary) ---
    private static final String REC_USER = "reception";
    private static final String REC_PASS = "1234";

    // --- Color Constants (Light Theme) ---
    private final Color PANEL_BG = new Color(255, 255, 255, 150); // Translucent White
    private final Color TEXT_COLOR = new Color(50, 50, 50);       // Dark Gray Text
    private final Color FIELD_BG = new Color(255, 255, 255, 200); // Lighter field bg
    private final Color BUTTON_COLOR = Color.BLACK;               // Black Button
    private final Color BUTTON_TEXT = Color.WHITE;

    // --- Components ---
    private JTextField userTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    private BufferedImage backgroundImage;

    public ReceptionistLoginFrame() {
        // --- Frame Setup ---
        setTitle("Hotel Management System - Receptionist Login");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- Load Background Image ---
        try {
            // Ensure this image exists in your /Rescources folder
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/Rescources/a170b7e6b576d72403c665e6337322e1.jpg")));
        } catch (Exception e) {
            System.err.println("Background image not found.");
            backgroundImage = null;
        }

        // --- Background Panel ---
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(200, 200, 200)); // Fallback gray
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        // --- Login Container (Glass Effect) ---
        JPanel loginPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PANEL_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40); // Rounded corners

                // Subtle white border for "glass" edge
                g2.setColor(new Color(255, 255, 255, 100));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 40, 40);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        loginPanel.setOpaque(false);
        loginPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // --- 1. Icon ---
        JLabel iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // Custom Receptionist Icon Drawing
        iconLabel.setIcon(new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.BLACK);

                // Draw User/Receptionist Icon Shape
                g2.fillOval(x + 25, y, 30, 30); // Head
                g2.fillArc(x + 10, y + 35, 60, 40, 0, 180); // Body
                // Draw Bell/Desk hint
                g2.fillRect(x, y + 60, 80, 5); // Desk line

                g2.dispose();
            }
            public int getIconWidth() { return 80; }
            public int getIconHeight() { return 70; }
        });
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        loginPanel.add(iconLabel, gbc);

        // --- 2. Title ---
        gbc.gridy++;
        JLabel titleLabel = new JLabel("Receptionist Login");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(0, 0, 5, 0);
        loginPanel.add(titleLabel, gbc);

        // --- 3. Subtitle ---
        gbc.gridy++;
        JLabel subTitle = new JLabel("Welcome onboard with us!");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitle.setForeground(new Color(80, 80, 80));
        subTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(0, 0, 25, 0);
        loginPanel.add(subTitle, gbc);

        // --- 4. Email/User Field ---
        gbc.gridy++;
        JLabel userLabel = new JLabel("Receptionist Email ID");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userLabel.setForeground(TEXT_COLOR);
        gbc.insets = new Insets(5, 5, 5, 5);
        loginPanel.add(userLabel, gbc);

        gbc.gridy++;
        userTextField = createStyledTextField("Enter your username");
        gbc.ipady = 10; // Taller field
        loginPanel.add(userTextField, gbc);

        // --- 5. Password Field ---
        gbc.gridy++;
        gbc.ipady = 0;
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setForeground(TEXT_COLOR);
        loginPanel.add(passLabel, gbc);

        gbc.gridy++;
        passwordField = createStyledPasswordField("Enter your password");
        gbc.ipady = 10;
        loginPanel.add(passwordField, gbc);

        // --- 6. Forgot Password ---
        gbc.gridy++;
        gbc.ipady = 0;
        JButton forgotBtn = new JButton("Forgot Password?");
        forgotBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        forgotBtn.setForeground(new Color(50, 50, 50));
        forgotBtn.setContentAreaFilled(false);
        forgotBtn.setBorderPainted(false);
        forgotBtn.setFocusPainted(false);
        forgotBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotBtn.setHorizontalAlignment(SwingConstants.RIGHT);
        loginPanel.add(forgotBtn, gbc);

        // --- 7. Login Button ---
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 10, 0);
        loginButton = new JButton("Login as Receptionist") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(50, 50, 50));
                } else {
                    g2.setColor(BUTTON_COLOR);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Very rounded

                g2.setColor(BUTTON_TEXT);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y - 2); // Slight visual adjustment

                g2.dispose();
            }
        };
        loginButton.setPreferredSize(new Dimension(200, 45));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(this);
        gbc.ipady = 15;
        loginPanel.add(loginButton, gbc);

        // --- 8. Message Label ---
        gbc.gridy++;
        gbc.ipady = 0;
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginPanel.add(messageLabel, gbc);

        backgroundPanel.add(loginPanel);
        add(backgroundPanel);

        setVisible(true);
    }

    // --- Helper: Styled Fields ---
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(Color.GRAY);
        field.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Padding inside text
        field.addFocusListener(new PlaceholderFocusListener(placeholder, field));
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(Color.GRAY);
        field.setEchoChar((char) 0);
        field.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        field.addFocusListener(new PlaceholderFocusListener(placeholder, field));
        return field;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String user = userTextField.getText();
            String pass = new String(passwordField.getPassword());

            if(user.equals("Enter your username") || pass.equals("Enter your password")) {
                messageLabel.setText("Please enter credentials.");
                return;
            }

            if (REC_USER.equals(user) && REC_PASS.equals(pass)) {
                messageLabel.setForeground(new Color(0, 150, 0));
                messageLabel.setText("Login Successful!");

                // --- LINK TO DASHBOARD ---
                dispose(); // Close Login
                new ReceptionistDashboardFrame(); // Open Dashboard
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Invalid Username or Password.");
            }
        }
    }

    // --- Placeholder Logic ---
    private class PlaceholderFocusListener implements FocusListener {
        private final String placeholder;
        private final JTextComponent component;

        public PlaceholderFocusListener(String placeholder, JTextComponent component) {
            this.placeholder = placeholder;
            this.component = component;
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (component.getText().equals(placeholder)) {
                component.setText("");
                component.setForeground(Color.BLACK);
                if (component instanceof JPasswordField) {
                    ((JPasswordField) component).setEchoChar('•');
                }
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (component.getText().isEmpty()) {
                component.setText(placeholder);
                component.setForeground(Color.GRAY);
                if (component instanceof JPasswordField) {
                    ((JPasswordField) component).setEchoChar((char) 0);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReceptionistLoginFrame::new);
    }
}