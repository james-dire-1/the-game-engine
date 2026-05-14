package com.james.renderEngine.texturing;

import com.james.renderEngine.utilities.GLUtilities;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

/**
 * Class that handles extracting the necessary info from an image file that is needed for creating OpenGL
 * textures. This info is stored in this class's fields for ease of retrieval after. Used by both ImageTexture
 * and CubeMapTexture.
 */
public class ImageDecoder {

    public final int width;
    public final int height;
    public final IntBuffer rawImageBuffer;

    /**
     * Constructor used internally for populating the fields that will be referred to when creating OpenGL
     * textures. Determining what these fields should be is done in decode() below.
     */
    private ImageDecoder(int width, int height, IntBuffer rawImageBuffer) {
        this.width = width;
        this.height = height;
        this.rawImageBuffer = rawImageBuffer;
    }

    /**
     * Does the heavy lifting for turning an image file into the fields we need for OpenGL textures. Pretty
     * much all the code in here is from The Cherno.
     */
    public static ImageDecoder decode(String path) {
        BufferedImage image = ImageBank.get(path);

        if (image == null) {
            System.out.println("Could not decode image " + path + "; was not buffered");
            throw new RuntimeException();
        }

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
