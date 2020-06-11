import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class PaintImageIcon extends JPanel {

    private  ImageIcon icon;
    // Constructor
    public PaintImageIcon() {
        // define icon property
        icon = new ImageIcon("src/Bird.gif");
        int w = icon.getIconWidth();

        int h = icon.getIconHeight();
        // set size
        setPreferredSize(new Dimension(w, h));
    }
    // highlight: paintComponent to paint any compoent, Graphics doesn't need to be declared
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        icon.paintIcon(this,g,0,0);
    }

    public static void createAndShowGUI() {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setContentPane(new PaintImageIcon());
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PaintImageIcon::createAndShowGUI);
    }


}
