import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Copy DefaultTableCellRenderer formation, it extends JLabel, and implements TableCellRenderer
 */
public class ColorColumnTableCellRenderer extends JLabel implements TableCellRenderer {

    Border borderForSelectedCell = null;
    Border borderForUnselectedCell = null;
    //Constructor
    ColorColumnTableCellRenderer() {
        //If true the component paints every pixel within its bounds.
        setOpaque(true); // must do so for the JLabel background color to be shown.
    }


    @Override // tableCellRenderer // this example setBackground, setBorder and setTooltipText
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // render color
        Color newColor = (Color) value;
        setBackground(newColor);

        // set up border (inset)
        if(isSelected) {
            // set selected cell border only once
            if(borderForSelectedCell == null) {
                borderForSelectedCell = BorderFactory.createMatteBorder(2, 2, 2, 2, table.getSelectionBackground());
            }
            setBorder(borderForSelectedCell);
        }
        else{
            if(borderForUnselectedCell == null) {
                borderForUnselectedCell = BorderFactory.createMatteBorder(2, 2, 2, 2, table.getBackground());
            }
            setBorder(borderForUnselectedCell);
        }

        // set tooltiptext
        setToolTipText("RGB value: " + newColor.getRed() + ", "
                + newColor.getGreen() + ", "
                + newColor.getBlue());

        return this;
    }
}
