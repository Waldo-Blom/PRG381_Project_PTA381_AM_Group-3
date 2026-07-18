package ui.panels;

import ui.popDiaglogs.AddMaterialDialog;
import ui.MainFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import utils.DBConnection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import utils.CurrentUser;

// This class builds the Materials management panel and uses Inheritance by extending JPanel.
// It bundles UI elements and data logic together, demonstrating Encapsulation.
public class MaterialsPnl extends javax.swing.JPanel {

    // Initializes the panel's layout, styles, and loads initial database records.
    // Uses Encapsulation by keeping this startup sequence hidden and self-contained.
    public MaterialsPnl() {
        initComponents();

        utils.uiUtilities.applyTableStyleProperties(tblDisplayMaterials, jScrollPane1);
        
        applyRoleRestrictions();

        refreshMaterialsData("", "All Categories");

        setupTableActionListeners();
    }
    
    // Secures the panel by hiding edit/delete features from unauthorized users.
    // Applies Encapsulation to protect sensitive application actions based on user roles.
    private void applyRoleRestrictions() {
        boolean canEdit = CurrentUser.isStorekeeper();
        
        btnAdd.setEnabled(canEdit);
        btnAdd.setVisible(canEdit);
        
        if (!canEdit) {
            javax.swing.table.TableColumnModel columnModel = tblDisplayMaterials.getColumnModel();
            columnModel.removeColumn(tblDisplayMaterials.getColumn("Edit"));
            columnModel.removeColumn(tblDisplayMaterials.getColumn("Delete"));
        }
    }

