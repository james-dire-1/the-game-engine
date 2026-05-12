package com.james.common.simulation.objects;

/**
 * Used to categorize PhysicalObjects. This is sent to clients when an PhysicalObject gets added to a Level
 * server-side so that the client knows what model (or models) should be used for rendering. Additionally,
 * scene types are included here, since a scene is really just a large PhysicalObject (which has numerous
 * AABBHitboxes).
 */
// TODO: 2025-06-25 This needs to be moved
public enum PhysicalObjectType {
    Wall,
    TestEnvironment,
    Other,
    DesertEnvironment,
    BeachEnvironment
}
