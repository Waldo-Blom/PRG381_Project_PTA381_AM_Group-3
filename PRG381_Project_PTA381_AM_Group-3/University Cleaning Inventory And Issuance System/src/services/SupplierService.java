package services;

import dao.SupplierDAO;
import model.Supplier;

import java.sql.Connection;
import java.util.List;

/**
 * Service layer for Supplier business logic.
 * Does NOT contain SQL - all persistence is delegated to SupplierDAO.
 */
public class SupplierService {

    private final SupplierDAO supplierDAO;

    /**
     * Constructor - initialize with database connection.
     */
    public SupplierService(Connection connection) {
        this.supplierDAO = new SupplierDAO(connection);
    }

    public boolean addSupplier(Supplier supplier) {
        return supplierDAO.addSupplier(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    public Supplier getSupplierById(int supplierId) {
        return supplierDAO.getSupplierById(supplierId);
    }

    public boolean updateSupplier(Supplier supplier) {
        return supplierDAO.updateSupplier(supplier);
    }

    public boolean deleteSupplier(int supplierId) {
        return supplierDAO.deleteSupplier(supplierId);
    }

    /**
     * Search suppliers by name, phone number, or email.
     */
    public List<Supplier> searchSuppliers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllSuppliers();
        }
        return supplierDAO.searchSuppliers(searchTerm.trim());
    }
}
