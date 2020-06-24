import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * Custom TableModel, TableModelListener,
 * TableCellRenderer render column width and comboBox for one cloumn
 * CellEditor, defaultTableCellEditor
 */
public class Custom_TableModel_Table extends  JPanel implements TableModelListener{
     private   JTable table;
     private TableColumn column;
     private Component component;

    // constructor
    Custom_TableModel_Table () {
        super();

        // Create Table
        table = new JTable(new CustomTableModel());
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);
        JScrollPane pane = new JScrollPane(table);
        add(pane);
        table.getModel().addTableModelListener(this);

        // when need to control column size
        initColumnSizes(table);
        // add checkbox in one column
        setUpComboBoxforSportsColumn(table, table.getColumnModel().getColumn(2));


    }
    // use TableCellRenderer to retrieve cell data
    private void initColumnSizes(JTable table) {
        int headerWidth, cellWidth;
        CustomTableModel model = (CustomTableModel) table.getModel();

        for (int i = 0; i <table.getColumnCount() ; i++) {
            // get down to each column
            column = table.getColumnModel().getColumn(i);
           // get header length
            component = table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(
                    null,column.getHeaderValue(),false,false,0,0); // todo: why this table is set null?
            headerWidth = component.getPreferredSize().width;
            // get cell length
            component = table.getDefaultRenderer(model.getColumnClass(i)).getTableCellRendererComponent(table,model.data[0][i],false,false, 0,i );// choose data[0][i] because data 0 has longest cell
            cellWidth = component.getPreferredSize().width;

            // compare and take the max value
            column.setPreferredWidth(Math.max(headerWidth, cellWidth));

        }
    }

    private void setUpComboBoxforSportsColumn(JTable table, TableColumn column) {
        //set up celleditor for the column
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Snowboarding");
        comboBox.addItem("Rowing");
        comboBox.addItem("Knitting");
        comboBox.addItem("Speed reading");
        comboBox.addItem("Pool");
        comboBox.addItem("None of the above");
        column.setCellEditor(new DefaultCellEditor(comboBox));

        // set up cell renderer for tooltiptext
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setToolTipText("Click for more options");
        column.setCellRenderer(cellRenderer);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        System.out.println(" listening in tableChanged...");
        showUpdatedTable();
    }
    // this class can also implements TableModel interface. Only the "fire" methods are from AbstractTableModel
    private class CustomTableModel extends AbstractTableModel {

        private String[] columnNames = {"First Name",
                "Last Name",
                "Sport",
                "# of Years",
                "Vegetarian"};
        private Object[][] data= {
                {"Kathy", "Smith",
                        "Snowboarding and Extra", 5, Boolean.FALSE},
                {"John", "Doe",
                        "Rowing", 3, Boolean.TRUE},
                {"Sue", "Black",
                        "Knitting", 2, Boolean.FALSE},
                {"Jane", "White",
                        "Speed reading", 20, Boolean.TRUE},
                {"Joe", "Brown",
                        "Pool", 10, Boolean.FALSE}
        };

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }

        /* highlight: how to change String to boolean for cells
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box. --> compare with DefaultTypeTable.java
         *
         * Returns the most specific superclass for all the cell values in the column.
         * This helps recognized column 4 to be integer and display right-aligned,
         * This also help column 5 to display as checkbox for boolean.
         */
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return getValueAt(0,columnIndex).getClass();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if(columnIndex < 2)
            return false; // the first two columns are not editable
            else return true;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }
        // highlight  this setValueAt method is called everytime the table has updates.
        // same as "tableChanged" methods implemented from TableModelListener. The "addTableModelListener"
        // shouldn't be override.

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            data[rowIndex][columnIndex] = aValue;
            // highlight: all implemented methods are from TableModel interface. Only this "fire" method is from AbstractTableModel Absttact Class
            fireTableCellUpdated(rowIndex,columnIndex);
            showUpdatedTable();
        }

//        @Override
        public void addTableModelListener(TableModelListener l) {
            // Dont override this method.
//            // todo: not sure whu add here the method only evoke once when start
//            // todo: how to evoke it as soon as the change happened
//           // showUpdatedTable();
           // System.out.println("listening in addTableModelListener//");
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }


    }
    private void showUpdatedTable() {
        for (int i = 0; i <table.getRowCount() ; i++) {
            System.out.print("  row " + i  +": ");
            for (int j = 0; j < table.getColumnCount(); j++) {
                System.out.print("  " + table.getValueAt(i,j));

            }
            System.out.println();
        }
        System.out.println("-------------------------");
    }

    public static void CreateAndDrawGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Custom Table Model - Recognize Column Class - Partail Editable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        Custom_TableModel_Table newContentPane = new Custom_TableModel_Table();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CreateAndDrawGUI();
            }
        });
    }


}