    // Fetches and filters materials from the database to update the table.
    // Abstracts complex SQL queries and data processing away from the main UI flow.
    public void refreshMaterialsData(String searchTerm, String selectedCategory) {
        DefaultTableModel model = (DefaultTableModel) tblDisplayMaterials.getModel();
        model.setRowCount(0); 

        int totalItemsCount = 0;
        int totalUnitsSum = 0;
        int lowStockCount = 0;

        StringBuilder queryBuilder = new StringBuilder(
            "SELECT m.material_name, m.category, m.quantity, m.reorder_level, m.unit_cost, s.supplier_name " +
            "FROM materials m " +
            "LEFT JOIN suppliers s ON m.supplier_id = s.supplier_id " +
            "WHERE 1=1"
        );

        if (searchTerm != null && !searchTerm.trim().isEmpty() && !searchTerm.equals("Search materials ...")) {
            queryBuilder.append(" AND LOWER(material_name) LIKE ?");
        }
        
        if (selectedCategory != null && !selectedCategory.equals("All Categories") && !selectedCategory.isEmpty()) {
            queryBuilder.append(" AND category = ?");
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                throw new SQLException("Database connection configuration failed.");
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {
                int paramIndex = 1;

                if (searchTerm != null && !searchTerm.trim().isEmpty() && !searchTerm.equals("Search materials ...")) {
                    pstmt.setString(paramIndex++, "%" + searchTerm.trim().toLowerCase() + "%");
                }
                
                if (selectedCategory != null && !selectedCategory.equals("All Categories") && !selectedCategory.isEmpty()) {
                    pstmt.setString(paramIndex++, selectedCategory);
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String name = rs.getString("material_name");
                        String category = rs.getString("category");
                        int quantity = rs.getInt("quantity");
                        int reorderLevel = rs.getInt("reorder_level");
                        
                        String supplierName = rs.getString("supplier_name");
                        if (supplierName == null) {
                            supplierName = "No Supplier Assigned";
                        }
                        double unitCost = rs.getDouble("unit_cost");

                        totalItemsCount++;
                        totalUnitsSum += quantity;
                        if (quantity <= reorderLevel) {
                            lowStockCount++;
                        }

                        Vector<Object> row = new Vector<>();
                        row.add(name);
                        row.add(category);
                        row.add(quantity);
                        row.add(reorderLevel);
                        row.add(supplierName); 
                        row.add("R " + String.format("%.2f", unitCost));
                        row.add("Edit");   
                        row.add("Delete"); 

                        model.addRow(row);
                    }
                }
            }
            
            lblTotalItems.setText(String.valueOf(totalItemsCount));
            lblLowStock.setText(String.valueOf(lowStockCount));
            lblTotalUnits.setText(String.valueOf(totalUnitsSum));

            tblDisplayMaterials.revalidate();
            tblDisplayMaterials.repaint();

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "PostgreSQL Driver could not be initialized: " + ex.getMessage(), "System Exception", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to pull inventory data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Default data loader that calls the filtered version without any search terms.
    // Demonstrates Polymorphism through Method Overloading for flexible data loading.
    public void refreshMaterialsData() {
        refreshMaterialsData("", "All Categories");
    }

    // Listens for mouse clicks on the table to trigger edit or delete actions.
    // Uses Polymorphism by overriding the MouseAdapter's default click behavior dynamically.
    private void setupTableActionListeners() {
        tblDisplayMaterials.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblDisplayMaterials.rowAtPoint(e.getPoint());
                int col = tblDisplayMaterials.columnAtPoint(e.getPoint());

                if (row < 0) return;

                String materialName = tblDisplayMaterials.getValueAt(row, 0).toString();
                String colName = tblDisplayMaterials.getColumnName(col);

                // Delete click action
                if (colName.equals("Delete")) {
                    int confirm = JOptionPane.showConfirmDialog(
                        MaterialsPnl.this, 
                        "Are you sure you want to delete '" + materialName + "'?", 
                        "Confirm Deletion", 
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteMaterialFromDatabase(materialName);
                    }
                }

                // Edit click action
                if (colName.equals("Edit")) {
                    String inputQty = JOptionPane.showInputDialog(
                        MaterialsPnl.this, 
                        "Enter new quantity for '" + materialName + "':", 
                        "Edit Material Stock", 
                        JOptionPane.QUESTION_MESSAGE
                    );

                    if (inputQty != null && !inputQty.trim().isEmpty()) {
                        try {
                            int newQty = Integer.parseInt(inputQty.trim());
                            if (newQty < 0) {
                                JOptionPane.showMessageDialog(MaterialsPnl.this, "Quantity cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            updateMaterialQuantityInDatabase(materialName, newQty);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(MaterialsPnl.this, "Please enter a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    // Safely removes a specific material record from the database.
    // Encapsulates the SQL deletion logic to keep data management secure and isolated.
    private void deleteMaterialFromDatabase(String name) {
        String sql = "DELETE FROM materials WHERE material_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Successfully removed from database.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshMaterialsData(); 
            } else {
                JOptionPane.showMessageDialog(this, "Material could not be found in the system.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "JDBC Driver error: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error during deletion: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Updates the stock quantity of a specific material in the database.
    // Uses Encapsulation to handle backend updates without exposing SQL to the user interface.
    private void updateMaterialQuantityInDatabase(String name, int newQty) {
        String sql = "UPDATE materials SET quantity = ? WHERE material_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, newQty);
            pstmt.setString(2, name);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Quantity updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshMaterialsData(); 
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "JDBC Driver error: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error during update: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Auto-generated method that builds and places all visual components on the screen.
    // Kept private to enforce strict Encapsulation, hiding the complex layout generation code.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contentPnl = new javax.swing.JPanel();
        searchPnl = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        cmbCategories = new javax.swing.JComboBox<>();
        btnAdd = new javax.swing.JButton();
        summaryPnl = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblTotalItems = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblLowStock = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblTotalUnits = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDisplayMaterials = new javax.swing.JTable();
        headerPnl = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(245, 246, 250));
        setMaximumSize(new java.awt.Dimension(1000, 700));
        setMinimumSize(new java.awt.Dimension(1000, 700));
        setLayout(new java.awt.BorderLayout());

        contentPnl.setBackground(new java.awt.Color(245, 246, 250));
        contentPnl.setMaximumSize(new java.awt.Dimension(1000, 70));
        contentPnl.setMinimumSize(new java.awt.Dimension(1000, 70));
        contentPnl.setPreferredSize(new java.awt.Dimension(1000, 70));
        contentPnl.setLayout(new javax.swing.BoxLayout(contentPnl, javax.swing.BoxLayout.Y_AXIS));

        searchPnl.setBackground(new java.awt.Color(245, 246, 250));
        searchPnl.setMaximumSize(new java.awt.Dimension(1000, 70));
        searchPnl.setMinimumSize(new java.awt.Dimension(1000, 70));
        searchPnl.setPreferredSize(new java.awt.Dimension(1000, 70));

        txtSearch.setText("Search materials ...");
        txtSearch.setToolTipText("Search materials ...");
        txtSearch.addActionListener(this::txtSearchActionPerformed);

        btnSearch.setText("Search");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        cmbCategories.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Categories", "Cleaners", "Disinfectants", "Tools", "Safety", "Consumables", "Hygiene" }));

        btnAdd.setText("Add new Material");
        btnAdd.addActionListener(this::btnAddActionPerformed);

        javax.swing.GroupLayout searchPnlLayout = new javax.swing.GroupLayout(searchPnl);
        searchPnl.setLayout(searchPnlLayout);
        searchPnlLayout.setHorizontalGroup(
            searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        searchPnlLayout.setVerticalGroup(
            searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPnlLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(cmbCategories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        contentPnl.add(searchPnl);

        summaryPnl.setMaximumSize(new java.awt.Dimension(1000, 90));
        summaryPnl.setMinimumSize(new java.awt.Dimension(1000, 90));
        summaryPnl.setPreferredSize(new java.awt.Dimension(1000, 90));
        summaryPnl.setLayout(new java.awt.GridLayout(1, 3, 1, 2));

        jPanel1.setBackground(new java.awt.Color(245, 246, 250));
        jPanel1.setLayout(new java.awt.GridLayout(1, 3, 100, 0));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Total Items");

        lblTotalItems.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTotalItems.setText("8");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(lblTotalItems)))
                .addGap(180, 180, 180))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalItems)
                .addContainerGap())
        );

        jPanel1.add(jPanel3);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Low Stock");

        lblLowStock.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblLowStock.setText("4");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(lblLowStock))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel4)))
                .addGap(181, 181, 181))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLowStock)
                .addContainerGap())
        );

        jPanel1.add(jPanel2);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Total Units");

        lblTotalUnits.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTotalUnits.setText("190");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblTotalUnits)))
                .addGap(178, 178, 178))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalUnits)
                .addContainerGap())
        );

        jPanel1.add(jPanel4);

        summaryPnl.add(jPanel1);

        contentPnl.add(summaryPnl);

        jPanel5.setBackground(new java.awt.Color(245, 246, 250));

        jScrollPane1.setBackground(new java.awt.Color(245, 246, 250));

        tblDisplayMaterials.setBackground(new java.awt.Color(245, 246, 250));
        tblDisplayMaterials.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Material", "Catergory", "Quantity", "Reorder Level", "Supplier", "Unit Cost", "Edit", "Delete"
            }
        ));
        jScrollPane1.setViewportView(tblDisplayMaterials);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 994, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                .addContainerGap())
        );

        contentPnl.add(jPanel5);

        add(contentPnl, java.awt.BorderLayout.CENTER);

        headerPnl.setBackground(new java.awt.Color(245, 246, 250));
        headerPnl.setMaximumSize(new java.awt.Dimension(1000, 70));
        headerPnl.setMinimumSize(new java.awt.Dimension(1000, 70));
        headerPnl.setPreferredSize(new java.awt.Dimension(1000, 70));
        headerPnl.setLayout(new javax.swing.BoxLayout(headerPnl, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Materials");
        headerPnl.add(jLabel1);

        jLabel2.setText("Manage your cleaning materials inventory");
        headerPnl.add(jLabel2);

        add(headerPnl, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        //Todo
    }//GEN-LAST:event_txtSearchActionPerformed

    // Opens the Add Material dialog and dims the background while it's active.
    // Uses Polymorphism to override default action listener behavior.
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        
        // Make the AddMaterialsDialog pop up appear, dim the background
        java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
        MainFrame mainFrame = (MainFrame) parentFrame;

        mainFrame.showDimOverlay(true);   

        AddMaterialDialog dialog = new AddMaterialDialog(parentFrame, true);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);           

        // runs AFTER dialog is closed/disposed
        mainFrame.showDimOverlay(false);  

        refreshMaterialsData("", "All Categories");

        // Refresh list after window is closed
        refreshMaterialsData("", "All Categories");
        
    }//GEN-LAST:event_btnAddActionPerformed

    // Reads the search input and triggers a targeted data refresh.
    // Demonstrates Polymorphism by executing custom instructions on button click.
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        String keyword = txtSearch.getText().trim();
        String selectedCategory = cmbCategories.getSelectedItem() != null ? cmbCategories.getSelectedItem().toString() : "All Categories";
        refreshMaterialsData(keyword, selectedCategory);// TODO add your handling code here:
    }//GEN-LAST:event_btnSearchActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cmbCategories;
    private javax.swing.JPanel contentPnl;
    private javax.swing.JPanel headerPnl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblLowStock;
    private javax.swing.JLabel lblTotalItems;
    private javax.swing.JLabel lblTotalUnits;
    private javax.swing.JPanel searchPnl;
    private javax.swing.JPanel summaryPnl;
    private javax.swing.JTable tblDisplayMaterials;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
