import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class ClicRadioButtonForPIcture extends JPanel implements ActionListener {
    String bird = "Bird", cat = "Cat", dog = "Dog", rabbit = "Rabbit", pig = "Pig";
    JLabel jLabelPic;

    public ClicRadioButtonForPIcture() {

        // super is needed oo without this, the radio buttons will clump together in the middle.
        super(new BorderLayout());
        // add radio buttons

        JRadioButton birdBtn = new JRadioButton(bird);
        birdBtn.setMnemonic(KeyEvent.VK_B);
        birdBtn.setActionCommand(bird);
        birdBtn.setSelected(true);

        JRadioButton catBtn = new JRadioButton(cat);
        catBtn.setMnemonic(KeyEvent.VK_C);
        catBtn.setActionCommand(cat);

        JRadioButton dogBtn = new JRadioButton(dog);
        dogBtn.setMnemonic(KeyEvent.VK_D);
        dogBtn.setActionCommand(dog);

        JRadioButton rabbitBtn = new JRadioButton(rabbit);
        rabbitBtn.setMnemonic(KeyEvent.VK_R);
        rabbitBtn.setActionCommand(rabbit);

        JRadioButton pigBtn = new JRadioButton(pig);
        pigBtn.setMnemonic(KeyEvent.VK_P);
        pigBtn.setActionCommand(pig);

        // ButtonGroup
        ButtonGroup bg = new ButtonGroup();
        bg.add(pigBtn);
        bg.add(birdBtn);
        bg.add(catBtn);
        bg.add(rabbitBtn);
        bg.add(dogBtn);

        // create a radioPanel to hold all radioButtons
        JPanel radioPanel = new JPanel(new GridLayout(0, 1)); // row 0 to hava vertical line display
        radioPanel.add(birdBtn);
        radioPanel.add(catBtn);
        radioPanel.add(dogBtn);
        radioPanel.add(rabbitBtn);
        radioPanel.add(pigBtn);

        // add listener
        rabbitBtn.addActionListener(this);
        rabbitBtn.addActionListener(this);
        rabbitBtn.addActionListener(this);
        rabbitBtn.addActionListener(this);
        rabbitBtn.addActionListener(this);

        // add JLabelPic
        //method 1 createImageIcon -- not sure not working
        ImageIcon imageIcons = createImageIcon("Bird.gif");


        // method 2 use URL
//        url = getClass().getResource("Bird.gif");
//        ImageIcon imageIcon = new ImageIcon(url,"a description");
        // method 3
//        ImageIcon imageIcon = new ImageIcon("src/Bird.gif");
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("Bird.gif"));
        jLabelPic = new JLabel(imageIcon);
        jLabelPic.setPreferredSize(new Dimension(imageIcon.getIconWidth(),imageIcon.getIconHeight()));

        add(radioPanel, BorderLayout.LINE_START);
        add(jLabelPic,BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }
    // create this method. it is not built-in
    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        jLabelPic.setIcon(createImageIcon(
                e.getActionCommand()
                        + ".gif"));
    }

    public static void creatGUI() {
        JFrame frame = new JFrame("Clic to see pictures");

        JComponent contentPane = new ClicRadioButtonForPIcture();
        // contentPane.setOpaque(true);
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClicRadioButtonForPIcture::creatGUI);
    }
}
