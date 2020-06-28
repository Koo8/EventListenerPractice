import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TableSorterCopy extends AbstractTableModel {

   private MouseListener mouseListener;

    // Constructor 1
    public TableSorterCopy() {
        // need mouseListener, instantiate here
        mouseListener = new Mouse_Handler();
    }



   // methods from superClass AbstractTableModel
    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }


    // Helper Classes

    //*** MouseListener - Mouse_Handler class
    private class Mouse_Handler extends MouseAdapter {
        // only need the mouseClicked method

        @Override
        public void mouseClicked(MouseEvent e) {
            // click on tableHeader to sort

            //first, get the header that has been mouseClick event
            JTableHeader h = (JTableHeader) e.getSource(); // getSource() is from EventObject, it returns The object on which the Event initially occurred.
            // get index of the header
            int columnIndexInView = h.columnAtPoint(e.getPoint());
            TableColumnModel columnModel = h.getColumnModel();
            int columnIndexInModel = columnModel.getColumn(columnIndexInView).getModelIndex();

        }
    }
}
