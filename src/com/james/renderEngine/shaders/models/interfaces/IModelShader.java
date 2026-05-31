package com.james.renderEngine.shaders.models.interfaces;

import com.james.renderEngine.shaders.models.components.ModelShaderFeature;
import org.lwjgl.util.vector.Matrix4f;

public interface IModelShader {

    ModelShaderFeature model();

    default void loadTransformationMatrix(Matrix4f matrix) {
        model().loadTransformationMatrix(matrix);
    }

    default void loadProjectionMatrix(Matrix4f matrix) {
        model().loadProjectionMatrix(matrix);
    }

    default void loadViewMatrix(Matrix4f matrix) {
        model().loadViewMatrix(matrix);
    }

}
