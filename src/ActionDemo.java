import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * When you have two or more component having the same function, use AbstractAction or
 * actionListener to centralized the state of the components.
 */

public class ActionDemo extends JPanel
        implements ItemListener {
    protected JTextArea textArea;
    protected String newline = "\n";
    protected Action leftAction, middleAction, rightAction;
    protected JCheckBoxMenuItem[] cbmi;

    private ActionDemo() {
        super(new BorderLayout());
        //setLayout(new BorderLayout());
//        Create a scrolled text area.
        textArea = new JTextArea(5, 30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        //Lay out the content pane.
        setPreferredSize(new Dimension(450, 350));
        add(scrollPane, BorderLayout.CENTER);

        //Create the actions shared by the toolbar and menu.
        leftAction =   new LeftAction(  "Go left",
                createNavigationIcon("Back24"),
                "This is the left button.",
                KeyEvent.VK_L);
        middleAction = new MiddleAction("Do something",
                createNavigationIcon("Up24"),
                "This is the middle button.",
                KeyEvent.VK_M);
        rightAction =  new RightAction( "Go right",
                createNavigationIcon("Forward24"),
                "This is the right button.",
                KeyEvent.VK_R);
    }

    /** resize imageIcon to 24x24 **/
    private Image resizeIcon(Image oriIcon, int w, int h) {

        BufferedImage resizedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(oriIcon,0,0,w, h,null);
        g2d.dispose();
        return resizedImage;
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createNavigationIcon(String imageName) {
        String imgLocation = "images/"
                + imageName
                + ".gif";
        URL imageURL = getClass().getResource(imgLocation);

        if (imageURL == null) {
            System.err.println("Resource not found: "
                    + imgLocation);
            return null;
        } else {
            ImageIcon bigIcon = new ImageIcon(imageURL);
            ImageIcon icon = new ImageIcon(resizeIcon(bigIcon.getImage(),24,24));
            return icon;
        }
    }

    public JMenuBar createMenuBar() {
//        JMenuItem menuItem = null;
        JMenuBar menuBar;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Create the first menu.
        JMenu mainMenu = new JMenu("Menu");

        Action[] actions = {leftAction, middleAction, rightAction};
        for (int i = 0; i < actions.length; i++) {
           JMenuItem menuItem = new JMenuItem(actions[i]);
            // remove icon from being shown
            menuItem.setIcon(null); //arbitrarily chose not to use icon
            mainMenu.add(menuItem);
        }

        //Set up the menu bar.
        menuBar.add(mainMenu);
        menuBar.add(createAbleMenu());
        return menuBar;
    }

    public void createToolBar() {
        JButton button = null;

        //Create the toolbar.
        JToolBar toolBar = new JToolBar();
        add(toolBar, BorderLayout.PAGE_START);

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
    }

    protected JMenu createAbleMenu() {
        JMenu ableMenu = new JMenu("Action State");
        cbmi = new JCheckBoxMenuItem[3];

        cbmi[0] = new JCheckBoxMenuItem("Left action enabled");
        cbmi[1] = new JCheckBoxMenuItem("Right action enabled");
        cbmi[2] = new JCheckBoxMenuItem("Middle action enabled");

        for (int i = 0; i < cbmi.length; i++) {
            cbmi[i].setSelected(true);
            cbmi[i].addItemListener(this);
            ableMenu.add(cbmi[i]);
        }

        return ableMenu;
    }

    public void itemStateChanged(ItemEvent e) {
        JCheckBoxMenuItem mi = (JCheckBoxMenuItem)(e.getSource()); // or e.getItem() works the same
        boolean selected =
                (e.getStateChange() == ItemEvent.SELECTED);

        //Set the enabled state of the appropriate Action.
        if (mi == cbmi[0]) {

            leftAction.setEnabled(selected);
        } else if (mi == cbmi[1]) {
            middleAction.setEnabled(selected);
        } else if (mi == cbmi[2]) {
            rightAction.setEnabled(selected);
        }
    }

    private void displayResult(String actionDescription,
                              ActionEvent e) {
        String s = ("Action event detected: "
                + actionDescription
                + newline
                + "    Event source: " + e.getSource()
                + newline);
        textArea.append(s);
    }

    public class LeftAction extends AbstractAction {
        private LeftAction(String text, ImageIcon icon,
                          String desc, Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            displayResult("Action for first button/menu item", e);
        }
    }

    public class MiddleAction extends AbstractAction {
        public MiddleAction(String text, ImageIcon icon,
                            String desc, Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            displayResult("Action for second button/menu item", e);
        }
    }

    public class RightAction extends AbstractAction {
        public RightAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            displayResult("Action for third button/menu item", e);
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the 
     * event-dispatching thread.
     */
    private  void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ActionDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create/set menu bar and content pane.
        ActionDemo demo = new ActionDemo();
        frame.setJMenuBar(demo.createMenuBar());
        demo.createToolBar();
        //demo.setOpaque(true); //content panes must be opaque
        frame.setContentPane(demo);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ActionDemo demo = new ActionDemo();

                demo.createAndShowGUI();
            }
        });
    }
}