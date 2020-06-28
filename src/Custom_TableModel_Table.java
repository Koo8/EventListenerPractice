import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Custom TableModel, TableModelListener,
 * TableCellRenderer render column width and comboBox for one cloumn
 * CellEditor , TableCellEditor, tooltiptext from renderer
 * TableCellEditor as parent class for ColorColumnCellEditor
 * TableCellRenderer as parent class for ColorColumnCellRenderer
 * ** This is a JPanel class that contain a JTable component
 * set up tooltips for cells and columnHeaders using "getToolTipText()" method from JTable and JTableHeader.
 * TableSorter.java was inserted between CustomTableModel and JTable, to achieve sorting and filtering functionality
 *
 */
public class Custom_TableModel_Table extends JPanel implements TableModelListener{
     private   JTable table;
     private TableColumn column;
     private Component component;
     private String clickString = "click to sort";

     private int headerWidth, cellWidth;
     private static CustomTableModel model;
     private String[] columnToolTips = {clickString,
             clickString,
             "The person's fav color, " +clickString ,
             "The person's favorite sport to participate in, " + clickString,
             "The number of years the person has played the sport, " + clickString,
             "If checked, the person eats no meat, " + clickString};

    // constructor
    Custom_TableModel_Table () {
        //super(new GridLayout(1, 0));
        model = new CustomTableModel();
        TableSorter sorter = new TableSorter(model); // ADD FOR SORTING

        // Create Table that use customized TableModel
       // table = new JTable(new CustomTableModel()); // oo this line was replaced by the block below for tooltip text display. I don't know how to do it otherwise.

        // insert the tableSorter between model and Jtable,to sort and filter the jtable
        // parameter was "new CustomTableModel", changed to "sorter" for sorting purpose
        table = new JTable(sorter) { // override "getToolTipText" from JComponent for cell tooltips, and "createDefaultTableHeader" from JTable for columnHeader tooltips
            @Override
            public String getToolTipText(MouseEvent event) {
                // firstly -- find the event fire location
                String tip = null;
                Point p = event.getPoint();//Returns the x,y position of the event relative to the source component.
                int rowIndex = rowAtPoint(p);//table.rowAtPoint();
                int columnIndex = columnAtPoint(p);
                int realColumnIndex = convertColumnIndexToModel(columnIndex);// translate the view index to a model index so that for sure the intended column is selected. Because Column index can be changed by drag and drop  etc.

                //set up Sports Column tooltips
                if (realColumnIndex == 3) {
                    tip = "This person's fav sports is " + getValueAt(rowIndex, columnIndex);

                    // set up checkBox column tooltips
                } else if (realColumnIndex == 5) {
                    TableModel model = getModel();
                    String firstName = (String) model.getValueAt(rowIndex,0);
                    String lastName = (String) model.getValueAt(rowIndex,1);
                    Boolean vegie = (Boolean) model.getValueAt(rowIndex,5);
                    if(Boolean.TRUE.equals(vegie)) {
                        tip = firstName + " " + lastName + " is a vegetarian.";
                    } else {
                        tip = firstName + " " + lastName + " isn't a vegetarian.";
                    }
                }else{ // when you have other renderers that supply their own tooltips
                    tip = super.getToolTipText(event);
                }
                return tip;
            }

            // set the code immediately below for another way of setting tableheader tool tips
            @Override
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) { // columnModel is one of JTableHeader fields, it is TableColumnModel of header
                   @Override // Allows the renderer's tips to be used if there is text set.
                    public String getToolTipText(MouseEvent e) {
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex(); // convert to model index.
                        return columnToolTips[realIndex];
                    }
                };
            }
        };

       // table.getTableHeader().setToolTipText("Only when all column has the same tool tips, use this simple code");

        sorter.setTableHeader(table.getTableHeader());  //ADDED to sort by column headers
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);
        JScrollPane pane = new JScrollPane(table);
        add(pane);
        table.getModel().addTableModelListener(this);

        // todo: when need to control column size
        // method 1 not working with added TableSorter.java
       // initColumnSizes(table);
       // method 2 : the table width initial set up need to be over 600, otherwise the horizontal bar is auto shown.
      init();
        // method3: the longest string may get cut if the table width is not long enough
