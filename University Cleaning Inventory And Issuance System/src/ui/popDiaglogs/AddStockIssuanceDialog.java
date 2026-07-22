/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.popDiaglogs;

import Controller.StockIssuanceController;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import model.StockIssuance;
import dao.StockIssuanceDAO;
import dao.CleanerDAO;
import utils.CurrentUser;
import model.User;
import javax.swing.JOptionPane;
import Controller.MaterialDAO;
import Controller.MaterialDatabaseDAO;
import utils.DBConnection;
import java.time.format.ResolverStyle;
import java.time.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.List;


import model.Material;
import model.Cleaner;
import model.StockIssuance;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;



/**
 *
 * @author waldo
 */
public class AddStockIssuanceDialog extends javax.swing.JDialog {
    
private final StockIssuanceController issuanceController;
private final MaterialDAO materialDAO;
private  CleanerDAO cleanerDAO;
private final List<Material> loadedMaterials = new ArrayList<>();
private final List<Cleaner> loadedCleaners = new ArrayList<>();
    
    /**
     * Creates new form AddMaterialDialog
     */
    public AddStockIssuanceDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
         issuanceController = new StockIssuanceController();
          materialDAO = new MaterialDatabaseDAO();
         
      try {
        cleanerDAO = new CleanerDAO(
                DBConnection.getConnection()
        );

        loadMaterials();
        loadCleaners();

    } catch (ClassNotFoundException ex) {

        JOptionPane.showMessageDialog(
                this,
                "Could not connect to the database.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );

        ex.printStackTrace();
    }
        
    }
    
   private void loadMaterials() {

    cmbMaterial.removeAllItems();
    loadedMaterials.clear();

    cmbMaterial.addItem(
            "Choose a material to issue"
    );

    try {

        List<Material> materials =
                materialDAO.getAllMaterials();

        for (Material material : materials) {

            // Only display materials that have stock available.
            if (material.getQuantity() > 0) {

                loadedMaterials.add(material);

                cmbMaterial.addItem(
                        material.getMaterialName()
                );
            }
        }

        System.out.println(
                "Materials loaded: "
                + loadedMaterials.size()
        );

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Materials could not be loaded.\n"
                + ex.getMessage(),
                "Loading Error",
                JOptionPane.ERROR_MESSAGE
        );

        ex.printStackTrace();
    }
}
    
    
    
private void loadCleaners() {

    cmbCleaner.removeAllItems();
    loadedCleaners.clear();

    cmbCleaner.addItem(
            "Choose a cleaner to issue to"
    );

    try {

        List<Cleaner> cleaners =
                cleanerDAO.getAllCleaners();

        for (Cleaner cleaner : cleaners) {

            /*
             * Only show active cleaners.
             * Remove this condition if CleanerDAO already filters them.
             */
            if (cleaner.getStatus() == null
                    || cleaner.getStatus()
                            .equalsIgnoreCase("active")) {

                loadedCleaners.add(cleaner);

                cmbCleaner.addItem(
                        cleaner.getName()
                );
            }
        }

        System.out.println(
                "Cleaners loaded: "
                + loadedCleaners.size()
        );

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Cleaners could not be loaded.\n"
                + ex.getMessage(),
                "Loading Error",
                JOptionPane.ERROR_MESSAGE
        );

        ex.printStackTrace();
    }
}

private int getSelectedMaterialId() {

    int selectedIndex =
            cmbMaterial.getSelectedIndex();

    if (selectedIndex <= 0) {

        throw new IllegalArgumentException(
                "Please select a material."
        );
    }

    /*
     * Combo index 0 is the placeholder, so subtract 1
     * to access the matching Material object.
     */
    Material selectedMaterial =
            loadedMaterials.get(selectedIndex - 1);

    return selectedMaterial.getMaterialId();
}

