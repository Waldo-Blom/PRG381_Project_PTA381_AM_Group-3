package ui.panels;

import  utils.uiUtilities;
import dao.ReportsDAO;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.List;

//Used to create the CSV export tool
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author waldo
 */
public class ReportsPnl extends javax.swing.JPanel {
    
     private final ReportsDAO dao = new ReportsDAO();
     
     // Maps combo box display names back to their database IDs for filtering
     private final java.util.Map<String, Integer> cleanersMap = new java.util.LinkedHashMap<>();
     private final java.util.Map<String, Integer> materialsMap = new java.util.LinkedHashMap<>();

    /**
     * Creates new form MaterialsPanel
     */
    public ReportsPnl() {
        initComponents();
        
        uiUtilities.applyTableStyleProperties(jTable1, jScrollPane1);
        uiUtilities.applyTableStyleProperties(jTable2, jScrollPane2);
        uiUtilities.applyTableStyleProperties(jTable3, jScrollPane3);
        uiUtilities.applyTableStyleProperties(jTable4, jScrollPane4);
        
        populateComboBoxes();
        loadReports();
    }
    
    //These combo boxes are populdated from data within the database
    private void populateComboBoxes() {
        // Issuance History: Cleaners
        cmbInssuanceHistoryCleaners.removeAllItems();
        cmbInssuanceHistoryCleaners.addItem("All Cleaners");
        cleanersMap.clear();
        cleanersMap.putAll(dao.getCleanersList());
        
        for (String name : cleanersMap.keySet()) {
            cmbInssuanceHistoryCleaners.addItem(name);
        }

        // Material Usage: Materials
        cmbMaterialUsageMaterials.removeAllItems();
        cmbMaterialUsageMaterials.addItem("All Materials");
        materialsMap.clear();
        materialsMap.putAll(dao.getMaterialsList());
        
        for (String name : materialsMap.keySet()) {
            cmbMaterialUsageMaterials.addItem(name);
        }
    }
     
    private void loadReports() {
        // Full initial load using overloaded methods
        fillTable(jTable1, dao.getInventoryReport());
        fillTable(jTable2, dao.getLowStockReport());
        fillTable(jTable3, dao.getIssuanceHistoryReport());
        fillTable(jTable4, dao.getMaterialUsageReport());

        // Update low stock count
        int lowCount = dao.getLowStockCount();
        lblItemsBelowReorderLevel.setText(lowCount + " items below reorder level");

        // Update Issuance History stats
        updateIssuanceStats(jTable3);

        // Update Material Usage stats
        updateMaterialUsageStats(jTable4);
        
        updateInventoryStats(); 
    }

    // Refresh methods if any of the items in the search boxes or combo boxes change
    private void refreshInventoryReport() {
        String search = edtSearchInventoryReport.getText().trim();
        if (search.equals("Search material or supplier")){
            search = "";
        }
        String sort = cmbInventoryreportValue.getSelectedItem().toString();

        List<Object[]> data = dao.getInventoryReport(search, sort);
        fillTable(jTable1, data);
    }

   private void refreshIssuanceHistory() {
        Integer cleanerId = getSelectedCleanerId();
        List<Object[]> data = dao.getIssuanceHistoryReport(cleanerId);
        fillTable(jTable3, data);
        updateIssuanceStats(jTable3);
    }

