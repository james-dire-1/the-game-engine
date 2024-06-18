package com.james.evenNewerCollisionsStuff;

import java.util.ArrayList;
import java.util.List;

public class ModelPreparations {

    private static final List<EllipsoidDimensions> ellipsoidDimensionsBank = new ArrayList<>();

    public static void init() {
        ModelMeshBankInR3.init("res/one-sided-wall5.dae");

        ellipsoidDimensionsBank.add(new EllipsoidDimensions(4, 5, 4));
    }

    public static EllipsoidDimensions getEllipsoidDimensions(float radiusX, float radiusY, float radiusZ) {
        for (EllipsoidDimensions dimensions : ellipsoidDimensionsBank) {
            if (dimensions.isOfRadius(radiusX, radiusY, radiusZ)) {
                return dimensions;
            }
        }

        return null;
    }

}
