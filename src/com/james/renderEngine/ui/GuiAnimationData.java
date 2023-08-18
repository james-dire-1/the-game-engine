package com.james.renderEngine.ui;

import com.james.tools.Time;

import java.util.function.Function;

/**
 * A container class representing animation data for guis. This class includes information for the attribute
 * you wish to animate, the math function you wish to blend between, the start time of the animation, the
 * duration of the animation, the two keyframe values, and whether you want to repeat the animation or not.
 */
public class GuiAnimationData {

    public final Attribute attribute;
    public final Function<Float, Float> mathFunction;
    public final float startTime;
    public final float animationLength;
    public final float startValue;
    public final float endValue;
    public final boolean repeat;

    public GuiAnimationData(Attribute attribute, Function<Float, Float> mathFunction, float delay,
                            float animationLength, float startValue, float endValue, boolean repeat) {
        this.attribute = attribute;
        this.mathFunction = mathFunction;
        this.startTime = Time.getCurrentTime() + delay;
        this.animationLength = animationLength;
        this.startValue = startValue;
        this.endValue = endValue;
        this.repeat = repeat;
    }

    public enum Attribute {
        PositionX,
        PositionY
    }

}
