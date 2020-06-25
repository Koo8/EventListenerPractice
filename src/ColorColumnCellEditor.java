import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Help Custom_TableModel_Table.java
 * This is a CellEditor, it extends AbstractCellEditor implement TableCellEidtor, just like an "DefaultCellEditor"
 * This class can't extends "DefaultCellEditor" directly because "DefaultCellEidtor" constructor requires CheckBox, ComboBox or TestField as Parameter for an editor,
 * but this class treat each cell as a button and trigger a ColorChooser by clicking the button
 */

public class ColorColumnCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    JButton button;
    JColorChooser chooser;
    JDialog dialog;
    Color currentColor; // initialized by getTableCellEditorComponent method

    // constructor
    public ColorColumnCellEditor() {
        // set up the editor from table's point of view- the button
        // button triggers the color chooser, the editor from the user's point of view
        button = new JButton();
        // for actionPerformed method to use later
        button.setActionCommand("edit"); // can set this string as a final static field.
        button.addActionListener(this);
        // when there is a background color, use this may in some situations help to bring up the background color
        button.setBorderPainted(true);

        chooser = new JColorChooser();
        dialog = chooser.createDialog(button,
                "Pick a color",
                true,chooser,//modal set true, parent frame not focusable for any input
                this, // OK button handler
                null); // cancel button handler
    }

    @Override // from AbstractCellEditor
    // return the value contained in the editor - which is the color of the component in this case
    public Object getCellEditorValue() {
        return currentColor;
    }


    @Override // from TableCellEditor - set initial value for the returned component
    // make the returned component be able to draw and receive user input.
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentColor = (Color) value;
        return button;
    }

    @Override // from actionListener
    // Handles events from the editor button and from
    // the dialog's OK button.
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand()== "edit") {
            // button is clicked
           // button.setBackground(Color.WHITE);
            button.setBackground(currentColor);
            chooser.setColor(currentColor); // without this, the color will be none on the chooser dialog
            dialog.setVisible(true);

            fireEditingStopped(); // so that the button will get back to the renderer-defined appearance. Make the renderer work again.
        }
        else {// other buttons such as "OK" button
            currentColor = chooser.getColor(); // button currentColor can be updated here by the chooser

        }
    }
}
