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
import model.StockIssuance;

public class StockIssuanceDAO {

    public boolean issueMaterial(StockIssuance issuance) {

    String stockSql =
            "SELECT quantity FROM materials WHERE material_id = ?";

    String insertSql =
            "INSERT INTO stock_issuances "
            + "(issuance_date, material_id, cleaner_id, "
            + "quantity_issued, issued_by_user_id, notes) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

    String updateStockSql =
            "UPDATE materials SET quantity = quantity - ? WHERE material_id = ?";

    try (Connection conn = DBConnection.getConnection()) {

        int availableStock;

        try (PreparedStatement ps = conn.prepareStatement(stockSql)) {
            ps.setInt(1, issuance.getMaterialId());

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalArgumentException("The selected material no longer exists.");
                }
                availableStock = rs.getInt("quantity");
            }
        }

        if (issuance.getQuantityIssued() > availableStock) {
            throw new IllegalArgumentException(
                    "Insufficient stock. Only " + availableStock + " item(s) are available.");
        }

        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setTimestamp(1, issuance.getIssuanceDate());
            ps.setInt(2, issuance.getMaterialId());
            ps.setInt(3, issuance.getCleanerId());
            ps.setInt(4, issuance.getQuantityIssued());
            ps.setInt(5, issuance.getIssuedByUserId());
            ps.setString(6, issuance.getNotes());
            ps.executeUpdate();
        }

        try (PreparedStatement ps = conn.prepareStatement(updateStockSql)) {
            ps.setInt(1, issuance.getQuantityIssued());
            ps.setInt(2, issuance.getMaterialId());
            ps.executeUpdate();
        }

        return true;

    } catch (SQLException | ClassNotFoundException ex) {
        ex.printStackTrace();
        return false;
    }
}

public List<StockIssuance> getAllIssuances() {

    List<StockIssuance> issuances = new ArrayList<>();

    String sql =
            "SELECT "
            + "si.issuance_id, "
            + "si.issuance_date, "
            + "si.material_id, "
            + "m.material_name, "
            + "si.cleaner_id, "
            + "c.full_name AS cleaner_name, "
            + "si.quantity_issued, "
            + "si.issued_by_user_id, "
            + "u.username AS issued_by_username, "
            + "si.notes "
            + "FROM stock_issuances si "
            + "JOIN materials m "
            + "ON si.material_id = m.material_id "
            + "JOIN cleaners c "
            + "ON si.cleaner_id = c.cleaner_id "
            + "LEFT JOIN users u "
            + "ON si.issued_by_user_id = u.user_id "
            + "ORDER BY si.issuance_date DESC";

    try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
    ) {

        while (rs.next()) {

            StockIssuance issuance = new StockIssuance();

            issuance.setIssuanceId(
                    rs.getInt("issuance_id"));

            issuance.setIssuanceDate(
                    rs.getTimestamp("issuance_date"));

            issuance.setMaterialId(
                    rs.getInt("material_id"));

            issuance.setMaterialName(
                    rs.getString("material_name"));

            issuance.setCleanerId(
                    rs.getInt("cleaner_id"));

            issuance.setCleanerName(
                    rs.getString("cleaner_name"));

            issuance.setQuantityIssued(
                    rs.getInt("quantity_issued"));

            issuance.setIssuedByUserId(
                    rs.getInt("issued_by_user_id"));

            issuance.setIssuedByUsername(
                    rs.getString("issued_by_username"));

            issuance.setNotes(
                    rs.getString("notes"));

            issuances.add(issuance);
        }

    } catch (SQLException | ClassNotFoundException ex) {
        ex.printStackTrace();
    }

    return issuances;
}

/**
 * Same as {getAllIssuances()}, but restricted to one cleaner,
 * matched by email since that's the shared link to the "cleaners" table.
 */
