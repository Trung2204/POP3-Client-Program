package com.pop3.application;

import java.io.IOException;

import com.pop3.application.controller.LoginController;

public class Main {
    public static void main(String[] args) throws IOException {
        // Start the login process
        new LoginController();
    }
}
