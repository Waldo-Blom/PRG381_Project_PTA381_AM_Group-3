package ui.panels;

import ui.MainFrame;
import ui.popDiaglogs.AddStockIssuanceDialog;
import utils.CurrentUser;
import Controller.StockIssuanceController;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.StockIssuance;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


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

    issuanceController = new StockIssuanceController();

    utils.uiUtilities.applyTableStyleProperties(jTable1, jScrollPane1);
    applyRoleRestrictions();

    loadIssuanceHistory();
     updateSummaryCards();

    }
    
    private void searchIssuances() {

    String searchText =
            SearchMaterial.getText()
                    .trim()
                    .toLowerCase();

    DefaultTableModel tableModel =
            (DefaultTableModel) jTable1.getModel();

    tableModel.setRowCount(0);

    List<StockIssuance> issuances =
            issuanceController.loadAllIssuances();

    for (StockIssuance issuance : issuances) {

        String materialValue =
                String.valueOf(
                        issuance.getMaterialId()
                ).toLowerCase();

        if (searchText.isEmpty()
                || materialValue.contains(searchText)) {

            addIssuanceToTable(
                    tableModel,
                    issuance
            );
        }
    }
}
    private void addIssuanceToTable(
        DefaultTableModel tableModel,
        StockIssuance issuance) {

    tableModel.addRow(new Object[]{
        issuance.getIssuanceDate(),
        issuance.getMaterialId(),
        issuance.getCleanerId(),
        issuance.getQuantityIssued(),
        issuance.getIssuedByUserId(),
        issuance.getNotes(),
        "Edit",
        "Delete"
    });
}
    
    
    
    
    
  
 private void loadIssuanceHistory() {

    DefaultTableModel tableModel =
            (DefaultTableModel) jTable1.getModel();

    tableModel.setRowCount(0);

    List<StockIssuance> issuances =
            issuanceController.loadAllIssuances();

    for (StockIssuance issuance : issuances) {
        addIssuanceToTable(
                tableModel,
                issuance
        );
    }
}
 
