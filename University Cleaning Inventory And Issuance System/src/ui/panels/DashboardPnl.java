package ui.panels;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;
import utils.DBConnection;

// This class builds the Dashboard interface and uses Inheritance by extending JPanel.
// It bundles UI components and data logic together, demonstrating Encapsulation.
public class DashboardPnl extends javax.swing.JPanel {

    // Sets up the panel's visual elements and loads initial data when created.
    // Applies Encapsulation by managing its own setup routines internally.
    public DashboardPnl() {
        initComponents(); 
        
        // Apply your custom table UI visual styles
        utils.uiUtilities.applyTableStyleProperties(tblLowStockItems, jScrollPane1);
        utils.uiUtilities.applyTableStyleProperties(tblRecentStockIssuances, jScrollPane3);
        
        // Load live system data immediately on launch
        refreshDashboard();
    }

    // Connects to the database to fetch live summary statistics for the dashboard cards.
    // Abstracts the complex SQL query execution away from the main UI flow.
    private void loadOverviewMetrics() {
        String queryMetrics = 
            "SELECT " +
            "  COALESCE(SUM(quantity * unit_cost), 0) as total_val, " +
            "  COUNT(*) as total_mats, " +
            "  COALESCE(SUM(quantity), 0) as total_qty, " +
            "  (SELECT COUNT(*) FROM materials WHERE quantity <= reorder_level) as low_stock_count, " +
            "  (SELECT COUNT(*) FROM cleaners WHERE status = 'active') as active_cleaners, " +
            "  (SELECT COUNT(*) FROM cleaners WHERE status = 'inactive') as inactive_cleaners, " +
            "  (SELECT COUNT(*) FROM stock_issuances WHERE issuance_date >= NOW() - INTERVAL '7 days') as recent_issuances " +
            "FROM materials";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return;
            
            try (PreparedStatement pstmt = conn.prepareStatement(queryMetrics);
                 ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    double totalValue = rs.getDouble("total_val");
                    int totalMaterials = rs.getInt("total_mats");
                    int totalUnits = rs.getInt("total_qty");
                    int lowStockCount = rs.getInt("low_stock_count");
                    int activeCleaners = rs.getInt("active_cleaners");
                    int inactiveCleaners = rs.getInt("inactive_cleaners");
                    int recentIssuances = rs.getInt("recent_issuances");

                    // 1. Update Inventory Value Card
                    pnlInventoryValue.setText("R " + String.format("%,.2f", totalValue));
                    jLabel18.setText("Across " + totalMaterials + " material types");

                    // 2. Update Total Materials Card
                    pnlTotalMaterials.setText(String.valueOf(totalMaterials));
                    jLabel16.setText("of " + totalUnits + " units total");

                    // 3. Update Low Stock Card
                    lblLowStockItems.setText(String.valueOf(lowStockCount));

                    // 4. Update Active Cleaners Card
                    lblActiveCleaners.setText(String.valueOf(activeCleaners));
                    jLabel19.setText(inactiveCleaners + " inactive");

                    // 5. Update Recent Issuances Card
                    lblRecentIssuances.setText(String.valueOf(recentIssuances));
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("[DEBUG] Failed to load dashboard metrics: " + ex.getMessage());
        }
    }

