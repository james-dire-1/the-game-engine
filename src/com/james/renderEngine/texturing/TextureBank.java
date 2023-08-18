package com.james.renderEngine.texturing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextureBank {

    private static final Map<String, BufferedImage> bufferedImageMap = new HashMap<>();

    public static void init(String... textureFilePaths) {
        for (String path : textureFilePaths) {
            BufferedImage image = getImage(path);
            bufferedImageMap.put(path, image);
        }
    }

    private static BufferedImage getImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(ImageTexture.class.getResourceAsStream(path));
        } catch (IOException e) {
            System.err.println("Unable to load texture");
        }

        return image;
    }

    public static BufferedImage getTexture(String path) {
        return bufferedImageMap.get(path);
    }

}
