import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.EventHandler;

public class MyMultiListener extends JPanel implements ActionListener {

    // fields
    private JTextArea topTextArea, bottomTextArea;
    //private JLabel firstLabel, secondLabel; // create one JLabel and reuse it inside the constructor
    private JButton firstButton, secondButton;
    private int counter = 0;

    // constructor
    public MyMultiListener() {

        // // // draw Swing component
        super(new GridBagLayout());

        setPreferredSize(new Dimension(400,450));
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5,5,5,5,Color.BLUE), BorderFactory.createEmptyBorder(5,5,5,5)));
       // setPreferredSize(new Dimension(400,450));
        GridBagLayout gridbag = (GridBagLayout) getLayout(); // highlight: not new GridBagLayout(), not to make a new layout, but to assign it to the built-in layout.
        GridBagConstraints c = new GridBagConstraints(); // reuse

        // create a JLabel and reuse it
        JLabel l = null;

        c.fill = GridBagConstraints.BOTH; // this makes all components take the most space in its designated area.
        c.gridwidth = GridBagConstraints.REMAINDER; // this makes all components take the full length of the width of the pane

        l= new JLabel("What MultiListener hear...");
        gridbag.setConstraints(l, c);
        add(l);

        c.weighty = 1;
        topTextArea = new JTextArea();
        topTextArea.setEditable(false);
        JScrollPane topScrollPane = new JScrollPane(topTextArea);
       // topScrollPane.setPreferredSize(new Dimension(200, 75));
        gridbag.setConstraints(topScrollPane, c);
        add(topScrollPane);

        c.weighty = 0;
        l = new JLabel("I am here");
        gridbag.setConstraints(l, c);
        add(l);

        c.weighty =1;
        bottomTextArea = new JTextArea();
        bottomTextArea.setEditable(false);
        JScrollPane bottomScrollPane = new JScrollPane(bottomTextArea);
        gridbag.setConstraints(bottomScrollPane,c);
        add(bottomScrollPane);

        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth =1; // changed from REMAINDER
        c.insets = new Insets(10, 10,0 , 10);
        firstButton = new JButton("First Button");
        firstButton.setPreferredSize(new Dimension(150, 30));
        gridbag.setConstraints(firstButton,c);
        add(firstButton);

        secondButton = new JButton("Second Button");
        secondButton.setPreferredSize(new Dimension(150, 30));
        secondButton.setActionCommand("My new command");
        gridbag.setConstraints(secondButton,c);
        add(secondButton);

        // add listener
        firstButton.addActionListener(this);
        secondButton.addActionListener(this);
        secondButton.addActionListener(new OtherListener(bottomTextArea));
        //todo: I haven't figure out EventHandler.create may do small event work
       // secondButton.addActionListener(EventHandler.create(ActionListener.class,bottomTextArea,"append","test test"));

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        topTextArea.append(e.getActionCommand() + " " + counter  +"\n");
        counter++;
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new MyMultiListener());
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }

        });
    }
}
//recommended to choose to implement separate classes for different kinds of event listeners
//implement your event listeners in a class that is not public, but somewhere more hidden
class OtherListener implements ActionListener{
    JTextArea a;
    public OtherListener(JTextArea a) {
        this.a = a;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        a.append(e.getActionCommand()+ "\n");
    }
}
