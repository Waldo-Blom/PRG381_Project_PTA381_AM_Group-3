///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package utils;
//
//import java.sql.SQLException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//
///**
// * 
// * @author waldo
// */
//
///**
// * This is an example of how it will be used so evyerone can test that the database is working for them (Will be removed later)
// */
//public class DatabaseExampleTest {
//
//    public static void main(String[] args) {
//
//        // Create a new instance of the db connection helper class
//        DBConnection db = new DBConnection();
//
//        try {
//            db.connect(); // Use the created connect method
//        } catch (ClassNotFoundException ex) {
//            ex.printStackTrace();
//            return; 
//        }
//
//        try {
//            // Build the SQL insert statement with static test data
//            String query = "INSERT INTO materials "
//                    + "(material_name, category, description, quantity, unit, reorder_level, unit_cost) "
//                    + "VALUES ('Test', 'Cleaners', 'Just testing the DB connection', "
//                    + "10, 'liters', 7, 34.99)";
//
//            // Execute the insert statement using the open connection from db.con
//            db.con.createStatement().execute(query);
//
//            System.out.println("Data Added");
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            System.out.println("Data not added");
//        }
//    }
//}