public List<StockIssuance> getIssuancesByCleanerEmail(String cleanerEmail) {

    List<StockIssuance> issuances = new ArrayList<>();

    String sql =
            "SELECT "
            + "si.issuance_id, "
            + "si.issuance_date, "
            + "si.material_id, "
            + "m.material_name, "
            + "si.cleaner_id, "
            + "c.full_name AS cleaner_name, "
            + "si.quantity_issued, "
            + "si.issued_by_user_id, "
            + "u.username AS issued_by_username, "
            + "si.notes "
            + "FROM stock_issuances si "
            + "JOIN materials m "
            + "ON si.material_id = m.material_id "
            + "JOIN cleaners c "
            + "ON si.cleaner_id = c.cleaner_id "
            + "LEFT JOIN users u "
            + "ON si.issued_by_user_id = u.user_id "
            + "WHERE LOWER(c.email) = LOWER(?) "
            + "ORDER BY si.issuance_date DESC";

    try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
    ) {

        ps.setString(1, cleanerEmail);

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                StockIssuance issuance = new StockIssuance();

                issuance.setIssuanceId(
                        rs.getInt("issuance_id"));

                issuance.setIssuanceDate(
                        rs.getTimestamp("issuance_date"));

                issuance.setMaterialId(
                        rs.getInt("material_id"));

                issuance.setMaterialName(
                        rs.getString("material_name"));

                issuance.setCleanerId(
                        rs.getInt("cleaner_id"));

                issuance.setCleanerName(
                        rs.getString("cleaner_name"));

                issuance.setQuantityIssued(
                        rs.getInt("quantity_issued"));

                issuance.setIssuedByUserId(
                        rs.getInt("issued_by_user_id"));

                issuance.setIssuedByUsername(
                        rs.getString("issued_by_username"));

                issuance.setNotes(
                        rs.getString("notes"));

                issuances.add(issuance);
            }
        }

    } catch (SQLException | ClassNotFoundException ex) {
        ex.printStackTrace();
    }

    return issuances;
}

    public StockIssuance getIssuanceById(int issuanceId) {

        String sql =
                "SELECT * FROM stock_issuances "
                + "WHERE issuance_id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, issuanceId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    StockIssuance issuance = new StockIssuance();

                    issuance.setIssuanceId(
                            rs.getInt("issuance_id"));

                    issuance.setIssuanceDate(
                            rs.getTimestamp("issuance_date"));

                    issuance.setMaterialId(
                            rs.getInt("material_id"));

                    issuance.setCleanerId(
                            rs.getInt("cleaner_id"));

                    issuance.setQuantityIssued(
                            rs.getInt("quantity_issued"));

                    issuance.setIssuedByUserId(
                            rs.getInt("issued_by_user_id"));

                    issuance.setNotes(
                            rs.getString("notes"));

                    return issuance;
                }
            }

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return null;
    }
    
    public boolean deleteIssuance(int issuanceId) {

    String findSql =
            "SELECT material_id, quantity_issued "
            + "FROM stock_issuances "
            + "WHERE issuance_id = ? "
            + "FOR UPDATE";

    String deleteSql =
            "DELETE FROM stock_issuances "
            + "WHERE issuance_id = ?";

    String restoreStockSql =
            "UPDATE materials "
            + "SET quantity = quantity + ? "
            + "WHERE material_id = ?";

    Connection conn = null;

    try {

        conn = DBConnection.getConnection();
        conn.setAutoCommit(false);

        int materialId;
        int quantityIssued;

        try (PreparedStatement findStatement =
                     conn.prepareStatement(findSql)) {

            findStatement.setInt(1, issuanceId);

            try (ResultSet rs =
                         findStatement.executeQuery()) {

                if (!rs.next()) {
                    throw new IllegalArgumentException(
                            "The selected issuance no longer exists."
                    );
                }

                materialId =
                        rs.getInt("material_id");

                quantityIssued =
                        rs.getInt("quantity_issued");
            }
        }

        try (PreparedStatement deleteStatement =
                     conn.prepareStatement(deleteSql)) {

            deleteStatement.setInt(1, issuanceId);

            if (deleteStatement.executeUpdate() != 1) {
                throw new SQLException(
                        "The issuance could not be deleted."
                );
            }
        }

        try (PreparedStatement restoreStatement =
                     conn.prepareStatement(restoreStockSql)) {

            restoreStatement.setInt(
                    1,
                    quantityIssued
            );

            restoreStatement.setInt(
                    2,
                    materialId
            );

            if (restoreStatement.executeUpdate() != 1) {
                throw new SQLException(
                        "The material stock could not be restored."
                );
            }
        }

        conn.commit();
        return true;

    } catch (IllegalArgumentException ex) {

        rollbackTransaction(conn);
        throw ex;

    } catch (SQLException | ClassNotFoundException ex) {

        rollbackTransaction(conn);
        ex.printStackTrace();
        return false;

    } finally {

        closeConnection(conn);
    }
}
 
    
 
    
    public boolean updateIssuanceQuantity(
        int issuanceId,
        int newQuantity,
        int issuedByUserId) {

    if (newQuantity <= 0) {
        throw new IllegalArgumentException(
                "Quantity must be greater than zero."
        );
    }

    String issuanceSql =
            "SELECT material_id, quantity_issued "
            + "FROM stock_issuances "
            + "WHERE issuance_id = ? "
            + "FOR UPDATE";

    String stockSql =
            "SELECT quantity "
            + "FROM materials "
            + "WHERE material_id = ? "
            + "FOR UPDATE";

    String updateIssuanceSql =
            "UPDATE stock_issuances "
            + "SET quantity_issued = ?, issued_by_user_id = ? "
            + "WHERE issuance_id = ?";

    String updateStockSql =
            "UPDATE materials "
            + "SET quantity = quantity - ? "
            + "WHERE material_id = ?";

    Connection conn = null;

    try {

        conn = DBConnection.getConnection();
        conn.setAutoCommit(false);

        int materialId;
        int oldQuantity;

        try (PreparedStatement issuanceStatement =
                     conn.prepareStatement(issuanceSql)) {

            issuanceStatement.setInt(
                    1,
                    issuanceId
            );

            try (ResultSet rs =
                         issuanceStatement.executeQuery()) {

                if (!rs.next()) {
                    throw new IllegalArgumentException(
                            "The selected issuance no longer exists."
                    );
                }

                materialId =
                        rs.getInt("material_id");

                oldQuantity =
                        rs.getInt("quantity_issued");
            }
        }

        int availableStock;

        try (PreparedStatement stockStatement =
                     conn.prepareStatement(stockSql)) {

            stockStatement.setInt(
                    1,
                    materialId
            );

            try (ResultSet rs =
                         stockStatement.executeQuery()) {

                if (!rs.next()) {
                    throw new IllegalArgumentException(
                            "The related material no longer exists."
                    );
                }

                availableStock =
                        rs.getInt("quantity");
            }
        }

        /*
         * Positive difference means more stock must be issued.
         * Negative difference means stock must be restored.
         */
        int difference =
                newQuantity - oldQuantity;

        if (difference > 0
                && difference > availableStock) {

            throw new IllegalArgumentException(
                    "Insufficient stock. Only "
                    + availableStock
                    + " additional item(s) are available."
            );
        }

        try (PreparedStatement stockUpdate =
                     conn.prepareStatement(updateStockSql)) {

            /*
             * If difference is negative, subtracting it adds
             * the stock back automatically.
             */
            stockUpdate.setInt(
                    1,
                    difference
            );

            stockUpdate.setInt(
                    2,
                    materialId
            );

            if (stockUpdate.executeUpdate() != 1) {
                throw new SQLException(
                        "The material stock could not be updated."
                );
            }
        }

        try (PreparedStatement issuanceUpdate =
                     conn.prepareStatement(updateIssuanceSql)) {

            issuanceUpdate.setInt(
                    1,
                    newQuantity
            );

            issuanceUpdate.setInt(
                    2,
                    issuedByUserId
            );

            issuanceUpdate.setInt(
                    3,
                    issuanceId
            );

            if (issuanceUpdate.executeUpdate() != 1) {
                throw new SQLException(
                        "The issuance quantity could not be updated."
                );
            }
        }

        conn.commit();
        return true;

    } catch (IllegalArgumentException ex) {

        rollbackTransaction(conn);
        throw ex;

    } catch (SQLException | ClassNotFoundException ex) {

        rollbackTransaction(conn);
        ex.printStackTrace();
        return false;

    } finally {

        closeConnection(conn);
    }
}
    
       private void closeConnection(Connection conn) {

    if (conn != null) {
        try {
            conn.setAutoCommit(true);
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
       
          private void rollbackTransaction(Connection conn) {

    if (conn != null) {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
}