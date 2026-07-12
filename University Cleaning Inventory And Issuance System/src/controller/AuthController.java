/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.User;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author waldo
 */
public class AuthController {
    
    // No database exists as of yet so a temp email and password are used
    String fullname = "Test User";
    String username = "admin";
    String email = "admin@sparklingclean.com";
    String password = "admin";
    String role = "Storekeeper";

    public User login(String emailEntred, String passwordEntered) {
        if (emailEntred.equalsIgnoreCase(email) && passwordEntered.equals(password)) {
            return new User(fullname, username, email, password, role);
        }
        return null; // no match
    }
    
     /**
     * Validates registration input. 
     * Duplicate user check will be done in the UserDAO once the database is availible and has been created
     
     */
    
    // Credit for the regex check: https://stackoverflow.com/questions/8204680/java-regex-email
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    
    public String register(String fullName, String username, String email,
                            String password, String confirmPassword, String role) {

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty()) {
            return "All fields are required.";
        }
        if (!validateEmail(email)) {
            return "Please enter a valid email address.";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }

        String passwordError = validatePassword(password);
        if (passwordError != null) {
            return passwordError;
        }

        // TODO: check userDAO usernameExists() 
        // TODO: userDAO registerUser(newUser) to actually store the user in the database

        return null; // validation passed
     }
    
    /** A Password is considered valid if all of the below are true:
     * - The passowrd has a length of at least 8 charathers
     * - The password has at least one upperacase letter
     * - The password contains at least one lower case letter
     * - The password contains at least one digit
     * - The passowrd contains at least 1 special character
     * 
     **/
    
     // Credit for the regex check: https://stackoverflow.com/questions/8204680/java-regex-email
    private static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

    private String validatePassword(String password) {
        if (password.length() < 8) { // < 8 as the min password length is 8
            return "Password must be at least " + 8 + " characters long.";
        }

        String specialChars = "!@#$%^&*()-_+=<>?/{}[]|~";
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            }
            if (Character.isLowerCase(c)) {
                hasLowercase = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (specialChars.indexOf(c) >= 0) {
                hasSpecialChar = true;
            }
        }

        if (!hasUppercase) {
            return "Password must contain at least one uppercase letter.";
        }
        if (!hasLowercase) {
            return "Password must contain at least one lowercase letter.";
        }
        if (!hasDigit) {
            return "Password must contain at least one number.";
        }
        if (!hasSpecialChar) {
            return "Password must contain at least one special character (e.g. !@#$%).";
        }

        return null;
    }
}
