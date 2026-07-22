/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.sql.Timestamp;
/**
 *
 * @author BC-STUDENT
 */
public class StockIssuance {
    private int issuanceId;
    private Timestamp issuanceDate;
    private int materialId;
    private int cleanerId;
    private int quantityIssued;
    private int issuedByUserId;
    private String notes;
    private String materialName;
    private String cleanerName;
    private String issuedByUsername;

    public StockIssuance() {
    }

    public StockIssuance(int issuanceId, Timestamp issuanceDate,
                         int materialId, int cleanerId,
                         int quantityIssued,
                         int issuedByUserId,
                         String notes) {

        this.issuanceId = issuanceId;
        this.issuanceDate = issuanceDate;
        this.materialId = materialId;
        this.cleanerId = cleanerId;
        this.quantityIssued = quantityIssued;
        this.issuedByUserId = issuedByUserId;
        this.notes = notes;
    }

    public int getIssuanceId() {
        return issuanceId;
    }

    public void setIssuanceId(int issuanceId) {
        this.issuanceId = issuanceId;
    }

    public Timestamp getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(Timestamp issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getCleanerId() {
        return cleanerId;
    }

    public void setCleanerId(int cleanerId) {
        this.cleanerId = cleanerId;
    }

    public int getQuantityIssued() {
        return quantityIssued;
    }

    public void setQuantityIssued(int quantityIssued) {
        this.quantityIssued = quantityIssued;
    }

    public int getIssuedByUserId() {
        return issuedByUserId;
    }

    public void setIssuedByUserId(int issuedByUserId) {
        this.issuedByUserId = issuedByUserId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getMaterialName() {
    return materialName;
}

public void setMaterialName(String materialName) {
    this.materialName = materialName;
}

public String getCleanerName() {
    return cleanerName;
}

public void setCleanerName(String cleanerName) {
    this.cleanerName = cleanerName;
}

public String getIssuedByUsername() {
    return issuedByUsername;
}

public void setIssuedByUsername(String issuedByUsername) {
    this.issuedByUsername = issuedByUsername;
}
}
