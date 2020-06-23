import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.awt.Dimension;
import java.awt.GridLayout;

/**
 * TableDemo is just like Default type table , except that it
 * uses a custom TableModel.
 */
public class TableDemo extends JPanel {
    private boolean DEBUG = true;

    public TableDemo() {
        super(new GridLayout(1,0));
        // create a table with customized tableModel, to avoid 3 cons defaultTableModel has
        JTable table = new JTable(new MyTableModel());
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane);
    }

    class MyTableModel extends AbstractTableModel { // extends for creating subclass tableModel use AbstractTableModel,not defaultTableModel

        private String[] columnNames = {"First Name",
                "Last Name",
                "Sport",
                "# of Years",
                "Vegetarian"};
        private Object[][] data = {
                {"Kathy", "Smith",
                        "Snowboarding", 5, Boolean.FALSE},
                {"John", "Doe",
                        "Rowing", 3, Boolean.TRUE},
                {"Sue", "Black",
                        "Knitting", 2, Boolean.FALSE},
                {"Jane", "White",
                        "Speed reading", 20, Boolean.TRUE},
                {"Joe", "Brown",
                        "Pool", 10, Boolean.FALSE}
        };

        @Override // tableModel
        public int getColumnCount() {
            return columnNames.length;
        }
        @Override // tableModel
        public int getRowCount() {
            return data.length;
        }
        @Override // tableModel
        public String getColumnName(int col) {
            return columnNames[col];
        }
        @Override // tableModel
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /* highlight: how to change String to boolean for cells
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box. --> compare with SimpleTableDemo.java
         *
         * Returns the most specific superclass for all the cell values in the column.
         * This helps recognized column 4 to be integer and display right-aligned,
         * This also help column 5 to display as checkbox for boolean.
         */
        @Override// tableModel
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         * compare with SimpleTableDemo.java --> some cells are editable here vs.
         * all are editable in simpleTableDemo
         */
        @Override // tableModel
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) {
                return false; // first and last name are not editable, others are editable
            } else {
                return true;
            }
        }
        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                        + " to " + value
                        + " (an instance of "
                        + value.getClass() + ")");
            }

            data[row][col] = value;
            // There ara a few fire table data change event methods from JTable's DefaultTableModel instance
            // inform all listeners that cell at this location is updated
            fireTableCellUpdated(row, col);

            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        TableDemo newContentPane = new TableDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
