package newStuff.evenNewer;

import com.james.common.tools.ModelLoader;

public class TestingModelLoader {

    public static void main(String[] args) {
        ModelLoader.init("/stall.obj", "/abstract-art.dae", "/one-sided-wall.dae", "/test-environment.dae", "/desert.dae");
    }

}
