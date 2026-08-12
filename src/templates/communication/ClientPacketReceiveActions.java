package templates.communication;

import com.james.renderEngine.gameObjects.DirectionalLight;
import com.james.renderEngine.gameObjects.Light;
import com.james.renderEngine.visuals.Skybox;
import com.james.audio.PositionalAudioMaster;
import com.james.tools.LightHandler;
import templates.audio.SoundToPathConverter;
import templates.common.audio.Sound;
import templates.common.simulation.objects.PhysicalObjectType;
import com.james.renderEngine.ui.Screen;
import com.james.serverSide.LevelInitializer;
import com.james.simulation.collisionEngine.hitboxes.CachedAABBHitbox;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.renderEngine.models.Model;
import com.james.simulation.objects.CachedPhysicalObject;
import com.james.simulation.objects.CachedConnectedPlayer;
import com.james.tools.ColorUtils;
import com.james.tools.Time;
import game.main.Main;
import game.ui.screens.ChatScreen;
import newStuff.rayStuffOnHold.GeneralSphereHitbox;
import game.ui.screens.UsernamePromptScreen;
import templates.rendering.PhysicalToVisualConverter;
import templates.gameplay.GameLoader;
import templates.gameplay.LocalGameLoader;
import templates.gameplay.OnlineGameLoader;
import templates.gameplay.PlayerHandler;
import templates.rendering.ModelBank;
import com.james.simulation.ClientLevel;
import org.lwjgl.util.vector.Vector3f;
import templates.rendering.Skyboxes;

import static templates.common.GlobalConstants.IS_DETAILED_NETWORK_DEBUG;
import static templates.common.GlobalConstants.IS_NETWORK_DEBUG;
import static templates.communication.Warnings.warn;

/**
 * Methods that handle what should happen on the client side when particular events occur on the server side.
 */
public class ClientPacketReceiveActions {

    private static final String
            WARN_ADD = "Attempted to add ",
            WARN_REM = "Attempted to remove ",
            WARN_MOV = "Attempted to move ",
            WARN_ROT = "Attempted to rotate ",
            WARN_SCA = "Attempted to scale ",
            WARN_TRA = "Attempted to transform ",
            WARN_COL = "Attempted to change color of ",
            WARN_ATT = "Attempted to change attenuation of ",
            WARN_DIR = "Attempted to change direction of ",
            WARN_PRO = "Attempted to change properties of ",
            WARN_SET = "Attempted to set ",
            WARN_SND = "Attempted to play ",
            WARN_MSG = "Attempted to receive ";

    private static final String
            PHYS_OBJ_ID_AND_TYPE    = "PhysicalObject of id %d and of type %s",
            PHYS_OBJ_ID             = "PhysicalObject of id %d",
            AABB_ID_PATH_IDENTIFIER = "AABBHitbox of mesh path %s and of sub mesh identifier %d, for PhysicalObject of id %d",
            CON_PLR_ID_AND_NAME     = "ConnectedPlayer of id %d and of username %s",
            CON_PLR_ID              = "ConnectedPlayer of id %d",
            SECS_PER_TICK           = "seconds per game tick to %f",
            GRAVITY                 = "gravity to (%f, %f, %f)",
            LIGHT_ID                = "Light of id %d",
            DIR_LIGHT_ID            = "DirectionalLight of id %d",
            SKYBOX_NAME             = "Skybox of name `%s`",
            SND_PHYS_OBJ            = "sound `%s` at PhysicalObject of id %d",
            SND_POS                 = "sound `%s` at position %s",
            SND_EMIT                = "sound `%s` at SoundEmitter of id %d",
            CHAT_MSG_PLR_ID         = "chat message from player of id %d",
            SYS_MSG                 = "system message";

    private static final String
            EXISTS          = "; already exists client-side",
            DNE             = "; doesn't exist client-side",
            DNE_PHYS_OBJ    = "; PhysicalObject doesn't exist client-side",
            DNE_CON_PLR     = "; ConnectedPlayer doesn't exist client-side",
            LVL_NOT_INST    = "; ClientLevel object hasn't even been instantiated yet",
            CHAT_NOT_INST   = "; ChatScreen object hasn't even been instantiated yet",
            HAND_NOT_INST   = "; LightHandler object hasn't even been instantiated yet",
            DNE_SND         = "; sound doesn't exist client-side";

