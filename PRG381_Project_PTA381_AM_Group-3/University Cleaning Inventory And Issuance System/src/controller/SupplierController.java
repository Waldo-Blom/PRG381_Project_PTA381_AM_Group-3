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

    public SupplierController() {
        try {
            Connection connection = DBConnection.getConnection();
            this.supplierService = new SupplierService(connection);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
