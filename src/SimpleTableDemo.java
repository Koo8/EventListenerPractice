
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SimpleTableDemo extends JPanel  implements TableModelListener{
    private boolean DEBUG = false;

    public SimpleTableDemo() {
        super(new GridLayout(1,0));
//declares the column names in a String array:
        String[] columnNames = {"First Name",
                "Last Name",
                "Sport",
                "# of Years",
                "Vegetarian"};
//data is initialized and stored in a two-dimensional Object array:
        Object[][] data = {
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
        // Construct table - using one of JTable constructors.
        // The cons of this constructor (inherent defaultTableModel)-> 1. each cell editable 2. all data are Strings 3 require all data to be put in beforehand
        final JTable table = new JTable(data, columnNames);
        // set dimension
        table.setPreferredScrollableViewportSize(new Dimension(600, 120));
        table.setFillsViewportHeight(true); // when set true, table use the entire height of the container

        // for debug purpose - add mouseListener
        if (DEBUG) {
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    printDebugData(table);
                }
            });
        }

        // highlight: create a scrollpane as container for a table
        // NOTE:oooo The scroll pane automatically places the table header at the top of the viewport.
        // The column names remain visible at the top of the viewing area when the table data is scrolled.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane);

        //By default, all columns in a table start out with equal width
        // to customize each column width, use the following code

        TableColumn column = null;
        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 2) {
                column.setPreferredWidth(100); //third column is bigger by defining this to 100 and others to 50
            } else {
                column.setPreferredWidth(50);
            }
        }
        // add TableModelListener
        table.getModel().addTableModelListener(this);
    }

    private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        TableModel model = table.getModel();

        System.out.println("Value of data: ");
        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j)); // model.getValue retrieve the cell data
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

    //@Override // tableModelListener
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        String columnName = model.getColumnName(column);
        Object cellData = model.getValueAt(row, column);
        System.out.println("row is " + row + " and colume is " + column + " has a value of " + cellData);
        System.out.println(columnName);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("SimpleTableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        SimpleTableDemo newContentPane = new SimpleTableDemo();
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