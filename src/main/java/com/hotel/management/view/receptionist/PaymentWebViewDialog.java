package com.hotel.management.view.receptionist;

import com.hotel.management.service.PayHereRetrievalService;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PaymentWebViewDialog extends JDialog {
    
    private WebView webView;
    private WebEngine webEngine;
    private String currentUrl;
    private boolean paymentCompleted = false;
    private boolean paymentSuccessful = false;
    private String paymentId = null;
    
    private final Color GOLD_COLOR = new Color(255, 180, 60);
    
    private static boolean javaFXInitialized = false;

    private static JFXPanel keepAlivePanel = null;
    
    public PaymentWebViewDialog(Frame owner, String paymentHtmlOrUrl) {
        super(owner, "Payment Gateway", true);
        
        if (!javaFXInitialized) {
            try {
                
                keepAlivePanel = new JFXPanel();
              
                Platform.setImplicitExit(false);
                javaFXInitialized = true;
                System.out.println("JavaFX Platform initialized with implicit exit disabled");
            } catch (Exception e) {
                System.err.println("JavaFX already initialized or error: " + e.getMessage());
            }
        } else {
            System.out.println("JavaFX Platform already initialized, reusing...");
        }
        
        setSize(900, 700);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(
                    PaymentWebViewDialog.this,
                    "Are you sure you want to cancel the payment?",
                    "Cancel Payment",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (result == JOptionPane.YES_OPTION) {
                    paymentCompleted = true;
                    paymentSuccessful = false;
                    dispose();
                }
            }
        });
        
        initComponents(paymentHtmlOrUrl);
    }
    
    private void initComponents(String paymentHtmlOrUrl) {
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(30, 30, 30));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel("Secure Payment Gateway");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(GOLD_COLOR);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        
        final JFXPanel fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);
        
        Platform.runLater(() -> {
            System.out.println("Platform.runLater executing - creating WebView...");
            try {
                webView = new WebView();
                webEngine = webView.getEngine();
                System.out.println("WebView and WebEngine created successfully");
                
                webEngine.setJavaScriptEnabled(true);
                
                webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
                    currentUrl = newValue;
                    System.out.println("URL Changed: " + newValue);
                    
                    if (newValue != null) {
                        if (newValue.contains("payment/return") || 
                            newValue.contains("payment_id") || 
                            newValue.contains("status=success")) {
                            
                            paymentSuccessful = true;
                            paymentCompleted = true;
                            
                            System.out.println("Extracting payment ID from URL: " + newValue);
                            
                            if (newValue.contains("?")) {
                                String queryString = newValue.split("\\?")[1];
                                System.out.println("Query string: " + queryString);
                                
                                String orderId = null;
                                String statusCode = null;
                                String md5sig = null;
                                
                                String[] params = queryString.split("&");
                                for (String param : params) {
                                    System.out.println("Checking parameter: " + param);
                                    
                                    if (param.startsWith("payment_id=")) {
                                        paymentId = param.substring("payment_id=".length());
                                        System.out.println("Found payment_id: " + paymentId);
                                    } else if (param.startsWith("order_id=")) {
                                        orderId = param.substring("order_id=".length());
                                        System.out.println("Found order_id: " + orderId);
                                    } else if (param.startsWith("status_code=")) {
                                        statusCode = param.substring("status_code=".length());
                                        System.out.println("Found status_code: " + statusCode);
                                    } else if (param.startsWith("md5sig=")) {
                                        md5sig = param.substring("md5sig=".length());
                                        System.out.println("Found md5sig: " + md5sig);
                                    }
                                }
                                
                                if (paymentId == null) {
                                    paymentId = orderId;
                                    System.out.println("payment_id not found, using order_id as reference");
                                }
                            }
                            
                            System.out.println("Final payment ID: " + paymentId);
                            
                            String actualPaymentId = paymentId; 
                            
                            Platform.runLater(() -> {
                                try {
                                    Thread.sleep(2000);
                                    
                                    System.out.println("Attempting to extract payment ID from PayHere page...");
                                    
                                    Object result = webEngine.executeScript(
                                        "var paymentIdElement = document.body.textContent.match(/Payment ID[\\s:#]*([0-9]+)/i);" +
                                        "if (paymentIdElement && paymentIdElement[1]) {" +
                                        "    paymentIdElement[1];" +
                                        "} else {" +
                                        "    var allText = document.body.innerText;" +
                                        "    var match = allText.match(/#([0-9]{12})/i);" +
                                        "    match ? match[1] : null;" +
                                        "}"
                                    );
                                    
                                    String extractedPaymentId = null;
                                    if (result != null) {
                                        extractedPaymentId = result.toString();
                                        System.out.println("Extracted payment ID from page: " + extractedPaymentId);
                                        
                                        paymentId = extractedPaymentId;
                                    } else {
                                        System.out.println("Could not extract payment ID from page HTML");
                                    }
                                    
                                    final String displayPaymentId = extractedPaymentId != null ? extractedPaymentId : paymentId;
                                    
                                    SwingUtilities.invokeLater(() -> {
                                        dispose();
                                    });
                                    
                                } catch (Exception e) {
                                    System.err.println("Error extracting payment ID: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            });
                        } else if (newValue.contains("payment/cancel") || 
                                   newValue.contains("status=cancel")) {
                            
                            paymentSuccessful = false;
                            paymentCompleted = true;
                            
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(
                                    PaymentWebViewDialog.this,
                                    "Payment was cancelled.",
                                    "Cancelled",
                                    JOptionPane.WARNING_MESSAGE
                                );
                                dispose();
                            });
                        }
                    }
                });
                
                webEngine.getLoadWorker().exceptionProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        System.err.println("WebView error: " + newValue.getMessage());
                    }
                });
                
                Scene scene = new Scene(webView);
                fxPanel.setScene(scene);
                System.out.println("Scene created and set to JFXPanel");
                
                System.out.println("About to load content...");
                if (paymentHtmlOrUrl.startsWith("<!DOCTYPE") || paymentHtmlOrUrl.startsWith("<html")) {
                    System.out.println("Loading HTML content (length: " + paymentHtmlOrUrl.length() + ")");
                    webEngine.loadContent(paymentHtmlOrUrl);
                } else {
                    System.out.println("Loading URL: " + paymentHtmlOrUrl);
                    webEngine.load(paymentHtmlOrUrl);
                }
                System.out.println("Content load initiated");
            } catch (Exception e) {
                System.err.println("Error initializing WebView: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    public boolean isPaymentSuccessful() {
        return paymentSuccessful;
    }
    
    public boolean isPaymentCompleted() {
        return paymentCompleted;
    }
    
    public String getPaymentId() {
        return paymentId;
    }
    
    public String getCurrentUrl() {
        return currentUrl;
    }
}
