/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Cleaner;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the Cleaner entity.
 * This is the ONLY class allowed to contain raw SQL for cleaners.
 *
 * Matches the real "cleaners" table:
 *   cleaner_id SERIAL PRIMARY KEY
 *   employee_id VARCHAR(20) UNIQUE NOT NULL
 *   full_name   VARCHAR(100) NOT NULL
 *   email       VARCHAR(100) UNIQUE NOT NULL
 *   phone       VARCHAR(20)
 *   department  VARCHAR(50) NOT NULL
 *   hire_date   DATE NOT NULL
 *   status      VARCHAR(15) CHECK (status IN ('active','inactive')) DEFAULT 'active'
 *
 * @author waldo
 */
public class CleanerDAO {

    private final Connection connection;
    private String lastError;

    public CleanerDAO(Connection connection) {
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
     * Insert a new cleaner into the database.
     */
    public boolean addCleaner(Cleaner cleaner) {
        lastError = null;
        String sql = "INSERT INTO cleaners (employee_id, full_name, email, phone, department, hire_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cleaner.getEmployeeId());
            pstmt.setString(2, cleaner.getName());
            pstmt.setString(3, cleaner.getEmail());
            pstmt.setString(4, cleaner.getPhone());
            pstmt.setString(5, cleaner.getDepartment());
            pstmt.setDate(6, Date.valueOf(cleaner.getHireDate()));
            pstmt.setString(7, cleaner.getStatus());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get every cleaner in the database.
     */
    public List<Cleaner> getAllCleaners() {
        lastError = null;
        List<Cleaner> cleaners = new ArrayList<>();
        String sql = "SELECT * FROM cleaners ORDER BY full_name";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cleaners.add(mapRow(rs));
            }
        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return cleaners;
    }

    /**
     * Get a single cleaner by its ID.
     */
    public Cleaner getCleanerById(int cleanerId) {
        lastError = null;
        String sql = "SELECT * FROM cleaners WHERE cleaner_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cleanerId);
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
     * Update an existing cleaner.
     */
    public boolean updateCleaner(Cleaner cleaner) {
        lastError = null;
        String sql = "UPDATE cleaners SET employee_id = ?, full_name = ?, email = ?, phone = ?, department = ?, hire_date = ?, status = ? WHERE cleaner_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cleaner.getEmployeeId());
            pstmt.setString(2, cleaner.getName());
            pstmt.setString(3, cleaner.getEmail());
            pstmt.setString(4, cleaner.getPhone());
            pstmt.setString(5, cleaner.getDepartment());
            pstmt.setDate(6, Date.valueOf(cleaner.getHireDate()));
            pstmt.setString(7, cleaner.getStatus());
            pstmt.setInt(8, cleaner.getCleanerId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a cleaner by ID.
     */
    public boolean deleteCleaner(int cleanerId) {
        lastError = null;
        String sql = "DELETE FROM cleaners WHERE cleaner_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cleanerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Search cleaners by employee ID, full name, or email.
     */
    public List<Cleaner> searchCleaners(String searchTerm) {
        lastError = null;
        List<Cleaner> cleaners = new ArrayList<>();
        String sql = "SELECT * FROM cleaners "
                + "WHERE LOWER(employee_id) LIKE LOWER(?) "
                + "OR LOWER(full_name) LIKE LOWER(?) "
                + "OR LOWER(email) LIKE LOWER(?) "
                + "ORDER BY full_name";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String pattern = "%" + searchTerm + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            pstmt.setString(3, pattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    cleaners.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return cleaners;
    }

    /**
     * Get the distinct list of statuses currently stored in the database.
     */
    public List<String> getStatuses() {
        lastError = null;
        List<String> statuses = new ArrayList<>();
        String sql = "SELECT DISTINCT status FROM cleaners WHERE status IS NOT NULL ORDER BY status";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                statuses.add(rs.getString("status"));
            }
        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return statuses;
    }

    /**
     * Get the distinct list of departments currently stored in the database.
     */
    public List<String> getDepartments() {
        lastError = null;
        List<String> departments = new ArrayList<>();
        String sql = "SELECT DISTINCT department FROM cleaners WHERE department IS NOT NULL ORDER BY department";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                departments.add(rs.getString("department"));
            }
        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return departments;
    }

    private Cleaner mapRow(ResultSet rs) throws SQLException {
        return new Cleaner(
                rs.getInt("cleaner_id"),
                rs.getString("employee_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("department"),
                rs.getDate("hire_date").toLocalDate(),
                rs.getString("status")
        );
    }
}
