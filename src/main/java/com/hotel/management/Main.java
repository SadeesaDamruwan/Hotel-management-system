package com.hotel.management;
import com.hotel.management.view.LoginSelectionFrame;

import javax.swing.*;
public class Main {
    
    public static void main(String[] args) {
        // Set look and feel for better UI consistency across platforms
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
        
        // Launch the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginSelectionFrame loginFrame = new LoginSelectionFrame();
            loginFrame.setVisible(true);
        });
    }
}
