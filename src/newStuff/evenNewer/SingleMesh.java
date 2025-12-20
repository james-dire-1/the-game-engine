package newStuff.evenNewer;

public class SingleMesh {

    public final float[] vertexPositions;
    public final float[] textureCoords;
    public final float[] normals;
    public final int[] indices;

    public SingleMesh(float[] vertexPositions, float[] textureCoords, float[] normals, int[] indices) {
        this.vertexPositions = vertexPositions;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;
    }

}