/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;


import dao.StockIssuanceDAO;
import java.util.ArrayList;
import java.util.List;
import model.StockIssuance;
import model.User;
import utils.CurrentUser;

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

    /**
     * Loads the issuance history. 
     * 
     * Storekeepers and Owners see everyting;
     * 
     * a logged-in Cleaner only sees issuances that were issued
     * to them, matched via their account email against the cleaners
     * table's email.
     */
    public List<StockIssuance> loadAllIssuances() {

        if (CurrentUser.isCleaner()) {

            User currentUser = CurrentUser.get();

            if (currentUser == null || currentUser.getEmail() == null) {
                return new ArrayList<>();
            }

            return issuanceDAO.getIssuancesByCleanerEmail(
                    currentUser.getEmail()
            );
        }

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

    int issuedByUserId = getCurrentLoggedInUserId();

    return issuanceDAO.updateIssuanceQuantity(
            issuanceId,
            newQuantity,
            issuedByUserId
    );
}

    /**
     * get user_id of whoever is currently logged in, so an edit
     * to an issuance can record who most recently made the change.
     */
    private int getCurrentLoggedInUserId() {

        User currentUser = CurrentUser.get();

        if (currentUser == null || currentUser.getId() == null) {
            throw new IllegalStateException(
                    "No user is currently logged in."
            );
        }

        return Integer.parseInt(currentUser.getId());
    }
}