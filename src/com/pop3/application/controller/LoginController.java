package com.pop3.application.controller;

import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JOptionPane;

import com.pop3.application.model.POP3ClientModel;
import com.pop3.application.view.LoginWindow;
import com.pop3.application.view.MainAppWindow;

public class LoginController {
    private LoginWindow loginWindow;
    private POP3ClientModel pop3ClientModel;

    public LoginController() throws IOException {
        loginWindow = new LoginWindow();
        pop3ClientModel = new POP3ClientModel("pop-mail.outlook.com", 995);

        // Add action listener to the login button in the view
        ActionListener loginListener = e -> {
			try {
				performLogin();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		};
        loginWindow.addLoginListener(loginListener);
    }

    private void performLogin() throws IOException {
        String username = loginWindow.getUsername();
        String password = loginWindow.getPassword();
        try {
            if (pop3ClientModel.authenticate(username, password)) {
            	System.out.println("Log in successfully!");
                // On successful authentication, proceed to open the main application window
                // and close the login window
                loginWindow.close();
                // Open main application window here
                // Create and display the main application window
                MainAppWindow mainAppWindow = new MainAppWindow(username);
                @SuppressWarnings("unused")
				MainAppController mainAppController = new MainAppController(pop3ClientModel, mainAppWindow);
                mainAppWindow.setVisible(true);
            } else {
            	pop3ClientModel.reset();
                // Handle authentication failure
                JOptionPane.showMessageDialog(null, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
        	// Reset the model in case of an IOException as well
        	pop3ClientModel.reset();
            // Handle exceptions such as network errors
            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
