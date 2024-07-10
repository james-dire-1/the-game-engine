package com.james.common.networking;

import java.io.Serializable;

// TODO: 2024-07-07 does this need to implement serializable?
public enum PacketType implements Serializable {
    // Server to client
    LEVEL_IS_READY,
    PHYSICAL_OBJECT_ADDED_TO_LEVEL,
    PHYSICAL_OBJECT_MOVED,
    AABB_HITBOX_ADDED,
    LEVEL_SECONDS_PER_GAME_TICK_CHANGED,
    LEVEL_GRAVITY_CHANGED,

    // Client to server
    PLAYER_JOINED,
    PLAYER_MOVED,
    LEVEL_CHANGE_PAUSE_STATE
}
