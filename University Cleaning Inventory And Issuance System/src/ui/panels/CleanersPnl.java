// package ui.panels;

// import controller.CleanerController;
// import model.Cleaner;

// import ui.MainFrame;

// import ui.popDiaglogs.AddCleanersDialog;
// import ui.popDiaglogs.EditCleanersDialog;

// import ui.utils.AlertUtils;
// import java.awt.event.MouseListener;
// import java.awt.event.MouseEvent;
// import java.util.List;
// import javax.swing.table.DefaultTableModel;

// import utils.CurrentUser;
// import utils.uiUtilities;

// /*
//  * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//  * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
//  */

// /**
//  *
//  * @author waldo
//  */
// public class CleanersPnl extends javax.swing.JPanel {

//     private final CleanerController cleanerController = new CleanerController();
//     private List<Cleaner> currentCleaners;
//     private boolean loadingCombos = false;

//     /**
//      * Creates new form MaterialsPanel
//      */
//     public CleanersPnl() {
//         initComponents();
        
//          uiUtilities.applyTableStyleProperties(jTable1, jScrollPane1,
//             new int[]{140, 190, 120, 120, 90, 100, 90, 60, 70});
        
//          // Placeholder ("hint") text for the inventory search box - clears  itself automatically when the user clicks into it
//         uiUtilities.installPlaceholder(jTextField1, "Search cleaners by name, ID, or email ...");
        
//         applyRoleRestrictions();
        
//         setupTableColumns();
//         setupTableListeners();
//         setupSearchListener();
//         setupComboListeners();
//         if (cleanerController.getConnectionError() != null) {
//             AlertUtils.showErrorAlert("Database Connection Error", cleanerController.getConnectionError());
//         }
//         loadFilterCombos();
//         loadCleaners();
//     }
    
//      private void applyRoleRestrictions() {
//         // Only Storekeeper can Create/Update/Delete Cleaners
//         // The Owner can only view
//         boolean canEdit = CurrentUser.isStorekeeper();
        
//         // Only storekeeper will see and be able to add Materials
//         btnAddCleaner.setEnabled(canEdit);
//         btnAddCleaner.setVisible(canEdit);
        
//         //The colums for edit and delete are hidden for everyone except the storekeeper
//         if (!canEdit) {
//         // Remove Edit and Delete columns entirely for roles that can't use them
//         javax.swing.table.TableColumnModel columnModel = jTable1.getColumnModel();
        
//         columnModel.removeColumn(jTable1.getColumn("Edit"));
//         columnModel.removeColumn(jTable1.getColumn("Delete"));
//          }
        
//     }

//     /**
//      * Sets column widths and disables auto-resize so every column of data is
//      * fully visible, with horizontal scrolling for anything that overflows.
//      */
//     private void setupTableColumns() {
//         jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
//         int[] widths = {150, 100, 130, 140, 90, 110, 100, 60, 70};
//         for (int i = 0; i < widths.length && i < jTable1.getColumnModel().getColumnCount(); i++) {
//             jTable1.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
//         }
//     }

//     /**
//      * Loads all cleaners from PostgreSQL into the table.
//      */
//     private void loadCleaners() {
//         currentCleaners = cleanerController.getAllCleaners();
//         populateTable(currentCleaners);
//         updateStats();
//     }

//     /**
//      * Fills the JTable with the given list of cleaners.
//      */
//     private void populateTable(List<Cleaner> cleaners) {
//         DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
//         model.setRowCount(0);
//         for (Cleaner cleaner : cleaners) {
//             model.addRow(new Object[]{
//                     cleaner.getName(),
//                     cleaner.getEmail(),
//                     cleaner.getPhone(),
//                     cleaner.getDepartment(),
//                     cleaner.getEmployeeId(),
//                     cleaner.getHireDate() != null ? cleaner.getHireDate().toString() : "",
//                     cleaner.getStatus(),
//                     "Edit",
//                     "Delete"
//             });
//         }
//     }

//     /**
//      * Updates the summary cards at the top of the panel.
//      */
//     private void updateStats() {
//         if (currentCleaners == null) {
//             return;
//         }
//         long active = currentCleaners.stream().filter(c -> "Active".equalsIgnoreCase(c.getStatus())).count();
//         long inactive = currentCleaners.size() - active;
//         long departments = currentCleaners.stream().map(Cleaner::getDepartment).distinct().count();

//         jLabel11.setText(String.valueOf(currentCleaners.size()));
//         jLabel12.setText(String.valueOf(active));
//         jLabel13.setText(String.valueOf(inactive));
//         jLabel14.setText(String.valueOf(departments));
//     }

//     /**
//      * Loads the distinct statuses and departments from PostgreSQL into the
//      * filter combo boxes at the top of the panel.
//      */
//     private void loadFilterCombos() {
//         loadingCombos = true;

//         jComboBox2.removeAllItems();
//         jComboBox2.addItem("All Status");
//         for (String status : cleanerController.getStatuses()) {
//             jComboBox2.addItem(status);
//         }

//         jComboBox1.removeAllItems();
//         jComboBox1.addItem("All Departments");
//         for (String department : cleanerController.getDepartments()) {
//             jComboBox1.addItem(department);
//         }

//         loadingCombos = false;
//     }

//     /**
//      * Applies the current search text and combo box filters to the table.
//      * Typing in the search box filters the JTable automatically.
//      */
//     private void applyFilters() {
//         String searchTerm = jTextField1.getText().trim();
//         List<Cleaner> results = searchTerm.isEmpty()
//                 ? cleanerController.getAllCleaners()
//                 : cleanerController.searchCleaners(searchTerm);

//         Object selectedDepartment = jComboBox1.getSelectedItem();
//         if (selectedDepartment != null && !"All Departments".equals(selectedDepartment)) {
//             results.removeIf(c -> !selectedDepartment.equals(c.getDepartment()));
//         }

//         Object selectedStatus = jComboBox2.getSelectedItem();
//         if (selectedStatus != null && !"All Status".equals(selectedStatus)) {
//             results.removeIf(c -> !selectedStatus.equals(c.getStatus()));
//         }

//         currentCleaners = results;
//         populateTable(currentCleaners);
//         updateStats();
//     }

//     /**
//      * Filters the table automatically as the user types in the search field.
//      */
//     private void setupSearchListener() {
//         jTextField1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
//             @Override
//             public void insertUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
//             @Override
//             public void removeUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
//             @Override
//             public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
//         });
//     }

//     /**
//      * Re-applies filters whenever the department or status combo box changes.
//      */
//     private void setupComboListeners() {
//         jComboBox1.addActionListener(e -> { if (!loadingCombos) applyFilters(); });
//         jComboBox2.addActionListener(e -> { if (!loadingCombos) applyFilters(); });
//     }

//     /**
//      * This method is called from within the constructor to initialize the form.
//      * WARNING: Do NOT modify this code. The content of this method is always
//      * regenerated by the Form Editor.
//      */
//     @SuppressWarnings("unchecked")
//     // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
//     private void initComponents() {

//         contentPnl = new javax.swing.JPanel();
//         searchPnl = new javax.swing.JPanel();
//         jTextField1 = new javax.swing.JTextField();
//         jButton1 = new javax.swing.JButton();
//         jComboBox1 = new javax.swing.JComboBox<>();
//         btnAddCleaner = new javax.swing.JButton();
//         jComboBox2 = new javax.swing.JComboBox<>();
//         summaryPnl = new javax.swing.JPanel();
//         statsPnl = new javax.swing.JPanel();
//         invValuePnl = new javax.swing.JPanel();
//         jLabel6 = new javax.swing.JLabel();
//         jLabel11 = new javax.swing.JLabel();
//         totalMatsPnl = new javax.swing.JPanel();
//         jLabel7 = new javax.swing.JLabel();
//         jLabel12 = new javax.swing.JLabel();
//         lowStockItemsPnl = new javax.swing.JPanel();
//         jLabel8 = new javax.swing.JLabel();
//         jLabel13 = new javax.swing.JLabel();
//         activeCleanersPnl = new javax.swing.JPanel();
//         jLabel9 = new javax.swing.JLabel();
//         jLabel14 = new javax.swing.JLabel();
//         jPanel2 = new javax.swing.JPanel();
//         jScrollPane1 = new javax.swing.JScrollPane();
//         jTable1 = new javax.swing.JTable();
//         headerPnl = new javax.swing.JPanel();
//         jLabel1 = new javax.swing.JLabel();
//         jLabel2 = new javax.swing.JLabel();

