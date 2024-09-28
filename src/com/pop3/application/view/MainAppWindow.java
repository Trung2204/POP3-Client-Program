package com.pop3.application.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainAppWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
    private JButton listMailsButton;
    private JTextArea messageDisplayArea;
    private JButton quitButton;
    private JButton checkStatusButton;
    private JButton checkConnectionButton;
    private JButton  operationsButton;
    private JTextField numMessageField;
    private JButton okButton;
    private JButton resetButton;
    private JMenuItem retrieveItem, markForDeletionItem;

    public MainAppWindow(String username) {
    	// User Avatar
    	ImageIcon userIcon = new ImageIcon(getClass().getResource("/resources/user.png"));
        Image image = userIcon.getImage();
        Image newimg = image.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        userIcon = new ImageIcon(newimg);  // transform it back
        JLabel userAvatar = new JLabel(userIcon);
        userAvatar.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        
        // Initialize components and layout
        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 0));
        JLabel loggedInLabel = new JLabel("Logged in");
        loggedInLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        listMailsButton = new JButton("List Mails");
        quitButton = new JButton("Quit");
        checkStatusButton = new JButton("Check Mail Box Status");
        checkConnectionButton = new JButton("Check Connection");
        operationsButton = new JButton("Operations");
        // Create the dropdown menu
        JPopupMenu dropdownMenu = new JPopupMenu();
        retrieveItem = new JMenuItem("Retrieve");
        retrieveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                operationsButton.setText(retrieveItem.getText());
            }
        });
        markForDeletionItem = new JMenuItem("Mark For Deletion");
        markForDeletionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                operationsButton.setText(markForDeletionItem.getText());
            }
        });
        dropdownMenu.add(retrieveItem);
        dropdownMenu.add(markForDeletionItem);
        // Add MouseListener to the Operations button to show the dropdown menu
        operationsButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dropdownMenu.show(operationsButton, 0, operationsButton.getHeight());
            }
        });
        numMessageField = new JTextField();
        numMessageField.setPreferredSize(new Dimension(130, 30)); // Set width to 200 and height to 30
        // Placeholder text for numMessageField
        String placeholderText = "Message's number";
        numMessageField.setText(placeholderText);
        numMessageField.setForeground(Color.GRAY);
        numMessageField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (numMessageField.getText().equals(placeholderText)) {
                    numMessageField.setText("");
                    numMessageField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (numMessageField.getText().isEmpty()) {
                    numMessageField.setForeground(Color.GRAY);
                    numMessageField.setText(placeholderText);
                }
            }
        });
        okButton = new JButton("OK");
        resetButton = new JButton("Reset");
        
        // Operations Panel
        JPanel operationsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        operationsPanel.add(operationsButton);
        operationsPanel.add(numMessageField);
        operationsPanel.add(okButton);
        operationsPanel.add(resetButton);
        operationsPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 10)); // Top, left, bottom, right
        
        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(listMailsButton);
        buttonsPanel.add(checkStatusButton);
        buttonsPanel.add(checkConnectionButton);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 10)); // Top, left, bottom, right
        
        // Quit Button Panel
        JPanel quitButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        quitButtonPanel.add(quitButton);
        
        // North Panel
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        
        // User Info Panel
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(loggedInLabel);
        
        // Adding components to North Panel
        northPanel.add(userAvatar, BorderLayout.WEST);
        northPanel.add(userInfoPanel, BorderLayout.CENTER);
        northPanel.add(buttonsPanel, BorderLayout.EAST);
        
        JLabel messagesLabel = new JLabel("Messages:");
        messagesLabel.setBorder(BorderFactory.createEmptyBorder(50, 10, 0, 0));
        
        // Message Display Area
        messageDisplayArea = new JTextArea();
        messageDisplayArea.setEditable(false); // Make it non-editable if it's just for display
        JScrollPane scrollPane = new JScrollPane(messageDisplayArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Top, left, bottom, right
        
        // Panel to hold both message display area and new text label
        JPanel textAndDisplayPanel = new JPanel();
        textAndDisplayPanel.setLayout(new BorderLayout());
        textAndDisplayPanel.add(messagesLabel, BorderLayout.NORTH); // Add text at the top
        textAndDisplayPanel.add(scrollPane, BorderLayout.CENTER); // Add display area below
        
        // Main Content Panel with BoxLayout
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.add(operationsPanel);
        mainContentPanel.add(textAndDisplayPanel);
        
        // Layout the components
        setLayout(new BorderLayout());
        add(northPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
        add(quitButtonPanel, BorderLayout.SOUTH);

        // Set frame properties
        setTitle("POP3 Client");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setCheckMailsAction(ActionListener action) {
    	listMailsButton.addActionListener(action);
    }
    
    public void setCheckStatusAction(ActionListener action) {
    	checkStatusButton.addActionListener(action);
    }
    
    public void setCheckConnectionAction(ActionListener action) {
    	checkConnectionButton.addActionListener(action);
    }
    
    public JTextArea getMessageDisplayArea() {
        return messageDisplayArea;
    }
    
    public JButton getOperationsButton() { return operationsButton; }
    public JTextField getNumMessageField() { return numMessageField; }
    public void setOkAction(ActionListener action) {
    	okButton.addActionListener(action);
    }
    
    public void setResetAction(ActionListener action) {
    	resetButton.addActionListener(action);
    }
    
    public JButton getQuitButton() {
        return quitButton;
    }

}
