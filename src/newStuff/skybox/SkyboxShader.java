package newStuff.skybox;

import com.james.renderEngine.shaders.Shader;
import org.lwjgl.util.vector.Matrix4f;

public class SkyboxShader extends Shader {

    private static final String VERTEX_FILE = "/newStuff/skybox/skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = "/newStuff/skybox/skyboxFragmentShader.txt";

    private int location_projectionMatrix;
    private int location_viewMatrix;

    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "vertexPosition");
    }

    @Override
    protected void getUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrixToUniform(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Matrix4f matrix, boolean unmoving) {
        Matrix4f toLoad;

        if (unmoving) {
            toLoad = identity;
        } else {
            toLoad = new Matrix4f(matrix);
            toLoad.m30 = 0f;
            toLoad.m31 = 0f;
            toLoad.m32 = 0f;
        }
        super.loadMatrixToUniform(location_viewMatrix, toLoad);
    }

    private static final Matrix4f identity = new Matrix4f();

}