//         setBackground(new java.awt.Color(245, 246, 250));
//         setMaximumSize(new java.awt.Dimension(1000, 700));
//         setMinimumSize(new java.awt.Dimension(1000, 700));
//         setLayout(new java.awt.BorderLayout());

//         contentPnl.setBackground(new java.awt.Color(245, 246, 250));
//         contentPnl.setMaximumSize(new java.awt.Dimension(1000, 70));
//         contentPnl.setMinimumSize(new java.awt.Dimension(1000, 70));
//         contentPnl.setPreferredSize(new java.awt.Dimension(1000, 70));
//         contentPnl.setLayout(new javax.swing.BoxLayout(contentPnl, javax.swing.BoxLayout.Y_AXIS));

//         searchPnl.setBackground(new java.awt.Color(245, 246, 250));
//         searchPnl.setMaximumSize(new java.awt.Dimension(1000, 70));
//         searchPnl.setMinimumSize(new java.awt.Dimension(1000, 70));

//         jTextField1.setText("Search cleaners by name, ID, or email ...");
//         jTextField1.setToolTipText("Search materials ...");
//         jTextField1.addActionListener(this::jTextField1ActionPerformed);

//         jButton1.setBackground(new java.awt.Color(59, 91, 219));
//         jButton1.setForeground(new java.awt.Color(255, 255, 255));
//         jButton1.setText("Search");

//         jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Departments", "Item 2", "Item 3", "Item 4" }));

//         btnAddCleaner.setBackground(new java.awt.Color(59, 91, 219));
//         btnAddCleaner.setForeground(new java.awt.Color(255, 255, 255));
//         btnAddCleaner.setText("Add new Cleaner");
//         btnAddCleaner.addActionListener(this::btnAddCleanerActionPerformed);

//         jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Status", "Item 2", "Item 3", "Item 4" }));

//         javax.swing.GroupLayout searchPnlLayout = new javax.swing.GroupLayout(searchPnl);
//         searchPnl.setLayout(searchPnlLayout);
//         searchPnlLayout.setHorizontalGroup(
//                 searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                         .addGroup(searchPnlLayout.createSequentialGroup()
//                                 .addContainerGap()
//                                 .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                 .addComponent(jButton1)
//                                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                                 .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                                 .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                                 .addComponent(btnAddCleaner, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                 .addContainerGap(36, Short.MAX_VALUE))
//         );
//         searchPnlLayout.setVerticalGroup(
//                 searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                         .addGroup(searchPnlLayout.createSequentialGroup()
//                                 .addGap(17, 17, 17)
//                                 .addGroup(searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                                         .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                         .addComponent(jButton1)
//                                         .addComponent(btnAddCleaner, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                         .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                         .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
//                                 .addContainerGap(9, Short.MAX_VALUE))
//         );

//         contentPnl.add(searchPnl);

//         summaryPnl.setBackground(new java.awt.Color(245, 246, 250));
//         summaryPnl.setMaximumSize(new java.awt.Dimension(1000, 100));
//         summaryPnl.setMinimumSize(new java.awt.Dimension(1000, 100));
//         summaryPnl.setPreferredSize(new java.awt.Dimension(1000, 100));

//         statsPnl.setMaximumSize(new java.awt.Dimension(1000, 140));
//         statsPnl.setMinimumSize(new java.awt.Dimension(1000, 140));
//         statsPnl.setPreferredSize(new java.awt.Dimension(1000, 140));
//         statsPnl.setLayout(new java.awt.GridLayout(1, 4, 3, 0));

//         invValuePnl.setBackground(new java.awt.Color(255, 255, 255));
//         invValuePnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

//         jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
//         jLabel6.setText("Total Staff");

//         jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
//         jLabel11.setText("6");

//         javax.swing.GroupLayout invValuePnlLayout = new javax.swing.GroupLayout(invValuePnl);
//         invValuePnl.setLayout(invValuePnlLayout);
//         invValuePnlLayout.setHorizontalGroup(
//                 invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                         .addGroup(invValuePnlLayout.createSequentialGroup()
//                                 .addGap(16, 16, 16)
//                                 .addGroup(invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                         .addComponent(jLabel11)
//                                         .addComponent(jLabel6))
//                                 .addContainerGap(166, Short.MAX_VALUE))
//         );
//         invValuePnlLayout.setVerticalGroup(
//                 invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                         .addGroup(invValuePnlLayout.createSequentialGroup()
//                                 .addContainerGap()
//                                 .addComponent(jLabel6)
//                                 .addGap(18, 18, 18)
//                                 .addComponent(jLabel11)
//                                 .addContainerGap(60, Short.MAX_VALUE))
//         );

//         statsPnl.add(invValuePnl);

//         totalMatsPnl.setBackground(new java.awt.Color(255, 255, 255));
//         totalMatsPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

//         jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
//         jLabel7.setText("Active");

//         jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
//         jLabel12.setText("8");

//         javax.swing.GroupLayout totalMatsPnlLayout = new javax.swing.GroupLayout(totalMatsPnl);
//         totalMatsPnl.setLayout(totalMatsPnlLayout);
//         totalMatsPnlLayout.setHorizontalGroup(
//                 totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                         .addGroup(totalMatsPnlLayout.createSequentialGroup()
//                                 .addGap(16, 16, 16)
//                                 .addGroup(totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                         .addComponent(jLabel7)
//                                         .addGroup(totalMatsPnlLayout.createSequentialGroup()
//                                                 .addGap(6, 6, 6)
//                                                 .addComponent(jLabel12)))
//                                 .addContainerGap(190, Short.MAX_VALUE))
//         );
//         totalMatsPnlLayout.setVerticalGroup(
//                 totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                         .addGroup(totalMatsPnlLayout.createSequentialGroup()
//                                 .addContainerGap()
//                                 .addComponent(jLabel7)
//                                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                 .addComponent(jLabel12)
//                                 .addContainerGap(56, Short.MAX_VALUE))
//         );

//         statsPnl.add(totalMatsPnl);

//         lowStockItemsPnl.setBackground(new java.awt.Color(255, 255, 255));
//         lowStockItemsPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

//         jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
//         jLabel8.setText("Inactive");

//         jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
//         jLabel13.setText("4");

//         javax.swing.GroupLayout lowStockItemsPnlLayout = new javax.swing.GroupLayout(lowStockItemsPnl);
//         lowStockItemsPnl.setLayout(lowStockItemsPnlLayout);
//         lowStockItemsPnlLayout.setHorizontalGroup(
//                 lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                         .addGroup(lowStockItemsPnlLayout.createSequentialGroup()
//                                 .addGap(16, 16, 16)
//                                 .addGroup(lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                         .addComponent(jLabel8)
//                                         .addGroup(lowStockItemsPnlLayout.createSequentialGroup()
//                                                 .addGap(6, 6, 6)
//                                                 .addComponent(jLabel13)))
//                                 .addContainerGap(180, Short.MAX_VALUE))
//         );
//         lowStockItemsPnlLayout.setVerticalGroup(
//                 lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                         .addGroup(lowStockItemsPnlLayout.createSequentialGroup()
//                                 .addContainerGap()
//                                 .addComponent(jLabel8)
//                                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                 .addComponent(jLabel13)
//                                 .addContainerGap(56, Short.MAX_VALUE))
//         );

//         statsPnl.add(lowStockItemsPnl);

