package com.james.renderEngine.texturing;

import com.james.renderEngine.utilities.GLUtilities;
import newStuff.skybox.ImageDecoder;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class ImageTexture {

    public final int id;

    private ImageTexture(String path) {
        this.id = load(path);
    }

    // TODO: 2024-12-24 I heard that it can be beneficial to add some mipmapping to text guis, so consider a way
    // TODO: 2024-12-24 to add mipmapping to this method
    // Some code I got from The Cherno
    private static int load(String path) {
        ImageDecoder imageDecoder = ImageDecoder.decode(path);

        int texId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, imageDecoder.width, imageDecoder.height, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, imageDecoder.rawImageBuffer);
        glBindTexture(GL_TEXTURE_2D, 0);

        GLUtilities.addTextureToList(texId);

        return texId;
    }

    private static final Map<String, ImageTexture> pathToTextureMap = new HashMap<>();

    public static ImageTexture getOrCreate(String path) {
        ImageTexture texture = pathToTextureMap.get(path);

        if (texture == null) {
            texture = new ImageTexture(path);
            pathToTextureMap.put(path, texture);
        }

        return texture;
    }

}