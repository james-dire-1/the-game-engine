package newStuff.skybox;

import com.james.renderEngine.texturing.ImageBank;
import com.james.renderEngine.utilities.GLUtilities;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

public class ImageDecoder {

    public final int width;
    public final int height;
    public final IntBuffer rawImageBuffer;

    private ImageDecoder(int width, int height, IntBuffer rawImageBuffer) {
        this.width = width;
        this.height = height;
        this.rawImageBuffer = rawImageBuffer;
    }

    public static ImageDecoder decode(String path) {
        BufferedImage image = ImageBank.get(path);

        int width = image.getWidth();
        int height  = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0x00ff0000) >> 16;
            int g = (pixels[i] & 0x0000ff00) >> 8;
            int b = (pixels[i] & 0x000000ff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        return new ImageDecoder(width, height, GLUtilities.toIntBuffer(data));
    }

}
