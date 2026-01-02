package com.hotel.management.view.admin;

import com.hotel.management.model.Staff;
import com.hotel.management.service.EmailService;
import com.hotel.management.service.StaffService;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import javax.swing.text.JTextComponent;

public class AddStaffPanel extends JPanel {

    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color LABEL_COLOR = GOLD_COLOR;
    private final Color FIELD_BORDER_COLOR = GOLD_COLOR;
    private final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color FORM_BACKGROUND = new Color(0, 0, 0, 150);
    private final Color CALENDAR_BG = new Color(40, 40, 40);
    
    private JTextField nameField;
    private JDateChooser dobField;
    private JTextField staffIdField;
    private JTextField nicField;
    private JComboBox<String> titleComboBox;
    private JComboBox<String> roleComboBox;
    private JTextField emailField;
    private JTextField phoneField;
    private JCheckBox sendEmailCheckbox;
    
    private final StaffService staffService;

    public AddStaffPanel() {
        this.staffService = new StaffService();
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel formContainer = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FORM_BACKGROUND);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        formContainer.setOpaque(false);
        formContainer.setBorder(new EmptyBorder(40, 60, 40, 60));

        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.fill = GridBagConstraints.BOTH;
        mainGbc.weightx = 1.0;
        mainGbc.weighty = 1.0;
        add(formContainer, mainGbc);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Add New Staff");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(GOLD_COLOR);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 15, 40, 15);
        formContainer.add(titleLabel, gbc);

        gbc.insets = new Insets(5, 15, 5, 15);

        gbc.gridy++;
        addLabel(formContainer, "Full Name", gbc, 0, gbc.gridy, 4);
        gbc.gridy++;
        nameField = addTextField(formContainer, "", gbc, 0, gbc.gridy, 4);

        gbc.gridy++;
        addLabel(formContainer, "Date Of Birth", gbc, 0, gbc.gridy, 2);
        addLabel(formContainer, "Staff Id", gbc, 2, gbc.gridy, 2);

        gbc.gridy++;
        dobField = addDatePicker(formContainer, gbc, 0, gbc.gridy, 2);

        String autoId = "STF-" + (1000 + new Random().nextInt(9000)); 
        staffIdField = addAutoIdField(formContainer, autoId, gbc, 2, gbc.gridy, 2);

        gbc.gridy++;
        addLabel(formContainer, "NIC/Passport No.", gbc, 0, gbc.gridy, 2);

        gbc.gridy++;
        nicField = addTextField(formContainer, "", gbc, 0, gbc.gridy, 2);

        titleComboBox = addComboBox(formContainer, new String[]{"Mr", "Ms", "Mrs"}, "Mr", gbc, 2, gbc.gridy, 1);
        roleComboBox = addComboBox(formContainer, new String[]{"Role", "Admin", "Receptionist", "Maintenance Staff", "Cleaning Staff"}, "Role", gbc, 3, gbc.gridy, 1);

        gbc.gridy++;
        addLabel(formContainer, "Email Address", gbc, 0, gbc.gridy, 2);
        addLabel(formContainer, "Phone number", gbc, 2, gbc.gridy, 2);

        gbc.gridy++;
        emailField = addTextField(formContainer, "", gbc, 0, gbc.gridy, 2);
        phoneField = addTextField(formContainer, "", gbc, 2, gbc.gridy, 2);

        gbc.gridy++;
        gbc.insets = new Insets(20, 15, 5, 15);
        
        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        checkboxPanel.setOpaque(false);
        
