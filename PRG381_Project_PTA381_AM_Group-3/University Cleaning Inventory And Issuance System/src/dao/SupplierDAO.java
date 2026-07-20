/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the Supplier entity.
 * This is the ONLY class allowed to contain raw SQL for suppliers.
 * All PostgreSQL access for suppliers happens here.
 *
 * @author waldo
 */
public class SupplierDAO {

    private final Connection connection;

    public SupplierDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Insert a new supplier into the database.
     */
    public boolean addSupplier(Supplier supplier) {
        String sql = "INSERT INTO suppliers (name, email, phone, address, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getEmail());
            pstmt.setString(3, supplier.getPhone());
            pstmt.setString(4, supplier.getAddress());
            pstmt.setString(5, supplier.getStatus());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get every supplier in the database.
     */
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers ORDER BY name";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                suppliers.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    /**
     * Get a single supplier by its ID.
     */
    public Supplier getSupplierById(int supplierId) {
        String sql = "SELECT * FROM suppliers WHERE supplier_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, supplierId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update an existing supplier.
     */
    public boolean updateSupplier(Supplier supplier) {
        String sql = "UPDATE suppliers SET name = ?, email = ?, phone = ?, address = ?, status = ? WHERE supplier_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getEmail());
            pstmt.setString(3, supplier.getPhone());
            pstmt.setString(4, supplier.getAddress());
            pstmt.setString(5, supplier.getStatus());
            pstmt.setInt(6, supplier.getSupplierId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a supplier by ID.
     */
    public boolean deleteSupplier(int supplierId) {
        String sql = "DELETE FROM suppliers WHERE supplier_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, supplierId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Search suppliers by name, phone number, or email.
     */
    public List<Supplier> searchSuppliers(String searchTerm) {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers "
                + "WHERE LOWER(name) LIKE LOWER(?) "
                + "OR phone LIKE ? "
                + "OR LOWER(email) LIKE LOWER(?) "
                + "ORDER BY name";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String pattern = "%" + searchTerm + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            pstmt.setString(3, pattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    suppliers.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    private Supplier mapRow(ResultSet rs) throws SQLException {
        return new Supplier(
                rs.getInt("supplier_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getString("status")
        );
    }
}
