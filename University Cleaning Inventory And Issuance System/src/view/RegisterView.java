/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.AuthController;
import javax.swing.JOptionPane;
import ui.RegisterFrame;
import ui.LoginFrame;

/**
 *
 * @author waldo
 */
public class RegisterView {
    
       private final AuthController authController = new AuthController();

    /**
     * Handles the "Create Account" button on RegisterFrame.
     */
    public void handleCreateAccount(RegisterFrame frame, String fullName, String username,
                                     String email, String password, String confirmPassword,
                                     String role) {

        String error = authController.register(fullName, username, email, password, confirmPassword, role);
        
        // If it is null then it was successfull. 
        // If not null there was some error and then the string explaining what the error was is passed to the MessageDialog
        if (error != null) {
            JOptionPane.showMessageDialog(frame, error, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(frame, "Your was created succssfully. You can now sign in.",
                "Success", JOptionPane.INFORMATION_MESSAGE);

        frame.dispose();
        new LoginFrame().setVisible(true);
    }

    /**
     * Handles the "Back to Sign In" link on RegisterFrame.
     */
    public void handleBackToSignIn(RegisterFrame frame) {
        frame.dispose();
        new LoginFrame().setVisible(true);
    }
    
}