//         activeCleanersPnl.setBackground(new java.awt.Color(255, 255, 255));
//         activeCleanersPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

//         jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
//         jLabel9.setText("Departments");

//         jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
//         jLabel14.setText("5");

//         javax.swing.GroupLayout activeCleanersPnlLayout = new javax.swing.GroupLayout(activeCleanersPnl);
//         activeCleanersPnl.setLayout(activeCleanersPnlLayout);
//         activeCleanersPnlLayout.setHorizontalGroup(
//                 activeCleanersPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                         .addGroup(activeCleanersPnlLayout.createSequentialGroup()
//                                 .addGap(16, 16, 16)
//                                 .addGroup(activeCleanersPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                         .addComponent(jLabel9)
//                                         .addGroup(activeCleanersPnlLayout.createSequentialGroup()
//                                                 .addGap(6, 6, 6)
//                                                 .addComponent(jLabel14)))
//                                 .addContainerGap(147, Short.MAX_VALUE))
//         );
//         activeCleanersPnlLayout.setVerticalGroup(
//                 activeCleanersPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                         .addGroup(activeCleanersPnlLayout.createSequentialGroup()
//                                 .addContainerGap()
//                                 .addComponent(jLabel9)
//                                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                 .addComponent(jLabel14)
//                                 .addContainerGap(56, Short.MAX_VALUE))
//         );

//         statsPnl.add(activeCleanersPnl);

//         summaryPnl.add(statsPnl);

//         contentPnl.add(summaryPnl);

//         jScrollPane1.setBackground(new java.awt.Color(245, 246, 250));

//         jTable1.setBackground(new java.awt.Color(245, 246, 250));
//         jTable1.setModel(new javax.swing.table.DefaultTableModel(
//                 new Object [][] {
//                         {null, null, null, null, null, null, null, null, null},
//                         {null, null, null, null, null, null, null, null, null},
//                         {null, null, null, null, null, null, null, null, null},
//                         {null, null, null, null, null, null, null, null, null}
//                 },
//                 new String [] {
//                         "Cleaner", "Email", "Phone number", "Department", "Employee ID", "Hire Date", "Status", "Edit", "Delete"
//                 }
//         ));
//         jTable1.setRowHeight(45);
//         jScrollPane1.setViewportView(jTable1);

//         javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
//         jPanel2.setLayout(jPanel2Layout);
//         jPanel2Layout.setHorizontalGroup(
//                 jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                         .addGroup(jPanel2Layout.createSequentialGroup()
//                                 .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 994, Short.MAX_VALUE)
//                                 .addContainerGap())
//         );
//         jPanel2Layout.setVerticalGroup(
//                 jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
//                                 .addGap(0, 10, Short.MAX_VALUE)
//                                 .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
//         );

//         contentPnl.add(jPanel2);

//         add(contentPnl, java.awt.BorderLayout.CENTER);

//         headerPnl.setBackground(new java.awt.Color(245, 246, 250));
//         headerPnl.setMaximumSize(new java.awt.Dimension(1000, 70));
//         headerPnl.setMinimumSize(new java.awt.Dimension(1000, 70));
//         headerPnl.setPreferredSize(new java.awt.Dimension(1000, 70));
//         headerPnl.setLayout(new javax.swing.BoxLayout(headerPnl, javax.swing.BoxLayout.Y_AXIS));

//         jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
//         jLabel1.setText("Cleaners");
//         headerPnl.add(jLabel1);

//         jLabel2.setText("Manage cleaning staff records");
//         headerPnl.add(jLabel2);

//         add(headerPnl, java.awt.BorderLayout.NORTH);
//     }// </editor-fold>                        

//     private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {                                            
//         applyFilters();
//     }                                           

//     private void btnAddCleanerActionPerformed(java.awt.event.ActionEvent evt) {                                              
//         // Make the AddMaterialsDialog pop up appear, dim the background
//         java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
//         MainFrame mainFrame = (MainFrame) parentFrame;

//         mainFrame.showDimOverlay(true);   // dim the background BEFORE showing dialog

//         AddCleanersDialog dialog = new AddCleanersDialog(parentFrame, true, cleanerController);
//         dialog.setLocationRelativeTo(parentFrame);
//         dialog.setVisible(true);           // this line BLOCKS here until dialog closes (since it's modal)

//         mainFrame.showDimOverlay(false);  // runs AFTER dialog is closed/disposed

//         if (dialog.isCleanerAdded()) {
//             loadFilterCombos();
//             loadCleaners();
//         }
//     }                                             

//     private void setupTableListeners() {
//         jTable1.addMouseListener(new MouseListener() {
//             @Override
//             public void mouseClicked(MouseEvent e) {
//                 int row = jTable1.rowAtPoint(e.getPoint());
//                 int col = jTable1.columnAtPoint(e.getPoint());

//                 if (row >= 0 && col >= 0 && currentCleaners != null && row < currentCleaners.size()) {
//                     if (col == 7) { // Edit column
//                         handleEditCleaner(currentCleaners.get(row));
//                     } else if (col == 8) { // Delete column
//                         handleDeleteCleaner(currentCleaners.get(row));
//                     }
//                 }
//             }

//             @Override
//             public void mousePressed(MouseEvent e) {}

//             @Override
//             public void mouseReleased(MouseEvent e) {}

//             @Override
//             public void mouseEntered(MouseEvent e) {}

//             @Override
//             public void mouseExited(MouseEvent e) {}
//         });
//     }

//     private void handleEditCleaner(Cleaner cleaner) {
//         java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
//         MainFrame mainFrame = (MainFrame) parentFrame;

//         mainFrame.showDimOverlay(true);

//         EditCleanersDialog dialog = new EditCleanersDialog(parentFrame, true, cleanerController, cleaner);
//         dialog.setLocationRelativeTo(parentFrame);
//         dialog.setVisible(true);

//         mainFrame.showDimOverlay(false);

//         if (dialog.isCleanerUpdated()) {
//             loadFilterCombos();
//             loadCleaners();
//         }
//     }

//     private void handleDeleteCleaner(Cleaner cleaner) {
//         int response = AlertUtils.showDeleteConfirmation("Cleaner", cleaner.getName());

//         if (response == javax.swing.JOptionPane.YES_OPTION) {
//             boolean success = cleanerController.deleteCleaner(cleaner.getCleanerId());
//             if (success) {
//                 AlertUtils.showDeletedAlert("Cleaner");
//                 loadFilterCombos();
//                 loadCleaners();
//             } else {
//                 String reason = cleanerController.getLastError();
//                 AlertUtils.showErrorAlert("Error", reason != null
//                         ? "Failed to delete cleaner:\n" + reason
//                         : "Failed to delete cleaner. Please try again.");
//             }
//         }
//     }


//     // Variables declaration - do not modify                     
//     private javax.swing.JPanel activeCleanersPnl;
//     private javax.swing.JButton btnAddCleaner;
//     private javax.swing.JPanel contentPnl;
//     private javax.swing.JPanel headerPnl;
//     private javax.swing.JPanel invValuePnl;
//     private javax.swing.JButton jButton1;
//     private javax.swing.JComboBox<String> jComboBox1;
//     private javax.swing.JComboBox<String> jComboBox2;
//     private javax.swing.JLabel jLabel1;
//     private javax.swing.JLabel jLabel11;
//     private javax.swing.JLabel jLabel12;
//     private javax.swing.JLabel jLabel13;
//     private javax.swing.JLabel jLabel14;
//     private javax.swing.JLabel jLabel2;
//     private javax.swing.JLabel jLabel6;
//     private javax.swing.JLabel jLabel7;
//     private javax.swing.JLabel jLabel8;
//     private javax.swing.JLabel jLabel9;
//     private javax.swing.JPanel jPanel2;
//     private javax.swing.JScrollPane jScrollPane1;
//     private javax.swing.JTable jTable1;
//     private javax.swing.JTextField jTextField1;
//     private javax.swing.JPanel lowStockItemsPnl;
//     private javax.swing.JPanel searchPnl;
//     private javax.swing.JPanel statsPnl;
//     private javax.swing.JPanel summaryPnl;
//     private javax.swing.JPanel totalMatsPnl;
//     // End of variables declaration                   
// }

