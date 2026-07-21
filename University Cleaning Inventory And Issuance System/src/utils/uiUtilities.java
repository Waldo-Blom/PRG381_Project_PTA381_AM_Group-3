package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author waldo
 */
public class uiUtilities {

    // Left/right padding (in px) applied to every table cell, including the
    // formatted currency/date columns, so they all keep a consistent look
    private static final int CELL_PADDING = 15;

    // Shared formats used by formatCurrencyColumn() / formatDateColumn() below
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("R #,##0.00");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("d MMMM yyyy HH:mm");

    // Default colour used for placeholder text, and the client-property key
    // PLACEHOLDER_ACTIVE_KEY is used to track whether a field is currently showing its placeholder
    private static final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);
    private static final String PLACEHOLDER_ACTIVE_KEY = "uiUtilities.placeholderActive";

    // This method will be used to give all the different tables in the app the same style
    // This will just be more effiecent than changing the same properties on all the tables
    public static void applyTableStyleProperties(JTable table, JScrollPane scrollPane) {
        applyTableStyleProperties(table, scrollPane, null);
    }

    // Overload that also lets you set relative column widths so text doesn't get cut off.
    public static void applyTableStyleProperties(JTable table, JScrollPane scrollPane, int[] columnWidths) {
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

        // Make sure columns are proportionally resized to fill the
        // available width instead of getting clipped/cut off
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

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

        // Add padding to all table columns
        DefaultTableCellRenderer paddingRenderer = createPaddedRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(paddingRenderer);
        }

        // Apply relative column widths, if provided
        if (columnWidths != null) {
            TableColumnModel cm = table.getColumnModel();
            int count = Math.min(columnWidths.length, cm.getColumnCount());
            for (int i = 0; i < count; i++) {
                cm.getColumn(i).setPreferredWidth(columnWidths[i]);
            }
        }

        // Remove border and set colour to the scrollplane of the tables
        if (scrollPane != null) {
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setBackground(whiteColor);
        }
    }

    // Formats a column as currency, e.g. "R 1 250.00".
    public static void formatCurrencyColumn(JTable table, int columnIndex) {
        applyRendererToColumn(table, columnIndex, createFormattingRenderer(
                value -> value instanceof Number
                        ? CURRENCY_FORMAT.format(((Number) value).doubleValue())
                        : null,
                SwingConstants.RIGHT));
    }

    // Formats a column as a readable date/time, e.g. "19 April 2026 13:30".
    public static void formatDateColumn(JTable table, int columnIndex) {
        applyRendererToColumn(table, columnIndex, createFormattingRenderer(
                value -> value instanceof Date
                        ? DATE_FORMAT.format((Date) value)
                        : null,
                SwingConstants.LEFT));
    }

    
    private interface CellTextFormatter {
        String format(Object value);
    }

    // Builds a padded renderer that displays cell values through the given formatter
    private static DefaultTableCellRenderer createFormattingRenderer(
            CellTextFormatter formatter, int horizontalAlignment) {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean isSel, boolean hasFoc, int row, int col) {
                String text = val == null ? "" : formatter.format(val);
                if (text == null) {
                    text = val.toString(); 
                }
                super.getTableCellRendererComponent(t, text, isSel, hasFoc, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, CELL_PADDING, 0, CELL_PADDING));
                setHorizontalAlignment(horizontalAlignment);
                return this;
            }
        };
    }

    // Plain padded renderer (no special formatting) used by applyTableStyleProperties()
    private static DefaultTableCellRenderer createPaddedRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean isSel, boolean hasFoc, int row, int col) {
                super.getTableCellRendererComponent(t, val, isSel, hasFoc, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, CELL_PADDING, 0, CELL_PADDING));
                return this;
            }
        };
    }

    private static void applyRendererToColumn(JTable table, int columnIndex, DefaultTableCellRenderer renderer) {
        if (table == null || columnIndex < 0 || columnIndex >= table.getColumnCount()) {
            return; 
        }
        table.getColumnModel().getColumn(columnIndex).setCellRenderer(renderer);
    }

    // Installs "greyed out hint" placeholder text on a search field 
    public static void installPlaceholder(JTextField field, String placeholderText) {
        installPlaceholder(field, placeholderText, PLACEHOLDER_COLOR);
    }

    public static void installPlaceholder(JTextField field, String placeholderText, Color placeholderColor) {
        // Capture the field's real text colour before its greyed it out
        Color normalColor = field.getForeground();
        if (normalColor.equals(placeholderColor)) {
            normalColor = Color.BLACK; 
        }
        
        final Color restoreColor = normalColor;

        if (field.getText().isEmpty() || field.getText().equals(placeholderText)) {
            showPlaceholder(field, placeholderText, placeholderColor);
        }

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (Boolean.TRUE.equals(field.getClientProperty(PLACEHOLDER_ACTIVE_KEY))) {
                    field.setText("");
                    field.setForeground(restoreColor);
                    field.putClientProperty(PLACEHOLDER_ACTIVE_KEY, false);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    showPlaceholder(field, placeholderText, placeholderColor);
                }
            }
        });
    }

    private static void showPlaceholder(JTextField field, String placeholderText, Color placeholderColor) {
        field.setText(placeholderText);
        field.setForeground(placeholderColor);
        field.putClientProperty(PLACEHOLDER_ACTIVE_KEY, true);
    }

    public static String getFieldText(JTextField field) {
        if (Boolean.TRUE.equals(field.getClientProperty(PLACEHOLDER_ACTIVE_KEY))) {
            return "";
        }
        return field.getText().trim();
    }
}