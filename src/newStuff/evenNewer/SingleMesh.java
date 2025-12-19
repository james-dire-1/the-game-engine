package newStuff.evenNewer;

public class SingleMesh {

    public final float[] vertexPositions;
    public final float[] textureCoords;
    public final float[] normals;
    public final int[] indices;

    public SingleMesh(float[] vertexPositions, float[] textureCoords, float[] normals, int[] indices) {
        System.out.println("Another mesh was created");

        this.vertexPositions = vertexPositions;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;

        System.out.println("vertexPositions.length = " + vertexPositions.length/3);
        System.out.println("textureCoords.length = " + textureCoords.length/2);
        System.out.println("normals.length = " + normals.length/3);
        System.out.println("indices.length = " + indices.length);
    }

}