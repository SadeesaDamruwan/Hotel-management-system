package com.hotel.management.view.receptionist;

import com.hotel.management.model.ReceptionistStaff;
import com.hotel.management.service.ReceptionistStaffManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReceptionistPanel extends JPanel {

    private ReceptionistStaffManagement backend;

    public ReceptionistPanel(ReceptionistStaffManagement backend) {
        this.backend = backend;

        setLayout(new BorderLayout());
        JLabel title = new JLabel("Receptionist Staff Management", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JTextArea listArea = new JTextArea();
        listArea.setEditable(false);
        refreshList(listArea);

        add(new JScrollPane(listArea), BorderLayout.CENTER);

        JPanel controls = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();

        JButton addBtn = new JButton("Add Receptionist");
        addBtn.addActionListener(e -> {
            backend.addReceptionist(new ReceptionistStaff(
                    idField.getText(),
                    nameField.getText(),
                    contactField.getText()
            ));
            refreshList(listArea);
        });

        JButton removeBtn = new JButton("Remove Receptionist");
        removeBtn.addActionListener(e -> {
            backend.removeReceptionist(idField.getText());
            refreshList(listArea);
        });

        controls.add(new JLabel("ID:"));
        controls.add(idField);
        controls.add(new JLabel("Name:"));
        controls.add(nameField);
        controls.add(new JLabel("Contact:"));
        controls.add(contactField);
        controls.add(addBtn);
        controls.add(removeBtn);

        add(controls, BorderLayout.SOUTH);
    }

    private void refreshList(JTextArea textArea) {
        StringBuilder sb = new StringBuilder();

        for (ReceptionistStaff r : backend.getAllReceptionists()) {
            sb.append("ID: ").append(r.getId())
                    .append(" | Name: ").append(r.getName())
                    .append(" | Contact: ").append(r.getContact())
                    .append("\n");
        }

        textArea.setText(sb.toString());
    }
}
