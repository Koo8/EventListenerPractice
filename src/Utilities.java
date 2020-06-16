import java.awt.*;
import java.awt.image.BufferedImage;

public class Utilities {
    public static Image resizeIcon(Image oriIcon, int w, int h) {

        BufferedImage resizedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(oriIcon,0,0,w, h,null);
        g2d.dispose();
        return resizedImage;
    }
}

