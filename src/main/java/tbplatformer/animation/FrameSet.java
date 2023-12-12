package tbplatformer.animation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/** Set of images source for animation. */
public class FrameSet {

    private BufferedImage[] images;

    public FrameSet(int length, String sourceImageFileName, int width, int height)
            throws IOException {

        BufferedImage sourceImage = ImageIO.read(new File(sourceImageFileName));

        images = new BufferedImage[length];

        if (length == 1) {
            images[0] = sourceImage;

        } else {
            for (int i = 0; i < length; i++) {
                images[i] = sourceImage.getSubimage(i * width + i, 0, width, height);
            }

        }
    }

    public BufferedImage[] getFrames() {
        return images;
    }

}
