import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * demonstrate the loading of image files into icons for use in a SwingUI.
 * It creates a toolbar with a thumbnail preview of each image.
 * Clicking on the thumbnail will show the full image
 * in the main display area.
 *
 * **todo: **
 * 1. first loaded with a picture in the JPanel
 * 2. center the pictures in case they are smaller than the space assigned.
 */
public class IconDemoApp extends JFrame {

    private JLabel photographLabel = new JLabel();
    private JToolBar buttonBar = new JToolBar();

    private String imagedir = "images/";

    private MissingIcon placeholderIcon = new MissingIcon();

    /**
     * List of all the descriptions of the image files. These correspond one to
     * one with the image file names
     */
    private String[] imageCaptions = { "First Baby", "Second baby",
            "Clocktower from the West", "The Mansion", "Sun Auditorium"};

    /**
     * List of all the image files to load.
     */
    private String[] imageFileNames = { "sunw01.jpg", "sunw02.jpg",
            "sunw03.jpg", "sunw006.jpg", "sunw05.jpg"};

    /**
     * Main entry point to the demo. Loads the Swing elements on the "Event
     * Dispatch Thread".
     *
     * @param args
     */
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                IconDemoApp app = new IconDemoApp();
                app.setVisible(true);
            }
        });
    }

    /**
     * Default constructor for the demo.
     */
    public IconDemoApp() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Icon Demo: Please Select an Image");

        // A label for displaying the pictures
//        photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
//        photographLabel.setHorizontalTextPosition(JLabel.CENTER);
//        photographLabel.setHorizontalAlignment(JLabel.CENTER);
        photographLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // We add two glue components. Later in process() we will add thumbnail buttons
        // to the toolbar in between these glue components. This will center the
        // buttons in the toolbar.
        buttonBar.add(Box.createGlue());
        buttonBar.add(Box.createGlue());

        add(buttonBar, BorderLayout.SOUTH);
        add(photographLabel, BorderLayout.CENTER);

        setSize(600, 400);

        // this centers the frame on the screen
        setLocationRelativeTo(null);

        // start the image loading SwingWorker in a background thread
        loadimages.execute();
    }

    /**
     * Annonymouse inner class for simple SwingWorker object
     * SwingWorker class that loads the images a background thread and calls publish
     * when a new one is ready to be displayed.
     *
     * We use Void as the first SwingWroker param as we do not need to return
     * anything from doInBackground().
     */
    private SwingWorker<Void, ThumbnailAction> loadimages = new SwingWorker<Void, ThumbnailAction>() {

        /**
         * Creates full size and thumbnail versions of the target image files.
         */
        @Override
        protected Void doInBackground() throws Exception {
            for (int i = 0; i < imageCaptions.length; i++) {
                ImageIcon icon;
                icon = createImageIcon(imagedir + imageFileNames[i], imageCaptions[i]);

                ThumbnailAction thumbAction;
                if(icon != null){

                    ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(icon.getImage(), 32, 32));

                    thumbAction = new ThumbnailAction(icon, thumbnailIcon, imageCaptions[i]);

                }else{
                    // the image failed to load for some reason
                    // so load a placeholder instead
                    thumbAction = new ThumbnailAction(placeholderIcon, placeholderIcon, imageCaptions[i]);
                }
                publish(thumbAction); // this method invoke process() from event dispatch thread
            }
            // unfortunately we must return something, and only null is valid to
            // return when the return type is void.
            return null;
        }

        /**
         * invoked by publish() from SwingWorker thread
         * Process all loaded images.
         */
        @Override
        protected void process(List<ThumbnailAction> chunks) {
            for (ThumbnailAction thumbAction : chunks) {
                System.out.println(chunks.size());
                JButton thumbButton = new JButton(thumbAction);
                // add the new button BEFORE the last glue
                // this centers the buttons in the toolbar
                buttonBar.add(thumbButton, buttonBar.getComponentCount() - 1);

            }
        }
    };

    protected ImageIcon createImageIcon(String path,
                                        String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    /**
     * Action class is used to seperate functionality from its component,
     * that shows the image specified in it's constructor.
     */
    private class ThumbnailAction extends AbstractAction{

        /**
         *The icon if the full image we want to display.
         */
        private Icon displayPhoto;

        public ThumbnailAction(Icon photo, Icon thumb, String desc){
            displayPhoto = photo;

            // The short description becomes the tooltip of a button.
            putValue(SHORT_DESCRIPTION, desc);

            // The LARGE_ICON_KEY is the key for setting the
            // icon when an Action is applied to a button.
            putValue(LARGE_ICON_KEY, thumb);
        }

        /**
         * Shows the full image in the main area and sets the application title.
         */
        public void actionPerformed(ActionEvent e) {
            photographLabel.setIcon(displayPhoto);
            setTitle("Icon Demo: " + getValue(SHORT_DESCRIPTION).toString());
        }
    }
}