        sendEmailCheckbox = new JCheckBox("Send welcome email to staff member");
        sendEmailCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sendEmailCheckbox.setForeground(TEXT_COLOR);
        sendEmailCheckbox.setOpaque(false);
        sendEmailCheckbox.setFocusPainted(false);
        sendEmailCheckbox.setSelected(true); 
        sendEmailCheckbox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        sendEmailCheckbox.setIcon(new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_BORDER_COLOR);
                g2.drawRoundRect(x, y, 16, 16, 3, 3);
                g2.dispose();
            }
            @Override
            public int getIconWidth() { return 18; }
            @Override
            public int getIconHeight() { return 18; }
        });
        
        sendEmailCheckbox.setSelectedIcon(new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GOLD_COLOR);
                g2.fillRoundRect(x, y, 16, 16, 3, 3);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawPolyline(new int[]{x + 4, x + 7, x + 13}, new int[]{y + 8, y + 12, y + 5}, 3);
                g2.dispose();
            }
            @Override
            public int getIconWidth() { return 18; }
            @Override
            public int getIconHeight() { return 18; }
        });
        
        checkboxPanel.add(sendEmailCheckbox);
        
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        formContainer.add(checkboxPanel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(30, 15, 10, 15);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        buttonPanel.setOpaque(false);

        JButton addBtn = new JButton("Add") {
            private boolean isHovered = false;
            private boolean isPressed = false;
            
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }
                    
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                    
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent e) {
                        isPressed = true;
                        repaint();
                    }
                    
                    @Override
                    public void mouseReleased(java.awt.event.MouseEvent e) {
                        isPressed = false;
                        repaint();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor;
                if (isPressed) {
                    bgColor = new Color(140, 140, 140); 
                } else if (isHovered) {
                    bgColor = new Color(200, 200, 200); 
                } else {
                    bgColor = new Color(180, 180, 180); 
                }
                
                if (!isPressed) {
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.fillRoundRect(2, 3, getWidth() - 2, getHeight() - 2, 10, 10);
                }
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, bgColor,
                    0, getHeight(), bgColor.darker()
                );
                g2.setPaint(gradient);
                
                int offset = isPressed ? 2 : 0;
                g2.fillRoundRect(offset, offset, getWidth() - offset, getHeight() - offset, 10, 10);
                
                if (isHovered && !isPressed) {
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillRoundRect(offset + 1, offset + 1, getWidth() - offset - 2, getHeight() / 2, 10, 10);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        addBtn.setPreferredSize(new Dimension(120, 40));
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.setForeground(new Color(181, 139, 0));
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setContentAreaFilled(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e -> handleAddStaff());
        buttonPanel.add(addBtn);

        gbc.gridx = 0;
        gbc.gridwidth = 4;
        formContainer.add(buttonPanel, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        formContainer.add(Box.createVerticalGlue(), gbc);
    }

    private void handleAddStaff() {
        String name = getFieldText(nameField);
        Date dobDate = dobField.getDate();
        String dob = "";
        if (dobDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            dob = sdf.format(dobDate);
        }
        String staffId = staffIdField.getText();
        String nic = getFieldText(nicField);
        String title = (String) titleComboBox.getSelectedItem();
        String role = (String) roleComboBox.getSelectedItem();
        String email = getFieldText(emailField);
        String phone = getFieldText(phoneField);
        
        if (name.isEmpty() || name.equals("Full Name")) {
            showError("Please enter staff name");
            return;
        }
        
        if (dob.isEmpty()) {
            showError("Please select date of birth");
            return;
        }
        
        if (nic.isEmpty()) {
            showError("Please enter NIC/Passport number");
            return;
        }
        
        if (role.equals("Role")) {
            showError("Please select a role");
            return;
        }
        
        if (email.isEmpty()) {
            showError("Please enter email address");
            return;
        }
        
        if (phone.isEmpty()) {
            showError("Please enter phone number");
            return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String joinDate = sdf.format(new Date());
        
        Staff staff = new Staff(staffId, name, dob, nic, title, role, email, phone, joinDate);
        
        if (staffService.addStaff(staff)) {
            JOptionPane.showMessageDialog(this,
                    "Staff member added successfully!\nStaff ID: " + staffId,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            
            if (sendEmailCheckbox.isSelected()) {
                final String finalEmail = email;
                final String finalName = name;
                final String finalStaffId = staffId;
                final String finalRole = role;
                final String finalPhone = phone;
                final String finalJoinDate = joinDate;
                
                new Thread(() -> {
                    EmailService.sendStaffWelcomeEmail(
                        finalEmail,
                        finalName,
                        finalStaffId,
                        finalRole,
                        finalPhone,
                        finalJoinDate
                    );
                }).start();
            }
            
            clearForm();
        } else {
            showError("Failed to add staff. Staff ID may already exist.");
        }
    }
    
    private void clearForm() {
        nameField.setText("");
        nameField.setForeground(TEXT_COLOR);
        
        dobField.setDate(null);
        
        String newId = "STF-" + (1000 + new Random().nextInt(9000));
        staffIdField.setText(newId);
        
        nicField.setText("");
        nicField.setForeground(TEXT_COLOR);
        
        titleComboBox.setSelectedIndex(0);
        roleComboBox.setSelectedIndex(0);
        
        emailField.setText("");
        emailField.setForeground(TEXT_COLOR);
        
        phoneField.setText("");
        phoneField.setForeground(TEXT_COLOR);
        
        sendEmailCheckbox.setSelected(true);
    }
    
    private String getFieldText(JTextField field) {
        String text = field.getText().trim();
        Color fg = field.getForeground();
        
        if (fg.equals(PLACEHOLDER_COLOR)) {
            return "";
        }
        
        return text;
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }


    private void addLabel(JPanel parent, String text, GridBagConstraints gbc, int x, int y, int width) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(LABEL_COLOR);

        GridBagConstraints labelGbc = (GridBagConstraints) gbc.clone();
        labelGbc.gridx = x;
        labelGbc.gridy = y;
        labelGbc.gridwidth = width;
        labelGbc.insets = new Insets(10, 15, 5, 15);
        parent.add(label, labelGbc);
    }

    private JTextField addAutoIdField(JPanel parent, String value, GridBagConstraints gbc, int x, int y, int width) {
        JTextField textField = new JTextField(value) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };

        textField.setOpaque(false);
        textField.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textField.setForeground(TEXT_COLOR); 
        textField.setCaretColor(GOLD_COLOR);
        textField.setEditable(false); 
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        GridBagConstraints fieldGbc = (GridBagConstraints) gbc.clone();
        fieldGbc.gridx = x;
        fieldGbc.gridy = y;
        fieldGbc.gridwidth = width;
        parent.add(textField, fieldGbc);
        
        return textField;
    }

    private JDateChooser addDatePicker(JPanel parent, GridBagConstraints gbc, int x, int y, int width) {
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.getJCalendar().setWeekOfYearVisible(false);
        dateChooser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        GridBagConstraints fieldGbc = (GridBagConstraints) gbc.clone();
        fieldGbc.gridx = x;
        fieldGbc.gridy = y;
        fieldGbc.gridwidth = width;
        parent.add(dateChooser, fieldGbc);
        
        return dateChooser;
    }

    private JTextField addTextField(JPanel parent, String placeholder, GridBagConstraints gbc, int x, int y, int width) {
        JTextField textField = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };

        textField.setOpaque(false);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(GOLD_COLOR);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        if (!placeholder.isEmpty()) {
            textField.setText(placeholder);
            textField.setForeground(PLACEHOLDER_COLOR);
            textField.addFocusListener(new PlaceholderFocusListener(placeholder, textField));
        }

        GridBagConstraints fieldGbc = (GridBagConstraints) gbc.clone();
        fieldGbc.gridx = x;
        fieldGbc.gridy = y;
        fieldGbc.gridwidth = width;
        parent.add(textField, fieldGbc);
        
        return textField;
    }

    private JComboBox<String> addComboBox(JPanel parent, String[] items, String defaultItem, GridBagConstraints gbc, int x, int y, int width) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setSelectedItem(defaultItem);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setForeground(Color.BLACK);
        comboBox.setOpaque(false);

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
                        int w = 8;
                        int h = 5;
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
                popup.setBorder(BorderFactory.createLineBorder(GOLD_COLOR, 1));
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
                    setOpaque(true);
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                } else {
                    setOpaque(true);
                    if (isSelected) {
                        setBackground(GOLD_COLOR);
                        setForeground(Color.BLACK);
                    } else {
                        setBackground(new Color(40, 40, 40));
                        setForeground(Color.WHITE);
                    }
                }
                setBorder(new EmptyBorder(5, 10, 5, 10));
                return this;
            }
        });

        GridBagConstraints comboGbc = (GridBagConstraints) gbc.clone();
        comboGbc.gridx = x;
        comboGbc.gridy = y;
        comboGbc.gridwidth = width;
        parent.add(comboBox, comboGbc);
        
        return comboBox;
    }

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
                component.setForeground(TEXT_COLOR);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (component.getText().isEmpty()) {
                component.setText(placeholder);
                component.setForeground(PLACEHOLDER_COLOR);
            }
        }
    }

}