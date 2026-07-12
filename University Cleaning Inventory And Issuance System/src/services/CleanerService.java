package services;

import models.Cleaner;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for Cleaner CRUD operations
 * Handles all database interactions for cleaners
 */
public class CleanerService {
    private Connection connection;

    /**
     * Constructor - initialize with database connection
     */
    public CleanerService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Add a new cleaner to the database
     * @param cleaner The cleaner object to add
     * @return true if cleaner was added successfully
     */
    public boolean addCleaner(Cleaner cleaner) {
        String sql = "INSERT INTO cleaners (name, email, phone, department, hire_date, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cleaner.getName());
            pstmt.setString(2, cleaner.getEmail());
            pstmt.setString(3, cleaner.getPhone());
            pstmt.setString(4, cleaner.getDepartment());
            pstmt.setDate(5, Date.valueOf(cleaner.getHireDate()));
            pstmt.setString(6, cleaner.getStatus());
            
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all cleaners from the database
     * @return List of all cleaners
     */
    public List<Cleaner> getAllCleaners() {
        List<Cleaner> cleaners = new ArrayList<>();
        String sql = "SELECT * FROM cleaners ORDER BY name";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Cleaner cleaner = new Cleaner(
                    rs.getInt("cleaner_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("department"),
                    rs.getDate("hire_date").toLocalDate(),
                    rs.getString("status")
                );
                cleaners.add(cleaner);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cleaners;
    }

    /**
     * Get a cleaner by ID
     * @param cleanerId The cleaner ID
     * @return The cleaner object, or null if not found
     */
    public Cleaner getCleanerById(int cleanerId) {
        String sql = "SELECT * FROM cleaners WHERE cleaner_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cleanerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Cleaner(
                        rs.getInt("cleaner_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("department"),
                        rs.getDate("hire_date").toLocalDate(),
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
     * Update an existing cleaner
     * @param cleaner The cleaner object with updated values
     * @return true if cleaner was updated successfully
     */
    public boolean updateCleaner(Cleaner cleaner) {
        String sql = "UPDATE cleaners SET name = ?, email = ?, phone = ?, department = ?, hire_date = ?, status = ? WHERE cleaner_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cleaner.getName());
            pstmt.setString(2, cleaner.getEmail());
            pstmt.setString(3, cleaner.getPhone());
            pstmt.setString(4, cleaner.getDepartment());
            pstmt.setDate(5, Date.valueOf(cleaner.getHireDate()));
            pstmt.setString(6, cleaner.getStatus());
            pstmt.setInt(7, cleaner.getCleanerId());
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a cleaner by ID
     * @param cleanerId The cleaner ID to delete
     * @return true if cleaner was deleted successfully
     */
    public boolean deleteCleaner(int cleanerId) {
        String sql = "DELETE FROM cleaners WHERE cleaner_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cleanerId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Search cleaners by name, email, or phone
     * @param searchTerm The term to search for
     * @return List of matching cleaners
     */
    public List<Cleaner> searchCleaners(String searchTerm) {
        List<Cleaner> cleaners = new ArrayList<>();
        String sql = "SELECT * FROM cleaners WHERE name LIKE ? OR email LIKE ? OR phone LIKE ? ORDER BY name";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String pattern = "%" + searchTerm + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            pstmt.setString(3, pattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Cleaner cleaner = new Cleaner(
                        rs.getInt("cleaner_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("department"),
                        rs.getDate("hire_date").toLocalDate(),
                        rs.getString("status")
                    );
                    cleaners.add(cleaner);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cleaners;
    }

    /**
     * Get cleaners by department
     * @param department The department to filter by
     * @return List of cleaners in the specified department
     */
    public List<Cleaner> getCleanersByDepartment(String department) {
        List<Cleaner> cleaners = new ArrayList<>();
        String sql = "SELECT * FROM cleaners WHERE department = ? ORDER BY name";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, department);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Cleaner cleaner = new Cleaner(
                        rs.getInt("cleaner_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("department"),
                        rs.getDate("hire_date").toLocalDate(),
                        rs.getString("status")
                    );
                    cleaners.add(cleaner);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cleaners;
    }

    /**
     * Get cleaners by status
     * @param status The status to filter by (e.g., "Active", "Inactive")
     * @return List of cleaners with the specified status
     */
    public List<Cleaner> getCleanersByStatus(String status) {
        List<Cleaner> cleaners = new ArrayList<>();
        String sql = "SELECT * FROM cleaners WHERE status = ? ORDER BY name";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Cleaner cleaner = new Cleaner(
                        rs.getInt("cleaner_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("department"),
                        rs.getDate("hire_date").toLocalDate(),
                        rs.getString("status")
                    );
                    cleaners.add(cleaner);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cleaners;
    }

    /**
     * Get total number of cleaners
     * @return The count of all cleaners
     */
    public int getTotalCleaners() {
        String sql = "SELECT COUNT(*) as count FROM cleaners";
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
     * Get count of active cleaners
     * @return The count of active cleaners
     */
    public int getActiveCleanersCount() {
        String sql = "SELECT COUNT(*) as count FROM cleaners WHERE status = 'Active'";
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
     * Get count of inactive cleaners
     * @return The count of inactive cleaners
     */
    public int getInactiveCleanersCount() {
        String sql = "SELECT COUNT(*) as count FROM cleaners WHERE status = 'Inactive'";
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
     * Get all unique departments
     * @return List of department names
     */
    public List<String> getAllDepartments() {
        List<String> departments = new ArrayList<>();
        String sql = "SELECT DISTINCT department FROM cleaners WHERE department IS NOT NULL ORDER BY department";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                departments.add(rs.getString("department"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    /**
     * Check if cleaner with given email already exists
     * @param email The email to check
     * @return true if email exists, false otherwise
     */
    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) as count FROM cleaners WHERE email = ?";
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
