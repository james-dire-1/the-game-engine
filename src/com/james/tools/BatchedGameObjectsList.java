package com.james.tools;

import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.models.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchedGameObjectsList {

    private final Map<Model, List<GameObject>> gameObjectsMap = new HashMap<>();

    private int count;
    public int getCount() { return count; }

    public void addGameObject(GameObject gameObject) {
        count++;

        if (gameObjectsMap.containsKey(gameObject.model)) {
            List<GameObject> batch = gameObjectsMap.get(gameObject.model);
            batch.add(gameObject);
        } else {
            List<GameObject> newBatch = new ArrayList<>();
            newBatch.add(gameObject);
            gameObjectsMap.put(gameObject.model, newBatch);
        }
    }

    public void removeGameObject(GameObject gameObject) {
        count--;

        List<GameObject> batch = gameObjectsMap.get(gameObject.model);
        boolean success = batch.remove(gameObject);

        if (!success)
            throw new RuntimeException("No such game object was found.");

        if (batch.isEmpty()) {
            gameObjectsMap.remove(gameObject.model);
        }
    }

    public Map<Model, List<GameObject>> getGameObjectsMap() {
        return gameObjectsMap;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Batched Collection:\n");
        for (Model model : gameObjectsMap.keySet()) {
            str.append("\tMODEL: ").append(model).append("\n");

            for (GameObject gameObject : gameObjectsMap.get(model)) {
                str.append("\t\tGAME OBJECT: ").append(gameObject).append("\n");
            }
        }
        str.append("----------");

        return str.toString();
    }
}