    private static final String
            PHYS_ADD_EXISTS     = WARN_ADD + PHYS_OBJ_ID_AND_TYPE + EXISTS,
            PHYS_ADD_LVL        = WARN_ADD + PHYS_OBJ_ID_AND_TYPE + LVL_NOT_INST,
            PHYS_REM_DNE        = WARN_REM + PHYS_OBJ_ID + DNE,
            PHYS_REM_LVL        = WARN_REM + PHYS_OBJ_ID + LVL_NOT_INST,
            PHYS_MOV_DNE        = WARN_MOV + PHYS_OBJ_ID + DNE,
            PHYS_MOV_LVL        = WARN_MOV + PHYS_OBJ_ID + LVL_NOT_INST,
            PHYS_ROT_DNE        = WARN_ROT + PHYS_OBJ_ID + DNE,
            PHYS_ROT_LVL        = WARN_ROT + PHYS_OBJ_ID + LVL_NOT_INST,
            PHYS_SCA_DNE        = WARN_SCA + PHYS_OBJ_ID + DNE,
            PHYS_SCA_LVL        = WARN_SCA + PHYS_OBJ_ID + LVL_NOT_INST,
            PHYS_TRA_DNE        = WARN_TRA + PHYS_OBJ_ID + DNE,
            PHYS_TRA_LVL        = WARN_TRA + PHYS_OBJ_ID + LVL_NOT_INST,
            AABB_ADD_DNE_PHYS   = WARN_ADD + AABB_ID_PATH_IDENTIFIER + DNE_PHYS_OBJ,
            AABB_ADD_EXISTS     = WARN_ADD + AABB_ID_PATH_IDENTIFIER + EXISTS,
            AABB_ADD_LVL        = WARN_ADD + AABB_ID_PATH_IDENTIFIER + LVL_NOT_INST,
            AABB_REM_DNE_PHYS   = WARN_REM + AABB_ID_PATH_IDENTIFIER + DNE_PHYS_OBJ,
            AABB_REM_DNE        = WARN_REM + AABB_ID_PATH_IDENTIFIER + DNE,
            AABB_REM_LVL        = WARN_REM + AABB_ID_PATH_IDENTIFIER + LVL_NOT_INST,
            PLR_ADD_EXISTS      = WARN_ADD + CON_PLR_ID_AND_NAME + EXISTS,
            PLR_ADD_LVL         = WARN_ADD + CON_PLR_ID_AND_NAME + LVL_NOT_INST,
            PLR_TRA_DNE         = WARN_TRA + CON_PLR_ID + DNE,
            PLR_TRA_LVL         = WARN_TRA + CON_PLR_ID + LVL_NOT_INST,
            PLR_REM_DNE_PLR     = WARN_REM + CON_PLR_ID + DNE_CON_PLR,
            PLR_REM_LVL         = WARN_REM + CON_PLR_ID + LVL_NOT_INST,
            SECS_LVL            = WARN_SET + SECS_PER_TICK + LVL_NOT_INST,
            GRAV_LVL            = WARN_SET + GRAVITY + LVL_NOT_INST,
            CHAT_MSG_DNE_PLR    = WARN_MSG + CHAT_MSG_PLR_ID + DNE_CON_PLR,
            CHAT_MSG_CHAT       = WARN_MSG + CHAT_MSG_PLR_ID + CHAT_NOT_INST,
            CHAT_MSG_LVL        = WARN_MSG + CHAT_MSG_PLR_ID + LVL_NOT_INST,
            SYS_MSG_CHAT        = WARN_MSG + SYS_MSG + CHAT_NOT_INST,
            SYS_MSG_LVL         = WARN_MSG + SYS_MSG + LVL_NOT_INST,
            LGT_ADD_EXISTS      = WARN_ADD + LIGHT_ID + EXISTS,
            LGT_ADD_HAND        = WARN_ADD + LIGHT_ID + HAND_NOT_INST,
            LGT_REM_DNE         = WARN_REM + LIGHT_ID + DNE,
            LGT_REM_HAND        = WARN_REM + LIGHT_ID + HAND_NOT_INST,
            LGT_MOV_DNE         = WARN_MOV + LIGHT_ID + DNE,
            LGT_MOV_HAND        = WARN_MOV + LIGHT_ID + HAND_NOT_INST,
            LGT_COL_DNE         = WARN_COL + LIGHT_ID + DNE,
            LGT_COL_HAND        = WARN_COL + LIGHT_ID + HAND_NOT_INST,
            LGT_ATT_DNE         = WARN_ATT + LIGHT_ID + DNE,
            LGT_ATT_HAND        = WARN_ATT + LIGHT_ID + HAND_NOT_INST,
            LGT_PRO_DNE         = WARN_PRO + LIGHT_ID + DNE,
            LGT_PRO_HAND        = WARN_PRO + LIGHT_ID + HAND_NOT_INST,
            DIR_LGT_ADD_EXISTS  = WARN_ADD + DIR_LIGHT_ID + EXISTS,
            DIR_LGT_ADD_HAND    = WARN_ADD + DIR_LIGHT_ID + HAND_NOT_INST,
            DIR_LGT_REM_EXISTS  = WARN_REM + DIR_LIGHT_ID + EXISTS,
            DIR_LGT_REM_HAND    = WARN_REM + DIR_LIGHT_ID + HAND_NOT_INST,
            DIR_LGT_DIR_DNE     = WARN_DIR + DIR_LIGHT_ID + DNE,
            DIR_LGT_DIR_HAND    = WARN_DIR + DIR_LIGHT_ID + HAND_NOT_INST,
            DIR_LGT_COL_DNE     = WARN_COL + DIR_LIGHT_ID + DNE,
            DIR_LGT_COL_HAND    = WARN_COL + DIR_LIGHT_ID + HAND_NOT_INST,
            DIR_LGT_PRO_DNE     = WARN_PRO + DIR_LIGHT_ID + DNE,
            DIR_LGT_PRO_HAND    = WARN_PRO + DIR_LIGHT_ID + HAND_NOT_INST,
            SKYBOX_DNE          = WARN_SET + SKYBOX_NAME + DNE,
            SND_PHYS_DNE_SND    = WARN_SND + SND_PHYS_OBJ + DNE_SND,
            SND_PHYS_DNE_PHYS   = WARN_SND + SND_PHYS_OBJ + DNE_PHYS_OBJ,
            SND_PHYS_LVL        = WARN_SND + SND_PHYS_OBJ + LVL_NOT_INST,
            SND_POS_DNE_SND     = WARN_SND + SND_POS + DNE_SND,
            SND_EMIT_DNE_SND    = WARN_SND + SND_EMIT + DNE_SND;

