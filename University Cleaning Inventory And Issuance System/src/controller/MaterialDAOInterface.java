/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.Material;
import java.util.List;

public interface MaterialDAOInterface {
    void addMaterial(Material material);
    void updateMaterial(Material material);
    void deleteMaterial(int materialId);
    List<Material> getAllMaterials();
    List<Material> getLowStockMaterials();
    List<Material> searchMaterials(String query);
}