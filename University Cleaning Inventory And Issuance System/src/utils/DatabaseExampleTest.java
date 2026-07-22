
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author waldo
 */

/**
 * This is an example of how it will be used so evyerone can test that the database is working for them (Will be removed later)
 */
public class DatabaseExampleTest {

    public static void main(String[] args) {

        boolean status = false;

        String sql = "INSERT INTO materials "
                + "(material_name, category, description, quantity, unit, reorder_level, unit_cost) "
                + "VALUES (?,?,?,?,?,?,?)";
        //NBN placeholders are allways just a "?"

        try {
            Connection con = DBConnection.getConnection(); // Connect to the db

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "Test");
            ps.setString(2, "Cleaners");
            ps.setString(3, "Just testing the DB connection");
            ps.setInt(4, 10);
            ps.setString(5, "liters");
            ps.setInt(6, 7);
            ps.setDouble(7, 34.99);

            int rows = ps.executeUpdate(); // returns the ammount of rows that were udpated

            if (rows > 0) {
                status = true;
            }

            ps.close();
            con.close();

        } catch (ClassNotFoundException ex) {
            System.out.print(ex.getMessage());
        } catch (SQLException ex) {
            System.out.print(ex.getMessage());
        }

        if (status) {
            System.out.println("Data Added");
        } else {
            System.out.println("Data not added");
        }
    }
}

