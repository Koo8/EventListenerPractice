import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.undo.*;

/**
 * All Swing text components support cut, copy, paste,
 * and insert characters. Each editing command is
 * implemented by an Action object.
 */

public class TextComponentDemo extends JFrame {
    private JTextPane textPane;
    private AbstractDocument doc;
    private static final int MAX_CHARACTERS = 300;
    private JTextArea changeLog;
    private String newline = "\n";
    private HashMap<String, Action> actions;

    //undo helpers
    private UndoAction undoAction;
    private RedoAction redoAction;
    private UndoManager undo = new UndoManager();

    private TextComponentDemo() {
        super("TextComponentDemo");

        //Create the text pane and configure it.
        textPane = new JTextPane();
        // todo: no matter set to 0 or this below, the caret is always at 0;
        textPane.setCaretPosition(textPane.getStyledDocument().getLength());
        textPane.setMargin(new Insets(5, 5, 5, 5));
        StyledDocument styledDoc = textPane.getStyledDocument();
        if (styledDoc instanceof AbstractDocument) {
            doc = (AbstractDocument) styledDoc;
            doc.setDocumentFilter(new DocumentSizeFilter(MAX_CHARACTERS));
        } else {
            System.err.println("Text pane's document isn't an AbstractDocument!");
            System.exit(-1);
        }
        JScrollPane scrollPane = new JScrollPane(textPane);
        //textPane.setPreferredSize(new Dimension(200, 200));
        scrollPane.setPreferredSize(new Dimension(200, 200));

        //Create the text area for the status log and configure it.
        changeLog = new JTextArea(5, 30);
        changeLog.setEditable(false);
        JScrollPane scrollPaneForLog = new JScrollPane(changeLog);

        //Create a split pane for the change log and the text area.
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                scrollPane, scrollPaneForLog);
        splitPane.setOneTouchExpandable(false);

        //Create the status area.
//        JPanel statusPane = new JPanel(new GridLayout(1,1));
        CaretListenerLabel caretListenerLabel =
                new CaretListenerLabel("Caret Status");
//        statusPane.add(caretListenerLabel);

        //Add the components.
        getContentPane().add(splitPane, BorderLayout.CENTER);
       // getContentPane().add(statusPane, BorderLayout.PAGE_END);
        getContentPane().add(caretListenerLabel, BorderLayout.PAGE_END);

        //Set up the menu bar.
        actions = createActionTable(textPane);
        JMenu editMenu = createEditMenu();
        JMenu styleMenu = createStyleMenu();
        JMenuBar mb = new JMenuBar();
        mb.add(editMenu);
        mb.add(styleMenu);
        setJMenuBar(mb);

        //Add some key bindings.
        addBindings();

        //Put the initial text into the text pane.
        initDocument();
        textPane.setCaretPosition(doc.getLength());

