package services;

import models.Supplier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for Supplier CRUD operations
 * Handles all database interactions for suppliers
 */
public class SupplierService {
    private Connection connection;

    /**
     * Constructor - initialize with database connection
     */
    public SupplierService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Add a new supplier to the database
     * @param supplier The supplier object to add
     * @return true if supplier was added successfully
     */
    public boolean addSupplier(Supplier supplier) {
        String sql = "INSERT INTO suppliers (name, email, phone, address, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getEmail());
            pstmt.setString(3, supplier.getPhone());
            pstmt.setString(4, supplier.getAddress());
            pstmt.setString(5, supplier.getStatus());
            
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all suppliers from the database
     * @return List of all suppliers
     */
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers ORDER BY name";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Supplier supplier = new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("status")
                );
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    /**
     * Get a supplier by ID
     * @param supplierId The supplier ID
     * @return The supplier object, or null if not found
     */
    public Supplier getSupplierById(int supplierId) {
        String sql = "SELECT * FROM suppliers WHERE supplier_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, supplierId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update an existing supplier
     * @param supplier The supplier object with updated values
     * @return true if supplier was updated successfully
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
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a supplier by ID
     * @param supplierId The supplier ID to delete
     * @return true if supplier was deleted successfully
     */
    public boolean deleteSupplier(int supplierId) {
        String sql = "DELETE FROM suppliers WHERE supplier_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, supplierId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Search suppliers by name, email, or phone
     * @param searchTerm The term to search for
     * @return List of matching suppliers
     */
    public List<Supplier> searchSuppliers(String searchTerm) {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers WHERE name LIKE ? OR email LIKE ? OR phone LIKE ? OR address LIKE ? ORDER BY name";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String pattern = "%" + searchTerm + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            pstmt.setString(3, pattern);
            pstmt.setString(4, pattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("status")
                    );
                    suppliers.add(supplier);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    /**
     * Get suppliers by status
     * @param status The status to filter by (e.g., "Active", "Inactive")
     * @return List of suppliers with the specified status
     */
    public List<Supplier> getSuppliersByStatus(String status) {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers WHERE status = ? ORDER BY name";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("status")
                    );
                    suppliers.add(supplier);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    /**
     * Get total number of suppliers
     * @return The count of all suppliers
     */
    public int getTotalSuppliers() {
        String sql = "SELECT COUNT(*) as count FROM suppliers";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Check if supplier with given email already exists
     * @param email The email to check
     * @return true if email exists, false otherwise
     */
    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) as count FROM suppliers WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
