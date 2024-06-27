package com.james.renderEngine.models;

// TODO: 2024-06-20 consider changing the package that this is in
public class ModelFactory {

    private static Model cube;
    private static Model verticalPlane;
    private static Model gui;

    public static Model getCube() {
        if (cube == null) {
            float[] vertexPositions = {
                    -0.5f, 0.5f, 0.5f, // front top left 0
                    0.5f, 0.5f, 0.5f, // front top right 1
                    -0.5f, -0.5f, 0.5f, // front bottom left 2
                    0.5f, -0.5f, 0.5f, // front bottom right 3
                    -0.5f, 0.5f, -0.5f, // back top left 4
                    0.5f, 0.5f, -0.5f, // back top right 5
                    -0.5f, -0.5f, -0.5f, // back bottom left 6
                    0.5f, -0.5f, -0.5f // back bottom right 7
            };

            int[] indices = {
                    0, 2, 1, 1, 2, 3, // front face
                    4, 0, 5, 5, 0, 1, // top face
                    1, 3, 5, 5, 3, 7, // right face
                    4, 6, 0, 0, 6, 2, // left face
                    5, 7, 4, 4, 7, 6, // back face
                    2, 6, 3, 3, 6, 7 // bottom face
            };

            cube = new Model(vertexPositions, indices);
        }

        return cube;
    }

    public static Model getVerticalPlane() {
        if (verticalPlane == null) {
            float[] vertexPositions = {
                    -0.5f, 0.5f, 0,
                    -0.5f, -0.5f, 0,
                    0.5f, 0.5f, 0,
                    0.5f, -0.5f, 0
            };

            int[] indices = {
                    0, 1, 2, 2, 1, 3
            };

            verticalPlane = new Model(vertexPositions, indices);
        }

        return verticalPlane;
    }

}
