/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.User;
import utils.DBConnection;
import utils.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author waldo
 */
public class UserDAO {

    /**
     * Looks up a user by email and checks the supplied password against the
     * stored hash.
     *
     * @param email
     * @param plainPassword
     * @return User if login is successful, otherwise null.
     * @throws java.lang.Exception
     */
    public User login(String email, String plainPassword) throws Exception {

        User user = null; // Intialize the user state

        String sql = "SELECT user_id, username, email, password_hash, role, full_name FROM users WHERE email = ?";

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String storedHash = rs.getString("password_hash");

                if (PasswordUtil.verify(plainPassword, storedHash)) {

                    user = new User();
                    // We might use the user_id later so we store it incase we might need it
                    user.setId(String.valueOf(rs.getInt("user_id"))); 
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(storedHash);
                    user.setRole(rs.getString("role"));
                    user.setFullName(rs.getString("full_name"));
                }
            }

            rs.close();
            ps.close();
            con.close();

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return user;
    }

    /**
     * Checks whether a username already exists.
     *
     * @param username
     * @return true if username exists, otherwise false.
     */
    public boolean usernameExists(String username) {

        boolean exists = false;

        String sql = "SELECT * FROM users WHERE username = ?";

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = true;
            }

            rs.close();
            ps.close();
            con.close();

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exists;
    }

    /**
     * Checks whether an email already exists.
     *
     * @param email
     * @return true if email exists, otherwise false.
     */
    public boolean emailExists(String email) {

        boolean exists = false;

        String sql = "SELECT * FROM users WHERE email = ?";

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = true;
            }

            rs.close();
            ps.close();
            con.close();

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exists;
    }

    /**
     * Registers a new user.
     *
     * @param user
     * @param plainPassword
     * @return User with generated ID if successful, otherwise null.
     * @throws java.lang.Exception
     */
    public User registerUser(User user, String plainPassword) throws Exception {

        User registeredUser = null;

        String sql = "INSERT INTO users (username, email, password_hash, role, full_name) VALUES (?, ?, ?, ?, ?)";

        String passwordHash = PasswordUtil.hash(plainPassword);

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, passwordHash);
            ps.setString(4, user.getRole());
            ps.setString(5, user.getFullName());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                user.setPassword(passwordHash);
                registeredUser = user;
            }

            ps.close();
            con.close();

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return registeredUser;
    }
}