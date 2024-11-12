import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage {
    public static void main(String[] args) {
        // Create the login frame
        JFrame loginFrame = new JFrame("Login Page");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(500, 300);
        loginFrame.setLayout(new BorderLayout());

        // Create a panel with GridBagLayout for better alignment and centering
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        // Username label and text field
        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField(15);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(userLabel, constraints);
        constraints.gridx = 1;
        panel.add(userText, constraints);

        // Password label and password field
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passText = new JPasswordField(15);
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(passLabel, constraints);
        constraints.gridx = 1;
        panel.add(passText, constraints);

        // Button to hold and show password
        JButton showPasswordButton = new JButton("Show Password");
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(showPasswordButton, constraints);

        // Button to submit login
        JButton loginButton = new JButton("Login");
        constraints.gridx = 1;
        constraints.gridy = 3;
        panel.add(loginButton, constraints);

        // Label to display message (success/failure)
        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        panel.add(messageLabel, constraints);

        // Password visibility toggle
        showPasswordButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                passText.setEchoChar((char) 0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                passText.setEchoChar('*');
            }
        });

        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passText.getPassword());

                if (username.equals("user") && password.equals("password")) {
                    messageLabel.setText("Login successful!");
                    messageLabel.setForeground(Color.GREEN);
                    loginFrame.dispose();
                    //new mainSidebar();
                } else {
                    messageLabel.setText("Invalid username or password.");
                    messageLabel.setForeground(Color.RED);
                }
            }
        });

        loginFrame.add(panel, BorderLayout.CENTER);
        loginFrame.setVisible(true);
    }
}
