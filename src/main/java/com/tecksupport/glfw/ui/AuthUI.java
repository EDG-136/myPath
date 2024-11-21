package com.tecksupport.glfw.ui;

import com.tecksupport.glfw.view.Window;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImString;

import java.util.HashMap;

public class AuthUI {
    private final Window window;
    private final HashMap<String, String> userDatabase = new HashMap<>(); // Simulated database
    private final ImString usernameBuffer = new ImString("", 128); // Using ImGui ImString
    private final ImString passwordBuffer = new ImString("", 128);
    private String loginMessage = ""; // Stores the message to display
    private String signupMessage = ""; // Message for successful signup or welcome message
    private boolean isLoggedIn = false; // To track login status

    public AuthUI(Window window) {
        this.window = window;
    }

    public void renderLoginPage() {
        // Set the size and position of the login window
        ImGui.setNextWindowSize(400, 200, ImGuiCond.Always);
        ImGui.setNextWindowPos(window.getWindowWidth() / 2.0f - 200, window.getWindowHeight() / 2.0f - 100, ImGuiCond.Always);

        ImGui.begin("Login Page");

        ImGui.text("Please log in to access the application.");
        ImGui.spacing();

        // Input fields for username and password
        ImGui.inputText("Username", usernameBuffer, ImGuiInputTextFlags.None);
        ImGui.inputText("Password", passwordBuffer, ImGuiInputTextFlags.Password);

        // Handle the "Sign In" button
        if (ImGui.button("Sign In")) {
            String username = usernameBuffer.get().trim();
            String password = passwordBuffer.get().trim();
            if (validateCredentials(username, password)) {
                isLoggedIn = true; // User has logged in
                loginMessage = ""; // Clear message on successful login
                signupMessage = ""; // Clear any previous signup message
            } else {
                loginMessage = "Invalid credentials. Please try again."; // Set error message
            }
        }

        // Handle the "Sign Up" button
        ImGui.sameLine(); // Position "Sign Up" next to "Sign In"
        if (ImGui.button("Sign Up")) {
            String username = usernameBuffer.get().trim();
            String password = passwordBuffer.get().trim();
            boolean signUpSuccess = handleSignUp(username, password); // Assume this function returns a boolean indicating success
            if (signUpSuccess) {
                signupMessage = "Welcome, " + username + "! You have successfully signed up."; // Set welcome message
            } else {
                signupMessage = "Signup failed. Please try again."; // Set error message for signup
            }
        }

        ImGui.spacing();

        // Display login message (if any)
        if (!loginMessage.isEmpty()) {
            ImGui.textColored(1.0f, 0.0f, 0.0f, 1.0f, loginMessage); // Red text for error messages
        }
        if (!signupMessage.isEmpty()) {
            ImGui.textColored(0.0f, 1.0f, 0.0f, 1.0f, signupMessage); // Green text for success message
        }

        ImGui.end();
    }
    private boolean validateCredentials(String username, String password) {
        // Replace with real credential validation logic
        return userDatabase.containsKey(username) && userDatabase.get(username).equals(password);
    }
    private boolean handleSignUp(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Sign-Up Failed: Username and password cannot be empty.");
            return false;
        }

        if (userDatabase.containsKey(username)) {
            System.out.println("Sign-Up Failed: Username already exists.");
        } else {
            userDatabase.put(username, password);
            System.out.println("Sign-Up Successful! Welcome, " + username + "!");
        }
        userDatabase.put(username, password);
        return true;
    }

    private void handleSignIn(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Sign-In Failed: Username and password cannot be empty.");
            return;
        }

        if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
            System.out.println("Sign-In Successful! Welcome back, " + username + "!");
            isLoggedIn = true;
        } else {
            System.out.println("Sign-In Failed: Invalid username or password.");
        }
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