//********** */
// package ui.panels;

// import controller.CleanerController;
// import model.Cleaner;
// import ui.MainFrame;
// import ui.popDiaglogs.AddCleanersDialog;
// import ui.popDiaglogs.EditCleanersDialog;
// import ui.utils.AlertUtils;
// import java.awt.event.MouseListener;
// import java.awt.event.MouseEvent;
// import java.util.List;
// import javax.swing.table.DefaultTableModel;

// import utils.CurrentUser;

// /*
//  * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//  * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
//  */

// /**
//  *
//  * @author waldo
//  */
// public class CleanersPnl extends javax.swing.JPanel {

//     private static final String SEARCH_PLACEHOLDER = "Search cleaners by name, ID, or email ...";

//     private final CleanerController cleanerController = new CleanerController();
//     private List<Cleaner> currentCleaners;
//     private boolean loadingCombos = false;

//     /**
//      * Creates new form MaterialsPanel
//      */
//     public CleanersPnl() {
//         initComponents();
//         setupTableColumns();
//         setupTableListeners();
//         setupSearchPlaceholder();
//         setupSearchListener();
//         setupComboListeners();
//         if (cleanerController.getConnectionError() != null) {
//             AlertUtils.showErrorAlert("Database Connection Error", cleanerController.getConnectionError());
//         }
//         loadFilterCombos();
//         loadCleaners();
//     }

//     /**
//      * The search field ships with literal instructional text already typed
//      * into it (not a real placeholder), so it must be cleared on focus and
//      * restored when left empty, otherwise every search/filter treats that
//      * text as an actual search term and returns zero rows.
//      */
//     private void setupSearchPlaceholder() {
//         jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
//             @Override
//             public void focusGained(java.awt.event.FocusEvent e) {
//                 if (jTextField1.getText().equals(SEARCH_PLACEHOLDER)) {
//                     jTextField1.setText("");
//                 }
//             }

//             @Override
//             public void focusLost(java.awt.event.FocusEvent e) {
//                 if (jTextField1.getText().trim().isEmpty()) {
//                     jTextField1.setText(SEARCH_PLACEHOLDER);
//                 }
//             }
//         });
//     }

//     /**
//      * Treats the field as empty if it still holds the un-cleared placeholder.
//      */
//     private String getSearchTerm() {
//         String text = jTextField1.getText().trim();
//         return text.equals(SEARCH_PLACEHOLDER) ? "" : text;
//     }

//     /**
//      * Sets column widths and disables auto-resize so every column of data is
//      * fully visible, with horizontal scrolling for anything that overflows.
//      */
//     private void setupTableColumns() {
//         jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
//         int[] widths = {150, 100, 130, 140, 90, 110, 100, 60, 70};
//         for (int i = 0; i < widths.length && i < jTable1.getColumnModel().getColumnCount(); i++) {
//             jTable1.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
//         }
//     }

//     /**
//      * Loads all cleaners from PostgreSQL into the table.
//      */
//     private void loadCleaners() {
//         currentCleaners = cleanerController.getAllCleaners();
//         populateTable(currentCleaners);
//         updateStats();
//     }

//     /**
//      * Fills the JTable with the given list of cleaners.
//      */
//     private void populateTable(List<Cleaner> cleaners) {
//         DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
//         model.setRowCount(0);
//         for (Cleaner cleaner : cleaners) {
//             model.addRow(new Object[]{
//                 cleaner.getName(),
//                 cleaner.getEmail(),
//                 cleaner.getPhone(),
//                 cleaner.getDepartment(),
//                 cleaner.getEmployeeId(),
//                 cleaner.getHireDate() != null ? cleaner.getHireDate().toString() : "",
//                 cleaner.getStatus(),
//                 "Edit",
//                 "Delete"
//             });
//         }
//     }

//     /**
//      * Updates the summary cards at the top of the panel.
//      */
//     private void updateStats() {
//         if (currentCleaners == null) {
//             return;
//         }
//         long active = currentCleaners.stream().filter(c -> "Active".equalsIgnoreCase(c.getStatus())).count();
//         long inactive = currentCleaners.size() - active;
//         long departments = currentCleaners.stream().map(Cleaner::getDepartment).distinct().count();

//         jLabel11.setText(String.valueOf(currentCleaners.size()));
//         jLabel12.setText(String.valueOf(active));
//         jLabel13.setText(String.valueOf(inactive));
//         jLabel14.setText(String.valueOf(departments));
//     }

//     /**
//      * Loads the distinct statuses and departments from PostgreSQL into the
//      * filter combo boxes at the top of the panel.
//      */
//     private void loadFilterCombos() {
//         loadingCombos = true;

//         jComboBox2.removeAllItems();
//         jComboBox2.addItem("All Status");
//         for (String status : cleanerController.getStatuses()) {
//             jComboBox2.addItem(status);
//         }

//         jComboBox1.removeAllItems();
//         jComboBox1.addItem("All Departments");
//         for (String department : cleanerController.getDepartments()) {
//             jComboBox1.addItem(department);
//         }

//         loadingCombos = false;
//     }

//     /**
//      * Applies the current search text and combo box filters to the table.
//      * Typing in the search box filters the JTable automatically.
//      */
//     private void applyFilters() {
//         String searchTerm = getSearchTerm();
//         List<Cleaner> results = searchTerm.isEmpty()
//                 ? cleanerController.getAllCleaners()
//                 : cleanerController.searchCleaners(searchTerm);

//         Object selectedDepartment = jComboBox1.getSelectedItem();
//         if (selectedDepartment != null && !"All Departments".equals(selectedDepartment)) {
//             results.removeIf(c -> !selectedDepartment.equals(c.getDepartment()));
//         }

//         Object selectedStatus = jComboBox2.getSelectedItem();
//         if (selectedStatus != null && !"All Status".equals(selectedStatus)) {
//             results.removeIf(c -> !selectedStatus.equals(c.getStatus()));
//         }

//         currentCleaners = results;
//         populateTable(currentCleaners);
//         updateStats();
//     }

//     /**
//      * Filters the table automatically as the user types in the search field.
//      */
//     private void setupSearchListener() {
//         jTextField1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
//             @Override
//             public void insertUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
//             @Override
//             public void removeUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
//             @Override
//             public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
//         });
//     }

//     /**
//      * Re-applies filters whenever the department or status combo box changes.
//      */
//     private void setupComboListeners() {
//         jComboBox1.addActionListener(e -> { if (!loadingCombos) applyFilters(); });
//         jComboBox2.addActionListener(e -> { if (!loadingCombos) applyFilters(); });
//     }

//     /**
//      * This method is called from within the constructor to initialize the form.
//      * WARNING: Do NOT modify this code. The content of this method is always
//      * regenerated by the Form Editor.
//      */
//     @SuppressWarnings("unchecked")
//     // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
//     private void initComponents() {

//         contentPnl = new javax.swing.JPanel();
//         searchPnl = new javax.swing.JPanel();
//         jTextField1 = new javax.swing.JTextField();
//         jButton1 = new javax.swing.JButton();
//         jComboBox1 = new javax.swing.JComboBox<>();
//         btnAddCleaner = new javax.swing.JButton();
//         jComboBox2 = new javax.swing.JComboBox<>();
//         summaryPnl = new javax.swing.JPanel();
//         statsPnl = new javax.swing.JPanel();
//         invValuePnl = new javax.swing.JPanel();
//         jLabel6 = new javax.swing.JLabel();
//         jLabel11 = new javax.swing.JLabel();
//         totalMatsPnl = new javax.swing.JPanel();
//         jLabel7 = new javax.swing.JLabel();
//         jLabel12 = new javax.swing.JLabel();
//         lowStockItemsPnl = new javax.swing.JPanel();
//         jLabel8 = new javax.swing.JLabel();
//         jLabel13 = new javax.swing.JLabel();
//         activeCleanersPnl = new javax.swing.JPanel();
//         jLabel9 = new javax.swing.JLabel();
//         jLabel14 = new javax.swing.JLabel();
//         jPanel2 = new javax.swing.JPanel();
//         jScrollPane1 = new javax.swing.JScrollPane();
//         jTable1 = new javax.swing.JTable();
//         headerPnl = new javax.swing.JPanel();
//         jLabel1 = new javax.swing.JLabel();
//         jLabel2 = new javax.swing.JLabel();

