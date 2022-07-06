/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * This creates a custom table renderer object to render the online games table.
 * @author mortimer
 */
public class TableRenderer extends DefaultTableCellRenderer{
    private boolean rowOneHighlighted=false;//determines the colour of row one
    private boolean rowTwoHighlighted=false;//determines the colour of row two
    private boolean rowThreeHighlighted=false;//determines the colour of row three
    private static final Color HIGHLIGHT_COLOR=Color.WHITE;//stores the color used for highlighted rows
    private static final Color JLABEL_COLOUR=Color.LIGHT_GRAY;//stores the default JLabel rows.
    private final OnlineMenu ONLINE;//stores the online menu in which the table is locared
    /**
     * Creates a custom table renderer to render the online game table.
     * @param online The online menu form in which the online table is in.
     */
    public TableRenderer(OnlineMenu online){
        ONLINE=online;//stores the online menu form
    }
    /**
     * Updates the variables that change the table row colour
     * @param rowOneIsHighlighted Whether row one should be highlighted
     * @param rowTwoIsHighlighted Whether row two should be highlighted
     * @param rowThreeIsHighlighted Whether row three should be highlighted
     */
    public void updateRowColour(boolean rowOneIsHighlighted,boolean rowTwoIsHighlighted,boolean rowThreeIsHighlighted){
        rowOneHighlighted=rowOneIsHighlighted;rowTwoHighlighted=rowTwoIsHighlighted;rowThreeHighlighted=rowThreeIsHighlighted;//sets object variables equal to the passed variables
    }
    /**
     * Returns the customised component to be rendered
     * @param table The table
     * @param value The value of the component
     * @param isSelected Whether the component is selected
     * @param hasFocus Whether the component is focused
     * @param row The row of the component
     * @param col The column of the table
     * @return The component
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);//gets JLabel for the row + col chosen
        //Get the status for the current row and sets the colour of the JLabel 
        Color cellColour = JLABEL_COLOUR;
        switch (row) {
            case 0:
                if (rowOneHighlighted) {
                    cellColour = HIGHLIGHT_COLOR;
                }
                break;
            case 1:
                if (rowTwoHighlighted) {
                    cellColour = HIGHLIGHT_COLOR;
                }
                break;
            default:
                if (rowThreeHighlighted) {
                    cellColour = HIGHLIGHT_COLOR;
                }
                break;
        }
        if (isSelected) {
            if (cellColour == JLABEL_COLOUR) {
                ONLINE.setPlayButtonEnabled(false);
                ONLINE.setDeleteButtonEnabled(!table.getValueAt(row, 0).equals(""));
            } else {
                ONLINE.setPlayButtonEnabled(true);
                ONLINE.setDeleteButtonEnabled(false);
                cellColour = table.getSelectionBackground();
            }
        }
        label.setBackground(cellColour);//sets colour to chosen colour
        return label;
    }
}