    // Retrieves low stock items from the database to populate the warning table.
    // Encapsulates the specific data-fetching and table-updating logic within this method.
    private void loadLowStockTable() {
        DefaultTableModel model = (DefaultTableModel) tblLowStockItems.getModel();
        model.setRowCount(0); 

        String query = 
            "SELECT m.material_name, m.quantity, m.reorder_level, s.supplier_name " +
            "FROM materials m " +
            "LEFT JOIN suppliers s ON m.supplier_id = s.supplier_id " +
            "WHERE m.quantity <= m.reorder_level " +
            "ORDER BY m.quantity ASC";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return;
            
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    String name = rs.getString("material_name");
                    int qty = rs.getInt("quantity");
                    int min = rs.getInt("reorder_level");
                    String supplier = rs.getString("supplier_name");
                    
                    if (supplier == null) {
                        supplier = "No Supplier Assigned";
                    }

                    model.addRow(new Object[]{name, qty, min, supplier});
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("[DEBUG] Failed to load low stock table: " + ex.getMessage());
        }
    }

    // Fetches the latest stock issuance records and formats them for the recent activity table.
    // Keeps the database connection and data processing logic neatly Encapsulated.
    private void loadRecentIssuancesTable() {
        DefaultTableModel model = (DefaultTableModel) tblRecentStockIssuances.getModel();
        model.setRowCount(0); 

        String query = 
            "SELECT m.material_name, c.full_name, i.quantity_issued, i.issuance_date " +
            "FROM stock_issuances i " +
            "LEFT JOIN materials m ON i.material_id = m.material_id " +
            "LEFT JOIN cleaners c ON i.cleaner_id = c.cleaner_id " +
            "ORDER BY i.issuance_date DESC " +
            "LIMIT 10"; 

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return;
            
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    String material = rs.getString("material_name");
                    String cleanerName = rs.getString("full_name");
                    int qty = rs.getInt("quantity_issued");
                    Timestamp date = rs.getTimestamp("issuance_date");
                    
                    String formattedDate = (date != null) ? dateFormat.format(date) : "N/A";

                    model.addRow(new Object[]{
                        material, 
                        cleanerName != null ? cleanerName : "Unknown", 
                        qty, 
                        formattedDate
                    });
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("[DEBUG] Failed to load issuances table: " + ex.getMessage());
        }
    }

    // A public helper method that updates all dashboard sections at once.
    // Uses Abstraction to give outside classes a simple way to reload data without knowing how it works.
    public void refreshDashboard() {
        loadOverviewMetrics();
        loadLowStockTable();
        loadRecentIssuancesTable();
    }
    
    // Auto-generated method that builds and places all visual components on the screen.
    // Kept private to enforce strict Encapsulation, hiding messy layout code from the outside.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jMenuItem1 = new javax.swing.JMenuItem();
        headerPnl = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        centerWrapperPnl = new javax.swing.JPanel();
        statsPnl = new javax.swing.JPanel();
        invValuePnl = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        pnlInventoryValue = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        totalMatsPnl = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        pnlTotalMaterials = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lowStockItemsPnl = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lblLowStockItems = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        activeCleanersPnl = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lblActiveCleaners = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        recentIssuancesPnl = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lblRecentIssuances = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lowStockAlertPnl = new javax.swing.JPanel();
        lowstockPnl2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLowStockItems = new javax.swing.JTable();
        lowstockPnl1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        issuancesPnl = new javax.swing.JPanel();
        issuancesPnl2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblRecentStockIssuances = new javax.swing.JTable();
        issuancesPnl1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jMenuItem1.setText("jMenuItem1");

        setBackground(new java.awt.Color(245, 246, 250));
        setMaximumSize(new java.awt.Dimension(1000, 700));
        setMinimumSize(new java.awt.Dimension(1000, 700));
        setLayout(new java.awt.BorderLayout());

        headerPnl.setBackground(new java.awt.Color(245, 246, 250));
        headerPnl.setMaximumSize(new java.awt.Dimension(1000, 70));
        headerPnl.setMinimumSize(new java.awt.Dimension(1000, 70));
        headerPnl.setPreferredSize(new java.awt.Dimension(1000, 70));
        headerPnl.setLayout(new javax.swing.BoxLayout(headerPnl, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Dashboard");
        headerPnl.add(jLabel1);

        jLabel2.setText("Overview of your clieaning supplies inventory");
        headerPnl.add(jLabel2);

        add(headerPnl, java.awt.BorderLayout.NORTH);

        centerWrapperPnl.setBackground(new java.awt.Color(245, 246, 250));
        centerWrapperPnl.setLayout(new javax.swing.BoxLayout(centerWrapperPnl, javax.swing.BoxLayout.Y_AXIS));

        statsPnl.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        statsPnl.setMaximumSize(new java.awt.Dimension(1000, 90));
        statsPnl.setMinimumSize(new java.awt.Dimension(1000, 90));
        statsPnl.setPreferredSize(new java.awt.Dimension(1000, 90));
        statsPnl.setLayout(new java.awt.GridLayout(1, 5, 3, 0));

        invValuePnl.setBackground(new java.awt.Color(255, 255, 255));
        invValuePnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Inventory Value");

        pnlInventoryValue.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        pnlInventoryValue.setForeground(new java.awt.Color(27, 38, 59));
        pnlInventoryValue.setText("R100,000");

        jLabel18.setText("Accross 8 material types");

        javax.swing.GroupLayout invValuePnlLayout = new javax.swing.GroupLayout(invValuePnl);
        invValuePnl.setLayout(invValuePnlLayout);
        invValuePnlLayout.setHorizontalGroup(
            invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invValuePnlLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(invValuePnlLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlInventoryValue)
                            .addComponent(jLabel18))))
                .addGap(39, 39, 39))
        );
        invValuePnlLayout.setVerticalGroup(
            invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invValuePnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlInventoryValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statsPnl.add(invValuePnl);

        totalMatsPnl.setBackground(new java.awt.Color(255, 255, 255));
        totalMatsPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Total Materials");

        pnlTotalMaterials.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        pnlTotalMaterials.setForeground(new java.awt.Color(13, 110, 253));
        pnlTotalMaterials.setText("8");

        jLabel16.setText("of 190 units total");

        javax.swing.GroupLayout totalMatsPnlLayout = new javax.swing.GroupLayout(totalMatsPnl);
        totalMatsPnl.setLayout(totalMatsPnlLayout);
        totalMatsPnlLayout.setHorizontalGroup(
            totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalMatsPnlLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(totalMatsPnlLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(pnlTotalMaterials))))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        totalMatsPnlLayout.setVerticalGroup(
            totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalMatsPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTotalMaterials)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statsPnl.add(totalMatsPnl);

        lowStockItemsPnl.setBackground(new java.awt.Color(255, 255, 255));
        lowStockItemsPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Low Stock Items");

        lblLowStockItems.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblLowStockItems.setForeground(new java.awt.Color(220, 53, 69));
        lblLowStockItems.setText("4");

        jLabel17.setText("Needs attention");

        javax.swing.GroupLayout lowStockItemsPnlLayout = new javax.swing.GroupLayout(lowStockItemsPnl);
        lowStockItemsPnl.setLayout(lowStockItemsPnlLayout);
        lowStockItemsPnlLayout.setHorizontalGroup(
            lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lowStockItemsPnlLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(lowStockItemsPnlLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(lblLowStockItems))))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        lowStockItemsPnlLayout.setVerticalGroup(
            lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lowStockItemsPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLowStockItems)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statsPnl.add(lowStockItemsPnl);

        activeCleanersPnl.setBackground(new java.awt.Color(255, 255, 255));
        activeCleanersPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Active Cleaners");

        lblActiveCleaners.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblActiveCleaners.setForeground(new java.awt.Color(25, 135, 84));
        lblActiveCleaners.setText("5");

        jLabel19.setText("1 inactive");

        javax.swing.GroupLayout activeCleanersPnlLayout = new javax.swing.GroupLayout(activeCleanersPnl);
        activeCleanersPnl.setLayout(activeCleanersPnlLayout);
        activeCleanersPnlLayout.setHorizontalGroup(
            activeCleanersPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(activeCleanersPnlLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(activeCleanersPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(activeCleanersPnlLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(activeCleanersPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(lblActiveCleaners))))
                .addContainerGap(84, Short.MAX_VALUE))
        );
        activeCleanersPnlLayout.setVerticalGroup(
            activeCleanersPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(activeCleanersPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblActiveCleaners)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statsPnl.add(activeCleanersPnl);

        recentIssuancesPnl.setBackground(new java.awt.Color(255, 255, 255));
        recentIssuancesPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Recent Issuances");

        lblRecentIssuances.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblRecentIssuances.setForeground(new java.awt.Color(108, 117, 125));
        lblRecentIssuances.setText("0");

        jLabel20.setText("in the Last 7 days");

        javax.swing.GroupLayout recentIssuancesPnlLayout = new javax.swing.GroupLayout(recentIssuancesPnl);
        recentIssuancesPnl.setLayout(recentIssuancesPnlLayout);
        recentIssuancesPnlLayout.setHorizontalGroup(
            recentIssuancesPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recentIssuancesPnlLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(recentIssuancesPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(recentIssuancesPnlLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(recentIssuancesPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(lblRecentIssuances))))
                .addContainerGap(74, Short.MAX_VALUE))
        );
        recentIssuancesPnlLayout.setVerticalGroup(
            recentIssuancesPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recentIssuancesPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRecentIssuances)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statsPnl.add(recentIssuancesPnl);

        centerWrapperPnl.add(statsPnl);

        lowStockAlertPnl.setMaximumSize(new java.awt.Dimension(1000, 270));
        lowStockAlertPnl.setMinimumSize(new java.awt.Dimension(1000, 270));
        lowStockAlertPnl.setPreferredSize(new java.awt.Dimension(1000, 270));
        lowStockAlertPnl.setLayout(new java.awt.BorderLayout());

        lowstockPnl2.setBackground(new java.awt.Color(245, 246, 250));

        jScrollPane1.setBackground(new java.awt.Color(245, 246, 250));

        tblLowStockItems.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblLowStockItems.setForeground(new java.awt.Color(27, 38, 59));
        tblLowStockItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Material", "Current", "Min", "Supplier"
            }
        ));
        tblLowStockItems.setFocusable(false);
        tblLowStockItems.setGridColor(new java.awt.Color(240, 242, 245));
        tblLowStockItems.setRowHeight(30);
        tblLowStockItems.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(tblLowStockItems);
        if (tblLowStockItems.getColumnModel().getColumnCount() > 0) {
            tblLowStockItems.getColumnModel().getColumn(0).setHeaderValue("Material");
            tblLowStockItems.getColumnModel().getColumn(1).setHeaderValue("Current");
            tblLowStockItems.getColumnModel().getColumn(2).setHeaderValue("Min");
            tblLowStockItems.getColumnModel().getColumn(3).setHeaderValue("Supplier");
        }

        javax.swing.GroupLayout lowstockPnl2Layout = new javax.swing.GroupLayout(lowstockPnl2);
        lowstockPnl2.setLayout(lowstockPnl2Layout);
        lowstockPnl2Layout.setHorizontalGroup(
            lowstockPnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        lowstockPnl2Layout.setVerticalGroup(
            lowstockPnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lowstockPnl2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE))
        );

        lowStockAlertPnl.add(lowstockPnl2, java.awt.BorderLayout.CENTER);

        lowstockPnl1.setBackground(new java.awt.Color(245, 246, 250));
        lowstockPnl1.setMaximumSize(new java.awt.Dimension(998, 40));
        lowstockPnl1.setMinimumSize(new java.awt.Dimension(998, 40));
        lowstockPnl1.setPreferredSize(new java.awt.Dimension(998, 40));
        lowstockPnl1.setRequestFocusEnabled(false);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Low stock items");

        javax.swing.GroupLayout lowstockPnl1Layout = new javax.swing.GroupLayout(lowstockPnl1);
        lowstockPnl1.setLayout(lowstockPnl1Layout);
        lowstockPnl1Layout.setHorizontalGroup(
            lowstockPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lowstockPnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(888, Short.MAX_VALUE))
        );
        lowstockPnl1Layout.setVerticalGroup(
            lowstockPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lowstockPnl1Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        lowStockAlertPnl.add(lowstockPnl1, java.awt.BorderLayout.PAGE_START);

        centerWrapperPnl.add(lowStockAlertPnl);

        issuancesPnl.setLayout(new java.awt.BorderLayout());

        tblRecentStockIssuances.setBackground(new java.awt.Color(245, 246, 250));
        tblRecentStockIssuances.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Material", "Issued To", "Quantity", "Date"
            }
        ));
        jScrollPane3.setViewportView(tblRecentStockIssuances);

        javax.swing.GroupLayout issuancesPnl2Layout = new javax.swing.GroupLayout(issuancesPnl2);
        issuancesPnl2.setLayout(issuancesPnl2Layout);
        issuancesPnl2Layout.setHorizontalGroup(
            issuancesPnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        issuancesPnl2Layout.setVerticalGroup(
            issuancesPnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
        );

        issuancesPnl.add(issuancesPnl2, java.awt.BorderLayout.CENTER);

        issuancesPnl1.setBackground(new java.awt.Color(245, 246, 250));
        issuancesPnl1.setMaximumSize(new java.awt.Dimension(1000, 30));
        issuancesPnl1.setMinimumSize(new java.awt.Dimension(1000, 30));
        issuancesPnl1.setPreferredSize(new java.awt.Dimension(1000, 30));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Recent Stock Issuances");

        javax.swing.GroupLayout issuancesPnl1Layout = new javax.swing.GroupLayout(issuancesPnl1);
        issuancesPnl1.setLayout(issuancesPnl1Layout);
        issuancesPnl1Layout.setHorizontalGroup(
            issuancesPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(issuancesPnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(843, Short.MAX_VALUE))
        );
        issuancesPnl1Layout.setVerticalGroup(
            issuancesPnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(issuancesPnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        issuancesPnl.add(issuancesPnl1, java.awt.BorderLayout.PAGE_START);

        centerWrapperPnl.add(issuancesPnl);

        add(centerWrapperPnl, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel activeCleanersPnl;
    private javax.swing.JPanel centerWrapperPnl;
    private javax.swing.JPanel headerPnl;
    private javax.swing.JPanel invValuePnl;
    private javax.swing.JPanel issuancesPnl;
    private javax.swing.JPanel issuancesPnl1;
    private javax.swing.JPanel issuancesPnl2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lblActiveCleaners;
    private javax.swing.JLabel lblLowStockItems;
    private javax.swing.JLabel lblRecentIssuances;
    private javax.swing.JPanel lowStockAlertPnl;
    private javax.swing.JPanel lowStockItemsPnl;
    private javax.swing.JPanel lowstockPnl1;
    private javax.swing.JPanel lowstockPnl2;
    private javax.swing.JLabel pnlInventoryValue;
    private javax.swing.JLabel pnlTotalMaterials;
    private javax.swing.JPanel recentIssuancesPnl;
    private javax.swing.JPanel statsPnl;
    private javax.swing.JTable tblLowStockItems;
    private javax.swing.JTable tblRecentStockIssuances;
    private javax.swing.JPanel totalMatsPnl;
    // End of variables declaration//GEN-END:variables
}
