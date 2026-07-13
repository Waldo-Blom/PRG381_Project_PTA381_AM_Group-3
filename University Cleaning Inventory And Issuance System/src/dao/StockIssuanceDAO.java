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

        String sql =
                "INSERT INTO stock_issuances "
                + "(issuance_date, material_id, cleaner_id, "
                + "quantity_issued, issued_by_user_id, notes) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setTimestamp(1, issuance.getIssuanceDate());
            ps.setInt(2, issuance.getMaterialId());
            ps.setInt(3, issuance.getCleanerId());
            ps.setInt(4, issuance.getQuantityIssued());
            ps.setInt(5, issuance.getIssuedByUserId());
            ps.setString(6, issuance.getNotes());

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<StockIssuance> getAllIssuances() {

        List<StockIssuance> issuances = new ArrayList<>();

        String sql =
                "SELECT * FROM stock_issuances "
                + "ORDER BY issuance_date DESC";

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

                issuance.setCleanerId(
                        rs.getInt("cleaner_id"));

                issuance.setQuantityIssued(
                        rs.getInt("quantity_issued"));

                issuance.setIssuedByUserId(
                        rs.getInt("issued_by_user_id"));

                issuance.setNotes(
                        rs.getString("notes"));

                issuances.add(issuance);
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
}