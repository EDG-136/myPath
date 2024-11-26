package com.tecksupport.glfw.ui;

import com.tecksupport.database.UserAuthQuery;
import com.tecksupport.database.UserAuthQuery;
import com.tecksupport.glfw.view.Window;
import imgui.ImFont;
import imgui.ImGui;
import imgui.ImVec4;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthUI {
    private final static String NONE = "";
    private final static String REGISTER_SUCCESS = "Successfully registered!";
    private final static String INVALID_USER_OR_PASS = "The username or password is invalid!";
    private final static String REGISTER_INFO_EXISTED = "The username or student id already exists!";
    private final static ImVec4 RED = new ImVec4(1.0f, 0.0f, 0.0f, 1.0f);
    private final static ImVec4 GREEN = new ImVec4(0.0f, 1.0f, 0.0f, 1.0f);
    private final static ImVec4 WHITE = new ImVec4();

    private final UserAuthQuery userAuthQuery;
    private final Window window;
    private final ImString studentIDBuffer = new ImString("", 128); // Using ImGui ImString
    private final ImString usernameBuffer = new ImString("", 128); // Using ImGui ImString
    private final ImString passwordBuffer = new ImString("", 128);
    private final ImString firstNameBuffer = new ImString("", 128); // Using ImGui ImString
    private final ImString lastNameBuffer = new ImString("", 128);
    private final ImBoolean showPasswordBuffer = new ImBoolean();
    private String title = "Login Page";
    private String displayMessage;
    private ImVec4 displayMessageColor = new ImVec4();
    private boolean isLoggedIn = false; // To track login status
    private boolean isSignUp;
    private float width;
    private float height;

    public AuthUI(Window window, UserAuthQuery userAuthQuery) {
        this.window = window;
        this.userAuthQuery = userAuthQuery;
    }

    public void renderLoginPage() {
        // Set the size and position of the login window
        ImFont defaultFont = ImGui.getFont();
        ImFont loginFont = new ImFont(defaultFont.ptr);
        loginFont.setScale(1.4f);
        ImGui.pushFont(loginFont);
        if (isSignUp) {
            width = window.getWindowWidth() / 1.75f;
            height = window.getWindowHeight() / 1.5f;
        } else {
            width = window.getWindowWidth() / 1.75f;
            height = window.getWindowHeight() / 2.0f;
        }

        ImGui.setNextWindowPos((window.getScreenWidth() - width) / 2.0f, (window.getScreenHeight() - height) / 2.0f, ImGuiCond.Once);
        ImGui.setNextWindowSize(width, height, ImGuiCond.Once);
        displayCombo();
    }

    private void displayCombo() {
        ImGui.begin(title, getWindowFlag());

        ImGui.text("Please log in to access the application.");
        ImGui.spacing();

        displayInputFields();

        ImGui.newLine();

        handleLoginIn();
        ImGui.sameLine();
        handleSignUp();

        ImGui.spacing();

        displayMessage();

        ImGui.popFont();
        ImGui.end();
    }

    private void displayInputFields() {
        float itemWidth = 200;

        int passwordFlag;
        if (!showPasswordBuffer.get())
            passwordFlag = ImGuiInputTextFlags.Password;
        else
            passwordFlag = ImGuiInputTextFlags.None;

        if (isSignUp)
        {
            // Reset Display Message
            setDisplayMessage(NONE);

            // Show Sign Up UI
            title = "Signup Page";
            ImGui.pushItemWidth(itemWidth / 2);

            ImGui.text("StudentID");

            ImGui.inputText("##StudentID", studentIDBuffer, ImGuiInputTextFlags.None);

            ImGui.popItemWidth();
            ImGui.pushItemWidth(itemWidth);

            ImGui.text("First Name:");
            ImGui.sameLine(itemWidth, 15);
            ImGui.text("Last Name:");

            ImGui.inputText("##FirstName", firstNameBuffer, ImGuiInputTextFlags.None);
            ImGui.sameLine();
            ImGui.inputText("##LastName", lastNameBuffer, ImGuiInputTextFlags.None);

            ImGui.text("Username:");
            ImGui.sameLine(itemWidth, 15);
            ImGui.text("Password:");

            ImGui.inputText("##Username", usernameBuffer, ImGuiInputTextFlags.None);
            ImGui.sameLine();
            ImGui.inputText("##Password", passwordBuffer, passwordFlag);

            ImGui.spacing();
            ImGui.newLine();
            ImGui.sameLine(itemWidth,20);
            ImGui.checkbox("Show Password", showPasswordBuffer);
            ImGui.sameLine();

            ImGui.popItemWidth();
        } else {
            // Show Login Page
            title = "Login Page";
            ImGui.pushItemWidth(itemWidth);

            ImGui.text("Username:");

            ImGui.inputText("##Username", usernameBuffer, ImGuiInputTextFlags.None);

            ImGui.text("Password:");

            ImGui.inputText("##Password", passwordBuffer, passwordFlag);

            ImGui.spacing();
            ImGui.checkbox("Show Password", showPasswordBuffer);

            ImGui.popItemWidth();
        }
    }

    private int getWindowFlag() {
        return ImGuiWindowFlags.NoResize |
                ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoCollapse;
    }

    private void setDisplayMessage(String message) {
        switch (message) {
            case REGISTER_SUCCESS:
                displayMessageColor = GREEN;
                break;

            case REGISTER_INFO_EXISTED:
            case INVALID_USER_OR_PASS:
                displayMessageColor = RED;
                break;

            default:
                displayMessageColor = WHITE;
        }
        displayMessage = message;
    }

    private void displayMessage() {
        if (displayMessage != null)
            ImGui.textColored(displayMessageColor, displayMessage);
    }

    private void handleSignUp() {

        // Handle the "Sign In" button
        if (!ImGui.button("Sign In"))
            return;

        if (!isSignUp) {
            isSignUp = true;
            return;
        }

        String studentID = studentIDBuffer.get().trim();
        String username = usernameBuffer.get().trim();
        String password = passwordBuffer.get().trim();
        String firstName = firstNameBuffer.get().trim();
        String lastName = lastNameBuffer.get().trim();

        // Display message if register failed
        // Show login menu if success
        if (!userAuthQuery.register(studentID, username, password, firstName, lastName)) {
            setDisplayMessage(REGISTER_INFO_EXISTED);
        } else {
            setDisplayMessage(REGISTER_SUCCESS);
            isSignUp = false;
        }
    }

    private void handleLoginIn() {
        if (!ImGui.button("Login In"))
            return;

        if (isSignUp) {
            setDisplayMessage(NONE);
            isSignUp = false;
            return;
        }

        String username = usernameBuffer.get().trim();
        String password = passwordBuffer.get().trim();

        if (userAuthQuery.isPasswordCorrect(username, password)) {
            isLoggedIn = true;
            return;
        }

        setDisplayMessage(INVALID_USER_OR_PASS);
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    private boolean isStudentIDValid(String studentID) {
        Pattern regex = Pattern.compile("^\\d{9}$");
        Matcher matcher = regex.matcher(studentID);
        return matcher.find();
    }

    private boolean isFirstNameValid(String firstName) {
        return firstName.length() <= 15;
    }

    private boolean isLastNameValid(String lastName) {
        return lastName.length() <= 15;
    }

    private boolean isUsernameValid(String username) {
        Pattern regex = Pattern.compile("\\W");
        Matcher matcher = regex.matcher(username);
        return matcher.find();
    }
}
