/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.AuthController;
import utils.CurrentUser;
import model.User;
import javax.swing.JOptionPane;
import ui.LoginFrame;
import ui.MainFrame;

/**
 *
 * @author waldo
 */


// LoginView IS a window (JFrame) inheriting JFrame gives us built-in
// window behavior (show, close, resize) for free, and lets NetBeans GUI Builder work.
public class LoginView {

    private final AuthController authController = new AuthController();

    /**
     * Handles the sign-in logic for LoginFrame's Sign In button.
     * LoginFrame calls this method and passes itself in so this class
     * can read the form fields and close/open windows as needed.
     */
    public void handleSignIn(LoginFrame frame, String email, String password) {

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Email and Password are required.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = authController.login(email, password);

        if (user == null) {
            JOptionPane.showMessageDialog(frame, "Invalid email or password.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CurrentUser.set(user);
        JOptionPane.showMessageDialog(frame, "Welcome, " + user.getFullName() + "!",
                "Success", JOptionPane.INFORMATION_MESSAGE);

        frame.dispose();
        new MainFrame().setVisible(true);
    }

    /**
     * Handles the "Register here" link on LoginFrame.
     */
    public void handleRegisterLink(LoginFrame frame) {
        frame.dispose();
        new ui.RegisterFrame().setVisible(true);
    }
}