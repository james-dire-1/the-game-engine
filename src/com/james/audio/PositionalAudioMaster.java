package com.james.audio;

import com.james.audio.objects.SoundEmitter;
import com.james.renderEngine.gameObjects.GameObject;
import com.james.tools.Time;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;

public class PositionalAudioMaster {

    private static final int MAX_CONCURRENT_SOUNDS_PER_OBJECT = 5;
    private static final String ERR_MSG = "PositionalAudioMaster: unexpected state";

    private static final Map<GameObject, int[]> gameObjectsMap = new HashMap<>();
    private static final Map<Integer, Map.Entry<SoundEmitter, int[]>> soundEmittersMap = new HashMap<>();

    public static void playSoundAtGameObject(String soundPath, GameObject gameObject) {
        int bufferId = AudioBank.getOrCreate(soundPath);
        int sourceId = AudioSourcePool.play(bufferId, 0, gameObject.getPosition(), null);
        Helpers.addGameObjectAndSourcePair(gameObject, sourceId);
    }

    public static void playSoundAtPosition(String soundPath, Vector3f position) {
        int bufferId = AudioBank.getOrCreate(soundPath);
        AudioSourcePool.play(bufferId, 0, position, null);
    }

    public static void createSoundEmitter(int customIdentifier, Vector3f position) {
        SoundEmitter soundEmitter = new SoundEmitter(position);
        Helpers.addSoundEmitter(customIdentifier, soundEmitter);
    }

    public static void destroySoundEmitter(int customIdentifier) {
        Map.Entry<SoundEmitter, int[]> previousValue = soundEmittersMap.remove(customIdentifier);

        if (previousValue == null)
            System.err.println(ERR_MSG);
    }

    public static void playSoundAtSoundEmitter(String soundPath, int customIdentifier) {
        int bufferId = AudioBank.getOrCreate(soundPath);
        Map.Entry<SoundEmitter, int[]> entry = soundEmittersMap.get(customIdentifier);

        if (entry == null) {
            System.err.println(ERR_MSG);
            return;
        }

        SoundEmitter soundEmitter = entry.getKey();
        int sourceId = AudioSourcePool.play(bufferId, 0, soundEmitter.getInterpolatedPosition(), null);
        Helpers.addSoundEmitterAndSourcePair(customIdentifier, sourceId);
    }

    public static void updatePositionOfSoundEmitter(int customIdentifier, float x, float y, float z) {
        Map.Entry<SoundEmitter, int[]> entry = soundEmittersMap.get(customIdentifier);

        if (entry == null) {
            System.err.println(ERR_MSG);
            return;
        }

        SoundEmitter soundEmitter = entry.getKey();
        soundEmitter.updatePrevPosition();
        soundEmitter.setCurrentPosition(x, y, z);
        soundEmitter.lastTime = Time.getCurrentTime();
    }

    // explain why only one version includes the deleting
    public static void update() {
        Iterator<GameObject> gameObjectsIterator = gameObjectsMap.keySet().iterator();

        while (gameObjectsIterator.hasNext()) {
            GameObject gameObject = gameObjectsIterator.next();
            if (gameObject.deleted) {
                gameObjectsIterator.remove();
                continue;
            }

            int[] sourceIds = gameObjectsMap.get(gameObject);
            for (int sourceId : sourceIds) {
                if (sourceId == -1) {
                    break;
                }

                boolean donePlaying = AudioSourcePool.changePosition(sourceId, gameObject.getPosition());
                if (donePlaying) {
                    Helpers.removeGameObjectAndSourcePair(gameObject, sourceId);
                }
            }
        }

        for (int customIdentifier : soundEmittersMap.keySet()) {
            Map.Entry<SoundEmitter, int[]> entry = soundEmittersMap.get(customIdentifier);
            SoundEmitter soundEmitter = entry.getKey();
            int[] sourceIds = entry.getValue();

            for (int sourceId : sourceIds) {
                if (sourceId == -1) {
                    break;
                }

                soundEmitter.updateInterpolatedPosition();
                boolean donePlaying = AudioSourcePool.changePosition(sourceId, soundEmitter.getInterpolatedPosition());

                if (donePlaying) {
                    Helpers.removeSoundEmitterAndSourcePair(customIdentifier, sourceId);
                }
            }
        }
    }

    // TODO: 2026-08-12 This should be called somewhere
    public static void clearEverything() {
        gameObjectsMap.clear();
        soundEmittersMap.clear();
    }

    private static class Helpers {
        private static void addGameObjectAndSourcePair(GameObject gameObject, int sourceId) {
            if (!gameObjectsMap.containsKey(gameObject)) {
                int[] sourceIds = new int[MAX_CONCURRENT_SOUNDS_PER_OBJECT];
                Arrays.fill(sourceIds, -1);
                gameObjectsMap.put(gameObject, sourceIds);
            }

            int[] sourceIds = gameObjectsMap.get(gameObject);
            addToArray(sourceIds, sourceId);
        }

        private static void addSoundEmitter(int customIdentifier, SoundEmitter soundEmitter) {
            int[] sourceIds = new int[MAX_CONCURRENT_SOUNDS_PER_OBJECT];
            Arrays.fill(sourceIds, -1);
            Map.Entry<SoundEmitter, int[]> pair = new AbstractMap.SimpleImmutableEntry<>(soundEmitter, sourceIds);
            soundEmittersMap.put(customIdentifier, pair);
        }

        private static void addSoundEmitterAndSourcePair(int customIdentifier, int sourceId) {
            int[] sourceIds = soundEmittersMap.get(customIdentifier).getValue();
            addToArray(sourceIds, sourceId);
        }

        private static void removeGameObjectAndSourcePair(GameObject gameObject, int sourceId) {
            int[] sourceIds = gameObjectsMap.get(gameObject);
            if (sourceIds == null) {
                System.err.println(ERR_MSG);
                return;
            }

            boolean success = removeFromArray(sourceIds, sourceId);
            if (!success)
                System.err.println(ERR_MSG);
        }

        private static void removeSoundEmitterAndSourcePair(int customIdentifier, int sourceId) {
            int[] sourceIds = soundEmittersMap.get(customIdentifier).getValue();
            if (sourceIds == null) {
                System.err.println(ERR_MSG);
                return;
            }

            boolean success = removeFromArray(sourceIds, sourceId);
            if (!success)
                System.err.println(ERR_MSG);
        }

        private static void addToArray(int[] array, int toAdd) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == -1) {
                    array[i] = toAdd;
                    break;
                }
            }
        }

        private static boolean removeFromArray(int[] array, int toRemove) {
            boolean foundSourceId = false;
            for (int i = 0; i < array.length; i++) {
                if (array[i] == toRemove) {
                    foundSourceId = true;
                }

                if (foundSourceId) {
                    if (array[i] == -1) {
                        break;
                    }

                    if (i == array.length - 1) {
                        array[i] = -1;
                    } else {
                        array[i] = array[i + 1];
                    }
                }
            }

            return foundSourceId;
        }
    }

}
