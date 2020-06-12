import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * create a toolbar with a thumbnail preview of each image
 * load image file into icons
 * click on thumbnail will show full image
 */

public class IconThumbNail extends JFrame {
    private JLabel photoLabel = new JLabel();
    private JToolBar iconBar = new JToolBar();

    // constructor
    IconThumbNail() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Click Image Icon to See Large Picture");
        setSize(600,500);
        setLocationRelativeTo(null);

        add(photoLabel, BorderLayout.CENTER);
        add(iconBar,BorderLayout.SOUTH);

        // use SwingWorker to download images in the background thread
//        swingWorkerThread.execute();
    }
    // anonymous inner class
//    SwingWorker<Void, ThumbAction> swingWorkerThread = new SwingWorker<Void, ThumbAction>() {
//        @Override
//        protected Void doInBackground() throws Exception {
//            ThumbAction thumbAction;
//            return thumbAction;
//        }
//    };
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                IconThumbNail iconThumbNail = new IconThumbNail();
                iconThumbNail.setVisible(true);
            }
        });
    }

    private class ThumbAction extends AbstractAction{
        ImageIcon displayIcon;

        ThumbAction() {

        }
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
