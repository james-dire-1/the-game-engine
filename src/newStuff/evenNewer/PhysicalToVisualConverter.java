package newStuff.evenNewer;

import com.james.common.simulation.objects.PhysicalObjectType;
import com.james.renderEngine.models.Model;
import templates.rendering.ModelBank;

public class PhysicalToVisualConverter {

    public static Model[] convert(PhysicalObjectType type) {
        Model[] models = null;

        if (type == PhysicalObjectType.Wall) {
            models = new Model[] { ModelBank.getWall() };
        } else if (type == PhysicalObjectType.TestEnvironment) {
            models = new Model[] { ModelBank.getTestEnvironment() };
        } else if (type == PhysicalObjectType.DesertEnvironment) {
            models = ModelBank.getDesertEnvironmentManyModels();
//            models = new Model[] { ModelBank.getDesertEnvironmentOneModel() };
        } else if (type == PhysicalObjectType.BeachEnvironment) {
            models = ModelBank.getBeachEnvironmentManyModels();
        } else if (type == PhysicalObjectType.Other) {
            models = new Model[] { ModelBank.getColorAbstractArt() };
        }

        return models;
    }

}
