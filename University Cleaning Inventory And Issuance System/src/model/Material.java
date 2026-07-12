package model;

public class Material {
    private int materialId;
    private String materialName;
    private String category;
    private String description;
    private int quantity;
    private String unit;
    private int reorderLevel;
    private double unitCost;
    private Integer supplierId; // Using Integer instead of int to allow null values (ON DELETE SET NULL)

    // Default Constructor
    public Material() {}

    // Overloaded Constructor matching the updated database fields
    public Material(int materialId, String materialName, String category, String description, 
                    int quantity, String unit, int reorderLevel, double unitCost, Integer supplierId) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.category = category;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
        this.reorderLevel = reorderLevel;
        this.unitCost = unitCost;
        this.supplierId = supplierId;
    }

    // Getters and Setters (Encapsulation)
    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }

    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public int getReorderLevel() { return reorderLevel; }
    public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }

    public double getUnitCost() { return unitCost; }
    public void setUnitCost(double unitCost) { this.unitCost = unitCost; }

    public Integer getSupplierId() { return supplierId; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }
}