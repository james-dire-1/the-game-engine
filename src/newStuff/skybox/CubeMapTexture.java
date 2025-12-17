package newStuff.skybox;

import com.james.renderEngine.utilities.GLUtilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL13.*;

/**
 * Represents a cube map texture. Wrapper around an OpenGL texture id.
 */
public class CubeMapTexture {

    private static final int NUM_FACES = 6;

    public final int id;

    /**
     * From the six image file paths, creates a cube map texture and stores the texture id.
     */
    private CubeMapTexture(String[] paths) {
        if (paths.length != 6)
            throw new RuntimeException();

        this.id = load(paths);
    }

    /**
     * Does all the heavy lifting of creating an OpenGL cube map texture from six image file paths. Uses the
     * ImageDecoder.
     */
    private static int load(String[] paths) {
        int texId = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, texId);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            ImageDecoder imageDecoder = ImageDecoder.decode(path);

            int face = GL_TEXTURE_CUBE_MAP_POSITIVE_X + i;
            glTexImage2D(face, 0, GL_RGBA, imageDecoder.width, imageDecoder.height, 0, GL_RGBA,
                    GL_UNSIGNED_BYTE, imageDecoder.rawImageBuffer);
        }

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
        GLUtilities.addTextureToList(texId);

        return texId;
    }

    private static final Map<String, CubeMapTexture> nameToTextureMap = new HashMap<>();

    /**
     * Creates a CubeMapTexture object and stores it in the map with an associated name for ease of retrieval
     * later. This method is specifically for creating a cube map texture from six distinct image files. Note
     * that no extension should be used for the fileNames.
     */
    public static void create(String name, String root, String[] fileNames) {
        String[] filePaths = new String[fileNames.length];

        for (int i = 0; i < fileNames.length; i++) {
            String filePath = root + "/" + fileNames[i] + ".png";
            filePaths[i] = filePath;
        }

        CubeMapTexture texture = new CubeMapTexture(filePaths);
        nameToTextureMap.put(name, texture);
    }

    /**
     * Creates a CubeMapTexture object and stores it in the map with an associated name for ease of retrieval
     * later. This method is specifically for creating a cube map texture from one image file only (all six
     * faces will use that image). Note that an extension should be used for the singleFilePath.
     */
    public static void create(String name, String singleFilePath) {
        String[] filePaths = new String[NUM_FACES];
        Arrays.fill(filePaths, singleFilePath);

        CubeMapTexture texture = new CubeMapTexture(filePaths);
        nameToTextureMap.put(name, texture);
    }

    /**
     * Retrieves a CubeMapTexture from its associated name.
     */
    public static CubeMapTexture get(String name) {
        return nameToTextureMap.get(name);
    }

}