//         setBackground(new java.awt.Color(245, 246, 250));
//         setMaximumSize(new java.awt.Dimension(1000, 700));
//         setMinimumSize(new java.awt.Dimension(1000, 700));
//         setLayout(new java.awt.BorderLayout());

//         contentPnl.setBackground(new java.awt.Color(245, 246, 250));
//         contentPnl.setMaximumSize(new java.awt.Dimension(1000, 70));
//         contentPnl.setMinimumSize(new java.awt.Dimension(1000, 70));
//         contentPnl.setPreferredSize(new java.awt.Dimension(1000, 70));
//         contentPnl.setLayout(new javax.swing.BoxLayout(contentPnl, javax.swing.BoxLayout.Y_AXIS));

//         searchPnl.setBackground(new java.awt.Color(245, 246, 250));
//         searchPnl.setMaximumSize(new java.awt.Dimension(1000, 70));
//         searchPnl.setMinimumSize(new java.awt.Dimension(1000, 70));

//         jTextField1.setText("Search cleaners by name, ID, or email ...");
//         jTextField1.setToolTipText("Search materials ...");
//         jTextField1.addActionListener(this::jTextField1ActionPerformed);

//         jButton1.setBackground(new java.awt.Color(59, 91, 219));
//         jButton1.setForeground(new java.awt.Color(255, 255, 255));
//         jButton1.setText("Search");

//         jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Departments", "Item 2", "Item 3", "Item 4" }));

//         btnAddCleaner.setBackground(new java.awt.Color(59, 91, 219));
//         btnAddCleaner.setForeground(new java.awt.Color(255, 255, 255));
//         btnAddCleaner.setText("Add new Cleaner");
//         btnAddCleaner.addActionListener(this::btnAddCleanerActionPerformed);

//         jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Status", "Item 2", "Item 3", "Item 4" }));

//         javax.swing.GroupLayout searchPnlLayout = new javax.swing.GroupLayout(searchPnl);
//         searchPnl.setLayout(searchPnlLayout);
//         searchPnlLayout.setHorizontalGroup(
//             searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//             .addGroup(searchPnlLayout.createSequentialGroup()
//                 .addContainerGap()
//                 .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
//                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                 .addComponent(jButton1)
//                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                 .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                 .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
//                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                 .addComponent(btnAddCleaner, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
//                 .addContainerGap(36, Short.MAX_VALUE))
//         );
//         searchPnlLayout.setVerticalGroup(
//             searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//             .addGroup(searchPnlLayout.createSequentialGroup()
//                 .addGap(17, 17, 17)
//                 .addGroup(searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                     .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                     .addComponent(jButton1)
//                     .addComponent(btnAddCleaner, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
//                     .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                     .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
//                 .addContainerGap(9, Short.MAX_VALUE))
//         );

//         contentPnl.add(searchPnl);

//         summaryPnl.setBackground(new java.awt.Color(245, 246, 250));
//         summaryPnl.setMaximumSize(new java.awt.Dimension(1000, 100));
//         summaryPnl.setMinimumSize(new java.awt.Dimension(1000, 100));
//         summaryPnl.setPreferredSize(new java.awt.Dimension(1000, 100));

//         statsPnl.setMaximumSize(new java.awt.Dimension(1000, 140));
//         statsPnl.setMinimumSize(new java.awt.Dimension(1000, 140));
//         statsPnl.setPreferredSize(new java.awt.Dimension(1000, 140));
//         statsPnl.setLayout(new java.awt.GridLayout(1, 4, 3, 0));

//         invValuePnl.setBackground(new java.awt.Color(255, 255, 255));
//         invValuePnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

//         jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
//         jLabel6.setText("Total Staff");

//         jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
//         jLabel11.setText("6");

//         javax.swing.GroupLayout invValuePnlLayout = new javax.swing.GroupLayout(invValuePnl);
//         invValuePnl.setLayout(invValuePnlLayout);
//         invValuePnlLayout.setHorizontalGroup(
//             invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//             .addGroup(invValuePnlLayout.createSequentialGroup()
//                 .addGap(16, 16, 16)
//                 .addGroup(invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                     .addComponent(jLabel11)
//                     .addComponent(jLabel6))
//                 .addContainerGap(166, Short.MAX_VALUE))
//         );
//         invValuePnlLayout.setVerticalGroup(
//             invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//             .addGroup(invValuePnlLayout.createSequentialGroup()
//                 .addContainerGap()
//                 .addComponent(jLabel6)
//                 .addGap(18, 18, 18)
//                 .addComponent(jLabel11)
//                 .addContainerGap(60, Short.MAX_VALUE))
//         );

//         statsPnl.add(invValuePnl);

//         totalMatsPnl.setBackground(new java.awt.Color(255, 255, 255));
//         totalMatsPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

//         jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
//         jLabel7.setText("Active");

//         jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
//         jLabel12.setText("8");

//         javax.swing.GroupLayout totalMatsPnlLayout = new javax.swing.GroupLayout(totalMatsPnl);
//         totalMatsPnl.setLayout(totalMatsPnlLayout);
//         totalMatsPnlLayout.setHorizontalGroup(
//             totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//             .addGroup(totalMatsPnlLayout.createSequentialGroup()
//                 .addGap(16, 16, 16)
//                 .addGroup(totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                     .addComponent(jLabel7)
//                     .addGroup(totalMatsPnlLayout.createSequentialGroup()
//                         .addGap(6, 6, 6)
//                         .addComponent(jLabel12)))
//                 .addContainerGap(190, Short.MAX_VALUE))
//         );
//         totalMatsPnlLayout.setVerticalGroup(
//             totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//             .addGroup(totalMatsPnlLayout.createSequentialGroup()
//                 .addContainerGap()
//                 .addComponent(jLabel7)
//                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                 .addComponent(jLabel12)
//                 .addContainerGap(56, Short.MAX_VALUE))
//         );

//         statsPnl.add(totalMatsPnl);

//         lowStockItemsPnl.setBackground(new java.awt.Color(255, 255, 255));
//         lowStockItemsPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

//         jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
//         jLabel8.setText("Inactive");

//         jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
//         jLabel13.setText("4");

//         javax.swing.GroupLayout lowStockItemsPnlLayout = new javax.swing.GroupLayout(lowStockItemsPnl);
//         lowStockItemsPnl.setLayout(lowStockItemsPnlLayout);
//         lowStockItemsPnlLayout.setHorizontalGroup(
//             lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//             .addGroup(lowStockItemsPnlLayout.createSequentialGroup()
//                 .addGap(16, 16, 16)
//                 .addGroup(lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                     .addComponent(jLabel8)
//                     .addGroup(lowStockItemsPnlLayout.createSequentialGroup()
//                         .addGap(6, 6, 6)
//                         .addComponent(jLabel13)))
//                 .addContainerGap(180, Short.MAX_VALUE))
//         );
//         lowStockItemsPnlLayout.setVerticalGroup(
//             lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//             .addGroup(lowStockItemsPnlLayout.createSequentialGroup()
//                 .addContainerGap()
//                 .addComponent(jLabel8)
//                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                 .addComponent(jLabel13)
//                 .addContainerGap(56, Short.MAX_VALUE))
//         );

//         statsPnl.add(lowStockItemsPnl);

//         activeCleanersPnl.setBackground(new java.awt.Color(255, 255, 255));
//         activeCleanersPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

