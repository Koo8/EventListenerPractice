import javax.sound.midi.MidiEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.net.URL;

/**
 * To achieve:
 * JFrame has a JMenuBar -- which has a Main-Menu and a "Active State"-Menu
 * ***** the Main menu has 3 MenuItems to control Left Right and Up actions
 * ***** the ACTIVE state menu has 3 JCheckBoxItems that set 3 actions able - unable
 * JPanel has a ToolBar with 3 Buttons Left Right and Forward
 * JPanel has a text area that has a scrol pane to display "actionPerformed" result
 * oo a toolbar button and a menu item perform the same function described by an action. -- centralize the handling of state of action
 */

public class THreeActionByButtonAndMenu extends JPanel implements ItemListener {

    private JTextArea textArea;
   // private JButton leftBtn, rightBtn, forwardBtn;
    private Action leftAction, rightAction, middleAction;
    protected JCheckBoxMenuItem[] cbmi;
    // constructor
    private THreeActionByButtonAndMenu() {

        // set up layout
       // super(new BorderLayout());
        // or
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400,300));
        textArea = new JTextArea("This is the text area,that will need a scrollpane to run very looooooooooooooooooooooooooooooooooooooooog. \n",2,10);
        textArea.setEditable(false);
        // add scrolling to the textarea
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane,BorderLayout.CENTER);
        //instantiate 3 actions for menuItem and buttons
        leftAction = new LeftAction("left",createIcon("Back24.gif"),"left button", KeyEvent.VK_L);
        rightAction = new RightAction("right", createIcon("Forward24.gif"), "move to right", KeyEvent.VK_R);
        middleAction = new MiddleAction("middle", createIcon("Up24.gif"), "move to Upward", KeyEvent.VK_U);
    }

    private void createToolBar() {
        JToolBar toolBar = new JToolBar();
        add(toolBar, BorderLayout.PAGE_START);
        JButton button = null;
        //first button
        button = new JButton(leftAction);
        if (button.getIcon() != null) {
            button.setText(""); //an icon-only button
        }
        toolBar.add(button);

        //second button
        button = new JButton(middleAction);
        if (button.getIcon() != null) {
            button.setText(""); //an icon-only button
        }
        toolBar.add(button);

        //third button
        button = new JButton(rightAction);
        if (button.getIcon() != null) {
            button.setText(""); //an icon-only button
        }
        toolBar.add(button);
       // return toolBar;
    }

    private Icon createIcon(String fileName) {
        URL url = getClass().getResource("images/" + fileName);
        ImageIcon icon = new ImageIcon(url);
        ImageIcon smallIcon = new ImageIcon(Utilities.resizeIcon(icon.getImage(),24,24));
        return smallIcon;
    }

    private JMenuBar createJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        // create Main Menu
        JMenu mainMenu = new JMenu("Main");
        Action[] actions = {leftAction, middleAction, rightAction};
        // add action to each item, not itemListener - to select or disselct item.
        for (int i = 0; i <actions.length ; i++) {
            JMenuItem item = new JMenuItem(actions[i]);
            //item.setIcon(null);
            mainMenu.add(item);
        }


        // create Active state Menu
        JMenu activeStateMenu = new JMenu("Active State");
        cbmi = new JCheckBoxMenuItem[3];

        cbmi[0] = new JCheckBoxMenuItem("Left action enabled");
        cbmi[1] = new JCheckBoxMenuItem("Right action enabled");
        cbmi[2] = new JCheckBoxMenuItem("Middle action enabled");

        for (int i = 0; i < cbmi.length; i++) {
            cbmi[i].setSelected(true);
            cbmi[i].addItemListener(this);
            activeStateMenu.add(cbmi[i]);
        }


        menuBar.add(mainMenu);
        menuBar.add(activeStateMenu);
        return menuBar;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
        boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
        if (item.getText().contains("Left")) {
            leftAction.setEnabled(selected);
        } else if (item.getText().contains("Right")) {
            rightAction.setEnabled(selected);
        } else if(item.getText().contains("Middle")) {
            middleAction.setEnabled(selected);
        }

    }

