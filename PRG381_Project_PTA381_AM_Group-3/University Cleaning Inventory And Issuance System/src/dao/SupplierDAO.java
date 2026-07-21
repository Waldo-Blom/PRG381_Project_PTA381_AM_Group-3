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
 *
 * Matches the real "suppliers" table:
 *   supplier_id   SERIAL PRIMARY KEY
 *   supplier_name VARCHAR(100) NOT NULL
 *   contact_name  VARCHAR(100)
 *   phone         VARCHAR(20)
 *   email         VARCHAR(100) UNIQUE
 *
 * @author waldo
 */
public class SupplierDAO {

    private final Connection connection;
    private String lastError;

    public SupplierDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * The most recent database error message, useful for surfacing the real
     * reason an operation failed (e.g. a constraint violation) in the UI.
     */
    public String getLastError() {
        return lastError;
    }

    /**
     * Insert a new supplier into the database.
     */
    public boolean addSupplier(Supplier supplier) {
        lastError = null;
        String sql = "INSERT INTO suppliers (supplier_name, contact_name, phone, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getContactName());
            pstmt.setString(3, supplier.getPhone());
            pstmt.setString(4, supplier.getEmail());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get every supplier in the database.
     */
    public List<Supplier> getAllSuppliers() {
        lastError = null;
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers ORDER BY supplier_name";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                suppliers.add(mapRow(rs));
            }
        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return suppliers;
    }

    /**
     * Get a single supplier by its ID.
     */
    public Supplier getSupplierById(int supplierId) {
        lastError = null;
        String sql = "SELECT * FROM suppliers WHERE supplier_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, supplierId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update an existing supplier.
     */
    public boolean updateSupplier(Supplier supplier) {
        lastError = null;
        String sql = "UPDATE suppliers SET supplier_name = ?, contact_name = ?, phone = ?, email = ? WHERE supplier_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getContactName());
            pstmt.setString(3, supplier.getPhone());
            pstmt.setString(4, supplier.getEmail());
            pstmt.setInt(5, supplier.getSupplierId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a supplier by ID.
     */
    public boolean deleteSupplier(int supplierId) {
        lastError = null;
        String sql = "DELETE FROM suppliers WHERE supplier_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, supplierId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Search suppliers by name, phone number, or email.
     */
    public List<Supplier> searchSuppliers(String searchTerm) {
        lastError = null;
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers "
                + "WHERE LOWER(supplier_name) LIKE LOWER(?) "
                + "OR phone LIKE ? "
                + "OR LOWER(email) LIKE LOWER(?) "
                + "ORDER BY supplier_name";
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
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return suppliers;
    }

    private Supplier mapRow(ResultSet rs) throws SQLException {
        return new Supplier(
                rs.getInt("supplier_id"),
                rs.getString("supplier_name"),
                rs.getString("contact_name"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }
}
