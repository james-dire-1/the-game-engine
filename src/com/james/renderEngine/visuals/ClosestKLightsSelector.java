package com.james.renderEngine.visuals;

import com.james.renderEngine.gameObjects.Light;
import org.lwjgl.util.vector.Vector3f;

public class ClosestKLightsSelector {

    private Light[] closestLights;
    private float[] smallestDistances;
    private float biggestDistance;
    private int indexOfBiggestDistance;

    private int k;

    public ClosestKLightsSelector(int k) {
        this.k = k;
        this.closestLights = new Light[k];
        this.smallestDistances = new float[k];
    }

    public void setK(int k) {
        if (this.k != k) {
            this.k = k;
            this.closestLights = new Light[k];
            this.smallestDistances = new float[k];
        }
    }

    public Light[] getClosestLights() {
        return closestLights;
    }

    public void determineClosest(Light[] allLights, Vector3f object) {
        if (allLights.length <= k) {
            System.arraycopy(allLights, 0, closestLights, 0, allLights.length);
            return;
        }

        for (int i = 0; i < k; i++) {
            Light currentLight = allLights[i];
            closestLights[i] = currentLight;
            smallestDistances[i] = squaredDistance(currentLight, object);
            determineBiggestValueOfSmallestValues();
        }

        for (int i = k; i < allLights.length; i++) {
            Light currentLight = allLights[i];
            float currentDistance = squaredDistance(currentLight, object);

            if (currentDistance < biggestDistance) {
                closestLights[indexOfBiggestDistance] = currentLight;
                smallestDistances[indexOfBiggestDistance] = currentDistance;
                determineBiggestValueOfSmallestValues();
            }
        }
    }

    private float squaredDistance(Light light, Vector3f objectPosition) {
        Vector3f lightPosition = light.getPosition();
        float dx = lightPosition.x - objectPosition.x;
        float dz = lightPosition.z - objectPosition.z;

        return dx * dx + dz * dz;
    }

    private void determineBiggestValueOfSmallestValues() {
        biggestDistance = smallestDistances[0];
        indexOfBiggestDistance = 0;

        for (int i = 1; i < k; i++) {
            float currentValue = smallestDistances[i];

            if (currentValue < biggestDistance) {
                biggestDistance = currentValue;
                indexOfBiggestDistance = i;
            }
        }
    }

}