//    public void itemStateChanged(ItemEvent e) {
//        JCheckBoxMenuItem mi = (JCheckBoxMenuItem)(e.getSource()); // or e.getItem() works the same
//        boolean selected =
//                (e.getStateChange() == ItemEvent.SELECTED);
//
//        //Set the enabled state of the appropriate Action.
//        if (mi == cbmi[0]) {
//
//            leftAction.setEnabled(selected);
//        } else if (mi == cbmi[1]) {
//            middleAction.setEnabled(selected);
//        } else if (mi == cbmi[2]) {
//            rightAction.setEnabled(selected);
//        }
//    }

    //------ 3 Action classes ----- //

    // oo Read  comments for better understanding of this class -- create 3 action classes
    private class LeftAction extends AbstractAction {
        /*** The key used for storing the name for the action, used for a menu or button.*/
        public static final String NAME_KEY = NAME;

        /*** The key used for storing a small icon for the action, used for toolbar buttons.*/
        public static final String SMALL_ICON_KEY = SMALL_ICON;

        /*** The key used for storing a short description for the action, used for tooltip text.*/
        public static final String SHORT_DESCRIPTION_KEY = SHORT_DESCRIPTION;

        /*** The key used for storing a longer description for the action, could be used for* context-sensitive help.*/
        public static final String LONG_DESCRIPTION_KEY = LONG_DESCRIPTION;

        // Constructor type One
        public LeftAction() {
            super();
        }
        // Constructor Type Two
        public LeftAction(String name) {
            super(name);
        }
        // Constructor Type Three
        public LeftAction(String name, Icon icon) {
            super(name, icon);
        }

        // oooo Constructor Four - we use this constructor
        public LeftAction(String name, Icon icon, String short_des, Integer mnemonic) {
            super(name, icon);
            putValue(SHORT_DESCRIPTION, short_des);
            putValue(MNEMONIC_KEY, mnemonic);
           // putValue(ACTION_COMMAND_KEY, "leftMove");
        }

        // oogetter and settersoo --- use getValue() and putValue()
        /*** Gets the name of this Action.  A value of null indicates that this Action
         * does not have a name.*/
        public String getName()
        {
            return (String)getValue(NAME_KEY);
        }
        /**
         * Sets the name of this Action.  A value of null indicates that this Action
         * should not have a name.
         *
         * @param name The name of this Action.
         */
        public void setName(String name)
        {
            putValue(NAME_KEY, name);
        }

        /**
         * Gets the small icon for this Action.  A value of null indicates that this Action
         * does not have a small icon.
         *
         * @return The small icon for this Action.
         */
        public Icon getSmallIcon()
        {
            return (Icon)getValue(SMALL_ICON_KEY);
        }

        /**
         * Sets the small icon for this Action.  A value of null indicates that this Action
         * should not have a small icon.
         *
         * @param icon The small icon for this Action.
         */
        public void setSmallIcon(Icon icon)
        {
            putValue(SMALL_ICON_KEY, icon);
        }
        /**
         * Gets the mnemonic key for this Action.  A value of 0 indicates that this Action does not
         * have a mnemonic key.
         *
         * @return The mnemonic key for this Action.
         */
        public int getMnemonic()
        {
//            Object value = getValue(MNEMONIC_KEY);
//
//            if (value == null)
//                return 0;
//            else
//                return ((Integer)value).intValue();
            return (int) getValue(MNEMONIC_KEY);
        }

        /**
         * Sets the mnemonic key for this Action.  A value of 0 indicates that this Action should not
         * have a mnemonic key.
         *
         * @param mnemonic The mnemonic for this action (This should be one of the KeyEvent.VK_x
         * constants).
         */
        public void setMnemonic(int mnemonic)
        {
            putValue(
                    MNEMONIC_KEY,
                    (mnemonic != 0) ? mnemonic : null
            );
        }
        /**
         * Gets the action command for this Action.  A value of null indicates that this Action
         * does not have an action command.
         *
         * @return The action command for this Action.
         */
        public String getActionCommand()
        {
            return (String)getValue(ACTION_COMMAND_KEY);
        }

        /**
         * Sets the action command for this Action.  A value of null indicates that this Action
         * should not have an action command.
         *
         * @param command The action command for this Action.
         */
        public void setActionCommand(String command)
        {
            putValue(ACTION_COMMAND_KEY, command);
        }

        // THe Override method
        @Override
        public void actionPerformed(ActionEvent e) {
            displayValue(e);
        }
    }

    private class RightAction extends AbstractAction {

        public RightAction(String name, Icon icon, String short_des, Integer mnemonic) {
            super(name, icon);
            putValue(SHORT_DESCRIPTION, short_des);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {;
            displayValue(e);
        }
    }

    private class MiddleAction extends AbstractAction {

        public MiddleAction(String name, Icon icon, String short_des, Integer mnemonic) {
            super(name, icon);
            putValue(SHORT_DESCRIPTION, short_des);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            displayValue(e);
        }
    }
    // todo:
    private void displayValue(ActionEvent e) {
        String s = ("Action event detected: "
                + "    Event source: " + e.getSource() + "\n");
        textArea.append(s);
    }


    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Three actions on and off ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        THreeActionByButtonAndMenu pane = new THreeActionByButtonAndMenu();
        frame.setJMenuBar(pane.createJMenuBar());
        //pane.setOpaque(true);
        pane.createToolBar(); // for action to be updated, must in this method, not in constructor
        frame.setContentPane(pane);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
//                THreeActionByButtonAndMenu demo = new THreeActionByButtonAndMenu();
//                demo.createAndShowGUI();
                createAndShowGUI();
            }
        });
    }

}
