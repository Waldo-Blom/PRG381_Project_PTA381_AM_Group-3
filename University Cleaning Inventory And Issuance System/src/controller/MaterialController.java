/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Material;

/**
 *
 * @author BC-STUDENT
 */
public class MaterialController implements MaterialDAOInterface{
    
    @Override
    public void addMaterial(Material material) {
        throw new UnsupportedOperationException(
                "Not implemented in this DAO yet."
        );
    }

    @Override
    public void updateMaterial(Material material) {
        throw new UnsupportedOperationException(
                "Not implemented in this DAO yet."
        );
    }

    @Override
    public void deleteMaterial(int materialId) {
        throw new UnsupportedOperationException(
                "Not implemented in this DAO yet."
        );
    }

    @Override
    public List<Material> getAllMaterials() {

        List<Material> materials = new ArrayList<>();

        String sql =
                "SELECT material_id, material_name, category, "
                + "description, quantity, unit, reorder_level, "
                + "unit_cost, supplier_id "
                + "FROM materials "
                + "ORDER BY material_name";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Material material = new Material();

                material.setMaterialId(
                        rs.getInt("material_id")
                );

                material.setMaterialName(
                        rs.getString("material_name")
                );

                material.setCategory(
                        rs.getString("category")
                );

                material.setDescription(
                        rs.getString("description")
                );

                material.setQuantity(
                        rs.getInt("quantity")
                );

                material.setUnit(
                        rs.getString("unit")
                );

                material.setReorderLevel(
                        rs.getInt("reorder_level")
                );

                material.setUnitCost(
                        rs.getDouble("unit_cost")
                );

                int supplierId =
                        rs.getInt("supplier_id");

                if (!rs.wasNull()) {
                    material.setSupplierId(supplierId);
                }

                materials.add(material);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Materials found: " + materials.size());

        return materials;
    }

    @Override
    public List<Material> getLowStockMaterials() {

        List<Material> materials = new ArrayList<>();

        String sql =
                "SELECT material_id, material_name, category, "
                + "description, quantity, unit, reorder_level, "
                + "unit_cost, supplier_id "
                + "FROM materials "
                + "WHERE quantity <= reorder_level "
                + "ORDER BY material_name";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Material material = new Material();

                material.setMaterialId(
                        rs.getInt("material_id")
                );

                material.setMaterialName(
                        rs.getString("material_name")
                );

                material.setCategory(
                        rs.getString("category")
                );

                material.setDescription(
                        rs.getString("description")
                );

                material.setQuantity(
                        rs.getInt("quantity")
                );

                material.setUnit(
                        rs.getString("unit")
                );

                material.setReorderLevel(
                        rs.getInt("reorder_level")
                );

                material.setUnitCost(
                        rs.getDouble("unit_cost")
                );

                int supplierId =
                        rs.getInt("supplier_id");

                if (!rs.wasNull()) {
                    material.setSupplierId(supplierId);
                }

                materials.add(material);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return materials;
    }

    @Override
    public List<Material> searchMaterials(String query) {

        List<Material> materials = new ArrayList<>();

        String sql =
                "SELECT material_id, material_name, category, "
                + "description, quantity, unit, reorder_level, "
                + "unit_cost, supplier_id "
                + "FROM materials "
                + "WHERE LOWER(material_name) LIKE ? "
                + "OR LOWER(category) LIKE ? "
                + "ORDER BY material_name";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            String searchValue =
                    "%" + query.toLowerCase().trim() + "%";

            ps.setString(1, searchValue);
            ps.setString(2, searchValue);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Material material = new Material();

                    material.setMaterialId(
                            rs.getInt("material_id")
                    );

                    material.setMaterialName(
                            rs.getString("material_name")
                    );

                    material.setCategory(
                            rs.getString("category")
                    );

                    material.setDescription(
                            rs.getString("description")
                    );

                    material.setQuantity(
                            rs.getInt("quantity")
                    );

                    material.setUnit(
                            rs.getString("unit")
                    );

                    material.setReorderLevel(
                            rs.getInt("reorder_level")
                    );

                    material.setUnitCost(
                            rs.getDouble("unit_cost")
                    );

                    int supplierId =
                            rs.getInt("supplier_id");

                    if (!rs.wasNull()) {
                        material.setSupplierId(supplierId);
                    }

                    materials.add(material);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return materials;
    }
}
