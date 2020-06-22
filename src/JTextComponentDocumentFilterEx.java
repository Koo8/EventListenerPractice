import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.HashMap;

public class JTextComponentDocumentFilterEx extends JFrame {

    // Fields
    private JTextPane textPane;
    private AbstractDocument doc;
    private final int MAX_CHARACTER = 300;
    private JTextArea log;
    private JMenuBar menuBar;

    private UndoAction undoAction;
    private RedoAction redoAction;
    private UndoManager undoManager = new UndoManager();

    private CaretListenerLabel caretListenerLabel;

    // Construtor
    public JTextComponentDocumentFilterEx() {

        super("JTextComponent DocumentFilter");
        createMainDisplay();
        createMenuBar();
        setEmacsKeyBindingForTextPane(); // to use key for navigation within textPane
        inItContent(); // add content to TextPane AbstractDocument
        // add listeners
        //Start watching for undoable edits and caret changes.
        doc.addUndoableEditListener(new MyUndoableEditListener());
        textPane.addCaretListener(caretListenerLabel);
        doc.addDocumentListener(new MyDocumentListener());

    }

    // Methods for constructor
    private void createMainDisplay() {
        // layout is as this --
        // slitPane --> two scrollPanes , --> one is for textPane, one is for text area
        // create top JTextPane
        textPane = new JTextPane();
        textPane.setMargin(new Insets(5,5,5,5));

        //** textPane.setCaretPosition(doc.getLength());
        // attach doc inside the textPane with docFilter

//        StyledDocument styledDoc = textPane.getStyledDocument();
//        if (styledDoc instanceof AbstractDocument) {
//            doc = (AbstractDocument) styledDoc;
//            doc.setDocumentFilter(new DocumentSizeFilter(MAX_CHARACTERS));
//        } else {
//            System.err.println("Text pane's document isn't an AbstractDocument!");
//            System.exit(-1);
//        }
        doc = (AbstractDocument) textPane.getStyledDocument();
        // document has a max size condition
        doc.setDocumentFilter(new DocumentSizeFilter(MAX_CHARACTER));
        // add scrollpane to the textpane
        JScrollPane scrollPane = new JScrollPane(textPane);
        // textPane.setPreferredSize(new Dimension(200,  200));
        scrollPane.setPreferredSize(new Dimension(300, 300));

        // create a text area for log status
        log = new JTextArea(5, 30);
        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);

