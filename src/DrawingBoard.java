import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * Holding left-click draws, and
 * right-clicking cycles the color.
 */
public class DrawingBoard {
    Color[] colors = {Color.red, Color.blue, Color.black};
    int currentColor = 0;
    BufferedImage img = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
    Graphics2D  g2d = img.createGraphics();

    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Creating a copy of the Graphics
            // so any reconfiguration we do on
            // it doesn't interfere with what
            // Swing is doing.
            Graphics2D g2copy = (Graphics2D) g.create();
            int w = img.getWidth();
            int h = img.getHeight();
            g2copy.drawImage(img, 0,0,w,h,null);

            // draw a swatch
            Color color = colors[currentColor];
            g2copy.setColor(color);
            g2copy.fillRect(1,1,20,20);
            g2copy.setColor(Color.BLACK);
            g2copy.drawRect(0,0,21,21);
            // after drawing, we dispose the Graphics copy we created
            g2copy.dispose();
        }
    };

    MouseAdapter drawingPen = new MouseAdapter() {
        Point prev;
        boolean condition = false;

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            if(SwingUtilities.isLeftMouseButton(e)){
                prev = e.getPoint();
            }
            if(SwingUtilities.isRightMouseButton(e) && !condition) {
                condition = true;
                currentColor = (currentColor + 1) % colors.length;
                panel.repaint();
            }
            //clear the board (middle click = 3 fingers)
            if (SwingUtilities.isMiddleMouseButton(e)) {
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, panel.getWidth(), panel.getHeight());
                panel.repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            if (SwingUtilities.isLeftMouseButton(e)){
                prev = null;
            }
            if(SwingUtilities.isRightMouseButton(e)) {
                condition = false;
            }
        }

        @Override
        // from mouseMotionListener
        // draw lines between two points
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if(prev != null) { // this is the time to draw with mouse
                Point next = e.getPoint();
                Color color = colors[currentColor];
                g2d.setColor(color);
                g2d.drawLine(prev.x,prev.y,next.x,next.y);

                // draw any time with mouse control
                panel.repaint();
                prev = next;
            }
        }
    };

    DrawingBoard() {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setStroke(new BasicStroke(3));

        panel.setPreferredSize(new Dimension(img.getWidth(),img.getHeight()));
        panel.setBackground(Color.WHITE);
        panel.addMouseListener(drawingPen);
        panel.addMouseMotionListener(drawingPen);

        // define cursor
        Cursor cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
        panel.setCursor(cursor);

        // attach to container

        JFrame frame = new JFrame("Drawing Board");
        frame.setContentPane(panel);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> new DrawingBoard());
    }
}