//        for (int i = 0; i < table.getColumnCount(); i++) {
//            adjustColumnSizes(table, i, 2);
//        }
        // add checkbox in one column
        setUpComboBoxforSportsColumn(table, table.getColumnModel().getColumn(3));

        // add ColorRenderer and ColorEditor to the colorColumn which is index 2, or use Color.class
        table.setDefaultEditor(table.getColumnClass(2),new ColorColumnCellEditor());
        // render border, background and tooltiptext for color column
        table.setDefaultRenderer(Color.class, new ColorColumnTableCellRenderer());

    }

    // method 1 - to adjust column width, not working after added TableSorter.java
    // use TableCellRenderer to retrieve the component
//    private void initColumnSizes(JTable table) {
//        int headerWidth, cellWidth;
//       // CustomTableModel model = (CustomTableModel) table.getModel();
//
//        for (int i = 0; i <table.getColumnCount() ; i++) {
//
//            // get down to each column
//            column = table.getColumnModel().getColumn(i);
//            component = table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(
//                    null,column.getHeaderValue(),false,false,0,0); // todo: why this table is set null?
//            headerWidth = component.getPreferredSize().width;//           // get header length
//            // get cell length
//            component = table.getDefaultRenderer(table.getModel().getColumnClass(i)).getTableCellRendererComponent(table,model.data[0][i],false,false, 0,i );// choose data[0][i] because data 0 has longest cell
//            cellWidth = component.getPreferredSize().width;
//            if(i == 2) {
//                cellWidth = 100; // to shorten the color column
//            }
//            // compare and take the max value
//            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
//
//        }
//    }
    // method 2 of column width adjustment - won't cut long string, if table is not wide enough, will show horizontal scroll to help
    public void init() {
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        TableColumnAdjuster tca = new TableColumnAdjuster(table);
        tca.adjustColumns();

    }
    // method 3 - will cut long string if table width is not enough, but will show all column in the view without a horizontal bar
    // also, todo: may occasionally has sorter outofindexbound error
//    public static void adjustColumnSizes(JTable table, int column, int margin) {
//        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
//        TableColumn col = colModel.getColumn(column);
//        int width;
//
//        TableCellRenderer renderer = col.getHeaderRenderer();
//        if (renderer == null) {
//            renderer = table.getTableHeader().getDefaultRenderer();
//        }
//        Component comp = renderer.getTableCellRendererComponent(
//                table, col.getHeaderValue(), false, false, 0, 0);
//        width = comp.getPreferredSize().width;
//
//        for (int r = 0; r < table.getRowCount(); r++) {
//            renderer = table.getCellRenderer(r, column);
//            comp = renderer.getTableCellRendererComponent(
//                    table, table.getValueAt(r, column), false, false, r, column);
//            int currentWidth = comp.getPreferredSize().width;
//            width = Math.max(width, currentWidth);
//        }
//
//        width += 2 * margin;
//
//        col.setPreferredWidth(width);
//    }

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
                "Favorite Color",
                "Sport",
                "# of Years",
                "Vegetarian"};
        private Object[][] data= {
                {"Kathy", "Smith",new Color(153, 0, 153),
                        "Snowboarding and Extra", 5, Boolean.FALSE},
                {"John", "Doe",new Color(51, 51, 153),
                        "Rowing", 3, Boolean.TRUE},
                {"Sue", "Black",new Color(51, 102, 51),
                        "Knitting", 2, Boolean.FALSE},
                {"Jane", "White",Color.red,
                        "Speed reading", 20, Boolean.TRUE},
                {"Joe", "Brown", Color.pink,
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
