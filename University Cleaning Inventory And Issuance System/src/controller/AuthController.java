/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.User;

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
    String role = "Owner";

    public User login(String emailEntred, String passwordEntered) {
        if (emailEntred.equalsIgnoreCase(email) && passwordEntered.equals(password)) {
            return new User(fullname, username, email, password, role);
        }
        return null; // no match
    }
}
