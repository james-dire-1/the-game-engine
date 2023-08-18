package com.james.renderEngine.textRendering;

/**
 * A container of information relating to each character for a specific font
 */
public class Character {

    public int x;
    public int y;
    public int width;
    public int height;
    public int xOffset;
    public int yOffset;
    public int xAdvance;

    public Character(int x, int y, int width, int height, int xOffset, int yOffset, int xAdvance) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.xAdvance = xAdvance;
    }

    @Override
    public String toString() {
        return "Character{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", xOffset=" + xOffset +
                ", yOffset=" + yOffset +
                ", xAdvance=" + xAdvance +
                '}';
    }
}
