package com.james.renderEngine.rendering;

import com.james.renderEngine.models.Model;
import com.james.tools.BatchedGameObjectsList;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRenderer {

    public final List<Model> models = new ArrayList<>();
    public abstract void prepare();
    public abstract void render(BatchedGameObjectsList batchedGameObjectsList);
    public abstract boolean satisfiesModelCriteria(Model model);
    public abstract void cleanUp();

}
