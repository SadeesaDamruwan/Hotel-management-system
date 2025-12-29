
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
        import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Objects;


public class AddGuestM extends JPanel {

    private final Color GOLD = new Color(255, 180, 60);
    private final Color PLACEHOLDER = new Color(180, 180, 180);
    private final Color TEXT = Color.WHITE;
    private final Color GLASS_BG = new Color(0, 0, 0, 130);

    private final Image backgroundImage;

    public AddGuestM() {

        // Load background image (change path for your project)
        backgroundImage = new ImageIcon(
                Objects.requireNonNull(getClass().getResource("mybackground.jpg"))
        ).getImage();


        setLayout(new GridBagLayout());
        setOpaque(false);

        JPanel glass = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(GLASS_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        glass.setOpaque(false);
        glass.setBorder(new EmptyBorder(30, 60, 30, 60));

        GridBagConstraints main = new GridBagConstraints();
        main.fill = GridBagConstraints.HORIZONTAL;
        main.insets = new Insets(10, 20, 10, 20);

        // ---------------- TITLE -------------------
        JLabel title = new JLabel("Add New Guest");
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(GOLD);

        main.gridx = 0;
        main.gridy = 0;
        main.gridwidth = 2;
        main.insets = new Insets(0, 20, 40, 20);
        glass.add(title, main);
        main.insets = new Insets(10, 20, 10, 20);

        // ---------------- FULL NAME -------------------
        main.gridy++;
        addLabel(glass, "Full Name", main);

        main.gridy++;
        addField(glass, "Enter full name", main);

        // ---------------- DOB -------------------
        main.gridy++;
        addLabel(glass, "Date of Birth", main);

        main.gridy++;
        addField(glass, "DD/MM/YYYY", main);

        // ---------------- NIC -------------------
        main.gridy++;
        addLabel(glass, "NIC / Passport No", main);

        main.gridy++;
        addField(glass, "NIC / Passport Number", main);

        // ---------------- EMAIL -------------------
        main.gridy++;
        addLabel(glass, "Email Address", main);

        main.gridy++;
        addField(glass, "Email Address", main);

        // ---------------- PHONE NUMBER -------------------
        // PHONE NUMBER (full-width)
        // PHONE NUMBER (full-width)
        main.gridy++;
        addLabel(glass, "Phone Number", main);

        main.gridy++;
        addField(glass, "Enter phone number", main);


        // ---------------- DROPDOWNS -------------------
        main.gridy++;
        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.setOpaque(false);

        // Create dropdowns
        JComboBox<String> titleCombo = (JComboBox<String>) createCombo(new String[]{"Mr", "Ms", "Mrs"}, "Mr");
        titleCombo.setForeground(Color.BLACK);

        JComboBox<String> countryCombo = (JComboBox<String>) createCombo(new String[]{"Sri Lanka", "India", "USA"}, "Country");
        countryCombo.setForeground(Color.BLACK);

// Add to row panel
        row.add(titleCombo);
        row.add(countryCombo);

// Add row to glass panel
        glass.add(row, main);


        // ---------------- BUTTONS -------------------
        main.gridy++;
        main.insets = new Insets(30, 20, 20, 20);

        JButton btn = new JButton("Add") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(GOLD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        glass.add(btn, main);

        // Add to main panel
        add(glass);
    }

    // ============ HELPER METHODS ================
    private void addLabel(JPanel parent, String text, GridBagConstraints gbc) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(GOLD);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        parent.add(lbl, gbc);
    }

    private void addField(JPanel parent, String placeholder, GridBagConstraints gbc) {
        JTextField field = new JTextField(placeholder);
        field.setOpaque(false);
        field.setForeground(PLACEHOLDER);
        field.setCaretColor(GOLD);

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(PLACEHOLDER);
                }
            }
        });

        parent.add(field, gbc);
    }

    private JComboBox<String> createCombo(String[] data, String def) {
        JComboBox<String> box = new JComboBox<>(data);
        box.setSelectedItem(def);
        box.setForeground(TEXT);
        box.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        box.setOpaque(false);

        box.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton b = new JButton("▼");
                b.setForeground(GOLD);
                b.setBorder(null);
                b.setContentAreaFilled(false);
                return b;
            }

            @Override

            protected ComboPopup createPopup() {
                BasicComboPopup popup = new BasicComboPopup((JComboBox) box); // cast FIX
                popup.setBorder(BorderFactory.createLineBorder(GOLD));
                return popup;
            }


        });

        box.setBorder(BorderFactory.createLineBorder(GOLD));

        return box;
    }

    // Paint background image
    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        super.paintComponent(g);
    }
}