//         jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
//         jLabel9.setText("Departments");

//         jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
//         jLabel14.setText("5");

//         javax.swing.GroupLayout activeCleanersPnlLayout = new javax.swing.GroupLayout(activeCleanersPnl);
//         activeCleanersPnl.setLayout(activeCleanersPnlLayout);
//         activeCleanersPnlLayout.setHorizontalGroup(
//             activeCleanersPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//             .addGroup(activeCleanersPnlLayout.createSequentialGroup()
//                 .addGap(16, 16, 16)
//                 .addGroup(activeCleanersPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                     .addComponent(jLabel9)
//                     .addGroup(activeCleanersPnlLayout.createSequentialGroup()
//                         .addGap(6, 6, 6)
//                         .addComponent(jLabel14)))
//                 .addContainerGap(147, Short.MAX_VALUE))
//         );
//         activeCleanersPnlLayout.setVerticalGroup(
//             activeCleanersPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//             .addGroup(activeCleanersPnlLayout.createSequentialGroup()
//                 .addContainerGap()
//                 .addComponent(jLabel9)
//                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                 .addComponent(jLabel14)
//                 .addContainerGap(56, Short.MAX_VALUE))
//         );

//         statsPnl.add(activeCleanersPnl);

//         summaryPnl.add(statsPnl);

//         contentPnl.add(summaryPnl);

//         jScrollPane1.setBackground(new java.awt.Color(245, 246, 250));

//         jTable1.setBackground(new java.awt.Color(245, 246, 250));
//         jTable1.setModel(new javax.swing.table.DefaultTableModel(
//             new Object [][] {
//                 {null, null, null, null, null, null, null, null, null},
//                 {null, null, null, null, null, null, null, null, null},
//                 {null, null, null, null, null, null, null, null, null},
//                 {null, null, null, null, null, null, null, null, null}
//             },
//             new String [] {
//                 "Cleaner", "Email", "Phone number", "Department", "Employee ID", "Hire Date", "Status", "Edit", "Delete"
//             }
//         ));
//         jTable1.setRowHeight(45);
//         jScrollPane1.setViewportView(jTable1);

//         javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
//         jPanel2.setLayout(jPanel2Layout);
//         jPanel2Layout.setHorizontalGroup(
//             jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//             .addGroup(jPanel2Layout.createSequentialGroup()
//                 .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 994, Short.MAX_VALUE)
//                 .addContainerGap())
//         );
//         jPanel2Layout.setVerticalGroup(
//             jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//             .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
//                 .addGap(0, 10, Short.MAX_VALUE)
//                 .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
//         );

//         contentPnl.add(jPanel2);

//         add(contentPnl, java.awt.BorderLayout.CENTER);

//         headerPnl.setBackground(new java.awt.Color(245, 246, 250));
//         headerPnl.setMaximumSize(new java.awt.Dimension(1000, 70));
//         headerPnl.setMinimumSize(new java.awt.Dimension(1000, 70));
//         headerPnl.setPreferredSize(new java.awt.Dimension(1000, 70));
//         headerPnl.setLayout(new javax.swing.BoxLayout(headerPnl, javax.swing.BoxLayout.Y_AXIS));

//         jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
//         jLabel1.setText("Cleaners");
//         headerPnl.add(jLabel1);

//         jLabel2.setText("Manage cleaning staff records");
//         headerPnl.add(jLabel2);

//         add(headerPnl, java.awt.BorderLayout.NORTH);
//     }// </editor-fold>                        

//     private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {                                            
//         applyFilters();
//     }                                           

//     private void btnAddCleanerActionPerformed(java.awt.event.ActionEvent evt) {                                              
//      // Make the AddMaterialsDialog pop up appear, dim the background
//     java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
//     MainFrame mainFrame = (MainFrame) parentFrame;
    
//     mainFrame.showDimOverlay(true);   // dim the background BEFORE showing dialog
    
//     AddCleanersDialog dialog = new AddCleanersDialog(parentFrame, true, cleanerController);
//     dialog.setLocationRelativeTo(parentFrame);
//     dialog.setVisible(true);           // this line BLOCKS here until dialog closes (since it's modal)
    
//     mainFrame.showDimOverlay(false);  // runs AFTER dialog is closed/disposed

//     if (dialog.isCleanerAdded()) {
//         loadFilterCombos();
//         loadCleaners();
//     }
//     }                                             

//     private void setupTableListeners() {
//         jTable1.addMouseListener(new MouseListener() {
//             @Override
//             public void mouseClicked(MouseEvent e) {
//                 int row = jTable1.rowAtPoint(e.getPoint());
//                 int col = jTable1.columnAtPoint(e.getPoint());
                
//                 if (row >= 0 && col >= 0 && currentCleaners != null && row < currentCleaners.size()) {
//                     if (col == 7) { // Edit column
//                         handleEditCleaner(currentCleaners.get(row));
//                     } else if (col == 8) { // Delete column
//                         handleDeleteCleaner(currentCleaners.get(row));
//                     }
//                 }
//             }
            
//             @Override
//             public void mousePressed(MouseEvent e) {}
            
//             @Override
//             public void mouseReleased(MouseEvent e) {}
            
//             @Override
//             public void mouseEntered(MouseEvent e) {}
            
//             @Override
//             public void mouseExited(MouseEvent e) {}
//         });
//     }

//     private void handleEditCleaner(Cleaner cleaner) {
//         java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
//         MainFrame mainFrame = (MainFrame) parentFrame;
        
//         mainFrame.showDimOverlay(true);
        
//         EditCleanersDialog dialog = new EditCleanersDialog(parentFrame, true, cleanerController, cleaner);
//         dialog.setLocationRelativeTo(parentFrame);
//         dialog.setVisible(true);
        
//         mainFrame.showDimOverlay(false);

//         if (dialog.isCleanerUpdated()) {
//             loadFilterCombos();
//             loadCleaners();
//         }
//     }

//     private void handleDeleteCleaner(Cleaner cleaner) {
//         int response = AlertUtils.showDeleteConfirmation("Cleaner", cleaner.getName());
        
//         if (response == javax.swing.JOptionPane.YES_OPTION) {
//             boolean success = cleanerController.deleteCleaner(cleaner.getCleanerId());
//             if (success) {
//                 AlertUtils.showDeletedAlert("Cleaner");
//                 loadFilterCombos();
//                 loadCleaners();
//             } else {
//                 String reason = cleanerController.getLastError();
//                 AlertUtils.showErrorAlert("Error", reason != null
//                         ? "Failed to delete cleaner:\n" + reason
//                         : "Failed to delete cleaner. Please try again.");
//             }
//         }
//     }


//     // Variables declaration - do not modify                     
//     private javax.swing.JPanel activeCleanersPnl;
//     private javax.swing.JButton btnAddCleaner;
//     private javax.swing.JPanel contentPnl;
//     private javax.swing.JPanel headerPnl;
//     private javax.swing.JPanel invValuePnl;
//     private javax.swing.JButton jButton1;
//     private javax.swing.JComboBox<String> jComboBox1;
//     private javax.swing.JComboBox<String> jComboBox2;
//     private javax.swing.JLabel jLabel1;
//     private javax.swing.JLabel jLabel11;
//     private javax.swing.JLabel jLabel12;
//     private javax.swing.JLabel jLabel13;
//     private javax.swing.JLabel jLabel14;
//     private javax.swing.JLabel jLabel2;
//     private javax.swing.JLabel jLabel6;
//     private javax.swing.JLabel jLabel7;
//     private javax.swing.JLabel jLabel8;
//     private javax.swing.JLabel jLabel9;
//     private javax.swing.JPanel jPanel2;
//     private javax.swing.JScrollPane jScrollPane1;
//     private javax.swing.JTable jTable1;
//     private javax.swing.JTextField jTextField1;
//     private javax.swing.JPanel lowStockItemsPnl;
//     private javax.swing.JPanel searchPnl;
//     private javax.swing.JPanel statsPnl;
//     private javax.swing.JPanel summaryPnl;
//     private javax.swing.JPanel totalMatsPnl;
//     // End of variables declaration                   
// }

