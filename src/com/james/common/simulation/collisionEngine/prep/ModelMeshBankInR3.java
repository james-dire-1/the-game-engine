package com.james.common.simulation.collisionEngine.prep;

import newStuff.evenNewer.ModelLoader;
import newStuff.evenNewer.SingleMesh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bank of ModelMeshes in R3 local space.
 */
public class ModelMeshBankInR3 {

    private static final Map<String, Map<Integer, ModelMesh>> modelMeshMap = new HashMap<>();

    /**
     * Creates all the ModelMeshes from the given file paths. Should be called at the beginning of the
     * game.
     */
    public static void init(String... modelMeshFilePaths) {
        for (String path : modelMeshFilePaths) {
            ModelLoader modelLoader = ModelLoader.get(path);
            List<SingleMesh> singleMeshes = modelLoader.getAllSingleMeshes();

            for (int i = 0; i < singleMeshes.size(); i++) {
                SingleMesh singleMesh = singleMeshes.get(i);
                ModelMesh modelMesh = new ModelMesh(singleMesh.vertexPositions, singleMesh.indices);

                if (!modelMeshMap.containsKey(path)) {
                    modelMeshMap.put(path, new HashMap<>());
                }

                modelMeshMap.get(path).put(i, modelMesh);
            }
        }
    }

    public static Map<String, Map<Integer, ModelMesh>> getModelMeshMap() {
        return modelMeshMap;
    }

    public static int getNumberOfSubMeshes(String meshPath) {
        return modelMeshMap.get(meshPath).size();
    }

}
