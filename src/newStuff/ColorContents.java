package newStuff;

public class ColorContents {

    public final boolean blend;
    public final int startColorIndex;
    public int endColorIndex;
    public float progress;

    public ColorContents(int startColorIndex, int endColorIndex, float progress) {
        this.blend = true;
        this.startColorIndex = startColorIndex;
        this.endColorIndex = endColorIndex;
        this.progress = progress;
    }

    public ColorContents(int color) {
        this.blend = false;
        this.startColorIndex = color;
    }

}