    public static void usernamePromptReceived() {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.usernamePromptReceived");

        if (LevelInitializer.isOnlineGame) {
            Screen.queueScreenForAddition(new UsernamePromptScreen());
        } else {
            new LocalClientPacketSendEvents().sendPlayerUsername("localplayer");
        }
    }

    public static void usernameSuccessReceived(String username, int color, Vector3f spawnPoint) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.usernameSuccessReceived");

        PlayerHandler.localUsername = username;
        PlayerHandler.localColor = ColorUtils.asNormalizedRGBArray(color);

        if (LevelInitializer.isOnlineGame) {
            Main.gameLoader = new OnlineGameLoader(spawnPoint);
        } else {
            Main.gameLoader = new LocalGameLoader(spawnPoint);
        }
    }

    public static void levelIsReadyReceived() {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.levelIsReadyReceived");

        ClientLevel.get().isReady = true;
    }

    public static void physicalObjectAddedReceived(int id, PhysicalObjectType type, Vector3f position, Vector3f rotation, float scale) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.physicalObjectAddedReceived " + "{id=" + id + "} {type=" + type +"}");

        CachedPhysicalObject object = new CachedPhysicalObject(new Vector3f(position), new Vector3f(rotation), scale);
        ClientLevel level = ClientLevel.get();

        if (level != null) {
            boolean success = level.addCachedPhysicalObject(id, object);

            if (success) {
                Model[] modelList = PhysicalToVisualConverter.convert(type);

                for (Model model : modelList) {
                    GameObject gameObject = new GameObject(model, new Vector3f(object.getPosition()), new Vector3f(object.getRotation()), scale);
                    GameLoader.batchedGameObjectsList.addGameObject(gameObject);

                    object.setGameObject(gameObject);
                }
            } else {
                warn(String.format(PHYS_ADD_EXISTS, id, type));
            }
        } else {
            warn(String.format(PHYS_ADD_LVL, id, type));
        }
    }

    public static void physicalObjectRemovedReceived(int id) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.physicalObjectRemovedReceived");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            CachedPhysicalObject object = level.getCachedPhysicalObject(id);
            boolean success = level.removeCachedPhysicalObject(id);

            if (success) {
                GameLoader.batchedGameObjectsList.removeGameObject(object.getGameObject());
            } else {
                warn(String.format(PHYS_REM_DNE, id));
            }
        } else {
            warn(String.format(PHYS_REM_LVL, id));
        }
    }

    public static void physicalObjectMovedReceived(int id, float x, float y, float z) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.physicalObjectMovedReceived");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            CachedPhysicalObject object = level.getCachedPhysicalObject(id);

            if (object != null) {
                object.updatePrevPosition();
                object.setPosition(x, y, z);
                object.lastTime = Time.getCurrentTime();
            } else {
                warn(String.format(PHYS_MOV_DNE, id));
            }
        } else {
            warn(String.format(PHYS_MOV_LVL, id));
        }
    }

    public static void physicalObjectRotatedReceived(int id, float rotX, float rotY, float rotZ) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.physicalObjectRotatedReceived");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            CachedPhysicalObject object = level.getCachedPhysicalObject(id);

            if (object != null) {
                object.updatePrevRotation();
                object.setRotation(rotX, rotY, rotZ);
                object.lastTime = Time.getCurrentTime();
            } else {
                warn(String.format(PHYS_ROT_DNE, id));
            }
        } else {
            warn(String.format(PHYS_ROT_LVL, id));
        }
    }

    public static void physicalObjectScaledReceived(int id, float scale) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.physicalObjectScaledReceived");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            CachedPhysicalObject object = level.getCachedPhysicalObject(id);

            if (object != null) {
                object.setScale(scale);
                // This must be done manually since scale is a value type, not a reference type
                object.getGameObject().setScale(scale);
            } else {
                warn(String.format(PHYS_SCA_DNE, id));
            }
        } else {
            warn(String.format(PHYS_SCA_LVL, id));
        }
    }

    public static void physicalObjectTransformChangedReceived(int id, Vector3f position, Vector3f rotation, float scale) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.physicalObjectTransformChangedReceived");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            CachedPhysicalObject object = level.getCachedPhysicalObject(id);

            if (object != null) {
                object.updatePrevPosition();
                object.updatePrevRotation();
                object.setPosition(position.x, position.y, position.z);
                object.setRotation(rotation.x, rotation.y, rotation.z);
                object.setScale(scale);
                // This must be done manually since scale is a value type, not a reference type
                object.getGameObject().setScale(scale);
                object.lastTime = Time.getCurrentTime();
            } else {
                warn(String.format(PHYS_TRA_DNE, id));
            }
        } else {
            warn(String.format(PHYS_TRA_LVL, id));
        }
    }

    public static void aabbHitboxAddedReceived(int id, String meshPath, int subMeshIdentifier) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.aabbHitboxAddedReceived " + "{id=" + id + "}");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            if (level.getCachedPhysicalObject(id) == null) {
                warn(String.format(AABB_ADD_DNE_PHYS, meshPath, subMeshIdentifier, id));
            }

            boolean success = level.addCachedAABBHitbox(id, new CachedAABBHitbox(id, meshPath, subMeshIdentifier));
            if (!success) {
                warn(String.format(AABB_ADD_EXISTS, meshPath, subMeshIdentifier, id));
            }
        } else {
            warn(String.format(AABB_ADD_LVL, meshPath, subMeshIdentifier, id));
        }
    }

    public static void aabbHitboxRemovedReceived(int id, String meshPath, int subMeshIdentifier) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.aabbHitboxRemovedReceived");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            if (level.getCachedPhysicalObject(id) == null) {
                warn(String.format(AABB_REM_DNE_PHYS, meshPath, subMeshIdentifier, id));
            }

            boolean success = level.removeCachedAABBHitbox(id, meshPath, subMeshIdentifier);
            if (!success) {
                warn(String.format(AABB_REM_DNE, meshPath, subMeshIdentifier, id));
            }
        } else {
            warn(String.format(AABB_REM_LVL, meshPath, subMeshIdentifier, id));
        }
    }

    public static void connectedPlayerAddedReceived(int id, String username, int color, float x, float y, float z, float rotY) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.connectedPlayerAddedReceived " + "{id=" + id + "}");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            CachedConnectedPlayer cachedConnectedPlayer = new CachedConnectedPlayer(username, color, new Vector3f(x, y, z), new Vector3f(0, rotY, 0));
            boolean success = level.addCachedConnectedPlayer(id, cachedConnectedPlayer);

            if (success) {
                Model model = ModelBank.getAbstractArt();
                GameObject gameObject = new GameObject(model, new Vector3f(cachedConnectedPlayer.getPosition()), new Vector3f(cachedConnectedPlayer.getRotation()), 1);
                GameLoader.batchedGameObjectsList.addGameObject(gameObject);

                cachedConnectedPlayer.setGameObject(gameObject);

                GeneralSphereHitbox generalSphereHitbox = new GeneralSphereHitbox(id, 1);
                ClientLevel.get().addGeneralSphereHitbox(generalSphereHitbox);
            } else {
                warn(String.format(PLR_ADD_EXISTS, id, username));
            }
        } else {
            warn(String.format(PLR_ADD_LVL, id, username));
        }
    }

    // TODO: 2025-07-01 Make a method that separates transform and rotation perhaps
    public static void connectedPlayerTransformChangedReceived(int id, float x, float y, float z, float rotY) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.connectedPlayerMovedReceived");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            CachedConnectedPlayer cachedConnectedPlayer = level.getCachedConnectedPlayer(id);

            if (cachedConnectedPlayer != null) {
                cachedConnectedPlayer.updatePrevPosition();
                cachedConnectedPlayer.updatePrevRotation();
                cachedConnectedPlayer.setPosition(x, y, z);
                cachedConnectedPlayer.setRotation(0, rotY, 0);
                cachedConnectedPlayer.lastTime = Time.getCurrentTime();
            } else {
                warn(String.format(PLR_TRA_DNE, id));
            }
        } else {
            warn(String.format(PLR_TRA_LVL, id));
        }
    }

    public static void connectedPlayerLeftReceived(int id) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.connectedPlayerLeftReceived");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            CachedConnectedPlayer cachedConnectedPlayer = level.getCachedConnectedPlayer(id);
            boolean success = level.removeCachedConnectedPlayer(id);

            if (success) {
                GameLoader.batchedGameObjectsList.removeGameObject(cachedConnectedPlayer.getGameObject());
            } else {
                warn(String.format(PLR_REM_DNE_PLR, id));
            }
        } else {
            warn(String.format(PLR_REM_LVL, id));
        }
    }

    public static void levelSecondsPerGameTickChangedReceived(float secondsPerGameTick) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.levelSecondsPerGameTickChangedReceived");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            level.secondsPerGameTick = secondsPerGameTick;
        } else {
            warn(String.format(SECS_LVL, secondsPerGameTick));
        }
    }

    public static void levelGravityChangedReceived(float x, float y, float z) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.levelGravityChangedReceived");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            level.gravity.set(x, y, z);
        } else {
            warn(String.format(GRAV_LVL, x, y, z));
        }
    }

    public static void chatMessageReceptionConfirmationReceived(int localMessageId) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.chatMessageReceptionConfirmationReceived");

        // TODO: 2025-07-25  
    }

    public static void chatMessageReceived(int playerId, String message) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.chatMessageReceived");

        ClientLevel level = ClientLevel.get();
        ChatScreen chatScreen = ChatScreen.get();

        if (level != null) {
            if (chatScreen != null) {
                CachedConnectedPlayer cachedConnectedPlayer = level.getCachedConnectedPlayer(playerId);

                if (cachedConnectedPlayer != null) {
                    String username = cachedConnectedPlayer.username;
                    float[] color = cachedConnectedPlayer.color;
                    chatScreen.appendChatWithPlayerMessage(username, color, message);
                } else {
                    warn(String.format(CHAT_MSG_DNE_PLR, playerId));
                }
            } else {
                warn(String.format(CHAT_MSG_CHAT, playerId));
            }
        } else {
            warn(String.format(CHAT_MSG_LVL, playerId));
        }
    }

    public static void systemMessageReceived(String message) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.systemMessageReceived");

        ClientLevel level = ClientLevel.get();
        ChatScreen chatScreen = ChatScreen.get();

        if (level != null) {
            if (chatScreen != null) {
                chatScreen.appendChatWithSystemMessage(message);
            } else {
                warn(SYS_MSG_CHAT);
            }
        } else {
            warn(SYS_MSG_LVL);
        }
    }

    public static void virtualLightAddedReceived(int id, Vector3f position, Vector3f color, Vector3f attenuation) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.virtualLightAddedReceived");

        LightHandler lightHandler = GameLoader.lightHandler;

        if (lightHandler != null) {
            Light light = new Light(new Vector3f(position), new Vector3f(color), new Vector3f(attenuation));
            boolean success = lightHandler.addLight(id, light);

            if (!success) {
                warn(String.format(LGT_ADD_EXISTS, id));
            }
        } else {
            warn(String.format(LGT_ADD_HAND, id));
        }
    }

    public static void virtualLightRemovedReceived(int id) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.virtualLightRemovedReceived");

        LightHandler lightHandler = GameLoader.lightHandler;

        if (lightHandler != null) {
            boolean success = lightHandler.removeLight(id);

            if (!success) {
                warn(String.format(LGT_REM_DNE, id));
            }
        } else {
            warn(String.format(LGT_REM_HAND, id));
        }
    }

    public static void virtualLightMovedReceived(int id, float x, float y, float z) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.virtualLightMovedReceived");

        LightHandler lightHandler = GameLoader.lightHandler;

        if (lightHandler != null) {
            Light light = lightHandler.getLight(id);

            if (light != null) {
                light.setPosition(x, y, z);
            } else {
                warn(String.format(LGT_MOV_DNE, id));
            }
        } else {
            warn(String.format(LGT_MOV_HAND, id));
        }
    }

    public static void virtualLightColorChangedReceived(int id, float r, float g, float b) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.virtualLightColorChangedReceived");

        LightHandler lightHandler = GameLoader.lightHandler;

        if (lightHandler != null) {
            Light light = lightHandler.getLight(id);

            if (light != null) {
                light.setColor(r, g, b);
            } else {
                warn(String.format(LGT_COL_DNE, id));
            }
        } else {
            warn(String.format(LGT_COL_HAND, id));
        }
    }

    public static void virtualLightAttenuationChangedReceived(int id, float att1, float att2, float att3) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.virtualLightAttenuationChangedReceived");

        LightHandler lightHandler = GameLoader.lightHandler;

        if (lightHandler != null) {
            Light light = lightHandler.getLight(id);

            if (light != null) {
                light.setAttenuation(att1, att2, att3);
            } else {
                warn(String.format(LGT_ATT_DNE, id));
            }
        } else {
            warn(String.format(LGT_ATT_HAND, id));
        }
    }

    public static void virtualLightPropertiesChangedReceived(int id, Vector3f position, Vector3f color, Vector3f attenuation) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.virtualLightPropertiesChangedReceived");

        LightHandler lightHandler = GameLoader.lightHandler;

        if (lightHandler != null) {
            Light light = lightHandler.getLight(id);

            if (light != null) {
                light.setPosition(position.x, position.y, position.z);
                light.setColor(color.x, color.y, color.z);
                light.setAttenuation(attenuation.x, attenuation.y, attenuation.z);
            } else {
                warn(String.format(LGT_PRO_DNE, id));
            }
        } else {
            warn(String.format(LGT_PRO_HAND, id));
        }
    }

    public static void virtualDirectionalLightAddedReceived(int id, Vector3f toLightDirection, Vector3f color) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.virtualDirectionalLightAddedReceived");

        LightHandler lightHandler = GameLoader.lightHandler;

        if (lightHandler != null) {
            DirectionalLight directionalLight = new DirectionalLight(new Vector3f(toLightDirection), new Vector3f(color));
            boolean success = lightHandler.addDirectionalLight(id, directionalLight);

            if (!success) {
                warn(String.format(DIR_LGT_ADD_EXISTS, id));
            }
        } else {
            warn(String.format(DIR_LGT_ADD_HAND, id));
        }
    }

    public static void virtualDirectionalLightRemovedReceived(int id) {
        if (IS_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.virtualDirectionalLightRemovedReceived");

        LightHandler lightHandler = GameLoader.lightHandler;

        if (lightHandler != null) {
            boolean success = lightHandler.removeDirectionalLight(id);

            if (!success) {
                warn(String.format(DIR_LGT_REM_EXISTS, id));
            }
        } else {
            warn(String.format(DIR_LGT_REM_HAND, id));
        }
    }

    public static void virtualDirectionalLightToLightDirectionChangedReceived(int id, float x, float y, float z) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.virtualDirectionalLightToLightDirectionChangedReceived");

        LightHandler lightHandler = GameLoader.lightHandler;

        if (lightHandler != null) {
            DirectionalLight directionalLight = lightHandler.getDirectionalLight(id);

            if (directionalLight != null) {
                directionalLight.setToLightDirection(x, y, z);
            } else {
                warn(String.format(DIR_LGT_DIR_DNE, id));
            }
        } else {
            warn(String.format(DIR_LGT_DIR_HAND, id));
        }
    }

    public static void virtualDirectionalLightColorChangedReceived(int id, float r, float g, float b) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.virtualDirectionalLightColorChangedReceived");

        LightHandler lightHandler = GameLoader.lightHandler;

        if (lightHandler != null) {
            DirectionalLight directionalLight = lightHandler.getDirectionalLight(id);

            if (directionalLight != null) {
                directionalLight.setColor(r, g, b);
            } else {
                warn(String.format(DIR_LGT_COL_DNE, id));
            }
        } else {
            warn(String.format(DIR_LGT_COL_HAND, id));
        }
    }

    public static void virtualDirectionalLightPropertiesChangedReceived(int id, Vector3f toLightDirection, Vector3f color) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.virtualDirectionalLightPropertiesChangedReceived");

        LightHandler lightHandler = GameLoader.lightHandler;

        if (lightHandler != null) {
            DirectionalLight directionalLight = lightHandler.getDirectionalLight(id);

            if (directionalLight != null) {
                directionalLight.setToLightDirection(toLightDirection.x, toLightDirection.y, toLightDirection.z);
                directionalLight.setColor(color.x, color.y, color.z);
            } else {
                warn(String.format(DIR_LGT_PRO_DNE, id));
            }
        } else {
            warn(String.format(DIR_LGT_PRO_HAND, id));
        }
    }

    public static void skyboxChangedReceived(String skyboxName, boolean unmoving) {
        Skybox skybox = Skyboxes.getByName(skyboxName);

        if (skybox != null) {
            Skybox.currentSkybox = skybox;
            Skybox.currentSkybox.unmoving = unmoving;
        } else {
            warn(String.format(SKYBOX_DNE, skyboxName));
        }
    }

    public static void playSoundAtPhysicalObjectReceived(Sound sound, int id) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.playSoundAtPhysicalObjectReceived");

        ClientLevel level = ClientLevel.get();

        if (level != null) {
            CachedPhysicalObject object = level.getCachedPhysicalObject(id);

            if (object != null) {
                GameObject gameObject = object.getGameObject();
                String soundPath = SoundToPathConverter.convert(sound);

                if (soundPath != null) {
                    PositionalAudioMaster.playSoundAtGameObject(soundPath, gameObject);
                } else {
                    warn(String.format(SND_PHYS_DNE_SND, sound, id));
                }
            } else {
                warn(String.format(SND_PHYS_DNE_PHYS, sound, id));
            }
        } else {
            warn(String.format(SND_PHYS_LVL, sound, id));
        }
    }

    public static void playSoundAtPositionReceived(Sound sound, Vector3f position) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.playSoundAtPositionReceived");

        String soundPath = SoundToPathConverter.convert(sound);

        if (soundPath != null) {
            PositionalAudioMaster.playSoundAtPosition(soundPath, position);
        } else {
            warn(String.format(SND_POS_DNE_SND, sound, position));
        }
    }

    public static void createSoundEmitterReceived(int customIdentifier, Vector3f position) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.createSoundEmitterReceived");

        PositionalAudioMaster.createSoundEmitter(customIdentifier, position);
    }

    public static void destroySoundEmitterReceived(int customIdentifier) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.destroySoundEmitterReceived");

        PositionalAudioMaster.destroySoundEmitter(customIdentifier);
    }

    public static void playSoundAtSoundEmitterReceived(Sound sound, int customIdentifier) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.playSoundAtSoundEmitterReceived");

        String soundPath = SoundToPathConverter.convert(sound);

        if (soundPath != null) {
            PositionalAudioMaster.playSoundAtSoundEmitter(soundPath, customIdentifier);
        } else {
            warn(String.format(SND_EMIT_DNE_SND, sound, customIdentifier));
        }
    }

    public static void updatePositionOfSoundEmitterReceived(int customIdentifier, float x, float y, float z) {
        if (IS_DETAILED_NETWORK_DEBUG) System.out.println("ClientPacketReceiveActions.updatePositionOfSoundEmitterReceived");

        PositionalAudioMaster.updatePositionOfSoundEmitter(customIdentifier, x, y, z);
    }

}
