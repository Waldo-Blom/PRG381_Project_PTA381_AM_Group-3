/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import model.Material;
import java.util.ArrayList;
import java.util.List;

public class MockMaterialDAO implements MaterialDAO {
    private static final List<Material> fakeDatabase = new ArrayList<>();

    // Populating realistic sample data matching your exact schema options
    static {
        fakeDatabase.add(new Material(1, "Pine Disinfectant 5L", "Disinfectants", "High-concentration sanitizing liquid", 12, "liters", 5, 145.50, 1));
        fakeDatabase.add(new Material(2, "Heavy Duty Nitrile Gloves", "Safety", "Box of 100 industrial gloves", 2, "packs", 10, 89.99, 2)); // Low stock alert trigger!
        fakeDatabase.add(new Material(3, "Microfiber Mop Heads", "Tools", "Replacement industrial mop heads", 15, "units", 5, 65.00, 1));
        fakeDatabase.add(new Material(4, "Bleach 750ml", "Cleaners", "Multi-surface bleaching agents", 4, "units", 8, 24.50, null)); // Demonstrating a null supplier link
    }

    @Override
    public void addMaterial(Material material) {
        fakeDatabase.add(material);
    }

    @Override
    public void updateMaterial(Material material) {
        for (int i = 0; i < fakeDatabase.size(); i++) {
            if (fakeDatabase.get(i).getMaterialId() == material.getMaterialId()) {
                fakeDatabase.set(i, material);
                return;
            }
        }
    }

    @Override
    public void deleteMaterial(int materialId) {
        fakeDatabase.removeIf(m -> m.getMaterialId() == materialId);
    }

    @Override
    public List<Material> getAllMaterials() {
        return fakeDatabase;
    }

    @Override
    public List<Material> getLowStockMaterials() {
        List<Material> lowStock = new ArrayList<>();
        for (Material m : fakeDatabase) {
            // Business rule validation check matching schema rules
            if (m.getQuantity() <= m.getReorderLevel()) {
                lowStock.add(m);
            }
        }
        return lowStock;
    }

    @Override
    public List<Material> searchMaterials(String query) {
        List<Material> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (Material m : fakeDatabase) {
            if (m.getMaterialName().toLowerCase().contains(lowerQuery) || 
                m.getCategory().toLowerCase().contains(lowerQuery)) {
                results.add(m);
            }
        }
        return results;
    }
    
    public double getTotalInventoryValue() {
        double totalValue = 0;
        for (Material m : fakeDatabase) {
            totalValue += (m.getQuantity() * m.getUnitCost());
        }
        return totalValue;
    }

    public int getTotalDistinctMaterials() {
        return fakeDatabase.size();
    }

    public int getTotalUnitCount() {
        int totalUnits = 0;
        for (Material m : fakeDatabase) {
            totalUnits += m.getQuantity();
        }
        return totalUnits;
    }
}