package com.james.renderEngine.particles;

import com.james.renderEngine.particles.dataTypes.*;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ComplexParticleSettings {

    private SortedMap<Float, Vector3fValue> vectorKeyframes;

    private final Map<FloatType, KeyframeType> keyframeTypes = new HashMap<>();
    private final Map<FloatType, SortedMap<Float, FloatValue>> allKeyframeData = new HashMap<FloatType, SortedMap<Float, FloatValue>>() {{
        put(FloatType.Rotation, new TreeMap<>());
        put(FloatType.Scale, new TreeMap<>());
        put(FloatType.GravityMultiplier, new TreeMap<>());
    }};

    public void addChangeKeyframe(float timestamp, FloatType type, float value) {
        addKeyframe(timestamp, type, new ConstantValue(value), KeyframeType.Change);
    }

    public void addChangeKeyframe(float timestamp, FloatType type, float minValue, float maxValue) {
        addKeyframe(timestamp, type, new RandomFloat(minValue, maxValue), KeyframeType.Change);
    }

    public void addTargetKeyframe(float timestamp, FloatType type, float value) {
        addKeyframe(timestamp, type, new ConstantValue(value), KeyframeType.Target);
    }

    public void addTargetKeyframe(float timestamp, FloatType type, float minValue, float maxValue) {
        addKeyframe(timestamp, type, new RandomFloat(minValue, maxValue), KeyframeType.Target);
    }

    public void finalizeParticleSettings(float particleLifetime) {
        finalizeForFloatType(FloatType.Rotation, 0);
        finalizeForFloatType(FloatType.Scale, 1);
        finalizeForFloatType(FloatType.GravityMultiplier, 1);
    }

    private void finalizeForFloatType(FloatType type, float defaultValueForType) {
        SortedMap<Float, FloatValue> keyframeData = allKeyframeData.get(type);
        if (!keyframeData.containsKey(0f)) {
            addKeyframe(0, type, new ConstantValue(defaultValueForType), KeyframeType.Target);
        }
    }

    private void addKeyframe(float timestamp, FloatType type, FloatValue value, KeyframeType keyframeType) {
        if (keyframeTypes.containsKey(type)) {
            if (!keyframeTypes.get(type).equals(keyframeType))
                throw new RuntimeException();
        } else {
            keyframeTypes.put(type, keyframeType);
        }

        allKeyframeData.get(type).put(timestamp, value);
    }

    public KeyframeType extractKeyframeType(FloatType type) {
        return keyframeTypes.get(type);
    }

    public Map<FloatType, KeyframeType> extractKeyframeTypes() {
        return keyframeTypes;
    }

    public Map<FloatType, SortedMap<Float, Float>> extractAllKeyframeDataAndApplyValues() {
        Map<FloatType, SortedMap<Float, Float>> allKeyframeData = new HashMap<>();

        allKeyframeData.put(FloatType.Rotation, extractKeyframeDataAndApplyValues(FloatType.Rotation));
        allKeyframeData.put(FloatType.Scale, extractKeyframeDataAndApplyValues(FloatType.Scale));
        allKeyframeData.put(FloatType.GravityMultiplier, extractKeyframeDataAndApplyValues(FloatType.GravityMultiplier));

        return allKeyframeData;
    }

    private SortedMap<Float, Float> extractKeyframeDataAndApplyValues(FloatType type) {
        SortedMap<Float, FloatValue> keyframeDataWithoutAppliedValues = allKeyframeData.get(type);
        SortedMap<Float, Float> keyframeDataWithAppliedValues = new TreeMap<>();

        for (Map.Entry<Float, FloatValue> entry : keyframeDataWithoutAppliedValues.entrySet()) {
            Float timestamp = entry.getKey();
            FloatValue value = entry.getValue();
            keyframeDataWithAppliedValues.put(timestamp, value.get());
        }

        return keyframeDataWithAppliedValues;
    }

}
