package ui.panels;

import ui.MainFrame;
import ui.popDiaglogs.AddStockIssuanceDialog;
import utils.CurrentUser;
import utils.uiUtilities;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.StockIssuance;

import controller.StockIssuanceController;




/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author waldo
 */
public class StockIssuancePnl extends javax.swing.JPanel {
    
 private final StockIssuanceController issuanceController;
    /**
     * Creates new form MaterialsPanel
     */
    public StockIssuancePnl() {
      initComponents();

    issuanceController =
            new StockIssuanceController();

    configureIssuanceTable();
    hideIssuanceIdColumn();

    uiUtilities.applyTableStyleProperties(
            jTable1,
            jScrollPane1,
            new int[]{100, 140, 140, 90, 120, 170, 60, 70}
    );

    uiUtilities.installPlaceholder(
            SearchMaterial,
            "Search by material ..."
    );

    applyRoleRestrictions();
    loadIssuanceHistory();
    updateSummaryCards();
    }
private void loadIssuanceHistory() {

    DefaultTableModel tableModel =
            (DefaultTableModel) jTable1.getModel();

    tableModel.setRowCount(0);

    List<StockIssuance> issuances =
            issuanceController.loadAllIssuances();

    for (StockIssuance issuance : issuances) {
        addIssuanceToTable(tableModel, issuance);
    }
}

private void addIssuanceToTable(
        DefaultTableModel tableModel,
        StockIssuance issuance) {

tableModel.addRow(new Object[]{
        issuance.getIssuanceId(),
        issuance.getIssuanceDate(),
        issuance.getMaterialName(),
        issuance.getCleanerName(),
        issuance.getQuantityIssued(),
        issuance.getIssuedByUsername(),
        issuance.getNotes(),
        "Edit",
        "Delete"
    });
}

private void hideIssuanceIdColumn() {

    jTable1.getColumnModel()
            .getColumn(0)
            .setMinWidth(0);

    jTable1.getColumnModel()
            .getColumn(0)
            .setMaxWidth(0);

    jTable1.getColumnModel()
            .getColumn(0)
            .setPreferredWidth(0);
}


private void updateSummaryCards() {

    List<StockIssuance> issuances =
            issuanceController.loadAllIssuances();

    int totalIssuance = issuances.size();
    int totalItemsIssued = 0;

    for (StockIssuance issuance : issuances) {
        totalItemsIssued += issuance.getQuantityIssued();
    }
    

    lblTotalIssuances.setText(
            String.valueOf(totalIssuance)
    );

    lblTototalItemsIssued.setText(
            String.valueOf(totalItemsIssued)
    );
}
private void filterIssuances() {

    String searchText =
            SearchMaterial.getText()
                    .trim()
                    .toLowerCase();

    /*
     * Ignore the placeholder text.
     */
    if (searchText.equals(
            "search by material ..."
                    .toLowerCase())) {

        searchText = "";
    }

    DefaultTableModel tableModel =
            (DefaultTableModel) jTable1.getModel();

    tableModel.setRowCount(0);

    try {

        List<StockIssuance> issuances =
                issuanceController.loadAllIssuances();

        for (StockIssuance issuance : issuances) {

            String materialName =
                    issuance.getMaterialName() == null
                    ? ""
                    : issuance.getMaterialName()
                            .toLowerCase();

            boolean matchesMaterial =
                    searchText.isEmpty()
                    || materialName.contains(searchText);

            if (matchesMaterial) {

                addIssuanceToTable(
                        tableModel,
                        issuance
                );
            }
        }

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "The issuance records could not be searched.\n"
                + ex.getMessage(),
                "Search Error",
                JOptionPane.ERROR_MESSAGE
        );

        ex.printStackTrace();
    }
}

