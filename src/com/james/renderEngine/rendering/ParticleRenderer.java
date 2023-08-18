package com.james.renderEngine.rendering;

import com.james.input.WindowResizeInput;
import com.james.math.Mth;
import com.james.renderEngine.particles.Particle;
import com.james.renderEngine.particles.ParticleHandler;
import com.james.renderEngine.shaders.ParticleShader;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class ParticleRenderer {

    private static final ParticleShader shader = new ParticleShader();

    public static void prepare() {
        shader.start();
        Matrix4f projectionMatrix = Mth.createProjectionMatrix(WindowResizeInput.width, WindowResizeInput.height);
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public static void render(List<Particle> particles) {
        shader.start();

        glBindVertexArray(ParticleHandler.getVaoId());
        glEnableVertexAttribArray(0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(false);

        for (Particle particle : particles) {
            Matrix4f viewModelMatrix = createViewModelMatrix(particle.position, particle.rotation, particle.scale);
            shader.loadViewModelMatrix(viewModelMatrix);

            glDrawElements(GL_TRIANGLES, ParticleHandler.getVertexCount(), GL_UNSIGNED_INT, 0);
        }

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        glDepthMask(true);
        glDisable(GL_BLEND);

        shader.stop();
    }

    private static Matrix4f createViewModelMatrix(Vector3f position, float rotation, float scale) {
        Matrix4f modelMatrix = new Matrix4f();
        Matrix4f.translate(position, modelMatrix, modelMatrix);
        Matrix4f viewMatrix = MasterRenderer.currentCamera.getViewMatrix();

        modelMatrix.m00 = viewMatrix.m00;
        modelMatrix.m01 = viewMatrix.m10;
        modelMatrix.m02 = viewMatrix.m20;
        modelMatrix.m10 = viewMatrix.m01;
        modelMatrix.m11 = viewMatrix.m11;
        modelMatrix.m12 = viewMatrix.m21;
        modelMatrix.m20 = viewMatrix.m02;
        modelMatrix.m21 = viewMatrix.m12;
        modelMatrix.m22 = viewMatrix.m22;

        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);

        return Matrix4f.mul(viewMatrix, modelMatrix, null);
    }

    public static void cleanUp() {
        shader.cleanUp();
    }

}
