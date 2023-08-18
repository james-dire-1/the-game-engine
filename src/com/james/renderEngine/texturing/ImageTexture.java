package com.james.renderEngine.texturing;

import com.james.renderEngine.utilities.GLUtilities;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class ImageTexture {

    public final int id;

    private ImageTexture(String path) {
        this.id = load(TextureBank.getTexture(path));
    }

    // Some code I got from The Cherno
    private static int load(BufferedImage image) {
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

        int texId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                GLUtilities.toIntBuffer(data));
        glBindTexture(GL_TEXTURE_2D, 0);

        GLUtilities.addTextureToList(texId);

        return texId;
    }

    private static final Map<String, ImageTexture> pathToTextureMap = new HashMap<>();

    public static ImageTexture getOrCreateImageTexture(String path) {
        ImageTexture texture = pathToTextureMap.get(path);

        if (texture != null) {
            return texture;
        }

        texture = new ImageTexture(path);
        pathToTextureMap.put(path, texture);

        return texture;
    }

}