private void editIssuanceQuantity(
         int issuanceId,
         int modelRow) {
Object currentQuantity =
            jTable1.getModel()
                    .getValueAt(modelRow, 4);

    String input =
            JOptionPane.showInputDialog(
                    this,
                    "Enter the new quantity:",
                    currentQuantity
            );

    if (input == null) {
        return;
    }

    try {

        int newQuantity =
                Integer.parseInt(
                        input.trim()
                );

        boolean updated =
                issuanceController
                        .updateIssuanceQuantity(
                                issuanceId,
                                newQuantity
                        );

        if (updated) {

            JOptionPane.showMessageDialog(
                    this,
                    "Quantity updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            refreshIssuancePage();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "The quantity could not be updated.",
                    "Update Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    } catch (NumberFormatException ex) {

        JOptionPane.showMessageDialog(
                this,
                "Enter a valid whole number.",
                "Invalid Quantity",
                JOptionPane.WARNING_MESSAGE
        );

    } catch (IllegalArgumentException ex) {

        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
    }
}

private void deleteIssuance(
        int issuanceId) {

     int choice =
            JOptionPane.showConfirmDialog(
                    this,
                    "Delete this stock issuance?\n"
                    + "The issued quantity will be returned to stock.",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

    if (choice != JOptionPane.YES_OPTION) {
        return;
    }

    try {

        boolean deleted =
                issuanceController
                        .deleteIssuance(issuanceId);

        if (deleted) {

            JOptionPane.showMessageDialog(
                    this,
                    "Issuance deleted and stock restored.",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE
            );

            refreshIssuancePage();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "The issuance could not be deleted.",
                    "Delete Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    } catch (IllegalArgumentException ex) {

        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Delete Error",
                JOptionPane.WARNING_MESSAGE
        );
    }
}


private void configureIssuanceTable() {

    DefaultTableModel model =
            new DefaultTableModel(
                    new Object[]{
                        "ID",
                        "Date",
                        "Material",
                        "Issued To",
                        "Quantity",
                        "Issued By",
                        "Notes",
                        "Edit",
                        "Delete"
                    },
                    0
            ) {

        @Override
        public boolean isCellEditable(
                int row,
                int column) {

            return false;
        }
    };

    jTable1.setModel(model);

    hideIssuanceIdColumn();
}



private void refreshIssuancePage() {
    loadIssuanceHistory();
    updateSummaryCards();
}

    
    private void applyRoleRestrictions() {
        
        boolean isStorekeeper = CurrentUser.isStorekeeper();
        boolean isCleaner = CurrentUser.isCleaner();
        // Owner is neither, Owner can only view this panel

        // Both Storekeeper and Cleaner can create new issuances
        btnIssueStock.setEnabled(isStorekeeper || isCleaner);
        btnIssueStock.setVisible(isStorekeeper || isCleaner);

        // Owner has NO Edit/Delete access at all, hide those colums
        // Storekeeper and Cleaner both get Edit/Delete columns, but Cleaner
        
        //To DO: NNB implement Stock issaunce difference between Storekeeper and Cleaner:
        // Storekeeper -> All records
        // Cleaner -> only their own records
        if (!isStorekeeper && !isCleaner) {
            javax.swing.table.TableColumnModel columnModel = jTable1.getColumnModel();
            columnModel.removeColumn(jTable1.getColumn("Edit"));
            columnModel.removeColumn(jTable1.getColumn("Delete"));
        }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contentPnl = new javax.swing.JPanel();
        searchPnl = new javax.swing.JPanel();
        SearchMaterial = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btnIssueStock = new javax.swing.JButton();
        statsPnl = new javax.swing.JPanel();
        invValuePnl = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblTotalIssuances = new javax.swing.JLabel();
        totalMatsPnl = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblTototalItemsIssued = new javax.swing.JLabel();
        lowStockItemsPnl = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
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

        SearchMaterial.setText("Search by material ...");
        SearchMaterial.setToolTipText("Search by material ...");
        SearchMaterial.addActionListener(this::SearchMaterialActionPerformed);

        jButton1.setBackground(new java.awt.Color(59, 91, 219));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Search");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        btnIssueStock.setBackground(new java.awt.Color(59, 91, 219));
        btnIssueStock.setForeground(new java.awt.Color(255, 255, 255));
        btnIssueStock.setText("Add New Issuance");
        btnIssueStock.addActionListener(this::btnIssueStockActionPerformed);

        javax.swing.GroupLayout searchPnlLayout = new javax.swing.GroupLayout(searchPnl);
        searchPnl.setLayout(searchPnlLayout);
        searchPnlLayout.setHorizontalGroup(
            searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SearchMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 291, Short.MAX_VALUE)
                .addComponent(btnIssueStock, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        searchPnlLayout.setVerticalGroup(
            searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPnlLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SearchMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(btnIssueStock, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        contentPnl.add(searchPnl);

        statsPnl.setBackground(new java.awt.Color(245, 246, 250));
        statsPnl.setMaximumSize(new java.awt.Dimension(1000, 90));
        statsPnl.setMinimumSize(new java.awt.Dimension(1000, 90));
        statsPnl.setPreferredSize(new java.awt.Dimension(1000, 90));
        statsPnl.setLayout(new java.awt.GridLayout(1, 4, 3, 0));

        invValuePnl.setBackground(new java.awt.Color(255, 255, 255));
        invValuePnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Total Issuances");

        lblTotalIssuances.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTotalIssuances.setText("6");

        javax.swing.GroupLayout invValuePnlLayout = new javax.swing.GroupLayout(invValuePnl);
        invValuePnl.setLayout(invValuePnlLayout);
        invValuePnlLayout.setHorizontalGroup(
            invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invValuePnlLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalIssuances)
                    .addComponent(jLabel6))
                .addContainerGap(219, Short.MAX_VALUE))
        );
        invValuePnlLayout.setVerticalGroup(
            invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invValuePnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalIssuances)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        statsPnl.add(invValuePnl);

        totalMatsPnl.setBackground(new java.awt.Color(255, 255, 255));
        totalMatsPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Total Items Issued");

        lblTototalItemsIssued.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTototalItemsIssued.setText("8");

        javax.swing.GroupLayout totalMatsPnlLayout = new javax.swing.GroupLayout(totalMatsPnl);
        totalMatsPnl.setLayout(totalMatsPnlLayout);
        totalMatsPnlLayout.setHorizontalGroup(
            totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalMatsPnlLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(totalMatsPnlLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblTototalItemsIssued)))
                .addContainerGap(200, Short.MAX_VALUE))
        );
        totalMatsPnlLayout.setVerticalGroup(
            totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalMatsPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTototalItemsIssued)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statsPnl.add(totalMatsPnl);

        lowStockItemsPnl.setBackground(new java.awt.Color(255, 255, 255));
        lowStockItemsPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Low Stock items");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel13.setText("4");

        javax.swing.GroupLayout lowStockItemsPnlLayout = new javax.swing.GroupLayout(lowStockItemsPnl);
        lowStockItemsPnl.setLayout(lowStockItemsPnlLayout);
        lowStockItemsPnlLayout.setHorizontalGroup(
            lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lowStockItemsPnlLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel8))
                .addContainerGap(212, Short.MAX_VALUE))
        );
        lowStockItemsPnlLayout.setVerticalGroup(
            lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lowStockItemsPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statsPnl.add(lowStockItemsPnl);

        contentPnl.add(statsPnl);

        jPanel1.setBackground(new java.awt.Color(245, 246, 250));

        jScrollPane1.setBackground(new java.awt.Color(245, 246, 250));

        jTable1.setBackground(new java.awt.Color(245, 246, 250));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Date", "Material", "Issued To", "Qauntity", "Issued By", "Notes", "Edit", "Delete"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        contentPnl.add(jPanel1);

        add(contentPnl, java.awt.BorderLayout.CENTER);

        headerPnl.setBackground(new java.awt.Color(245, 246, 250));
        headerPnl.setMaximumSize(new java.awt.Dimension(1000, 70));
        headerPnl.setMinimumSize(new java.awt.Dimension(1000, 70));
        headerPnl.setPreferredSize(new java.awt.Dimension(1000, 70));
        headerPnl.setLayout(new javax.swing.BoxLayout(headerPnl, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Stock Issuance");
        headerPnl.add(jLabel1);

        jLabel2.setText("Check out the needed cleaning materials");
        headerPnl.add(jLabel2);

        add(headerPnl, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here
           int viewRow =
            jTable1.rowAtPoint(evt.getPoint());

    int viewColumn =
            jTable1.columnAtPoint(evt.getPoint());

    if (viewRow < 0 || viewColumn < 0) {
        return;
    }

    int modelRow =
            jTable1.convertRowIndexToModel(viewRow);

    int modelColumn =
            jTable1.convertColumnIndexToModel(viewColumn);

    int issuanceId =
            Integer.parseInt(
                    jTable1.getModel()
                            .getValueAt(modelRow, 0)
                            .toString()
            );

    if (modelColumn == 7) {

        editIssuanceQuantity(
                issuanceId,
                modelRow
        );

    } else if (modelColumn == 8) {

        deleteIssuance(
                issuanceId
        );
    }
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnIssueStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIssueStockActionPerformed
        // Make the AddMaterialsDialog pop up appear, dim the background
        java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
        MainFrame mainFrame = (MainFrame) parentFrame;

        mainFrame.showDimOverlay(true);   // dim the background BEFORE showing dialog

        AddStockIssuanceDialog dialog = new AddStockIssuanceDialog(parentFrame, true);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);           // this line BLOCKS here until dialog closes (since it's modal)

        mainFrame.showDimOverlay(false);  // runs AFTER dialog is closed/disposed
        refreshIssuancePage();
    }//GEN-LAST:event_btnIssueStockActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        filterIssuances();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void SearchMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchMaterialActionPerformed
        // TODO add your handling code here:
        filterIssuances();
    }//GEN-LAST:event_SearchMaterialActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField SearchMaterial;
    private javax.swing.JButton btnIssueStock;
    private javax.swing.JPanel contentPnl;
    private javax.swing.JPanel headerPnl;
    private javax.swing.JPanel invValuePnl;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblTotalIssuances;
    private javax.swing.JLabel lblTototalItemsIssued;
    private javax.swing.JPanel lowStockItemsPnl;
    private javax.swing.JPanel searchPnl;
    private javax.swing.JPanel statsPnl;
    private javax.swing.JPanel totalMatsPnl;
    // End of variables declaration//GEN-END:variables
}
