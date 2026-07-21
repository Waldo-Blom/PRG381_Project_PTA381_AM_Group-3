package services;

import dao.CleanerDAO;
import model.Cleaner;

import java.sql.Connection;
import java.util.List;

/**
 * Service layer for Cleaner business logic.
 * Does NOT contain SQL - all persistence is delegated to CleanerDAO.
 */
public class CleanerService {

    private final CleanerDAO cleanerDAO;

    /**
     * Constructor - initialize with database connection.
     */
    public CleanerService(Connection connection) {
        this.cleanerDAO = new CleanerDAO(connection);
    }

    public boolean addCleaner(Cleaner cleaner) {
        return cleanerDAO.addCleaner(cleaner);
    }

    public List<Cleaner> getAllCleaners() {
        return cleanerDAO.getAllCleaners();
    }

    public Cleaner getCleanerById(int cleanerId) {
        return cleanerDAO.getCleanerById(cleanerId);
    }

    public boolean updateCleaner(Cleaner cleaner) {
        return cleanerDAO.updateCleaner(cleaner);
    }

    public boolean deleteCleaner(int cleanerId) {
        return cleanerDAO.deleteCleaner(cleanerId);
    }

    /**
     * Search cleaners by ID, name, or email.
     */
    public List<Cleaner> searchCleaners(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllCleaners();
        }
        return cleanerDAO.searchCleaners(searchTerm.trim());
    }

    /**
     * Distinct statuses currently stored in the database, for combo boxes.
     */
    public List<String> getStatuses() {
        return cleanerDAO.getStatuses();
    }

    /**
     * Distinct departments currently stored in the database, for combo boxes.
     */
    public List<String> getDepartments() {
        return cleanerDAO.getDepartments();
    }

    /**
     * The most recent database error message, if the last operation failed.
     */
    public String getLastError() {
        return cleanerDAO.getLastError();
    }
}
