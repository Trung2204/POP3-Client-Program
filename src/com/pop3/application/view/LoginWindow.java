package com.pop3.application.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LoginWindow {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginWindow() {
        frame = new JFrame("Login");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        // Set custom font
        Font font = new Font("SansSerif", Font.BOLD, 16);
        usernameField.setFont(font);
        passwordField.setFont(font);
        loginButton.setFont(font);

        // Set custom colors
        Color buttonColor = new Color(100, 149, 237); // Cornflower Blue
        loginButton.setBackground(buttonColor);
        loginButton.setForeground(Color.WHITE);

        // Use GridBagLayout for better control over layout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4); // Padding

        // Add components with GridBagConstraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridy++;
        panel.add(usernameField, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridy++;
        panel.add(passwordField, gbc);

        gbc.gridy++;
        panel.add(loginButton, gbc);

        frame.getContentPane().add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);
    }

    public String getUsername() { return usernameField.getText(); }

    public String getPassword() { return new String(passwordField.getPassword()); }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void close() {
        frame.dispose();
    }
}
