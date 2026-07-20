package ui.utils;

import javax.swing.JOptionPane;

public class AlertUtils {

    public static void showSuccessAlert(String title, String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void showErrorAlert(String title, String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showWarningAlert(String title, String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static int showConfirmationDialog(String title, String message) {
        return JOptionPane.showConfirmDialog(
                null,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
    }

    public static void showAddedAlert(String itemType) {
        showSuccessAlert("Success", itemType + " added successfully!");
    }

    public static void showUpdatedAlert(String itemType) {
        showSuccessAlert("Success", itemType + " updated successfully!");
    }

    public static void showDeletedAlert(String itemType) {
        showSuccessAlert("Success", itemType + " deleted successfully!");
    }

    public static int showDeleteConfirmation(String itemType, String itemName) {
        return showConfirmationDialog(
                "Delete " + itemType,
                "Are you sure you want to delete " + itemType.toLowerCase() + ": " + itemName + "?"
        );
    }

    public static void showDeletedFailedAlert(String itemType) {
        showErrorAlert("Error", "Failed to delete " + itemType.toLowerCase() + ". Please try again.");
    }

    public static void showAddedFailedAlert(String itemType) {
        showErrorAlert("Error", "Failed to add " + itemType.toLowerCase() + ". Please try again.");
    }

    public static void showUpdatedFailedAlert(String itemType) {
        showErrorAlert("Error", "Failed to update " + itemType.toLowerCase() + ". Please try again.");
    }

    public static void showValidationAlert(String message) {
        showWarningAlert("Validation Error", message);
    }
}
