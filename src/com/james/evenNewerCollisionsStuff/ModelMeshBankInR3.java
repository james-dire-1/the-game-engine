package com.james.evenNewerCollisionsStuff;

import com.james.tools.ModelLoader;

import java.util.HashMap;
import java.util.Map;

public class ModelMeshBankInR3 {

    private static final Map<String, ModelMesh> modelMeshMap = new HashMap<>();

    public static void init(String... modelMeshFilePaths) {
        for (String path : modelMeshFilePaths) {
            ModelLoader modelLoader = new ModelLoader(path);
            ModelMesh modelMesh = new ModelMesh(modelLoader.vertexPositions(), modelLoader.indices());
            modelMeshMap.put(path, modelMesh);
        }
    }

    public static ModelMesh getModelMesh(String path) {
        return modelMeshMap.get(path);
    }

    // TODO: 2024-06-16 I'm not really a huge fan of this encapsulation thing. Consider alternatives?
    public static Map<String, ModelMesh> getModelMeshMap() {
        return modelMeshMap;
    }

}
