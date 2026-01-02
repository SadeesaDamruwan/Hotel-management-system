package com.hotel.management.view.admin;

import com.hotel.management.model.Staff;
import com.hotel.management.service.StaffService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.text.JTextComponent;

public class RemoveStaffPanel extends JPanel {

    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color LABEL_COLOR = GOLD_COLOR;
    private final Color FIELD_BORDER_COLOR = GOLD_COLOR;
    private final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color FORM_BACKGROUND = new Color(0, 0, 0, 150);
    
    private JTextField staffIdField;
    private JTextField nicField;
    private JComboBox<String> titleComboBox;
    private JComboBox<String> roleComboBox;
    
    private final StaffService staffService;
    private Timer lookupTimer; 

    public RemoveStaffPanel() {
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

        JLabel titleLabel = new JLabel("Remove Staff");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(GOLD_COLOR);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2; 
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 15, 40, 15);
        formContainer.add(titleLabel, gbc);

        gbc.insets = new Insets(5, 15, 5, 15);

        gbc.gridy++;
        addLabel(formContainer, "NIC/Passport No.", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        nicField = addTextField(formContainer, "", gbc, 0, gbc.gridy, 2);
        
        nicField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                scheduleNicLookup();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                scheduleNicLookup();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                scheduleNicLookup();
            }
            
            private void scheduleNicLookup() {
                if (lookupTimer != null && lookupTimer.isRunning()) {
                    lookupTimer.stop();
                }
                
                lookupTimer = new Timer(300, evt -> {
                    String nic = getFieldText(nicField);
                    if (!nic.isEmpty() && nic.length() >= 3) { 
                        lookupStaffByNic(nic);
                    }
                });
                lookupTimer.setRepeats(false);
                lookupTimer.start();
            }
        });
        
        nicField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                String nic = getFieldText(nicField);
                if (!nic.isEmpty()) {
                    lookupStaffByNic(nic);
                }
            }
        });
        
        nicField.addActionListener(e -> {
            String nic = getFieldText(nicField);
            if (!nic.isEmpty()) {
                lookupStaffByNic(nic);
            }
        });

        gbc.gridy++;
        addLabel(formContainer, "Staff Id", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        staffIdField = addTextField(formContainer, "", gbc, 0, gbc.gridy, 2);

        gbc.gridy++;
        gbc.insets = new Insets(20, 15, 5, 15); 

        titleComboBox = addComboBox(formContainer, new String[]{"Mr", "Ms", "Mrs"}, "Mr", gbc, 0, gbc.gridy, 1);
        roleComboBox = addComboBox(formContainer, new String[]{"Role", "Admin", "Receptionist", "Maintenance Staff", "Cleaning Staff"}, "Role", gbc, 1, gbc.gridy, 1);

        gbc.gridy++;
        gbc.insets = new Insets(40, 15, 10, 15);
        gbc.gridwidth = 2;

        JButton removeBtn = new JButton("   Remove") {
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
                    bgColor = new Color(160, 140, 140); 
                } else if (isHovered) {
                    bgColor = new Color(210, 190, 190); 
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
                    g2.setColor(new Color(255, 200, 200, 30));
                    g2.fillRoundRect(offset + 1, offset + 1, getWidth() - offset - 2, getHeight() / 2, 10, 10);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        removeBtn.setPreferredSize(new Dimension(160, 45));
        removeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        removeBtn.setForeground(new Color(181, 139, 0)); 
        removeBtn.setFocusPainted(false);
        removeBtn.setBorderPainted(false);
        removeBtn.setContentAreaFilled(false);
        removeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        removeBtn.setIcon(new TrashIcon());
        
        removeBtn.addActionListener(e -> handleRemoveStaff());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(removeBtn);

        formContainer.add(btnPanel, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        formContainer.add(Box.createVerticalGlue(), gbc);
    }
    
    private void lookupStaffByNic(String nic) {
        Staff staff = staffService.findStaffByNic(nic);
        
        if (staff != null) {
            staffIdField.setText(staff.getId());
            staffIdField.setForeground(TEXT_COLOR);
            
            titleComboBox.setSelectedItem(staff.getTitle());
            
            roleComboBox.setSelectedItem(staff.getRole());
            
            System.out.println("✓ Found staff: " + staff.getName() + " (" + staff.getId() + ")");
        } else {
            staffIdField.setText("");
            staffIdField.setForeground(TEXT_COLOR);
            titleComboBox.setSelectedIndex(0);
            roleComboBox.setSelectedIndex(0);
            
        }
    }
    
    private void handleRemoveStaff() {
        String staffId = getFieldText(staffIdField);
        
        if (staffId.isEmpty()) {
            showError("Please enter Staff ID");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove staff member with ID: " + staffId + "?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (staffService.deleteStaff(staffId)) {
                JOptionPane.showMessageDialog(this,
                        "Staff member removed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                clearForm();
            } else {
                showError("Failed to remove staff. Staff ID not found.");
            }
        }
    }
    
    private void clearForm() {
        staffIdField.setText("");
        staffIdField.setForeground(TEXT_COLOR);
        
        nicField.setText("");
        nicField.setForeground(TEXT_COLOR);
        
        titleComboBox.setSelectedIndex(0);
        roleComboBox.setSelectedIndex(0);
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
                "Error",
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

    private class TrashIcon implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getForeground());
            int w = 12;
            int h = 14;
            int yOffset = 2;

            g2.fillRect(x + 3, y, 6, 2); 
            g2.fillRect(x, y + 2, w, 2); 
            g2.fillRect(x + 2, y + 4, 8, 10); 

            // Lines
            g2.setColor(new Color(180, 180, 180));
            g2.fillRect(x + 4, y + 6, 1, 6);
            g2.fillRect(x + 7, y + 6, 1, 6);

            g2.dispose();
        }

        @Override
        public int getIconWidth() { return 12; }
        @Override
        public int getIconHeight() { return 16; }
    }
}