package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 *
 * @author waldo
 */
public class uiUtilities {
    
    // This method will be used to give all the different tables in the app the same style
    // This will just be more effiecent than changing the same properties on all the tables
    public static void applyTableStyleProperties(JTable table, JScrollPane scrollPane) {
        // Colors and Fonts for the tables
        Color whiteColor = new Color(255, 255, 255);
        Color gridColor = new Color(240, 242, 245);
        Color darkNavy = new Color(27, 38, 59);
        Color headerBg = new Color(248, 249, 250);
        Color headerFg = new Color(108, 117, 125);
        
        Font tableFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 15);

        // Main style for the tables
        table.setBackground(whiteColor);
        table.setForeground(darkNavy);
        table.setFont(tableFont);
        table.setGridColor(gridColor);
        table.setRowHeight(25); // Add extra row height
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false); // No vertical borders
        table.setFocusable(false);

        // Header style for the tables
        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.setBackground(headerBg);
            header.setForeground(headerFg);
            header.setFont(headerFont);
            header.setReorderingAllowed(false);
            
            // Eliminate 3D/gradient styles
            header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, gridColor));
        }

        //Padding for the cells witin the tables
        DefaultTableCellRenderer paddingRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, 
                    boolean isSel, boolean hasFoc, int row, int col) {
                super.getTableCellRendererComponent(t, val, isSel, hasFoc, row, col);
                
                // add 15px left/right padding
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
                return this;
            }
        };

        // Add padding to all table columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(paddingRenderer);
        }

        // Remove border and set colour to the scrollplane of the tables
        if (scrollPane != null) {
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setBackground(whiteColor);
        }
    }
}