    private void refreshMaterialUsage() {
        Integer materialId = getSelectedMaterialId();
        String sort = cmbMaterialUsageValue.getSelectedItem().toString();
        List<Object[]> data = dao.getMaterialUsageReport(materialId, sort);
        fillTable(jTable4, data);
        updateMaterialUsageStats(jTable4);
    }

    
    // Refreshes the given JTable with new data: clears all existing rows,
    // then it repopulates the table's model with each row from the list that is provided
    //This method is used for all the tables on the Reports panel
    private void fillTable(JTable table, List<Object[]> rows) {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Object[] row : rows) {
            model.addRow(row);
        }
    }

    //In the database we retrieve data based on the id of items as this is the unique identifier
   private Integer getSelectedCleanerId() {
    String selected = (String) cmbInssuanceHistoryCleaners.getSelectedItem();
    if (selected == null || selected.equals("All Cleaners")){
        return null;
    }
    return cleanersMap.get(selected);
    }

    private Integer getSelectedMaterialId() {
        String selected = (String) cmbMaterialUsageMaterials.getSelectedItem();
        if (selected == null || selected.equals("All Materials")){
            return null;
        }
        return materialsMap.get(selected);
    }

    private void updateIssuanceStats(JTable table) {
        int rowCount = table.getRowCount();
        lblRecordAmount.setText(rowCount + " records");

        int totalItems = 0;
        for (int i = 0; i < rowCount; i++) {
            Object qty = table.getValueAt(i, 3); // column 3 = Quantity
            if (qty instanceof Integer) {
                totalItems += (int) qty;
            }
        }
        lblTotalItemsIssued.setText(String.valueOf(totalItems));
    }

    private void updateMaterialUsageStats(JTable table) {
        int rowCount = table.getRowCount();
        int totalMaterialsIssued = 0;
        double totalValueIssued = 0.0;
        int materialsWithIssuances = 0;

        for (int i = 0; i < rowCount; i++) {
            Object issued = table.getValueAt(i, 2); // Total Issued
            Object value = table.getValueAt(i, 4); // Value Issed (should be Value Issued)
            Object numIss = table.getValueAt(i, 5); // # Issuances

            if (issued instanceof Integer) {
                totalMaterialsIssued += (int) issued;
            }
            if (value instanceof Double) {
                totalValueIssued += (double) value;
            }
            if (numIss instanceof Integer && (int) numIss > 0) {
                materialsWithIssuances++;
            }
        }

        lblTotalMaterialsIssued.setText(String.valueOf(totalMaterialsIssued));
        lblTotalValueIssued.setText("R" + String.format("%,.2f", totalValueIssued));
        lblMaterialsWithIssuances.setText(String.valueOf(materialsWithIssuances));
    }
    
    private void updateInventoryStats() {
        int totalMaterials = dao.getTotalMaterialsCount();
        int totalUnits = dao.getTotalUnits();
        double totalValue = dao.getTotalValue();
        int lowStockItems = dao.getLowStockCount();   // already exists in ReportsDAO

        lblInventoryReportTotalMaterials.setText(String.valueOf(totalMaterials));
        lblInventoryReportTotalUnits.setText(String.valueOf(totalUnits));
        lblInventoryReportTotalValue.setText("R" + String.format("%,.2f", totalValue));
        lblInventoryReportLowStockItems.setText(String.valueOf(lowStockItems));
    }
    
    
    //The functions used to create the CSV export
    private void exportCurrentTabToCsv() {
        // Get the tab which the user is currently viewing
        int selectedIndex = jTabbedPane1.getSelectedIndex();

        JTable tableToExport;
        String defaultFileName;

        // Set names for the files based on the table export chosen
        switch (selectedIndex) {
            case 0:
                tableToExport = jTable1;
                defaultFileName = "inventory_report.csv";
                break;
            case 1:
                tableToExport = jTable2;
                defaultFileName = "low_stock_report.csv";
                break;
            case 2:
                tableToExport = jTable3;
                defaultFileName = "issuance_history_report.csv";
                break;
            case 3:
                tableToExport = jTable4;
                defaultFileName = "material_usage_report.csv";
                break;
            default:
                // Just default method to prevent any errors that could occur
                JOptionPane.showMessageDialog(this, "No report selected to export.",
                        "Export to CSV", JOptionPane.WARNING_MESSAGE);
                return;
        }

        // Let the user pick where to save the file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export to CSV");
        fileChooser.setSelectedFile(new File(defaultFileName));

        // If the user cancels the dialog abort the export
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION){
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();

        // Make sure the file always ends in .csv
        if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
            fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".csv");
        }

        try {
            writeTableToCsv(tableToExport, fileToSave);
            JOptionPane.showMessageDialog(this, "Exported to " + fileToSave.getName(),
                    "Export to CSV", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            // Catches any errors that could occur when the user tries to save but couldent
            JOptionPane.showMessageDialog(this, "Failed to export: " + ex.getMessage(),
                    "Export to CSV", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //This method gets the data from the table and actually writes it to the file
    //For this we are going to use a pipe "|" delited file
    private void writeTableToCsv(JTable table, File file) throws IOException {
        // Get the table's underlying data model (rows/columns), i.e what the colums in the table are
        //Used to loop through rows and colums
        javax.swing.table.TableModel model = table.getModel();

        // try-with-resources ensures the writer is closed automatically, even if an error occurs. (Just like we learned in PRG281 and PRG282)
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            int columnCount = model.getColumnCount();

            // Write the header row first (column names), separated by "|".
            StringBuilder header = new StringBuilder();
            for (int col = 0; col < columnCount; col++) {
                header.append(model.getColumnName(col));
                if (col < columnCount - 1){
                    header.append("|");
                } // pipe between columns, not after the last one
            }
            writer.println(header);

            // Write each data row underneath the header.
            for (int row = 0; row < model.getRowCount(); row++) {
                StringBuilder line = new StringBuilder();
                for (int col = 0; col < columnCount; col++) {
                    Object value = model.getValueAt(row, col);
                    // Guard against null cell values so we don't print the literal word "null".
                    line.append(value == null ? "" : value.toString());
                    if (col < columnCount - 1){
                        line.append("|");
                    }
                }
                writer.println(line);
            }
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        inventoryReportPnl = new javax.swing.JPanel();
        statsPnl = new javax.swing.JPanel();
        invValuePnl = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblInventoryReportTotalMaterials = new javax.swing.JLabel();
        totalMatsPnl = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblInventoryReportTotalUnits = new javax.swing.JLabel();
        lowStockItemsPnl = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lblInventoryReportTotalValue = new javax.swing.JLabel();
        activeCleanersPnl = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lblInventoryReportLowStockItems = new javax.swing.JLabel();
        searchPnl = new javax.swing.JPanel();
        edtSearchInventoryReport = new javax.swing.JTextField();
        searchMaterialOrSupplier = new javax.swing.JButton();
        cmbInventoryreportValue = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lowStockReportPnl = new javax.swing.JPanel();
        searchPnl3 = new javax.swing.JPanel();
        lblItemsBelowReorderLevel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        issuanceHistoryPnl = new javax.swing.JPanel();
        searchPnl6 = new javax.swing.JPanel();
        cmbInssuanceHistoryCleaners = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        lblRecordAmount = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblTotalItemsIssued = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        materialUsagePnl = new javax.swing.JPanel();
        searchPnl7 = new javax.swing.JPanel();
        cmbMaterialUsageMaterials = new javax.swing.JComboBox<>();
        cmbMaterialUsageValue = new javax.swing.JComboBox<>();
        statsPnl1 = new javax.swing.JPanel();
        invValuePnl1 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        lblTotalMaterialsIssued = new javax.swing.JLabel();
        totalMatsPnl1 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        lblTotalValueIssued = new javax.swing.JLabel();
        lowStockItemsPnl1 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        lblMaterialsWithIssuances = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        headerPnl = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        bntExportToCSV = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(245, 246, 250));
        setMaximumSize(new java.awt.Dimension(1000, 700));
        setMinimumSize(new java.awt.Dimension(1000, 700));
        setLayout(new java.awt.BorderLayout());

        contentPnl.setBackground(new java.awt.Color(245, 246, 250));
        contentPnl.setMaximumSize(new java.awt.Dimension(1000, 70));
        contentPnl.setMinimumSize(new java.awt.Dimension(1000, 70));
        contentPnl.setPreferredSize(new java.awt.Dimension(1000, 70));
        contentPnl.setLayout(new javax.swing.BoxLayout(contentPnl, javax.swing.BoxLayout.LINE_AXIS));

        jTabbedPane1.setBackground(new java.awt.Color(245, 246, 250));

        inventoryReportPnl.setBackground(new java.awt.Color(245, 246, 250));
        inventoryReportPnl.setLayout(new javax.swing.BoxLayout(inventoryReportPnl, javax.swing.BoxLayout.Y_AXIS));

        statsPnl.setMaximumSize(new java.awt.Dimension(1000, 100));
        statsPnl.setMinimumSize(new java.awt.Dimension(1000, 100));
        statsPnl.setPreferredSize(new java.awt.Dimension(1000, 100));
        statsPnl.setLayout(new java.awt.GridLayout(1, 4, 3, 0));

        invValuePnl.setBackground(new java.awt.Color(255, 255, 255));
        invValuePnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Total Materials");

        lblInventoryReportTotalMaterials.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblInventoryReportTotalMaterials.setText("6");

        javax.swing.GroupLayout invValuePnlLayout = new javax.swing.GroupLayout(invValuePnl);
        invValuePnl.setLayout(invValuePnlLayout);
        invValuePnlLayout.setHorizontalGroup(
            invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invValuePnlLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblInventoryReportTotalMaterials)
                    .addComponent(jLabel6))
                .addContainerGap(137, Short.MAX_VALUE))
        );
        invValuePnlLayout.setVerticalGroup(
            invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invValuePnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInventoryReportTotalMaterials)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        statsPnl.add(invValuePnl);

        totalMatsPnl.setBackground(new java.awt.Color(255, 255, 255));
        totalMatsPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Total Units");

        lblInventoryReportTotalUnits.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblInventoryReportTotalUnits.setText("6");

        javax.swing.GroupLayout totalMatsPnlLayout = new javax.swing.GroupLayout(totalMatsPnl);
        totalMatsPnl.setLayout(totalMatsPnlLayout);
        totalMatsPnlLayout.setHorizontalGroup(
            totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalMatsPnlLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(totalMatsPnlLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblInventoryReportTotalUnits))
                    .addComponent(jLabel7))
                .addContainerGap(161, Short.MAX_VALUE))
        );
        totalMatsPnlLayout.setVerticalGroup(
            totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalMatsPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInventoryReportTotalUnits)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        statsPnl.add(totalMatsPnl);

        lowStockItemsPnl.setBackground(new java.awt.Color(255, 255, 255));
        lowStockItemsPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Total Value");

        lblInventoryReportTotalValue.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblInventoryReportTotalValue.setText("R100,000");

        javax.swing.GroupLayout lowStockItemsPnlLayout = new javax.swing.GroupLayout(lowStockItemsPnl);
        lowStockItemsPnl.setLayout(lowStockItemsPnlLayout);
        lowStockItemsPnlLayout.setHorizontalGroup(
            lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lowStockItemsPnlLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lowStockItemsPnlLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblInventoryReportTotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8))
                .addContainerGap(96, Short.MAX_VALUE))
        );
        lowStockItemsPnlLayout.setVerticalGroup(
            lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lowStockItemsPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInventoryReportTotalValue)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        statsPnl.add(lowStockItemsPnl);

        activeCleanersPnl.setBackground(new java.awt.Color(255, 255, 255));
        activeCleanersPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Low Stock Items");

        lblInventoryReportLowStockItems.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblInventoryReportLowStockItems.setText("6");

        javax.swing.GroupLayout activeCleanersPnlLayout = new javax.swing.GroupLayout(activeCleanersPnl);
        activeCleanersPnl.setLayout(activeCleanersPnlLayout);
        activeCleanersPnlLayout.setHorizontalGroup(
            activeCleanersPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(activeCleanersPnlLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(activeCleanersPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(activeCleanersPnlLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblInventoryReportLowStockItems))
                    .addComponent(jLabel9))
                .addContainerGap(127, Short.MAX_VALUE))
        );
        activeCleanersPnlLayout.setVerticalGroup(
            activeCleanersPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(activeCleanersPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInventoryReportLowStockItems)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        statsPnl.add(activeCleanersPnl);

        inventoryReportPnl.add(statsPnl);

        searchPnl.setBackground(new java.awt.Color(245, 246, 250));
        searchPnl.setMaximumSize(new java.awt.Dimension(1000, 70));
        searchPnl.setMinimumSize(new java.awt.Dimension(1000, 70));

        edtSearchInventoryReport.setText("Search material or supplier");
        edtSearchInventoryReport.setToolTipText("text\tSearch material or supplier");
        edtSearchInventoryReport.addActionListener(this::edtSearchInventoryReportActionPerformed);

        searchMaterialOrSupplier.setText("Search");
        searchMaterialOrSupplier.addActionListener(this::searchMaterialOrSupplierActionPerformed);

        cmbInventoryreportValue.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Total Value ASC ↑", "Total Value DEC ↓", "Unit Cost ASC ↑", "Unit Cost DEC ↓", "Quantity ASC↑", "Quantity DEC ↓" }));
        cmbInventoryreportValue.addItemListener(this::cmbInventoryreportValueItemStateChanged);
        cmbInventoryreportValue.addActionListener(this::cmbInventoryreportValueActionPerformed);

        javax.swing.GroupLayout searchPnlLayout = new javax.swing.GroupLayout(searchPnl);
        searchPnl.setLayout(searchPnlLayout);
        searchPnlLayout.setHorizontalGroup(
            searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(edtSearchInventoryReport, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchMaterialOrSupplier)
                .addGap(18, 18, 18)
                .addComponent(cmbInventoryreportValue, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(228, Short.MAX_VALUE))
        );
        searchPnlLayout.setVerticalGroup(
            searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPnlLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtSearchInventoryReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchMaterialOrSupplier)
                    .addComponent(cmbInventoryreportValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        inventoryReportPnl.add(searchPnl);

        jScrollPane1.setBackground(new java.awt.Color(245, 246, 250));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Material", "Category", "Quantity", "Reorder Level", "Supplier", "Unit Cost", "Total Value", "Status"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        inventoryReportPnl.add(jScrollPane1);

        jTabbedPane1.addTab("Inventory Report", inventoryReportPnl);

        lowStockReportPnl.setBackground(new java.awt.Color(245, 246, 250));
        lowStockReportPnl.setLayout(new javax.swing.BoxLayout(lowStockReportPnl, javax.swing.BoxLayout.Y_AXIS));

        searchPnl3.setBackground(new java.awt.Color(255, 255, 255));
        searchPnl3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        searchPnl3.setMaximumSize(new java.awt.Dimension(1000, 100));
        searchPnl3.setMinimumSize(new java.awt.Dimension(1000, 100));

        lblItemsBelowReorderLevel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblItemsBelowReorderLevel.setText("4 items below reorder level");

        jLabel4.setText("Immediate attention required for restocking");

        javax.swing.GroupLayout searchPnl3Layout = new javax.swing.GroupLayout(searchPnl3);
        searchPnl3.setLayout(searchPnl3Layout);
        searchPnl3Layout.setHorizontalGroup(
            searchPnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPnl3Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(searchPnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblItemsBelowReorderLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(610, Short.MAX_VALUE))
        );
        searchPnl3Layout.setVerticalGroup(
            searchPnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPnl3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblItemsBelowReorderLevel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        lowStockReportPnl.add(searchPnl3);

        jScrollPane2.setBackground(new java.awt.Color(245, 246, 250));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Material", "Category", "Current", "Reorder Level", "Shortage", "Supplier"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        lowStockReportPnl.add(jScrollPane2);

        jTabbedPane1.addTab("Low Stock Report", lowStockReportPnl);

        issuanceHistoryPnl.setBackground(new java.awt.Color(245, 246, 250));
        issuanceHistoryPnl.setLayout(new javax.swing.BoxLayout(issuanceHistoryPnl, javax.swing.BoxLayout.Y_AXIS));

        searchPnl6.setBackground(new java.awt.Color(245, 246, 250));
        searchPnl6.setMaximumSize(new java.awt.Dimension(1000, 70));
        searchPnl6.setMinimumSize(new java.awt.Dimension(1000, 70));

        cmbInssuanceHistoryCleaners.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Cleaners" }));
        cmbInssuanceHistoryCleaners.addItemListener(this::cmbInssuanceHistoryCleanersItemStateChanged);

        javax.swing.GroupLayout searchPnl6Layout = new javax.swing.GroupLayout(searchPnl6);
        searchPnl6.setLayout(searchPnl6Layout);
        searchPnl6Layout.setHorizontalGroup(
            searchPnl6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPnl6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbInssuanceHistoryCleaners, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(804, Short.MAX_VALUE))
        );
        searchPnl6Layout.setVerticalGroup(
            searchPnl6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchPnl6Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(cmbInssuanceHistoryCleaners, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        issuanceHistoryPnl.add(searchPnl6);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(1000, 80));
        jPanel1.setMinimumSize(new java.awt.Dimension(1000, 80));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 80));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Showing ");

        lblRecordAmount.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblRecordAmount.setText("8 records");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Total Items Issued");

        lblTotalItemsIssued.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalItemsIssued.setText("25");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 747, Short.MAX_VALUE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblRecordAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTotalItemsIssued, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(40, 40, 40))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRecordAmount)
                    .addComponent(lblTotalItemsIssued))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        issuanceHistoryPnl.add(jPanel1);

        jScrollPane3.setBackground(new java.awt.Color(245, 246, 250));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Date", "Material", "Cleaner", "Quantity", "Issued By", "Notes"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        issuanceHistoryPnl.add(jScrollPane3);

        jTabbedPane1.addTab("Issuance History", issuanceHistoryPnl);

        materialUsagePnl.setBackground(new java.awt.Color(245, 246, 250));
        materialUsagePnl.setLayout(new javax.swing.BoxLayout(materialUsagePnl, javax.swing.BoxLayout.Y_AXIS));

        searchPnl7.setBackground(new java.awt.Color(245, 246, 250));
        searchPnl7.setMaximumSize(new java.awt.Dimension(1000, 70));
        searchPnl7.setMinimumSize(new java.awt.Dimension(1000, 70));

        cmbMaterialUsageMaterials.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Materials" }));
        cmbMaterialUsageMaterials.addItemListener(this::cmbMaterialUsageMaterialsItemStateChanged);

        cmbMaterialUsageValue.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Total Value Issued ASC ↑", "Total Value Issued DEC ↓", "Total Issed ASC ↑", "Total Issued DEC ↓", "#Issuances ASC↑", "#Issuances DEC ↓" }));
        cmbMaterialUsageValue.addItemListener(this::cmbMaterialUsageValueItemStateChanged);

        javax.swing.GroupLayout searchPnl7Layout = new javax.swing.GroupLayout(searchPnl7);
        searchPnl7.setLayout(searchPnl7Layout);
        searchPnl7Layout.setHorizontalGroup(
            searchPnl7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPnl7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbMaterialUsageMaterials, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cmbMaterialUsageValue, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(626, Short.MAX_VALUE))
        );
        searchPnl7Layout.setVerticalGroup(
            searchPnl7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPnl7Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(searchPnl7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbMaterialUsageMaterials, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMaterialUsageValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        materialUsagePnl.add(searchPnl7);

        statsPnl1.setMaximumSize(new java.awt.Dimension(1000, 100));
        statsPnl1.setMinimumSize(new java.awt.Dimension(1000, 100));
        statsPnl1.setPreferredSize(new java.awt.Dimension(1000, 100));
        statsPnl1.setLayout(new java.awt.GridLayout(1, 4, 3, 0));

        invValuePnl1.setBackground(new java.awt.Color(255, 255, 255));
        invValuePnl1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setText("Total Materials Issued");

        lblTotalMaterialsIssued.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTotalMaterialsIssued.setText("25");

        javax.swing.GroupLayout invValuePnl1Layout = new javax.swing.GroupLayout(invValuePnl1);
        invValuePnl1.setLayout(invValuePnl1Layout);
        invValuePnl1Layout.setHorizontalGroup(
            invValuePnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invValuePnl1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(invValuePnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalMaterialsIssued)
                    .addComponent(jLabel21))
                .addContainerGap(178, Short.MAX_VALUE))
        );
        invValuePnl1Layout.setVerticalGroup(
            invValuePnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invValuePnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalMaterialsIssued)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        statsPnl1.add(invValuePnl1);

        totalMatsPnl1.setBackground(new java.awt.Color(255, 255, 255));
        totalMatsPnl1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Total Value Issued");

        lblTotalValueIssued.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTotalValueIssued.setText("R100,000");

        javax.swing.GroupLayout totalMatsPnl1Layout = new javax.swing.GroupLayout(totalMatsPnl1);
        totalMatsPnl1.setLayout(totalMatsPnl1Layout);
        totalMatsPnl1Layout.setHorizontalGroup(
            totalMatsPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalMatsPnl1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(totalMatsPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(totalMatsPnl1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblTotalValueIssued))
                    .addComponent(jLabel23))
                .addContainerGap(198, Short.MAX_VALUE))
        );
        totalMatsPnl1Layout.setVerticalGroup(
            totalMatsPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalMatsPnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalValueIssued)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        statsPnl1.add(totalMatsPnl1);

        lowStockItemsPnl1.setBackground(new java.awt.Color(255, 255, 255));
        lowStockItemsPnl1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("Materials With Issuances");

        lblMaterialsWithIssuances.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblMaterialsWithIssuances.setText("7");

        javax.swing.GroupLayout lowStockItemsPnl1Layout = new javax.swing.GroupLayout(lowStockItemsPnl1);
        lowStockItemsPnl1.setLayout(lowStockItemsPnl1Layout);
        lowStockItemsPnl1Layout.setHorizontalGroup(
            lowStockItemsPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lowStockItemsPnl1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(lowStockItemsPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lowStockItemsPnl1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblMaterialsWithIssuances, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel25))
                .addContainerGap(160, Short.MAX_VALUE))
        );
        lowStockItemsPnl1Layout.setVerticalGroup(
            lowStockItemsPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lowStockItemsPnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMaterialsWithIssuances)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        statsPnl1.add(lowStockItemsPnl1);

        materialUsagePnl.add(statsPnl1);

        jScrollPane4.setBackground(new java.awt.Color(245, 246, 250));

        jTable4.setBackground(new java.awt.Color(245, 246, 250));
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Material", "Category", "Total Issued", "Remaining", "Value Issed", "# Issuances"
            }
        ));
        jScrollPane4.setViewportView(jTable4);

        materialUsagePnl.add(jScrollPane4);

        jTabbedPane1.addTab("Material Usage", materialUsagePnl);

        contentPnl.add(jTabbedPane1);

        add(contentPnl, java.awt.BorderLayout.CENTER);

        headerPnl.setBackground(new java.awt.Color(245, 246, 250));
        headerPnl.setMaximumSize(new java.awt.Dimension(1000, 70));
        headerPnl.setMinimumSize(new java.awt.Dimension(1000, 70));
        headerPnl.setPreferredSize(new java.awt.Dimension(1000, 70));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Reports");

        bntExportToCSV.setText("Export to CSV");
        bntExportToCSV.addActionListener(this::bntExportToCSVActionPerformed);

        jLabel2.setText("View different inventory reports");

        javax.swing.GroupLayout headerPnlLayout = new javax.swing.GroupLayout(headerPnl);
        headerPnl.setLayout(headerPnlLayout);
        headerPnlLayout.setHorizontalGroup(
            headerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPnlLayout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 642, Short.MAX_VALUE)
                .addComponent(bntExportToCSV, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
            .addGroup(headerPnlLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        headerPnlLayout.setVerticalGroup(
            headerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPnlLayout.createSequentialGroup()
                .addGroup(headerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(headerPnlLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPnlLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(bntExportToCSV, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        add(headerPnl, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void edtSearchInventoryReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtSearchInventoryReportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edtSearchInventoryReportActionPerformed

    private void searchMaterialOrSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchMaterialOrSupplierActionPerformed

        refreshInventoryReport();
    }//GEN-LAST:event_searchMaterialOrSupplierActionPerformed

    private void cmbInventoryreportValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbInventoryreportValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbInventoryreportValueActionPerformed

    private void cmbInventoryreportValueItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbInventoryreportValueItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED){
            refreshInventoryReport();
        }
    }//GEN-LAST:event_cmbInventoryreportValueItemStateChanged

    private void cmbInssuanceHistoryCleanersItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbInssuanceHistoryCleanersItemStateChanged
       if (evt.getStateChange() == ItemEvent.SELECTED){
           refreshIssuanceHistory();
       }
    }//GEN-LAST:event_cmbInssuanceHistoryCleanersItemStateChanged

    private void cmbMaterialUsageMaterialsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMaterialUsageMaterialsItemStateChanged
       if (evt.getStateChange() == ItemEvent.SELECTED){
           refreshMaterialUsage();
       }
    }//GEN-LAST:event_cmbMaterialUsageMaterialsItemStateChanged

    private void cmbMaterialUsageValueItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMaterialUsageValueItemStateChanged
       if (evt.getStateChange() == ItemEvent.SELECTED){
           refreshMaterialUsage();
       }
    }//GEN-LAST:event_cmbMaterialUsageValueItemStateChanged

    private void bntExportToCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntExportToCSVActionPerformed
     exportCurrentTabToCsv();
    }//GEN-LAST:event_bntExportToCSVActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel activeCleanersPnl;
    private javax.swing.JButton bntExportToCSV;
    private javax.swing.JComboBox<String> cmbInssuanceHistoryCleaners;
    private javax.swing.JComboBox<String> cmbInventoryreportValue;
    private javax.swing.JComboBox<String> cmbMaterialUsageMaterials;
    private javax.swing.JComboBox<String> cmbMaterialUsageValue;
    private javax.swing.JPanel contentPnl;
    private javax.swing.JTextField edtSearchInventoryReport;
    private javax.swing.JPanel headerPnl;
    private javax.swing.JPanel invValuePnl;
    private javax.swing.JPanel invValuePnl1;
    private javax.swing.JPanel inventoryReportPnl;
    private javax.swing.JPanel issuanceHistoryPnl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JLabel lblInventoryReportLowStockItems;
    private javax.swing.JLabel lblInventoryReportTotalMaterials;
    private javax.swing.JLabel lblInventoryReportTotalUnits;
    private javax.swing.JLabel lblInventoryReportTotalValue;
    private javax.swing.JLabel lblItemsBelowReorderLevel;
    private javax.swing.JLabel lblMaterialsWithIssuances;
    private javax.swing.JLabel lblRecordAmount;
    private javax.swing.JLabel lblTotalItemsIssued;
    private javax.swing.JLabel lblTotalMaterialsIssued;
    private javax.swing.JLabel lblTotalValueIssued;
    private javax.swing.JPanel lowStockItemsPnl;
    private javax.swing.JPanel lowStockItemsPnl1;
    private javax.swing.JPanel lowStockReportPnl;
    private javax.swing.JPanel materialUsagePnl;
    private javax.swing.JButton searchMaterialOrSupplier;
    private javax.swing.JPanel searchPnl;
    private javax.swing.JPanel searchPnl3;
    private javax.swing.JPanel searchPnl6;
    private javax.swing.JPanel searchPnl7;
    private javax.swing.JPanel statsPnl;
    private javax.swing.JPanel statsPnl1;
    private javax.swing.JPanel totalMatsPnl;
    private javax.swing.JPanel totalMatsPnl1;
    // End of variables declaration//GEN-END:variables
}
