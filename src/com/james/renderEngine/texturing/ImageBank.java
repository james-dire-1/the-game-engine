package com.james.renderEngine.texturing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageBank {

    private static final Map<String, BufferedImage> bufferedImageMap = new HashMap<>();

    public static void init(String... imageFilePaths) {
        for (String path : imageFilePaths) {
            BufferedImage image = getImage(path);
            bufferedImageMap.put(path, image);
        }
    }

    private static BufferedImage getImage(String path) {
        BufferedImage image = null;
        try {
            InputStream inputStream = ImageBank.class.getResourceAsStream(path);
            if (inputStream == null) {
                System.out.println("Could not buffer image " + path);
                throw new RuntimeException();
            }

            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            System.err.println("Unable to load image");
        }

        return image;
    }

    public static BufferedImage get(String path) {
        return bufferedImageMap.get(path);
    }

}
