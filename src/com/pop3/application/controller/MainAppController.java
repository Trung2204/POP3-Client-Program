package com.pop3.application.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.swing.JOptionPane;

import com.pop3.application.model.POP3ClientModel;
import com.pop3.application.view.MainAppWindow;

public class MainAppController {
    private POP3ClientModel model;
    private MainAppWindow view;

    public MainAppController(POP3ClientModel model, MainAppWindow view) {
        this.model = model;
        this.view = view;

        // Set up listeners for view actions
        setupCheckMailsListener();
        setupCheckStatusListener();
        setupCheckConnectionListener();
        setupQuitButtonListener();
        setupOkButtonListener();
        setupResetButtonListener();
    }
    
    private void setupCheckMailsListener() {
        view.setCheckMailsAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    listEmails();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
    
    private void setupCheckStatusListener() {
        view.setCheckStatusAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	showStatus();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
    
    private void setupCheckConnectionListener() {
        view.setCheckConnectionAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
					if (model.noop()) {
						// Clear previous messages in display area
						view.getMessageDisplayArea().setText("");
					    view.getMessageDisplayArea().setText("Connection is good.");
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
    }

    private void setupQuitButtonListener() {
        view.getQuitButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    model.quit(); // Call your method to close the POP3 connection
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                view.setVisible(false); // Hide the main window
                try {
					new LoginController();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
    }

    private void listEmails() throws IOException {
        // Retrieve messages from model
        String[] messages = model.listMessages();
        
        // Clear previous messages in display area
        view.getMessageDisplayArea().setText("");
        
        // Append each message with its number and size to the display area
        for (String message : messages) {
            String[] messageDetails = message.split(" "); // Assuming the format is "number size"
            String displayText = "Message " + messageDetails[0] + " - Size: " + messageDetails[1] + "\n";
            view.getMessageDisplayArea().append(displayText);
        }
    }
    
    private void showStatus() throws IOException {
    	// Retrieve messages from model
        String status = model.statusMailBox();
        
        // Clear previous messages in display area
        view.getMessageDisplayArea().setText("");
        
        // Display the response from server
        String[] mailBoxStatus = status.split(" "); // Assuming the format is "+OK number size"
        String displayText = "Total messages: " + mailBoxStatus[1] + " Total size: " + mailBoxStatus[2];
        view.getMessageDisplayArea().setText(displayText);
    }

    private void setupOkButtonListener() {
    	view.setOkAction(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String selectedAction = view.getOperationsButton().getText();
				String number = view.getNumMessageField().getText();
				if (!number.isEmpty()) {
		            try {
		                int messageNumber = Integer.parseInt(number); // Convert to integer
		                // Now you can send this number to your controller
		                if ("Retrieve".equals(selectedAction)) {
							try {
								retrieveMessage(messageNumber);
							} catch (IOException | MessagingException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					    } else if ("Mark For Deletion".equals(selectedAction)) {
					    	try {
								deleteMessage(messageNumber);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					    }
		                // Reset operations button text for next selection
		                view.getOperationsButton().setText("Operations");
		                // Clear the text field for next input
		                view.getNumMessageField().setText("");
		            } catch (NumberFormatException ex) {
		                // Handle invalid number input
		                JOptionPane.showMessageDialog(null, "Number required!", "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        } else {
		            JOptionPane.showMessageDialog(null, "Number required!", "Error", JOptionPane.ERROR_MESSAGE);
		        }
				
			}
		});
    }
    
    private void setupResetButtonListener() {
    	view.setResetAction(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
		    	try {
					if (model.rset()) {
						// Clear previous messages in display area
						view.getMessageDisplayArea().setText("");
					    view.getMessageDisplayArea().setText("All messages are unmarked.");
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
    }
    
    private void retrieveMessage(int messageNumber) throws IOException, MessagingException {        
        // Retrieve messages from model and get the formatted string with details
        String messageDetails = model.retrieveMessage(messageNumber);
        
        // Clear previous messages in display area
        view.getMessageDisplayArea().setText("");
        
        // Display retrieved message details in the display area
        view.getMessageDisplayArea().setText(messageDetails);
    }

    private void deleteMessage(int messageNumber) throws IOException {
    	if (model.dele(messageNumber)) {
    		// Clear previous messages in display area
			view.getMessageDisplayArea().setText("");
		    view.getMessageDisplayArea().setText("Message number " + messageNumber + " is marked for deletion.");
    	}
    }
    
}
