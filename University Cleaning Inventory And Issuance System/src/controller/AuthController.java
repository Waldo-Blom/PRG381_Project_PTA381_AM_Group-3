/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UserDAO;
import model.User;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author waldo
 */
public class AuthController {

    private final UserDAO userDAO = new UserDAO();

    /**
     * Attempts to log the user in against the database.
     * Returns the matching User on success, or null if the credentials are incorrect.
     * @param emailEntered
     * @param passwordEntered
     * @return 
     */
    public User login(String emailEntered, String passwordEntered) {
        try {
            return userDAO.login(emailEntered, passwordEntered);
        } catch (Exception ex) {
            return null;
        }
        
    }

     /**
     * Validates registration input, checks for duplicate username/email,
     * and stores the new user if everything passes.
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

        try {
            if (userDAO.usernameExists(username)) {
                return "That username is already taken.";
            }
            if (userDAO.emailExists(email)) {
                return "An account with that email already exists.";
            }

            User newUser = new User(fullName, username, email, null, role);
            // This is done as a seperate method call as in the .registerUser method we hash the password
            // And also save the user to the database
            User saved = userDAO.registerUser(newUser, password); 


            if (saved == null) {
                return "Registration failed. Please try again.";
            }
        } catch (Exception ex) {
            return "A database error occurred. Please try again later.";
        }

        return null; // registration succeeded
     }

    /** A Password is considered valid if all of the below are true:
     * - The password has a length of at least 8 charathers
     * - The password has at least one upperacase letter
     * - The password contains at least one lower case letter
     * - The password contains at least one digit
     * - The password contains at least 1 special character
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