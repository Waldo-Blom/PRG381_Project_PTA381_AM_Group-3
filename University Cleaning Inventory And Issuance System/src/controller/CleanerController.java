package controller;

import model.Cleaner;
import services.CleanerService;
import utils.DBConnection;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

/**
 * Controller connecting the Cleaners UI to the CleanerService.
 * The UI should never talk to the service or DAO layer directly.
 */
public class CleanerController {

    private CleanerService cleanerService;
    private String connectionError;

    public CleanerController() {
        try {
            Connection connection = DBConnection.getConnection();
            if (connection == null) {
                connectionError = "Could not connect to the database. Check that PostgreSQL is running and that the credentials in DBConnection.java are correct.";
                return;
            }
            this.cleanerService = new CleanerService(connection);
        } catch (ClassNotFoundException e) {
            connectionError = "PostgreSQL JDBC driver not found: " + e.getMessage();
            e.printStackTrace();
        }
    }

    /**
     * @return a message describing why the controller could not connect to
     * the database, or null if the connection succeeded.
     */
    public String getConnectionError() {
        return connectionError;
    }

    /**
     * The most recent database error message, if the last operation failed
     * (e.g. a constraint violation such as a duplicate email).
     */
    public String getLastError() {
        if (connectionError != null) {
            return connectionError;
        }
        return cleanerService == null ? null : cleanerService.getLastError();
    }

    public boolean addCleaner(Cleaner cleaner) {
        if (cleanerService == null) {
            return false;
        }
        return cleanerService.addCleaner(cleaner);
    }

    public List<Cleaner> getAllCleaners() {
        if (cleanerService == null) {
            return Collections.emptyList();
        }
        return cleanerService.getAllCleaners();
    }

    public boolean updateCleaner(Cleaner cleaner) {
        if (cleanerService == null) {
            return false;
        }
        return cleanerService.updateCleaner(cleaner);
    }

    public boolean deleteCleaner(int cleanerId) {
        if (cleanerService == null) {
            return false;
        }
        return cleanerService.deleteCleaner(cleanerId);
    }

    public List<Cleaner> searchCleaners(String searchTerm) {
        if (cleanerService == null) {
            return Collections.emptyList();
        }
        return cleanerService.searchCleaners(searchTerm);
    }

    public List<String> getStatuses() {
        if (cleanerService == null) {
            return Collections.emptyList();
        }
        return cleanerService.getStatuses();
    }

    public List<String> getDepartments() {
        if (cleanerService == null) {
            return Collections.emptyList();
        }
        return cleanerService.getDepartments();
    }
}
