
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;

public class JavaAbstractActionExample extends JPanel
{
    // our actions
    Action cutAction, copyAction, pasteAction;

    // our icons for the actions
    ImageIcon cutIcon, copyIcon, pasteIcon;

    public JavaAbstractActionExample()
    {
        super(new BorderLayout());
        createActions();
    }

    private void createActions()
    {
        // create our icons (for the toolbar)
        URL url = getClass().getResource("images/Copy-32.png");
        URL url1 = getClass().getResource("images/Cut-32.png");
        URL url2 = getClass().getResource("images/Paste-32.png");
        cutIcon = new ImageIcon(url1);
        copyIcon = new ImageIcon(url);
        pasteIcon = new ImageIcon(url2);

        // create our actions
        cutAction = new CutAction("Cut", cutIcon, "Cut stuff onto the clipboard", new Integer(KeyEvent.VK_CUT));
        copyAction = new CopyAction("Copy", copyIcon, "Copy stuff to the clipboard", new Integer(KeyEvent.VK_COPY));
        pasteAction = new PasteAction("Paste", pasteIcon, "Paste whatever is on the clipboard", new Integer(KeyEvent.VK_PASTE));
    }

//    private ImageIcon resizeIcon(ImageIcon originIcon, int w, int h) {
//        return null;
//    }

    /**
     * Create a JMenuBar, populating it with our Actions.
     */
    private JMenuBar createMenuBar()
    {
        // create the menubar
        JMenuBar menuBar = new JMenuBar();

        // create our main menu
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");

        // create our menu items, using the same actions the toolbar buttons use
        JMenuItem cutMenuItem = new JMenuItem(cutAction);
        JMenuItem copyMenuItem = new JMenuItem(copyAction);
        JMenuItem pasteMenuItem = new JMenuItem(pasteAction);

        // add the menu items to the Edit menu
        editMenu.add(cutMenuItem);
        editMenu.add(copyMenuItem);
        editMenu.add(pasteMenuItem);

        // add the menus to the menubar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        return menuBar;
    }

    /**
     * Create a JToolBar using our Actions.
     */
    private JToolBar createToolBar()
    {
        // create our toolbar
        JToolBar toolBar = new JToolBar();

        // create our toolbar buttons, using the same actions the menuitems use
        JButton cutButton = new JButton(cutAction);
        JButton copyButton = new JButton(copyAction);
        JButton pasteButton = new JButton(pasteAction);

        // add our buttons to the toolbar
        toolBar.add(cutButton);
        toolBar.add(copyButton);
        toolBar.add(pasteButton);

        return toolBar;
    }

    /**
     * Our "Cut" action.
     */
    public class CutAction extends AbstractAction
    {
        public CutAction(String name, ImageIcon icon, String shortDescription, Integer mnemonic)
        {
            super(name, icon);
            putValue(SHORT_DESCRIPTION, shortDescription);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e)
        {
            JOptionPane.showMessageDialog(null, "Would have done the 'Cut' action.");
        }
    }

    /**
     * Our "Copy" action.
     */
    public class CopyAction extends AbstractAction
    {
        public CopyAction(String name, ImageIcon icon, String shortDescription, Integer mnemonic)
        {
            super(name, icon);
            putValue(SHORT_DESCRIPTION, shortDescription);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e)
        {
            JOptionPane.showMessageDialog(null, "Would have done the 'Copy' action.");
        }
    }

    /**
     * Our "Paste" action.
     */
    public class PasteAction extends AbstractAction
    {
        public PasteAction(String name, ImageIcon icon, String shortDescription, Integer mnemonic)
        {
            super(name, icon);
            putValue(SHORT_DESCRIPTION, shortDescription);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e)
        {
            JOptionPane.showMessageDialog(null, "Would have done the 'Paste' action.");
        }
    }

    private static void createAndShowGUI()
    {
        // create a simple jframe
        JFrame frame = new JFrame("Java AbstractAction Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // construct our panel, including our toolbar
        JavaAbstractActionExample panel = new JavaAbstractActionExample();
        panel.add(panel.createToolBar(), BorderLayout.PAGE_START);
        panel.setOpaque(true);
        frame.setContentPane(panel);

        // add the menubar to the jframe
        frame.setJMenuBar(panel.createMenuBar());

        // show the jframe
        frame.setPreferredSize(new Dimension(2000, 800));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });
    }
}
