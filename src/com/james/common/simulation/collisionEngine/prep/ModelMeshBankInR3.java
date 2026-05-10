package com.james.common.simulation.collisionEngine.prep;

import newStuff.evenNewer.ModelLoader;
import newStuff.evenNewer.SingleMesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bank of ModelMeshes in R3 local space.
 */
public class ModelMeshBankInR3 {

    private static final Map<String, List<ModelMesh>> modelMeshMap = new HashMap<>();

    /**
     * Creates all the ModelMeshes from the given file paths. Should be called at the beginning of the game.
     */
    public static void init(String... modelMeshFilePaths) {
        for (String path : modelMeshFilePaths) {
            ModelLoader modelLoader = ModelLoader.get(path);
            List<SingleMesh> singleMeshes = modelLoader.getAllSingleMeshes();

            for (SingleMesh singleMesh : singleMeshes) {
                ModelMesh modelMesh = new ModelMesh(singleMesh.vertexPositions, singleMesh.indices);

                if (!modelMeshMap.containsKey(path)) {
                    modelMeshMap.put(path, new ArrayList<>());
                }

                modelMeshMap.get(path).add(modelMesh);
            }
        }
    }

    public static Map<String, List<ModelMesh>> getModelMeshMap() {
        return modelMeshMap;
    }

    public static int getNumberOfSubMeshes(String meshPath) {
        return modelMeshMap.get(meshPath).size();
    }

}
