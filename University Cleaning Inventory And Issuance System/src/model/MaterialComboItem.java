/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author BC-STUDENT
 */
public class MaterialComboItem {
private final int materialId;
    private final String materialName;

    public MaterialComboItem(
            int materialId,
            String materialName) {

        this.materialId = materialId;
        this.materialName = materialName;
    }

    public int getMaterialId() {
        return materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    @Override
    public String toString() {
        return materialName;
    }  
}