package ui.panels;

import controller.CleanerController;
import model.Cleaner;
import ui.MainFrame;
import ui.popDiaglogs.AddCleanersDialog;
import ui.popDiaglogs.EditCleanersDialog;
import ui.utils.AlertUtils;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import utils.CurrentUser;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author waldo
 */
public class CleanersPnl extends javax.swing.JPanel {

    private static final String SEARCH_PLACEHOLDER = "Search cleaners by name, ID, or email ...";

    private final CleanerController cleanerController = new CleanerController();
    private List<Cleaner> currentCleaners;
    private boolean loadingCombos = false;

    /**
     * Creates new form MaterialsPanel
     */
    public CleanersPnl() {
        initComponents();
        setupTableColumns();
        setupTableListeners();
        setupSearchPlaceholder();
        setupSearchListener();
        setupComboListeners();
        applyRoleRestrictions();
        if (cleanerController.getConnectionError() != null) {
            AlertUtils.showErrorAlert("Database Connection Error", cleanerController.getConnectionError());
        }
        loadFilterCombos();
        loadCleaners();
    }

    /**
     * Per the access matrix in {@link utils.CurrentUser}, only the
     * Storekeeper role may create, edit, or delete Cleaners - the Owner
     * role is View Only on this page. This hides the "Add new Cleaner"
     * button and removes the Edit/Delete columns entirely for anyone who
     * isn't a Storekeeper, mirroring the pattern used in MaterialsPnl.
     */
    private void applyRoleRestrictions() {
        boolean canEdit = CurrentUser.isStorekeeper();

        btnAddCleaner.setEnabled(canEdit);
        btnAddCleaner.setVisible(canEdit);

        if (!canEdit) {
            javax.swing.table.TableColumnModel columnModel = jTable1.getColumnModel();
            columnModel.removeColumn(jTable1.getColumn("Edit"));
            columnModel.removeColumn(jTable1.getColumn("Delete"));
        }
    }