private void filterIssuances() {

    String searchText =
            SearchMaterial.getText()
                    .trim()
                    .toLowerCase();

    String fromText =
            FromDate.getText().trim();

    String toText =
            ToDate.getText().trim();

    DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("MM/dd/yyyy");

    try {

        LocalDate fromDate = null;
        LocalDate toDate = null;

        if (!fromText.isEmpty()) {
            fromDate = LocalDate.parse(
                    fromText,
                    formatter
            );
        }

        if (!toText.isEmpty()) {
            toDate = LocalDate.parse(
                    toText,
                    formatter
            );
        }

        if (fromDate != null
                && toDate != null
                && fromDate.isAfter(toDate)) {

            throw new IllegalArgumentException(
                    "The From date cannot be after the To date."
            );
        }

        DefaultTableModel tableModel =
                (DefaultTableModel) jTable1.getModel();

        tableModel.setRowCount(0);

        List<StockIssuance> issuances =
                issuanceController.loadAllIssuances();

        for (StockIssuance issuance : issuances) {

            String materialValue =
                    String.valueOf(
                            issuance.getMaterialId()
                    ).toLowerCase();

            LocalDate issuanceLocalDate =
                    issuance.getIssuanceDate()
                            .toLocalDateTime()
                            .toLocalDate();

            boolean matchesMaterial =
                    searchText.isEmpty()
                    || materialValue.contains(searchText);

            boolean matchesFromDate =
                    fromDate == null
                    || !issuanceLocalDate.isBefore(fromDate);

            boolean matchesToDate =
                    toDate == null
                    || !issuanceLocalDate.isAfter(toDate);

            if (matchesMaterial
                    && matchesFromDate
                    && matchesToDate) {

                addIssuanceToTable(
                        tableModel,
                        issuance
                );
            }
        }

    } catch (DateTimeParseException ex) {

        JOptionPane.showMessageDialog(
                this,
                "Enter dates using MM/dd/yyyy.",
                "Invalid Date",
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
 
 
 
 
 
 
 
 
    
    private void updateSummaryCards() {

    List<StockIssuance> issuances =
            issuanceController.loadAllIssuances();

    int totalIssuances =
            issuances.size();

    int totalItemsIssued = 0;

    for (StockIssuance issuance : issuances) {
        totalItemsIssued +=
                issuance.getQuantityIssued();
    }

    TotalIssuance.setText(
            String.valueOf(totalIssuances)
    );

    TotalItemIssued.setText(
            String.valueOf(totalItemsIssued)
    );
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
        FromDate = new javax.swing.JFormattedTextField();
        ToDate = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        statsPnl = new javax.swing.JPanel();
        Totalisuuances = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        TotalIssuance = new javax.swing.JLabel();
        TotalItemsIssuedPnl = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        TotalItemIssued = new javax.swing.JLabel();
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

        jButton1.setText("Search");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        btnIssueStock.setText("+ New Issuance");
        btnIssueStock.addActionListener(this::btnIssueStockActionPerformed);

        FromDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("DD/MM/YYYY"))));
        FromDate.setText("mm/dd/yyyy");
        FromDate.addActionListener(this::FromDateActionPerformed);

        ToDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("DD/MM/YYYY"))));
        ToDate.setText("mm/dd/yyyy");
        ToDate.addActionListener(this::ToDateActionPerformed);

        jLabel3.setText("From:");

        jLabel4.setText("To:");

        javax.swing.GroupLayout searchPnlLayout = new javax.swing.GroupLayout(searchPnl);
        searchPnl.setLayout(searchPnlLayout);
        searchPnlLayout.setHorizontalGroup(
            searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SearchMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(24, 24, 24)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FromDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ToDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnIssueStock, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        searchPnlLayout.setVerticalGroup(
            searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPnlLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SearchMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(btnIssueStock, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FromDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ToDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        contentPnl.add(searchPnl);

        statsPnl.setBackground(new java.awt.Color(245, 246, 250));
        statsPnl.setMaximumSize(new java.awt.Dimension(1000, 90));
        statsPnl.setMinimumSize(new java.awt.Dimension(1000, 90));
        statsPnl.setPreferredSize(new java.awt.Dimension(1000, 90));
        statsPnl.setLayout(new java.awt.GridLayout(1, 4, 3, 0));

        Totalisuuances.setBackground(new java.awt.Color(255, 255, 255));
        Totalisuuances.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Total Issuances");

        TotalIssuance.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TotalIssuance.setText("6");

        javax.swing.GroupLayout TotalisuuancesLayout = new javax.swing.GroupLayout(Totalisuuances);
        Totalisuuances.setLayout(TotalisuuancesLayout);
        TotalisuuancesLayout.setHorizontalGroup(
            TotalisuuancesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TotalisuuancesLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(TotalisuuancesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TotalIssuance)
                    .addComponent(jLabel6))
                .addContainerGap(219, Short.MAX_VALUE))
        );
        TotalisuuancesLayout.setVerticalGroup(
            TotalisuuancesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TotalisuuancesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TotalIssuance)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        statsPnl.add(Totalisuuances);

        TotalItemsIssuedPnl.setBackground(new java.awt.Color(255, 255, 255));
        TotalItemsIssuedPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Total Items Issued");

        TotalItemIssued.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TotalItemIssued.setText("8");

        javax.swing.GroupLayout TotalItemsIssuedPnlLayout = new javax.swing.GroupLayout(TotalItemsIssuedPnl);
        TotalItemsIssuedPnl.setLayout(TotalItemsIssuedPnlLayout);
        TotalItemsIssuedPnlLayout.setHorizontalGroup(
            TotalItemsIssuedPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TotalItemsIssuedPnlLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(TotalItemsIssuedPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(TotalItemsIssuedPnlLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(TotalItemIssued)))
                .addContainerGap(200, Short.MAX_VALUE))
        );
        TotalItemsIssuedPnlLayout.setVerticalGroup(
            TotalItemsIssuedPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TotalItemsIssuedPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TotalItemIssued)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statsPnl.add(TotalItemsIssuedPnl);

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
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
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

    private void SearchMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchMaterialActionPerformed
        // TODO add your handling code here:
        searchIssuances();
    }//GEN-LAST:event_SearchMaterialActionPerformed

    private void btnIssueStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIssueStockActionPerformed




// Make the AddMaterialsDialog pop up appear, dim the background
    java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
    MainFrame mainFrame = (MainFrame) parentFrame;
    
    mainFrame.showDimOverlay(true);   // dim the background BEFORE showing dialog
    
    AddStockIssuanceDialog dialog = new AddStockIssuanceDialog(parentFrame, true);
    dialog.setLocationRelativeTo(parentFrame);
    dialog.setVisible(true);           // this line BLOCKS here until dialog closes (since it's modal)
    
    mainFrame.showDimOverlay(false);  // runs AFTER dialog is closed/disposed
    
   loadIssuanceHistory();
   updateSummaryCards();
    }//GEN-LAST:event_btnIssueStockActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        searchIssuances();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void FromDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FromDateActionPerformed
        // TODO add your handling code here:
        
        filterIssuances();
    }//GEN-LAST:event_FromDateActionPerformed

    private void ToDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToDateActionPerformed
        // TODO add your handling code here:
        filterIssuances();
        
    }//GEN-LAST:event_ToDateActionPerformed

    
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField FromDate;
    private javax.swing.JTextField SearchMaterial;
    private javax.swing.JFormattedTextField ToDate;
    private javax.swing.JLabel TotalIssuance;
    private javax.swing.JLabel TotalItemIssued;
    private javax.swing.JPanel TotalItemsIssuedPnl;
    private javax.swing.JPanel Totalisuuances;
    private javax.swing.JButton btnIssueStock;
    private javax.swing.JPanel contentPnl;
    private javax.swing.JPanel headerPnl;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel lowStockItemsPnl;
    private javax.swing.JPanel searchPnl;
    private javax.swing.JPanel statsPnl;
    // End of variables declaration//GEN-END:variables
}
