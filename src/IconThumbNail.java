import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

/**
 * create a toolbar with a thumbnail preview of each image
 * load image file into icons
 * click on thumbnail will show full image
 */

public class IconThumbNail extends JFrame {
    private JLabel photoLabel = new JLabel();
    private JToolBar iconBar = new JToolBar();
    private String[] imageFileNames = { "sunw01.jpg", "sunw02.jpg",
            "sunw03.jpg", "sunw006.jpg", "sunw05.jpg"};
    private String[] imageCaptions = { "First Baby", "Second baby",
            "Clocktower from the West", "The Mansion", "Sun Auditorium"};
    private MissingIcon  placeHolder = new MissingIcon();

    // constructor
    IconThumbNail() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Click Image Icon to See Large Picture");
        setSize(600,500);
        setLocationRelativeTo(null);

        add(photoLabel, BorderLayout.CENTER);
        add(iconBar,BorderLayout.SOUTH);

        iconBar.add(Box.createGlue());
        iconBar.add(Box.createGlue());
       // System.out.println(iconBar.getComponentCount()); // 2
        // use SwingWorker to download images in the background thread
        swingWorkerThread.execute();
    }
    // anonymous inner class
    // the following line is to declare the required types, Void is for DoInBackground(), ThumbAction is for process() type
    //private SwingWorker<Void, ThumbAction> loadimages = new SwingWorker<Void, ThumbAction>() {
    SwingWorker swingWorkerThread = new SwingWorker() {
        @Override
        protected Object doInBackground() throws Exception {
            for (int i = 0; i <imageFileNames.length ; i++) {
                // can't use the following new ImageIcon method, because it won't distinquish the good file with no_found file.
               // ImageIcon bigIcon = new ImageIcon("src/images/" + imageFileNames[i]);
               // System.out.println(bigIcon);

                ImageIcon bigIcon =createImageIcon("images/" + imageFileNames[i], imageCaptions[i]);
                // two possibilities - bigIcon is valid, or not valid
                ThumbAction action;
                if (bigIcon != null) {
                    ImageIcon smallIcon = new ImageIcon(resizeIcon(bigIcon.getImage(), 25,25));
                    action = new ThumbAction(bigIcon,smallIcon,imageCaptions[i]);
                } else {
                    action = new ThumbAction(placeHolder,placeHolder,imageCaptions[i]);
                }
                publish(action);
            }

            return null;
        }

        @Override
        protected void process(List chunks) {
            super.process(chunks);
            for(Object action: chunks) {
                JButton button = new JButton((Action) action);
                //add new button to the front of the last  glue -- to center the buttons in the bar
                iconBar.add(button,iconBar.getComponentCount()-1);
            }
        }
    };

    private Image resizeIcon(Image oriIcon, int w, int h) {

        BufferedImage resizedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(oriIcon,0,0,w, h,null);
        g2d.dispose();
        return resizedImage;
    }
// this method is a standard method on oracle doc website
    private ImageIcon createImageIcon(String path,
                                        String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

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
        /**
         * This action is called by ooJButtonoo
         * 1. Large_Icon_key to smallIcon
         * 2. SHort_description to descriptions
         * 3. when trigger, set bigIcon to the label
         */
        private Icon displayIcon;
        // define putValue inside the constructor
        public ThumbAction(Icon bigIcon, Icon thumbnail, String desc) {
            displayIcon = bigIcon;
            putValue(SHORT_DESCRIPTION, desc);
            putValue(LARGE_ICON_KEY, thumbnail);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            photoLabel.setIcon(displayIcon);
            photoLabel.setHorizontalAlignment(JLabel.CENTER);
            setTitle("Name: " + getValue(SHORT_DESCRIPTION).toString());
        }
    }
}