private int getSelectedCleanerId() {

    int selectedIndex =
            cmbCleaner.getSelectedIndex();

    if (selectedIndex <= 0) {

        throw new IllegalArgumentException(
                "Please select a cleaner."
        );
    }

    Cleaner selectedCleaner =
            loadedCleaners.get(selectedIndex - 1);

    return selectedCleaner.getCleanerId();
}




    private Timestamp getSelectedDate() {

        String dateText = txtDate.getText();

        if (dateText == null) {
            return new Timestamp(System.currentTimeMillis());
        }

        dateText = dateText.trim();

        System.out.println(
                "Date entered: [" + dateText + "]"
                + " Length: " + dateText.length()
        );

        if (dateText.isEmpty()
                || dateText.equalsIgnoreCase("mm/dd/yyyy")) {

            return new Timestamp(System.currentTimeMillis());
        }

        DateTimeFormatter formatter
                = DateTimeFormatter
                        .ofPattern("M/d/uuuu")
                        .withResolverStyle(
                                ResolverStyle.STRICT
                        );

        LocalDate selectedDate
                = LocalDate.parse(dateText, formatter);

        return Timestamp.valueOf(
                selectedDate.atStartOfDay()
        );

}
    
    
    
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AddStockIssuanceDialog.class.getName());

  private int getLoggedInUserId() {
    User currentUser = CurrentUser.get();
    if (currentUser != null) {
        // User.getId() returns a String (e.g., "5"), so parse it to int
        return Integer.parseInt(currentUser.getId());
    }
    // Fallback – should never happen if the user is logged in
    throw new IllegalStateException("No user is currently logged in.");
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        Quantity = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbMaterial = new javax.swing.JComboBox<>();
        cmbCleaner = new javax.swing.JComboBox<>();
        txtDate = new javax.swing.JFormattedTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        Notes = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(480, 360));
        setModal(true);

        jButton2.setText("Cancel");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setText("Issue Stock");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Quantity");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Select Cleaner");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Select Material");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Date");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setText("New Stock Issuance");
        jLabel9.setToolTipText("");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Notes (Optional)");

        cmbMaterial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Choose a materail to issue" }));

        cmbCleaner.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Choose a cleaner to issue to" }));

        txtDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("MM/dd/yyyy"))));
        txtDate.addActionListener(this::txtDateActionPerformed);

        Notes.setColumns(20);
        Notes.setRows(5);
        jScrollPane1.setViewportView(Notes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmbCleaner, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cmbMaterial, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel9)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel1)
                                                .addComponent(Quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(35, 35, 35)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel8)))
                                        .addComponent(jSeparator1)
                                        .addComponent(jScrollPane1)))))
                        .addContainerGap(60, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(12, 12, 12)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCleaner, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Quantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

try {

        int materialId =
                getSelectedMaterialId();

        int cleanerId =
                getSelectedCleanerId();

        String quantityText =
                Quantity.getText().trim();

        if (quantityText.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please enter the quantity."
            );
        }

        int quantity =
                Integer.parseInt(quantityText);

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }

        Timestamp issuanceDate =
                getSelectedDate();

        int loggedInUserId =
                getLoggedInUserId();

        String notes =
                Notes.getText().trim();

        StockIssuance issuance =
                new StockIssuance();

        issuance.setMaterialId(materialId);
        issuance.setCleanerId(cleanerId);
        issuance.setQuantityIssued(quantity);
        issuance.setIssuanceDate(issuanceDate);
        issuance.setIssuedByUserId(loggedInUserId);
        issuance.setNotes(notes);

        boolean saved =
                issuanceController.issueMaterial(
                        issuance
                );

        if (saved) {

            JOptionPane.showMessageDialog(
                    this,
                    "Stock issued successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Stock issuance could not be saved.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    } catch (NumberFormatException ex) {

        JOptionPane.showMessageDialog(
                this,
                "Quantity must be a whole number.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );

    }   catch (DateTimeParseException ex) {

    JOptionPane.showMessageDialog(
            this,
            "Enter a valid date using MM/dd/yyyy.\n"
            + "Example: 12/31/2026",
            "Invalid Date",
            JOptionPane.WARNING_MESSAGE
    );

    System.err.println(
            "Date parsing error: " + ex.getMessage()
    );

    } 




        catch (IllegalArgumentException ex) {

        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
}
      catch (Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Unexpected error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        ex.printStackTrace();
    }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDateActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
         //Change the Theme of the app
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AddStockIssuanceDialog dialog = new AddStockIssuanceDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea Notes;
    private javax.swing.JTextField Quantity;
    private javax.swing.JComboBox<String> cmbCleaner;
    private javax.swing.JComboBox<String> cmbMaterial;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JFormattedTextField txtDate;
    // End of variables declaration//GEN-END:variables
}