        // create JSplitPane to contain the above two components
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,scrollPane, scrollLog);

        // add to JFrame // oo without BorderLayout setUp, the display is messed up
        add(splitPane, BorderLayout.CENTER);
        // or write as below
        //  getContentPane().add(splitPane, BorderLayout.CENTER);

        // the bottom is a Jlabel
        caretListenerLabel = new CaretListenerLabel("Caret Status");
        add(caretListenerLabel, BorderLayout.SOUTH);
    }

    private void createMenuBar() {
        // on MenuBar, there are two menus --> edit menu and style menu       menuBar = new JMenuBar();
        menuBar = new JMenuBar();
        JMenu editMenu = createEditMenu();
        JMenu styleMenu = createStyleMenu();
        menuBar.add(editMenu);
        menuBar.add(styleMenu);
        setJMenuBar(menuBar);
    }

    private JMenu createEditMenu() {
        // add actios to menu
        JMenu menu = new JMenu("Edit");
        // add acctions class to the menu
        undoAction = new UndoAction();
        menu.add(undoAction);
        redoAction = new RedoAction();
        menu.add(redoAction);

        menu.addSeparator();
       // menu.add 4 built-in actions from defaultEditorKit
        menu.add(getActionByItsName(DefaultEditorKit.cutAction));
        menu.add(getActionByItsName(DefaultEditorKit.copyAction));
        menu.add(getActionByItsName(DefaultEditorKit.pasteAction));

        menu.addSeparator();
        menu.add(getActionByItsName(DefaultEditorKit.selectAllAction));


        return menu;
    }

    private JMenu createStyleMenu() {
        JMenu menu = new JMenu("Style");
        // // * Action objects can operate on other text components in the program,
        //// *  If you do not want to share, instantiate the Action object yourself.
        //// *
        //// * instead of getting the action from the editor kit,
        //// * this code creates an instance of the BoldAction
        //// * class. Thus, this action is not shared with any
        //// * other text component, and changing its name will
        //// * not affect any other text component.

        Action action = new StyledEditorKit.BoldAction(); // new instantiate, so that not be shared with other component
        action.putValue(Action.NAME, "Bold");
        menu.add(action);

        action = new StyledEditorKit.ItalicAction();
        action.putValue(Action.NAME, "Italic");
        menu.add(action);

        action = new StyledEditorKit.UnderlineAction();
        action.putValue(Action.NAME, "UnderLine");
        menu.add(action);


        menu.addSeparator();

        menu.add(new StyledEditorKit.FontSizeAction("12", 12));
        menu.add(new StyledEditorKit.FontSizeAction("14", 14));
        menu.add(new StyledEditorKit.FontSizeAction("18", 18));

        menu.addSeparator();

        menu.add(new StyledEditorKit.FontFamilyAction("Serif", "Serif"));
        menu.add(new StyledEditorKit.FontFamilyAction("SansSerif", "SansSerif"));

        menu.addSeparator();

        menu.add(new StyledEditorKit.ForegroundAction("Red", Color.RED));
        menu.add(new StyledEditorKit.ForegroundAction("Blue", Color.BLUE));

        return menu;
    }
    //Add a couple of emacs key bindings for navigation.
    private void setEmacsKeyBindingForTextPane() {
        // use inputMap for binding - get textpane inputMap;
        InputMap inputMap = textPane.getInputMap();

        // ctrl-b for backward one character etc.
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK);
        inputMap.put(key, DefaultEditorKit.backwardAction);
         key = KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK);
        inputMap.put(key, DefaultEditorKit.forwardAction);
        key = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK);
        inputMap.put(key, DefaultEditorKit.upAction);
        key = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);
        inputMap.put(key, DefaultEditorKit.downAction);
    }

    private void inItContent() {

        // define what to add to doc
        String[] initString =
                {"Use the mouse to place the caret.",
                        "Use the edit menu to cut, copy, paste, and select text.",
                        "Also to undo and redo changes.",
                        "Use the style menu to change the style of the text.",
                        "Use the arrow keys on the keyboard or these emacs key bindings to move the caret:",
                        "Ctrl-f, Ctrl-b, Ctrl-n, Ctrl-p."};

        // define how to add in style
        SimpleAttributeSet[] attributeSet = new SimpleAttributeSet[initString.length]; // each string has its own attribute design
        attributeSet[0] = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attributeSet[0],"Arial");
        StyleConstants.setFontSize(attributeSet[0], 16);
        attributeSet[1] = new SimpleAttributeSet(attributeSet[0]); // on top of having [0] attributes
        StyleConstants.setBold(attributeSet[1], true);
        attributeSet[2] = new SimpleAttributeSet(attributeSet[0]);
        StyleConstants.setItalic(attributeSet[2], true);
        attributeSet[3] = new SimpleAttributeSet(attributeSet[0]);
        StyleConstants.setFontSize(attributeSet[3], 26);
        attributeSet[4] = new SimpleAttributeSet(attributeSet[0]);
        StyleConstants.setBackground(attributeSet[4], Color.YELLOW);
        attributeSet[5] = new SimpleAttributeSet(attributeSet[0]);
        StyleConstants.setForeground(attributeSet[5], Color.RED);

        // use abstractDocument.insertString to insert string with its defined style
        for (int i = 0; i <initString.length; i++) {
            try {
                doc.insertString(doc.getLength(), initString[i] + "\n",attributeSet[i]);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }
    // for edit Menu
    private Action getActionByItsName(String actionName) {
        // there are 73 built-in actions from DefaultEditorKit for textpane editing
        // since textPane is JTextComponent, getActions() can be used
        Action[] actions = textPane.getActions(); // total 73 actions
        // create an empty hashmap
        HashMap<String, Action> actionList = new HashMap<>();
        // put 73 actions into the hashmap, with action.name as the key
        for(Action a: actions) {
            String actionNameHolder = (String) a.getValue(Action.NAME);
            actionList.put(actionNameHolder, a);
        }
        return actionList.get(actionName);
    }

    // Create caretLabel that has caretListener class
    //This listens for and reports caret movements.
    protected class CaretListenerLabel extends JLabel
            implements CaretListener {
        CaretListenerLabel(String label) {
            super(label);
        }

        //Might not be invoked from the event dispatch thread.
        public void caretUpdate(CaretEvent e) {
            displaySelectionInfo(e.getDot(), e.getMark());
        }

        //This method can be invoked from any thread.  It
        //invokes the setText and modelToView methods, which
        //must run on the event dispatch thread. We use
        //invokeLater to schedule the code for execution
        //on the event dispatch thread.
        void displaySelectionInfo(final int dot,
                                  final int mark) {
            SwingUtilities.invokeLater(() -> {
                if (dot == mark) {  // no selection -- where you are  == the end of selection
                    try {
                        Rectangle caretCoords = textPane.modelToView(dot);
                        /* Convert it to view coordinates. */
                        setText("caret: text position: " + dot
                                + ", view location = ["
                                + caretCoords.x + ", "
                                + caretCoords.y + "]"
                                + "\n");
                    } catch (BadLocationException ble) {
                        setText("caret: text position: " + dot + "\n");
                    }
                } else if (dot < mark) {
                    setText("selection from: " + dot
                            + " to " + mark + "\n");
                } else {
                    setText("selection from: " + mark
                            + " to " + dot +"\n");
                }
            });
        }
    }

    //And this one listens for any changes to the document.
    protected class MyDocumentListener
            implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
            displayEditInfo(e);
        }

        public void removeUpdate(DocumentEvent e) {
            displayEditInfo(e);
        }

        public void changedUpdate(DocumentEvent e) {
            displayEditInfo(e);
        }

        private void displayEditInfo(DocumentEvent e) {
            Document document = e.getDocument();
            int changeLength = e.getLength();
            log.append(e.getType().toString() + ": " +
                    changeLength + " character" +
                    ((changeLength == 1) ? ". " : "s. ") +
                    " Text length = " + document.getLength() +
                    "." + '\n');
        }
    }

    // Listeners
    private class MyUndoableEditListener implements UndoableEditListener {

        @Override
        public void undoableEditHappened(UndoableEditEvent e) {
            // to undo is to remember the edits in order and update the menu
            boolean result = undoManager.addEdit(e.getEdit());
            System.out.println("Listener for undo is tested");

            undoAction.updateUndoManagerState();
            redoAction.updateRedoState();
        }
    }

    // Custom Actions
    class UndoAction extends AbstractAction {
        // constructor
        UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("undo performed");
           // step 1 undo perform
            try{
                undoManager.undo();
            } catch (CannotUndoException ex) {
                ex.printStackTrace();
            }
            // step 2 - check if canUndo or not, and update the state of Action and the display on menu  accordingly
            updateUndoManagerState();
            redoAction.updateRedoState();

        }

        public void updateUndoManagerState() {
            if (undoManager.canUndo()) {
                undoAction.setEnabled(true);
                putValue(Action.NAME,undoManager.getUndoPresentationName());
            } else{
                undoAction.setEnabled(false);
                putValue(Action.NAME,"undo");
            }


        }
    }

    class RedoAction extends AbstractAction {
        // constructor
        RedoAction() {
            super("Redo");
            setEnabled(false);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                undoManager.redo();
            }catch(CannotRedoException ex) {
                ex.printStackTrace();
            }
            undoAction.updateUndoManagerState();
            updateRedoState();

        }
        public void updateRedoState() {
            if(undoManager.canRedo()) {
                redoAction.setEnabled(true);
                putValue(Action.NAME, undoManager.getRedoPresentationName());

            }else {
                redoAction.setEnabled(false);
                putValue(Action.NAME, "redo");
            }
        }
    }

    // CreateGUI

    private static void createAndShowGUI(){

        JTextComponentDocumentFilterEx frame = new JTextComponentDocumentFilterEx();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    // Main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->createAndShowGUI());
    }


}