        //Start watching for undoable edits and caret changes.
        doc.addUndoableEditListener(new MyUndoableEditListener());
        textPane.addCaretListener(caretListenerLabel);
        doc.addDocumentListener(new MyDocumentListener());
    }


    //// ******   Inner Classes ****** ////
    ////  3 listener classes  ////

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
                                + newline);
                    } catch (BadLocationException ble) {
                        setText("caret: text position: " + dot + newline);
                    }
                } else if (dot < mark) {
                    setText("selection from: " + dot
                            + " to " + mark + newline);
                } else {
                    setText("selection from: " + mark
                            + " to " + dot + newline);
                }
            });
        }
    }

    //This one listens for edits that can be undone.
    protected class MyUndoableEditListener
            implements UndoableEditListener {
        public void undoableEditHappened(UndoableEditEvent e) {
            //Remember the edit and update the menus.
            boolean res = undo.addEdit(e.getEdit()); // return boolean
            //System.out.println(" listener is set " + res  + " undo.canUndo is " + undo.canUndo());
            undoAction.updateUndoState();
            redoAction.updateRedoState();
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
            changeLog.append(e.getType().toString() + ": " +
                    changeLength + " character" +
                    ((changeLength == 1) ? ". " : "s. ") +
                    " Text length = " + document.getLength() +
                    "." + newline);
        }
    }

    //Add a couple of emacs key bindings for navigation.
    private void addBindings() {
        //associate an action with a key stroke by using a text
        // component"s input map
        InputMap inputMap = textPane.getInputMap();

        //Ctrl-b to go backward one character
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK);
        inputMap.put(key, DefaultEditorKit.backwardAction);

        //Ctrl-f to go forward one character
        key = KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK);
        inputMap.put(key, DefaultEditorKit.forwardAction);

        //Ctrl-p to go up one line
        key = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK);
        inputMap.put(key, DefaultEditorKit.upAction);

        //Ctrl-n to go down one line
        key = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);
        inputMap.put(key, DefaultEditorKit.downAction);
    }

    //Create the edit menu.
    private JMenu createEditMenu() {
        JMenu menu = new JMenu("Edit");

        //Undo and redo are actions of our own creation.
        undoAction = new UndoAction();
        menu.add(undoAction);

        redoAction = new RedoAction();
        menu.add(redoAction);

        menu.addSeparator();

        //These actions come from the default editor kit.
        //Get the ones we want and stick them in the menu.
        menu.add(getActionByName(DefaultEditorKit.cutAction));
        menu.add(getActionByName(DefaultEditorKit.copyAction));
        menu.add(getActionByName(DefaultEditorKit.pasteAction));

        menu.addSeparator();

        menu.add(getActionByName(DefaultEditorKit.selectAllAction));
        return menu;
    }

    //Create the style menu.
    private JMenu createStyleMenu() {
        JMenu menu = new JMenu("Style");

// * Action objects can operate on other text components in the program,
// *  If you do not want to share, instantiate the Action object yourself.
// *
// * instead of getting the action from the editor kit,
// * this code creates an instance of the BoldAction
// * class. Thus, this action is not shared with any
// * other text component, and changing its name will
// * not affect any other text component.

        Action action = new StyledEditorKit.BoldAction();
        action.putValue(Action.NAME, "Bold");
        menu.add(action);
        //menu.add(new StyledEditorKit.BoldAction()); // "font_bold" is the display, this may shared with other component

        action = new StyledEditorKit.ItalicAction();
        action.putValue(Action.NAME, "Italic");
        menu.add(action);

        action = new StyledEditorKit.UnderlineAction();
        action.putValue(Action.NAME, "Underline");
        menu.add(action);

        menu.addSeparator();

        menu.add(new StyledEditorKit.FontSizeAction("12", 12));
        menu.add(new StyledEditorKit.FontSizeAction("14", 14));
        menu.add(new StyledEditorKit.FontSizeAction("18", 18));

        menu.addSeparator();

        menu.add(new StyledEditorKit.FontFamilyAction("Serif",
                "Serif"));
        menu.add(new StyledEditorKit.FontFamilyAction("SansSerif",
                "SansSerif"));

        menu.addSeparator();

        menu.add(new StyledEditorKit.ForegroundAction("Red",
                Color.red));
        menu.add(new StyledEditorKit.ForegroundAction("Green",
                Color.green));
        menu.add(new StyledEditorKit.ForegroundAction("Blue",
                Color.blue));
        menu.add(new StyledEditorKit.ForegroundAction("Black",
                Color.black));

        return menu;
    }

    private void initDocument() {
        String[] initString =
                {"Use the mouse to place the caret.",
                        "Use the edit menu to cut, copy, paste, and select text.",
                        "Also to undo and redo changes.",
                        "Use the style menu to change the style of the text.",
                        "Use the arrow keys on the keyboard or these emacs key bindings to move the caret:",
                        "Ctrl-f, Ctrl-b, Ctrl-n, Ctrl-p."};

        SimpleAttributeSet[] attrs = initAttributes(initString.length);

        try {
            for (int i = 0; i < initString.length; i++) {
                // doc.getLength() is growing after insertString
                doc.insertString(doc.getLength(), initString[i] + newline,
                        attrs[i]);
            }
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text.");
        }
    }

    private SimpleAttributeSet[] initAttributes(int length) {
        //implementation of MutableAttributeSet using a hash table.
        SimpleAttributeSet[] attrs = new SimpleAttributeSet[length];

        attrs[0] = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attrs[0], "Helvetica");
        StyleConstants.setFontSize(attrs[0], 14);

        attrs[1] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setBold(attrs[1], true);

        attrs[2] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setItalic(attrs[2], true);

        attrs[3] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setFontSize(attrs[3], 20);

        attrs[4] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setFontSize(attrs[4], 12);

        attrs[5] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setForeground(attrs[5], Color.red);

        return attrs;
    }

    //The following two methods allow us to find an
    //action provided by the editor kit by its name.
    // can be used verbatim in other programs

    /**
     * takes the actions from the text pane and loads them into a HashMap
     */
    private HashMap<String, Action> createActionTable(JTextComponent textComponent) {
        // create an empty hashmap
        HashMap<String, Action> actions = new HashMap<>();
        // get the array of JTC's all built-in actions
        Action[] actionsArray = textPane.getActions();
       // Action[] actionsArray = textComponent.getActions();
        for (Action a : actionsArray) {
            // pub each built-in action into hashmap with Name Object as key, action as value
            actions.put((String) a.getValue(Action.NAME), a);
        }

        //JTextComponent has 73 built-in actions
        //System.out.println(actions.size() + " HaspMap for actions ");
        return actions;
    }


     // the method for retrieving an action by its name from the hash map:

    private Action getActionByName(String actionName) {

        return actions.get(actionName);
    }

    //// ***** Actions **** ////
    
    class UndoAction extends AbstractAction {
        UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            // check the state for undo or redo
            updateUndoState();
            redoAction.updateRedoState();
        }

        void updateUndoState() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getUndoPresentationName());
              //  System.out.println(" undo.canUndo triggered ");
            } else {
              //  System.out.println(" undo.canUndo is set false ");
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }

    class RedoAction extends AbstractAction {
        RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            updateRedoState();
            undoAction.updateUndoState();
        }

        void updateRedoState() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        final TextComponentDemo frame = new TextComponentDemo();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    //The standard main method.
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(() -> {
            //Turn off metal's use of bold fonts
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            createAndShowGUI();
        });
    }
}
