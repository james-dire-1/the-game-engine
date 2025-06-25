package com.james.renderEngine.textRendering.dataTypes;

import java.util.Random;

/**
 * A container of information relating to each character for a specific font
 */
public class Character {

    public final int id;
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final int xOffset;
    public final int yOffset;
    public final int xAdvance;

    /**
     * Creates a new character. Necessary corrections for padding values are NOT taken into consideration here.
     * All the values here are measured in pixels in accordance with the texture atlas. No normalized values.
     */
    public Character(int id, int x, int y, int width, int height, int xOffset, int yOffset, int xAdvance) {
        this.id = id;
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
