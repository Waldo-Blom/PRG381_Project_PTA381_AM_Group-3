/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;


import dao.StockIssuanceDAO;
import java.util.List;
import model.StockIssuance;

public class StockIssuanceController {

    private final StockIssuanceDAO issuanceDAO;

    public StockIssuanceController() {
        this.issuanceDAO = new StockIssuanceDAO();
    }

    public boolean issueMaterial(StockIssuance issuance) {

        if (issuance == null) {
            throw new IllegalArgumentException(
                    "Issuance details cannot be empty."
            );
        }

        if (issuance.getMaterialId() <= 0) {
            throw new IllegalArgumentException(
                    "A valid material must be selected."
            );
        }

        if (issuance.getCleanerId() <= 0) {
            throw new IllegalArgumentException(
                    "A valid cleaner must be selected."
            );
        }

        if (issuance.getQuantityIssued() <= 0) {
            throw new IllegalArgumentException(
                    "Quantity issued must be greater than zero."
            );
        }

        if (issuance.getIssuedByUserId() <= 0) {
            throw new IllegalArgumentException(
                    "A valid logged-in user is required."
            );
        }

        return issuanceDAO.issueMaterial(issuance);
    }

    public List<StockIssuance> loadAllIssuances() {
        return issuanceDAO.getAllIssuances();
    }

    public StockIssuance findIssuanceById(int issuanceId) {

        if (issuanceId <= 0) {
            throw new IllegalArgumentException(
                    "The issuance ID must be greater than zero."
            );
        }

        return issuanceDAO.getIssuanceById(issuanceId);
    }
    
    public boolean deleteIssuance(int issuanceId) {

    if (issuanceId <= 0) {
        throw new IllegalArgumentException(
                "A valid issuance must be selected."
        );
    }

    return issuanceDAO.deleteIssuance(
            issuanceId
    );
}
    
    public boolean updateIssuanceQuantity(
        int issuanceId,
        int newQuantity) {

    if (issuanceId <= 0) {
        throw new IllegalArgumentException(
                "A valid issuance must be selected."
        );
    }

    if (newQuantity <= 0) {
        throw new IllegalArgumentException(
                "Quantity must be greater than zero."
        );
    }

    return issuanceDAO.updateIssuanceQuantity(
            issuanceId,
            newQuantity
    );
}
}