package controller;

import model.Supplier;
import services.SupplierService;
import utils.DBConnection;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

/**
 * Controller connecting the Suppliers UI to the SupplierService.
 * The UI should never talk to the service or DAO layer directly.
 */
public class SupplierController {

    private SupplierService supplierService;
    private String connectionError;

    public SupplierController() {
        try {
            Connection connection = DBConnection.getConnection();
            if (connection == null) {
                connectionError = "Could not connect to the database. Check that PostgreSQL is running and that the credentials in DBConnection.java are correct.";
                return;
            }
            this.supplierService = new SupplierService(connection);
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
        return supplierService == null ? null : supplierService.getLastError();
    }

    public boolean addSupplier(Supplier supplier) {
        if (supplierService == null) {
            return false;
        }
        return supplierService.addSupplier(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        if (supplierService == null) {
            return Collections.emptyList();
        }
        return supplierService.getAllSuppliers();
    }

    public boolean updateSupplier(Supplier supplier) {
        if (supplierService == null) {
            return false;
        }
        return supplierService.updateSupplier(supplier);
    }

    public boolean deleteSupplier(int supplierId) {
        if (supplierService == null) {
            return false;
        }
        return supplierService.deleteSupplier(supplierId);
    }

    public List<Supplier> searchSuppliers(String searchTerm) {
        if (supplierService == null) {
            return Collections.emptyList();
        }
        return supplierService.searchSuppliers(searchTerm);
    }
}
