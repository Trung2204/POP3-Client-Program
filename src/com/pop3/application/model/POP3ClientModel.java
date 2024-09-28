package com.pop3.application.model;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class POP3ClientModel {

	private String host;
	private int port;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public POP3ClientModel(String host, int port) throws IOException {
    	this.host = host;
    	this.port = port;
    	initialize();
    	
    	// Read server greeting
    	if (!reader.readLine().startsWith("+OK")) {
    		throw new EOFException("Server closed connection unexpectedly."); // Greeting failed
        }
    }

    public boolean authenticate(String username, String password) throws IOException {
    	writer.println("USER " + username);
        if (!reader.readLine().startsWith("+OK")) {
            return false; // Authentication failed
        }

        writer.println("PASS " + password);
        if (!reader.readLine().startsWith("+OK")) {
            return false; // Authentication failed
        }
        return true; // Authentication successful
    }
    
    public String[] listMessages() throws IOException {
        writer.println("LIST");
        String response = reader.readLine();
        if (!response.startsWith("+OK")) {
            throw new IOException("Error listing messages: " + response);
        }

        List<String> messages = new ArrayList<>();
        while (!(response = reader.readLine()).equals(".")) {
            messages.add(response); // Add entire response line (number and size)
        }
        
        return messages.toArray(new String[0]);
    }
    
    public String statusMailBox() throws IOException {
        writer.println("STAT");
        String response = reader.readLine();
        if (!response.startsWith("+OK")) {
            throw new IOException("Error checking status of mailbox: " + response);
        }
        
        return response;
    }
    
    public boolean noop() throws IOException {
    	writer.println("NOOP");
        String response = reader.readLine();
        if (!response.startsWith("+OK")) {
            return false;
        }
        
        return true;
    }
    
    public String retrieveMessage(int messageNumber) throws IOException, MessagingException {
        writer.println("RETR " + messageNumber);
        String response = reader.readLine();

        StringBuilder messageDetails = new StringBuilder();

        // Check if the response is positive
        if (response.startsWith("+OK")) {
            // Use a ByteArrayOutputStream to capture the email data
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintWriter pw = new PrintWriter(byteArrayOutputStream, true);

            while (!(response = reader.readLine()).equals(".")) {
                pw.println(response);
            }
            pw.flush();

            // Convert the ByteArrayOutputStream into an InputStream
            InputStream emailData = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            // Parse the email using MimeMessage
            Session session = Session.getDefaultInstance(new Properties());
            MimeMessage message = new MimeMessage(session, emailData);

            // Parse headers
            String from = message.getHeader("From", null);
            String to = message.getHeader("To", null);
            String subject = message.getHeader("Subject", null);
            String date = message.getHeader("Date", null);

            // Append headers to messageDetails
            messageDetails.append("From: ").append(from).append("\n");
            messageDetails.append("To: ").append(to).append("\n");
            messageDetails.append("Subject: ").append(subject).append("\n");
            messageDetails.append("Date: ").append(date).append("\n\n");

            // Parse body for plain text
            Object content = message.getContent();
            if (content instanceof Multipart) {
                Multipart multipart = (Multipart) content;
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (bodyPart.isMimeType("text/plain")) {
                        String bodyText = (String) bodyPart.getContent();
                        // Append body text to messageDetails
                        messageDetails.append(bodyText);
                        break; // Assuming you only want the first text/plain part
                    }
                }
            } else if (content instanceof String) {
                // If it's not multipart, it might be plain text
                if (message.isMimeType("text/plain")) {
                    // Append plain text content to messageDetails
                    messageDetails.append(content);
                }
            }
        } else {
            throw new IOException("Error retrieving message: " + response);
        }
        
        // Save plain text content to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("message_" + messageNumber + "_" + System.currentTimeMillis() + ".txt"))) {
        	writer.write(messageDetails.toString());
        }

        return messageDetails.toString();
    }

    public boolean dele(int messageNumber) throws IOException {
        writer.println("DELE " + messageNumber);
        if (!reader.readLine().startsWith("+OK")) {
            return false; // Marking For Deletion failed
        }
        
        return true;
    }
    
    public boolean rset() throws IOException {
        writer.println("RSET");
        if (!reader.readLine().startsWith("+OK")) {
            return false; // Unmarking failed
        }
        
        return true;
    }
    
    public void reset() throws IOException {
        close();
        initialize();
    }
    
    public void quit() throws IOException {
        writer.println("QUIT");
        String response = reader.readLine();
        close();
        if (response.startsWith("+OK")) {
        	System.out.println("Server response QUIT: " + response);
        } else {
            System.out.println("Error quitting: " + response);
        }
    }
    
    private void close() throws IOException {
        // Close the reader, writer, and socket
        if (reader != null) {
            reader.close();
        }
        if (writer != null) {
            writer.close();
        }
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
    
    public void initialize() throws IOException {
    	SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = factory.createSocket(host, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // read responses from the server
        writer = new PrintWriter(socket.getOutputStream(), true); 					 // send commands to the server
    }
}

