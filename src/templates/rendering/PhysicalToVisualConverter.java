package templates.rendering;

import templates.common.simulation.objects.PhysicalObjectType;
import com.james.renderEngine.models.Model;
import templates.rendering.ModelBank;

/**
 * Provides a way to obtain the model (or models) that corresponds to the given PhysicalObjectType. Serves as a
 * bridge between the server-side physical world and the client-side visual world.
 */
public class PhysicalToVisualConverter {

    /**
     * Called in physicalObjectAddedReceived() of ClientPacketReceiveActions.
     */
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
