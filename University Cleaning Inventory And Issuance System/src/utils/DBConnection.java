/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author waldo
 */
public class DBConnection {

//    // PostgreSQL JDBC driver class
//    private static final String DRIVER = "org.postgresql.Driver";
//
//    // JDBC URL to connect to the local PostgreSQL server + database
//    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/sparkling_clean";
//
//    //Postgre local username and password that is required to access postgre
//    private static final String USER = "postgres";
//    private static final String PASSWORD = "password";
//
//    // Set connection object to connect to the Postgre database
//    Connection con;
//
//    //Set Constructor
//    public DBConnection() {
//    }
//
//    // Create a connect to database method
//    public void connect() throws ClassNotFoundException {
//        try {
//            // Dynamically load the JDBC driver class at runtime
//            Class.forName(DRIVER);
//            this.con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
//            if (this.con != null) {
//                System.out.println("Connected to database");
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//    }
    
    
    // PostgreSQL JDBC driver class
    private static final String DRIVER = "org.postgresql.Driver";

    // JDBC URL to connect to the local PostgreSQL server + database
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/sparkling_clean";

    //Postgre local username and password that is required to access postgre
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    //Set Constructor
    public DBConnection() {
    }

    // Create a connect to database method
    public static Connection getConnection() throws ClassNotFoundException {
        Connection con = null;
        try {
            // Dynamically load the JDBC driver class at runtime
            Class.forName(DRIVER);
            con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            if (con != null) {
                System.out.println("Connected to database");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return con;
    }
}