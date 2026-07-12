package ui.panels;

import controller.MockMaterialDAO;
import model.Material;
import javax.swing.table.DefaultTableModel;
import java.util.List;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author waldo
 */
public class DashboardPnl extends javax.swing.JPanel {

    private MockMaterialDAO materialService;

    /**
     * Creates new form DashboardPanel
     */
    public DashboardPnl() {
        initComponents(); 
        
        this.materialService = new MockMaterialDAO();
        
        utils.uiUtilities.applyTableStyleProperties(jTable1, jScrollPane1); //[cite: 2]
        utils.uiUtilities.applyTableStyleProperties(jTable3, jScrollPane3); //[cite: 2]
        
        initDashboardData(); // This triggers our new loading method below
    }

    private void initDashboardData() {
        double totalValue = materialService.getTotalInventoryValue();
        int distinctItems = materialService.getTotalDistinctMaterials();
        int lowStockCount = materialService.getLowStockMaterials().size();
        int totalUnits = materialService.getTotalUnitCount();
        
        jLabel11.setText(String.format("R%.2f", totalValue)); 
        jLabel18.setText("Across " + distinctItems + " material types");
        
        jLabel12.setText(String.valueOf(distinctItems));       
        jLabel16.setText("of " + totalUnits + " units total");
        
        jLabel13.setText(String.valueOf(lowStockCount));       
        
        List<Material> lowStockList = materialService.getLowStockMaterials();
        DefaultTableModel lowStockModel = (DefaultTableModel) jTable1.getModel();
        
        lowStockModel.setRowCount(0); 
        
        for (Material m : lowStockList) {
            lowStockModel.addRow(new Object[]{
                m.getMaterialName(),
                m.getQuantity() + " " + m.getUnit(),
                m.getReorderLevel(),
                m.getSupplierId() == null ? "None Assigned" : "Supplier ID: " + m.getSupplierId()
            });
        }
    }
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
        jLabel11 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        totalMatsPnl = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lowStockItemsPnl = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        activeCleanersPnl = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        recentIssuancesPnl = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lowStockAlertPnl = new javax.swing.JPanel();
        lowstockPnl2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lowstockPnl1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        issuancesPnl = new javax.swing.JPanel();
        issuancesPnl2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
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

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(27, 38, 59));
        jLabel11.setText("R100,000");

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
                            .addComponent(jLabel11)
                            .addComponent(jLabel18))))
                .addGap(39, 39, 39))
        );
        invValuePnlLayout.setVerticalGroup(
            invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invValuePnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statsPnl.add(invValuePnl);

        totalMatsPnl.setBackground(new java.awt.Color(255, 255, 255));
        totalMatsPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Total Materials");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(13, 110, 253));
        jLabel12.setText("8");

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
                            .addComponent(jLabel12))))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        totalMatsPnlLayout.setVerticalGroup(
            totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalMatsPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statsPnl.add(totalMatsPnl);

        lowStockItemsPnl.setBackground(new java.awt.Color(255, 255, 255));
        lowStockItemsPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Low Stock Items");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(220, 53, 69));
        jLabel13.setText("4");

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
                            .addComponent(jLabel13))))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        lowStockItemsPnlLayout.setVerticalGroup(
            lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lowStockItemsPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statsPnl.add(lowStockItemsPnl);

        activeCleanersPnl.setBackground(new java.awt.Color(255, 255, 255));
        activeCleanersPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Active Cleaners");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(25, 135, 84));
        jLabel14.setText("5");

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
                            .addComponent(jLabel14))))
                .addContainerGap(84, Short.MAX_VALUE))
        );
        activeCleanersPnlLayout.setVerticalGroup(
            activeCleanersPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(activeCleanersPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statsPnl.add(activeCleanersPnl);

        recentIssuancesPnl.setBackground(new java.awt.Color(255, 255, 255));
        recentIssuancesPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Recent Issuances");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(108, 117, 125));
        jLabel15.setText("0");

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
                            .addComponent(jLabel15))))
                .addContainerGap(74, Short.MAX_VALUE))
        );
        recentIssuancesPnlLayout.setVerticalGroup(
            recentIssuancesPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recentIssuancesPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
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

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable1.setForeground(new java.awt.Color(27, 38, 59));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable1.setFocusable(false);
        jTable1.setGridColor(new java.awt.Color(240, 242, 245));
        jTable1.setRowHeight(30);
        jTable1.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setHeaderValue("Material");
            jTable1.getColumnModel().getColumn(1).setHeaderValue("Current");
            jTable1.getColumnModel().getColumn(2).setHeaderValue("Min");
            jTable1.getColumnModel().getColumn(3).setHeaderValue("Supplier");
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

        jTable3.setBackground(new java.awt.Color(245, 246, 250));
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(jTable3);

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
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JPanel lowStockAlertPnl;
    private javax.swing.JPanel lowStockItemsPnl;
    private javax.swing.JPanel lowstockPnl1;
    private javax.swing.JPanel lowstockPnl2;
    private javax.swing.JPanel recentIssuancesPnl;
    private javax.swing.JPanel statsPnl;
    private javax.swing.JPanel totalMatsPnl;
    // End of variables declaration//GEN-END:variables
}
