package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

public class CheckoutPanel extends JPanel {

    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color LABEL_COLOR = GOLD_COLOR;
    private final Color FIELD_BORDER_COLOR = GOLD_COLOR;
    private final Color TEXT_COLOR = Color.white;
    private final Color FORM_BACKGROUND = new Color(0, 0, 0, 150);

    private JComboBox<String> guestCombo;
    private JComboBox<String> roomCombo;
    private JTextField roomRateField, taxesField, discountField, totalField, nightsField;
    private JCheckBox restaurantService, spaService, roomService, poolService;
    private JComboBox<String> paymentMethodCombo, paymentStatusCombo;
    private JDateChooser checkInChooser, checkOutChooser;

    private BufferedImage backgroundImage;
    private InvoiceTablePanel invoicePanel;

    public CheckoutPanel() {
        invoicePanel = new InvoiceTablePanel(); // shared table panel

        try {
            URL imageUrl = getClass().getResource("/reception.jpg");
            if (imageUrl != null) {
                backgroundImage = ImageIO.read(imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);

        JPanel formContainer = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FORM_BACKGROUND);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        formContainer.setOpaque(false);
        formContainer.setBorder(new EmptyBorder(30, 30, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setOpaque(false);
        formWrapper.add(formContainer, gbc);
        backgroundPanel.add(formWrapper, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Guest Checkout / Invoice");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(GOLD_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(0, 60, 30, 15);
        formContainer.add(titleLabel, gbc);

        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy++;

        // ------------------ Form Fields ------------------
        addLabel(formContainer, "Guest Name", gbc, 0, gbc.gridy, 1);
        gbc.gridx = 1;
        guestCombo = addComboBox(formContainer,
                new String[]{"John Doe", "Jane Smith", "Michael Brown", "Alice Green"},
                "Select Name", gbc, 1, gbc.gridy, 1);

        gbc.gridy++;
        gbc.gridx = 0;
        addLabel(formContainer, "Invoice ID", gbc, 0, gbc.gridy, 1);
        gbc.gridx = 1;
        addTextFieldWithPlaceholder(formContainer, "INV-001", gbc, 1, gbc.gridy, 1);

        gbc.gridy++;
        gbc.gridx = 0;
        addLabel(formContainer, "Room Number", gbc, 0, gbc.gridy, 1);
        gbc.gridx = 1;
        roomCombo = addComboBox(formContainer,
                new String[]{"101", "102", "201", "205", "301", "Penthouse-1"},
                "101", gbc, 1, gbc.gridy, 1);

        gbc.gridy++;
        gbc.gridx = 0;
        addLabel(formContainer, "Check-in Date", gbc, 0, gbc.gridy, 1);
        gbc.gridx = 1;
        checkInChooser = addDateChooser(formContainer, gbc, 1, gbc.gridy);

        gbc.gridy++;
        gbc.gridx = 0;
        addLabel(formContainer, "Check-out Date", gbc, 0, gbc.gridy, 1);
        gbc.gridx = 1;
        checkOutChooser = addDateChooser(formContainer, gbc, 1, gbc.gridy);

        gbc.gridy++;
        gbc.gridx = 0;
        addLabel(formContainer, "Room Rate ($)", gbc, 0, gbc.gridy, 1);
        gbc.gridx = 1;
        roomRateField = addTextFieldWithPlaceholder(formContainer,
                "Enter room rate", gbc, 1, gbc.gridy, 1);

        gbc.gridy++;
        gbc.gridx = 0;
        addLabel(formContainer, "Extra Services", gbc, 0, gbc.gridy, 1);
        gbc.gridx = 1;
        JPanel servicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        servicePanel.setOpaque(false);

        restaurantService = new JCheckBox("Restaurant");
        spaService = new JCheckBox("Spa");
        roomService = new JCheckBox("Room Service");
        poolService = new JCheckBox("Pool");

        restaurantService.setForeground(Color.BLACK);
        spaService.setForeground(Color.BLACK);
        roomService.setForeground(Color.BLACK);
        poolService.setForeground(Color.BLACK);

        servicePanel.add(restaurantService);
        servicePanel.add(spaService);
        servicePanel.add(roomService);
        servicePanel.add(poolService);

        formContainer.add(servicePanel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        addLabel(formContainer, "Taxes (%)", gbc, 0, gbc.gridy, 1);
        gbc.gridx = 1;
        taxesField = addTextFieldWithPlaceholder(formContainer,
                "e.g., 10", gbc, 1, gbc.gridy, 1);

        gbc.gridy++;
        gbc.gridx = 0;
        addLabel(formContainer, "Discount (%)", gbc, 0, gbc.gridy, 1);
        gbc.gridx = 1;
        discountField = addTextFieldWithPlaceholder(formContainer,
                "e.g., 5", gbc, 1, gbc.gridy, 1);

        gbc.gridy++;
        gbc.gridx = 0;
        addLabel(formContainer, "Final Total ($)", gbc, 0, gbc.gridy, 1);
        gbc.gridx = 1;
        totalField = addTextFieldWithPlaceholder(formContainer,
                "", gbc, 1, gbc.gridy, 1);
        totalField.setEditable(false);

        gbc.gridy++;
        gbc.gridx = 0;
        addLabel(formContainer, "Payment Method", gbc, 0, gbc.gridy, 1);
        gbc.gridx = 1;
        paymentMethodCombo = addComboBox(formContainer,
                new String[]{"Cash", "Card"},
                "Cash", gbc, 1, gbc.gridy, 1);

        gbc.gridy++;
        gbc.gridx = 0;
        addLabel(formContainer, "Payment Status", gbc, 0, gbc.gridy, 1);
        gbc.gridx = 1;
        paymentStatusCombo = addComboBox(formContainer,
                new String[]{"Paid", "Pending"},
                "Pending", gbc, 1, gbc.gridy, 1);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        // ------------------ Buttons ------------------
        JButton generateBtn = new JButton("✔️ Generate Invoice");
        generateBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        generateBtn.setBackground(GOLD_COLOR);
        generateBtn.addActionListener(e -> generateInvoice());
        formContainer.add(generateBtn, gbc);

        gbc.gridy++;
        JButton viewBtn = new JButton("📄 View All Invoices");
        viewBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        viewBtn.setBackground(GOLD_COLOR);
        viewBtn.addActionListener(e -> {
            JFrame window = new JFrame("Invoice List");
            window.add(invoicePanel);
            window.setSize(1000, 800);
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
        formContainer.add(viewBtn, gbc);

        gbc.gridy++;
        JButton clearBtn = new JButton("🧹 Clear Form");
        clearBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        clearBtn.setBackground(Color.GRAY);
        clearBtn.setForeground(Color.WHITE);
        clearBtn.addActionListener(e -> clearForm());
        formContainer.add(clearBtn, gbc);
    }

    // ------------------ HELPERS ------------------
    private void addLabel(JPanel parent, String text, GridBagConstraints gbc, int x, int y, int width) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(LABEL_COLOR);
        GridBagConstraints lb = (GridBagConstraints) gbc.clone();
        lb.gridx = x;
        lb.gridy = y;
        lb.gridwidth = width;
        lb.anchor = GridBagConstraints.WEST;
        parent.add(label, lb);
    }

    private JTextField addTextFieldWithPlaceholder(JPanel parent,
                                                   String placeholder,
                                                   GridBagConstraints gbc,
                                                   int x, int y, int width) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(Color.GRAY);
        field.setCaretColor(GOLD_COLOR);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        field.setText(placeholder);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_COLOR);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });

        GridBagConstraints f = (GridBagConstraints) gbc.clone();
        f.gridx = x;
        f.gridy = y;
        f.gridwidth = width;
        parent.add(field, f);
        return field;
    }

    private JDateChooser addDateChooser(JPanel parent, GridBagConstraints gbc, int x, int y) {
        JDateChooser chooser = new JDateChooser();
        chooser.setDateFormatString("yyyy-MM-dd");
        chooser.setOpaque(false);
        chooser.setBorder(BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1));
        GridBagConstraints f = (GridBagConstraints) gbc.clone();
        f.gridx = x;
        f.gridy = y;
        parent.add(chooser, f);
        return chooser;
    }

    private JComboBox<String> addComboBox(JPanel parent, String[] items,
                                          String defaultItem,
                                          GridBagConstraints gbc,
                                          int x, int y, int width) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setSelectedItem(defaultItem);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBorder(BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1));
        GridBagConstraints c = (GridBagConstraints) gbc.clone();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = width;
        parent.add(combo, c);
        return combo;
    }

    // ------------------ GENERATE INVOICE ------------------
    private void generateInvoice() {
        Double roomRate = parseDoubleStrict(roomRateField.getText(), "Room Rate", false);
        if (roomRate == null) return;

        int nights = 1;
        try {
            nights = Integer.parseInt(nightsField.getText());
            if (nights < 1) nights = 1;
        } catch (NumberFormatException e) {
            nights = 1;
        }

        double extras = 0;
        if (restaurantService.isSelected()) extras += 50;
        if (spaService.isSelected()) extras += 30;
        if (roomService.isSelected()) extras += 20;
        if (poolService.isSelected()) extras += 40;

        Double taxesPercent = parseDoubleStrict(taxesField.getText(), "Taxes (%)", true);
        if (taxesPercent == null) return;

        Double discountPercent = parseDoubleStrict(discountField.getText(), "Discount (%)", true);
        if (discountPercent == null) return;

        double subtotal = (roomRate * nights) + extras;
        double taxesAmount = subtotal * taxesPercent / 100.0;
        double discountAmount = subtotal * discountPercent / 100.0;
        double total = subtotal + taxesAmount - discountAmount;

        totalField.setText(String.format("%.2f", total));

        String checkIn = checkInChooser.getDate() != null ?
                new SimpleDateFormat("yyyy-MM-dd").format(checkInChooser.getDate()) : "";

        invoicePanel.addInvoice(
                "INV-" + System.currentTimeMillis() % 100000,
                guestCombo.getSelectedItem().toString(),
                roomCombo.getSelectedItem().toString(),
                nights,
                total,
                paymentMethodCombo.getSelectedItem().toString(),
                paymentStatusCombo.getSelectedItem().toString(),
                checkIn
        );

        JOptionPane.showMessageDialog(this, "Invoice added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // ------------------ CLEAR FORM ------------------
    private void clearForm() {
        roomRateField.setText("Enter room rate"); roomRateField.setForeground(Color.GRAY);
        nightsField.setText("1"); nightsField.setForeground(Color.GRAY);
        taxesField.setText("e.g., 10"); taxesField.setForeground(Color.GRAY);
        discountField.setText("e.g., 5"); discountField.setForeground(Color.GRAY);
        totalField.setText("");

        guestCombo.setSelectedIndex(0);
        roomCombo.setSelectedIndex(0);
        paymentMethodCombo.setSelectedIndex(0);
        paymentStatusCombo.setSelectedIndex(0);

        checkInChooser.setDate(null);
        checkOutChooser.setDate(null);

        restaurantService.setSelected(false);
        spaService.setSelected(false);
        roomService.setSelected(false);
        poolService.setSelected(false);
    }

    // ------------------ PARSE DOUBLE ------------------
    private Double parseDoubleStrict(String val, String fieldName, boolean isPercentage) {
        try {
            val = val.trim();
            if (val.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid number for " + fieldName + ".",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            if (isPercentage && val.contains("%")) {
                JOptionPane.showMessageDialog(this,
                        "Please enter " + fieldName + " as number only, without % sign.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            double d = Double.parseDouble(val);
            if (d < 0) {
                JOptionPane.showMessageDialog(this,
                        fieldName + " cannot be negative.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            return d;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number for " + fieldName + ".",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Guest Checkout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.add(new CheckoutPanel());
        frame.setVisible(true);
    }
}
