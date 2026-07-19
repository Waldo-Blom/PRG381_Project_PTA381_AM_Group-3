/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author waldo
 */
public class ReportsDAO {

    //This method is used in the getInventoryReport method
    private String getInventorySortColumn(String sortOption) {
        if (sortOption == null) {
            return "m.material_name ASC";
        }
        switch (sortOption) {
            case "Total Value ASC ↑":
                return "total_value ASC";
            case "Total Value DEC ↓":
                return "total_value DESC";
            case "Unit Cost ASC ↑":
                return "m.unit_cost ASC";
            case "Unit Cost DEC ↓":
                return "m.unit_cost DESC";
            case "Quantity ASC↑":
                return "m.quantity ASC";
            case "Quantity DEC ↓":
                return "m.quantity DESC";
            default:
                return "m.material_name ASC";
        }
    }

    // Inventory Report (with filtering and sorting) 
    public List<Object[]> getInventoryReport(String searchText, String sortOption) {
       
        List<Object[]> report = new ArrayList<>();
        
        // Build the SQL query using StringBuilder
        StringBuilder sql = new StringBuilder(
            "SELECT m.material_name, m.category, m.quantity, m.reorder_level, " // Select main material details
            + "s.supplier_name, m.unit_cost, (m.quantity * m.unit_cost) AS total_value, " // Join supplier name, unit cost, and calculate total value
            //CASE WHEN is used like a if statment in normal programming.
            //source: https://stackoverflow.com/questions/63447/how-do-i-perform-an-if-then-in-an-sql-select
            + "CASE WHEN m.quantity <= m.reorder_level THEN 'Low Stock' ELSE 'OK' END AS status " // Create a calculated status column
            + "FROM materials m " // Base table: materials using a alias 'm'
            + "LEFT JOIN suppliers s ON m.supplier_id = s.supplier_id " // Left join suppliers to get names. Using LEFT JOIN ensures materials without a supplier still appear
        );

        // List to hold the values that will be injected into the '?' placeholders
        List<Object> params = new ArrayList<>();

        // Search filter (material name or supplier name)
        if (searchText != null && !searchText.trim().isEmpty()) {
            sql.append("WHERE (m.material_name ILIKE ? OR s.supplier_name ILIKE ?) "); // ILIKE = a search that is not case sensitive
            String like = "%" + searchText.trim() + "%"; // The "%" is used to indicate a partial match
            params.add(like); // for material_name
            params.add(like); // for supplier_name
        }

        // Get the correct ORDER BY column/direction based on user selection
        String orderBy = getInventorySortColumn(sortOption);
        sql.append("ORDER BY ").append(orderBy); // Append the ordering clause
        
        /*
         * Example Query (when searchText = "bleach", sortOption = "Quantity DEC ↓"):
         * 
         * SELECT m.material_name, m.category, m.quantity, m.reorder_level, s.supplier_name, m.unit_cost, 
         * (m.quantity * m.unit_cost) AS total_value, 
         * CASE WHEN m.quantity <= m.reorder_level THEN 'Low Stock' ELSE 'OK' END AS status 
         * FROM materials m 
         * LEFT JOIN suppliers s ON m.supplier_id = s.supplier_id 
         * WHERE (m.material_name ILIKE '%bleach%' OR s.supplier_name ILIKE '%bleach%') 
         * ORDER BY m.quantity DESC;
         */

        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql.toString())) { 

            // Loop through the collected parameters and set them into the PreparedStatement
            // Index starts at 1 in SQL (JDBC is 1-indexed)
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
          
                while (rs.next()) {
                    // Add the data from the query to a object that can be loaded to the JTable
                    // The order of columns in this array matches the order expected by the JTable UI in the ReportsPnl
                    report.add(new Object[]{
                        rs.getString("material_name"),   
                        rs.getString("category"),        
                        rs.getInt("quantity"),          
                        rs.getInt("reorder_level"),     
                        rs.getString("supplier_name"),   // column might be null due to LEFT JOIN
                        rs.getDouble("unit_cost"),       
                        rs.getDouble("total_value"),     // calculated column
                        rs.getString("status")           // calculated column
                    });
                }
                rs.close();
                ps.close();
                con.close();
            }
            
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Error in getInventoryReport: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return report;
    }

    // Low Stock Report
    public List<Object[]> getLowStockReport() {
        List<Object[]> report = new ArrayList<>();
        String sql = "SELECT m.material_name, m.category, m.quantity, m.reorder_level, " // Select material details
                + "(m.reorder_level - m.quantity) AS shortage, " // Calculate how many units below reorder level
                + "s.supplier_name " // Supplier name 
                + "FROM materials m " // Main table
                + "LEFT JOIN suppliers s ON m.supplier_id = s.supplier_id " // LEFT JOIN to keep materials without supplier
                + "WHERE m.quantity <= m.reorder_level " // Only show low‑stock items
                + "ORDER BY shortage DESC"; // Order by the items that are needed the most

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                report.add(new Object[]{
                    rs.getString("material_name"),
                    rs.getString("category"),
                    rs.getInt("quantity"),
                    rs.getInt("reorder_level"),
                    rs.getInt("shortage"),             // calculated column
                    rs.getString("supplier_name")
                });
            }
            rs.close();
            ps.close();
            con.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Error in getLowStockReport: " + ex.getMessage());
            ex.printStackTrace();
        }
        return report;
    }

    // Low Stock Count used for lblItemsBelowReorderLevel
    public int getLowStockCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM materials WHERE quantity <= reorder_level";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Error in getLowStockCount: " + ex.getMessage());
            ex.printStackTrace();
        }
        return count;
    }

    // Issuance History (filtered by cleaner) 
    public List<Object[]> getIssuanceHistoryReport(Integer cleanerId) {
        List<Object[]> report = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT si.issuance_date, m.material_name, c.full_name, " // Date, material, cleaner
            + "si.quantity_issued, u.username, si.notes " // Quantity, who issued, notes
            + "FROM stock_issuances si " // Main transaction table
            + "JOIN materials m ON si.material_id = m.material_id " // Get material name
            + "JOIN cleaners c ON si.cleaner_id = c.cleaner_id " // Get cleaner name
            + "LEFT JOIN users u ON si.issued_by_user_id = u.user_id " // Get username of issuer 
        );
        List<Object> params = new ArrayList<>();

        // Cleaner filter (only applied if a specific cleaner is selected)
        if (cleanerId != null && cleanerId > 0) {
            sql.append("WHERE si.cleaner_id = ? "); // Filter by cleaner ID
            params.add(cleanerId);
        }

        sql.append("ORDER BY si.issuance_date DESC"); // Most recent issuance first

        /**
         * Example Query (when cleanerId = 3):
         * 
         * SELECT si.issuance_date, m.material_name, c.full_name, si.quantity_issued, u.username, si.notes
         * FROM stock_issuances si
         * JOIN materials m ON si.material_id = m.material_id
         * JOIN cleaners c ON si.cleaner_id = c.cleaner_id
         * LEFT JOIN users u ON si.issued_by_user_id = u.user_id
         * WHERE si.cleaner_id = 3
         * ORDER BY si.issuance_date DESC;
         */

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    report.add(new Object[]{
                        rs.getTimestamp("issuance_date"), 
                        rs.getString("material_name"),    
                        rs.getString("full_name"),        
                        rs.getInt("quantity_issued"),     
                        rs.getString("username"),         
                        rs.getString("notes")             
                    });
                }
                rs.close();
                ps.close();
                con.close();
            }
            
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Error in getIssuanceHistoryReport: " + ex.getMessage());
            ex.printStackTrace();
        }
        return report;
    }
    
    // This method is used in the getMaterialsUsageReport method
    private String getMaterialUsageSortColumn(String sortOption) {
        if (sortOption == null) {
            return "total_issued DESC";
        }
        switch (sortOption) {
            case "Total Value Issued ASC ↑":
                return "value_issued ASC";
            case "Total Value Issued DEC ↓":
                return "value_issued DESC";
            case "Total Issued ASC ↑":
                return "total_issued ASC";
            case "Total Issued DEC ↓":
                return "total_issued DESC";
            case "#Issuances ASC↑":
                return "num_issuances ASC";
            case "#Issuances DEC ↓":
                return "num_issuances DESC";
            default:
                return "total_issued DESC";
        }
    }

    // Material Usage Report (with material filter and sorting) 
    public List<Object[]> getMaterialUsageReport(Integer materialId, String sortOption) {
        List<Object[]> report = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT m.material_name, m.category, " // Basic material info
            // COALESCE is used to esnure if SUM is used and the value is zero it does not display NULL but instead 0 as it should
            //source: https://stackoverflow.com/questions/17048343/my-select-sum-query-returns-null-it-should-return-0        
            + "COALESCE(SUM(fi.quantity_issued), 0) AS total_issued, " // Total quantity issued
            + "m.quantity AS remaining, " // Current stock
            + "COALESCE(SUM(fi.quantity_issued * m.unit_cost), 0) AS value_issued, " // Total monetary value of issued items
            + "COUNT(fi.issuance_id) AS num_issuances " // Number of times issued
            + "FROM materials m " // Start with all materials
            + "LEFT JOIN stock_issuances fi ON fi.material_id = m.material_id " // Left join to include materials with zero issuances
        );
        List<Object> params = new ArrayList<>();

        // Material filter 
        if (materialId != null && materialId > 0) {
            sql.append("WHERE m.material_id = ? "); // Filter by material ID
            params.add(materialId);
        }

        sql.append("GROUP BY m.material_id, m.material_name, m.category, m.quantity "); // Group by material (required for aggregate functions)

        // Sorting
        String orderBy = getMaterialUsageSortColumn(sortOption);
        sql.append("ORDER BY ").append(orderBy);

        /**
         * Example Query (when materialId = 5, sortOption = "Total Issued DEC ↓"):
         * 
         * SELECT m.material_name, m.category, COALESCE(SUM(fi.quantity_issued), 0) AS total_issued,
         * m.quantity AS remaining, COALESCE(SUM(fi.quantity_issued * m.unit_cost), 0) AS value_issued,
         * COUNT(fi.issuance_id) AS num_issuances
         * FROM materials m
         * LEFT JOIN stock_issuances fi ON fi.material_id = m.material_id
         * WHERE m.material_id = 5
         * GROUP BY m.material_id, m.material_name, m.category, m.quantity
         * ORDER BY total_issued DESC;
         */

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    report.add(new Object[]{
                        rs.getString("material_name"),
                        rs.getString("category"),      
                        rs.getInt("total_issued"),           // calculated column
                        rs.getInt("remaining"),              // calculated column
                        rs.getDouble("value_issued"),       // calculated column
                        rs.getInt("num_issuances")          // calculated column
                    });
                }
                rs.close();
                ps.close();
                con.close();
            }
            
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Error in getMaterialUsageReport: " + ex.getMessage());
            ex.printStackTrace();
        }
        return report;
    }

    // Inventory reports stats
    // Returns the total number of different materials in the inventory.
    public int getTotalMaterialsCount() {
        String sql = "SELECT COUNT(*) FROM materials";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error in getTotalMaterialsCount: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Returns the total quantity of all materials (sum of quantity).
    public int getTotalUnits() {
        String sql = "SELECT SUM(quantity) FROM materials";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error in getTotalUnits: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Returns the total monetary value of all inventory (sum of quantity * unit_cost).
    public double getTotalValue() {
        String sql = "SELECT SUM(quantity * unit_cost) FROM materials";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error in getTotalValue: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }

    // Combobox Population Methods 
    public java.util.Map<String, Integer> getCleanersList() {
        java.util.Map<String, Integer> map = new java.util.LinkedHashMap<>(); // LinkedHashMap keeps DB order
        String sql = "SELECT cleaner_id, full_name FROM cleaners WHERE status = 'active' ORDER BY full_name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString("full_name"), rs.getInt("cleaner_id"));
            }
            rs.close();
            ps.close();
            con.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Error in getCleanersList: " + ex.getMessage());
            ex.printStackTrace();
        }
        return map;
     }

    public java.util.Map<String, Integer> getMaterialsList() {
        java.util.Map<String, Integer> map = new java.util.LinkedHashMap<>();
        String sql = "SELECT material_id, material_name FROM materials ORDER BY material_name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString("material_name"), rs.getInt("material_id"));
            }
            rs.close();
            ps.close();
            con.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Error in getMaterialsList: " + ex.getMessage());
            ex.printStackTrace();
        }
        return map;
    }

    // Overloaded methods 
    //These are used for when the Reports are first loaded without filters
    //See start of "private void loadReports()" method in ReportsPnl
    
    // Overload for InventoryReport without any filters
    public List<Object[]> getInventoryReport() {
        return getInventoryReport(null, null);
    }
    
    // Overload for issuance history without any filters
    public List<Object[]> getIssuanceHistoryReport() {
        return getIssuanceHistoryReport(null);
    }

    // Overload for material usage without any filters
    public List<Object[]> getMaterialUsageReport() {
        return getMaterialUsageReport(null, null);
    }
}