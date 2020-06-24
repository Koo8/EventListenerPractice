import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Use defaultTableModel
 * See constructor part for its limited benefits
 */
public class Default_TableModel_Table extends JPanel implements TableModelListener {

    // Constructor
    Default_TableModel_Table() {
        // super(new GridLayout(1, 0));
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
        // Jtable constructor
        JTable table = new JTable(data, columnNames);
        // table size
        table.setPreferredScrollableViewportSize(new Dimension(700, 150));
        // table take the entire height of the container
        table.setFillsViewportHeight(true);

        // create JScrollPane
        // highlight: create a scrollpane as container for a table
        // NOTE:oooo The scroll pane automatically places the table header at the top of the viewport.
        // The column names remain visible at the top of the viewing area when the table data is scrolled.

        JScrollPane pane = new JScrollPane(table);

        add(pane);

        //By default, all columns in a table start out with equal width
        // to customize each column width, use the following code
        // set the third column wider
        TableColumn column = null;
        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 2) {
                column.setPreferredWidth(100);
            } else {
                column.setPreferredWidth(50);
            }
        }

        // add TableModelListener
        table.getModel().addTableModelListener(this);
    }


    @Override // for TableModelListener, which is an interface implemented by JTable
    public void tableChanged(TableModelEvent e) {
        System.out.println("tableChanged method is entered");
        // TableModel is the source.
        TableModel model = (TableModel) e.getSource();
        Object celldata = model.getValueAt(e.getFirstRow(), e.getColumn());
        System.out.println("Cell data is " + celldata);
    }

    public static void CreateAndShowGUI() {
        JFrame frame = new JFrame(" MY Default Table - Simple ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Default_TableModel_Table panel = new Default_TableModel_Table();

        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CreateAndShowGUI();
            }
        });
    }
}