    /**
     * The search field ships with literal instructional text already typed
     * into it (not a real placeholder), so it must be cleared on focus and
     * restored when left empty, otherwise every search/filter treats that
     * text as an actual search term and returns zero rows.
     */
    private void setupSearchPlaceholder() {
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (jTextField1.getText().equals(SEARCH_PLACEHOLDER)) {
                    jTextField1.setText("");
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (jTextField1.getText().trim().isEmpty()) {
                    jTextField1.setText(SEARCH_PLACEHOLDER);
                }
            }
        });
    }

    /**
     * Treats the field as empty if it still holds the un-cleared placeholder.
     */
    private String getSearchTerm() {
        String text = jTextField1.getText().trim();
        return text.equals(SEARCH_PLACEHOLDER) ? "" : text;
    }

    /**
     * Sets column widths and disables auto-resize so every column of data is
     * fully visible, with horizontal scrolling for anything that overflows.
     */
    private void setupTableColumns() {
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        int[] widths = {150, 100, 130, 140, 90, 110, 100, 60, 70};
        for (int i = 0; i < widths.length && i < jTable1.getColumnModel().getColumnCount(); i++) {
            jTable1.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    /**
     * Loads all cleaners from PostgreSQL into the table.
     */
    private void loadCleaners() {
        currentCleaners = cleanerController.getAllCleaners();
        populateTable(currentCleaners);
        updateStats();
    }

    /**
     * Fills the JTable with the given list of cleaners.
     */
    private void populateTable(List<Cleaner> cleaners) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        for (Cleaner cleaner : cleaners) {
            model.addRow(new Object[]{
                cleaner.getName(),
                cleaner.getEmail(),
                cleaner.getPhone(),
                cleaner.getDepartment(),
                cleaner.getEmployeeId(),
                cleaner.getHireDate() != null ? cleaner.getHireDate().toString() : "",
                cleaner.getStatus(),
                "Edit",
                "Delete"
            });
        }
    }

    /**
     * Updates the summary cards at the top of the panel.
     */
    private void updateStats() {
        if (currentCleaners == null) {
            return;
        }
        long active = currentCleaners.stream().filter(c -> "Active".equalsIgnoreCase(c.getStatus())).count();
        long inactive = currentCleaners.size() - active;
        long departments = currentCleaners.stream().map(Cleaner::getDepartment).distinct().count();

        jLabel11.setText(String.valueOf(currentCleaners.size()));
        jLabel12.setText(String.valueOf(active));
        jLabel13.setText(String.valueOf(inactive));
        jLabel14.setText(String.valueOf(departments));
    }

    /**
     * Loads the distinct statuses and departments from PostgreSQL into the
     * filter combo boxes at the top of the panel.
     */
    private void loadFilterCombos() {
        loadingCombos = true;

        jComboBox2.removeAllItems();
        jComboBox2.addItem("All Status");
        for (String status : cleanerController.getStatuses()) {
            jComboBox2.addItem(status);
        }

        jComboBox1.removeAllItems();
        jComboBox1.addItem("All Departments");
        for (String department : cleanerController.getDepartments()) {
            jComboBox1.addItem(department);
        }

        loadingCombos = false;
    }

    /**
     * Applies the current search text and combo box filters to the table.
     * Typing in the search box filters the JTable automatically.
     */
    private void applyFilters() {
        String searchTerm = getSearchTerm();
        List<Cleaner> results = searchTerm.isEmpty()
                ? cleanerController.getAllCleaners()
                : cleanerController.searchCleaners(searchTerm);

        Object selectedDepartment = jComboBox1.getSelectedItem();
        if (selectedDepartment != null && !"All Departments".equals(selectedDepartment)) {
            results.removeIf(c -> !selectedDepartment.equals(c.getDepartment()));
        }

        Object selectedStatus = jComboBox2.getSelectedItem();
        if (selectedStatus != null && !"All Status".equals(selectedStatus)) {
            results.removeIf(c -> !selectedStatus.equals(c.getStatus()));
        }

        currentCleaners = results;
        populateTable(currentCleaners);
        updateStats();
    }

    /**
     * Filters the table automatically as the user types in the search field.
     */
    private void setupSearchListener() {
        jTextField1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilters(); }
        });
    }

    /**
     * Re-applies filters whenever the department or status combo box changes.
     */
    private void setupComboListeners() {
        jComboBox1.addActionListener(e -> { if (!loadingCombos) applyFilters(); });
        jComboBox2.addActionListener(e -> { if (!loadingCombos) applyFilters(); });
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
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        btnAddCleaner = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        summaryPnl = new javax.swing.JPanel();
        statsPnl = new javax.swing.JPanel();
        invValuePnl = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        totalMatsPnl = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lowStockItemsPnl = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        activeCleanersPnl = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
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

        jTextField1.setText("Search cleaners by name, ID, or email ...");
        jTextField1.setToolTipText("Search materials ...");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jButton1.setBackground(new java.awt.Color(59, 91, 219));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Search");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Departments", "Item 2", "Item 3", "Item 4" }));

        btnAddCleaner.setBackground(new java.awt.Color(59, 91, 219));
        btnAddCleaner.setForeground(new java.awt.Color(255, 255, 255));
        btnAddCleaner.setText("Add new Cleaner");
        btnAddCleaner.addActionListener(this::btnAddCleanerActionPerformed);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Status", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout searchPnlLayout = new javax.swing.GroupLayout(searchPnl);
        searchPnl.setLayout(searchPnlLayout);
        searchPnlLayout.setHorizontalGroup(
            searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAddCleaner, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );
        searchPnlLayout.setVerticalGroup(
            searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPnlLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(searchPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(btnAddCleaner, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        contentPnl.add(searchPnl);

        summaryPnl.setBackground(new java.awt.Color(245, 246, 250));
        summaryPnl.setMaximumSize(new java.awt.Dimension(1000, 100));
        summaryPnl.setMinimumSize(new java.awt.Dimension(1000, 100));
        summaryPnl.setPreferredSize(new java.awt.Dimension(1000, 100));

        statsPnl.setMaximumSize(new java.awt.Dimension(1000, 140));
        statsPnl.setMinimumSize(new java.awt.Dimension(1000, 140));
        statsPnl.setPreferredSize(new java.awt.Dimension(1000, 140));
        statsPnl.setLayout(new java.awt.GridLayout(1, 4, 3, 0));

        invValuePnl.setBackground(new java.awt.Color(255, 255, 255));
        invValuePnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Total Staff");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel11.setText("6");

        javax.swing.GroupLayout invValuePnlLayout = new javax.swing.GroupLayout(invValuePnl);
        invValuePnl.setLayout(invValuePnlLayout);
        invValuePnlLayout.setHorizontalGroup(
            invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invValuePnlLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel6))
                .addContainerGap(166, Short.MAX_VALUE))
        );
        invValuePnlLayout.setVerticalGroup(
            invValuePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invValuePnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addContainerGap(60, Short.MAX_VALUE))
        );

        statsPnl.add(invValuePnl);

        totalMatsPnl.setBackground(new java.awt.Color(255, 255, 255));
        totalMatsPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Active");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel12.setText("8");

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
                        .addComponent(jLabel12)))
                .addContainerGap(190, Short.MAX_VALUE))
        );
        totalMatsPnlLayout.setVerticalGroup(
            totalMatsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalMatsPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        statsPnl.add(totalMatsPnl);

        lowStockItemsPnl.setBackground(new java.awt.Color(255, 255, 255));
        lowStockItemsPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Inactive");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel13.setText("4");

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
                        .addComponent(jLabel13)))
                .addContainerGap(180, Short.MAX_VALUE))
        );
        lowStockItemsPnlLayout.setVerticalGroup(
            lowStockItemsPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lowStockItemsPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        statsPnl.add(lowStockItemsPnl);

        activeCleanersPnl.setBackground(new java.awt.Color(255, 255, 255));
        activeCleanersPnl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Departments");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel14.setText("5");

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
                        .addComponent(jLabel14)))
                .addContainerGap(147, Short.MAX_VALUE))
        );
        activeCleanersPnlLayout.setVerticalGroup(
            activeCleanersPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(activeCleanersPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        statsPnl.add(activeCleanersPnl);

        summaryPnl.add(statsPnl);

        contentPnl.add(summaryPnl);

        jScrollPane1.setBackground(new java.awt.Color(245, 246, 250));

        jTable1.setBackground(new java.awt.Color(245, 246, 250));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Cleaner", "Email", "Phone number", "Department", "Employee ID", "Hire Date", "Status", "Edit", "Delete"
            }
        ));
        jTable1.setRowHeight(45);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 994, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 10, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        contentPnl.add(jPanel2);

        add(contentPnl, java.awt.BorderLayout.CENTER);

        headerPnl.setBackground(new java.awt.Color(245, 246, 250));
        headerPnl.setMaximumSize(new java.awt.Dimension(1000, 70));
        headerPnl.setMinimumSize(new java.awt.Dimension(1000, 70));
        headerPnl.setPreferredSize(new java.awt.Dimension(1000, 70));
        headerPnl.setLayout(new javax.swing.BoxLayout(headerPnl, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Cleaners");
        headerPnl.add(jLabel1);

        jLabel2.setText("Manage cleaning staff records");
        headerPnl.add(jLabel2);

        add(headerPnl, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        applyFilters();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void btnAddCleanerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCleanerActionPerformed
     // Make the AddMaterialsDialog pop up appear, dim the background
    java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
    MainFrame mainFrame = (MainFrame) parentFrame;
    
    mainFrame.showDimOverlay(true);   // dim the background BEFORE showing dialog
    
    AddCleanersDialog dialog = new AddCleanersDialog(parentFrame, true, cleanerController);
    dialog.setLocationRelativeTo(parentFrame);
    dialog.setVisible(true);           // this line BLOCKS here until dialog closes (since it's modal)
    
    mainFrame.showDimOverlay(false);  // runs AFTER dialog is closed/disposed

    if (dialog.isCleanerAdded()) {
        loadFilterCombos();
        loadCleaners();
    }
    }//GEN-LAST:event_btnAddCleanerActionPerformed

    private void setupTableListeners() {
        jTable1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = jTable1.rowAtPoint(e.getPoint());
                int col = jTable1.columnAtPoint(e.getPoint());
                
                if (row >= 0 && col >= 0 && currentCleaners != null && row < currentCleaners.size()) {
                    if (col == 7) { // Edit column
                        handleEditCleaner(currentCleaners.get(row));
                    } else if (col == 8) { // Delete column
                        handleDeleteCleaner(currentCleaners.get(row));
                    }
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e) {}
            
            @Override
            public void mouseReleased(MouseEvent e) {}
            
            @Override
            public void mouseEntered(MouseEvent e) {}
            
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    private void handleEditCleaner(Cleaner cleaner) {
        // Defense in depth: see handleDeleteCleaner() for why this check
        // exists even though the Edit column is already hidden.
        if (!CurrentUser.isStorekeeper()) {
            AlertUtils.showErrorAlert("Permission Denied",
                    "Only the Storekeeper role has permission to edit a cleaner.");
            return;
        }

        java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
        MainFrame mainFrame = (MainFrame) parentFrame;
        
        mainFrame.showDimOverlay(true);
        
        EditCleanersDialog dialog = new EditCleanersDialog(parentFrame, true, cleanerController, cleaner);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
        
        mainFrame.showDimOverlay(false);

        if (dialog.isCleanerUpdated()) {
            loadFilterCombos();
            loadCleaners();
        }
    }

    private void handleDeleteCleaner(Cleaner cleaner) {
        // Defense in depth: the Edit/Delete columns are already removed for
        // non-Storekeepers in applyRoleRestrictions(), but this guards
        // against the action being triggered any other way.
        if (!CurrentUser.isStorekeeper()) {
            AlertUtils.showErrorAlert("Permission Denied",
                    "Only the Storekeeper role has permission to delete a cleaner.");
            return;
        }

        int response = AlertUtils.showDeleteConfirmation("Cleaner", cleaner.getName());
        
        if (response == javax.swing.JOptionPane.YES_OPTION) {
            boolean success = cleanerController.deleteCleaner(cleaner.getCleanerId());
            if (success) {
                AlertUtils.showDeletedAlert("Cleaner");
                loadFilterCombos();
                loadCleaners();
            } else {
                String reason = cleanerController.getLastError();
                // A foreign key violation (Postgres SQLState 23503) means this
                // cleaner_id is still referenced by another table (e.g. a
                // stock issuance record), so the database itself is blocking
                // the delete - surface that plainly instead of a raw SQL error.
                if (reason != null && reason.toLowerCase().contains("foreign key")) {
                    AlertUtils.showErrorAlert("Cannot Delete Cleaner",
                            "This cleaner (ID " + cleaner.getCleanerId() + ") cannot be deleted because "
                            + "other records still reference their cleaner ID (for example, stock issuance "
                            + "history). Set their status to Inactive instead, or remove the related records first.");
                } else {
                    AlertUtils.showErrorAlert("Error", reason != null
                            ? "Failed to delete cleaner:\n" + reason
                            : "Failed to delete cleaner. Please try again.");
                }
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel activeCleanersPnl;
    private javax.swing.JButton btnAddCleaner;
    private javax.swing.JPanel contentPnl;
    private javax.swing.JPanel headerPnl;
    private javax.swing.JPanel invValuePnl;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel lowStockItemsPnl;
    private javax.swing.JPanel searchPnl;
    private javax.swing.JPanel statsPnl;
    private javax.swing.JPanel summaryPnl;
    private javax.swing.JPanel totalMatsPnl;
    // End of variables declaration//GEN-END:variables
}
