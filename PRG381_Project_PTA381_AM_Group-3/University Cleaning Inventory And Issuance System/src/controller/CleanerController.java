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

    public CleanerController() {
        try {
            Connection connection = DBConnection.getConnection();
            this.cleanerService = new CleanerService(connection);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
