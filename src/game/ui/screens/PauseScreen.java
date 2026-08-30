package game.ui.screens;

import com.james.gameplay.PlayerHandler;
import com.james.networking.Client;
import com.james.renderEngine.textRendering.TextAlignment;
import com.james.renderEngine.ui.AnchorPoint;
import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.ui.dataTypes.AnchoredPosition;
import com.james.renderEngine.ui.dataTypes.ScreenSize;
import com.james.renderEngine.uiElements.PersistentGuiText;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.serverSide.LevelInitializer;
import com.james.serverSide.ServerThreadManager;
import com.james.serverSide.simulation.Level;
import com.james.simulation.ClientLevel;
import com.james.simulation.objects.CachedConnectedPlayer;
import com.james.wrapper.EngineUtils;
import game.main.Main;
import game.ui.uiElements.PersistentTitleHeader;
import templates.common.GlobalConstants;
import com.james.gameplay.LocalGameLoader;
import game.ui.uiElements.TitleButton;
import com.james.gameplay.GameLoader;
import com.james.gameplay.OnlineGameLoader;

import java.util.Collection;

public class PauseScreen extends Screen {

    public static boolean isOpen = false;
    public static boolean isInSettingsMenu = false;
    private static PauseScreen currentInstance;

    private static final int MULTIPLAYER_INFO_START_POSITION = -500;
    private static final int PIXELS_BETWEEN_MULTIPLAYER_INFO_ENTRIES = 25;
    private final PersistentGuiText[] multiplayerHeaderInfo = new PersistentGuiText[11];
    private int playersOnline;

    private PauseScreen() {
        PersistentTitleHeader header = new PersistentTitleHeader(LevelInitializer.isOnlineGame ? "Game Menu" : "Game Paused");
        super.addGui(header.getMesh());

        TitleButton.resetCurrentVerticalPosition();

        TitleButton resumeButton = new TitleButton("Resume Game");
        resumeButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                if (isOpen) {
                    close();
                }
            }
        });
        super.addGuis(resumeButton.getAllGuis());

        TitleButton settingsButton = new TitleButton("Settings...");
        settingsButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                tempClose();
                queueScreenForAddition(new SettingsScreen(SettingsScreen.PreviousScreen.PAUSE_SCREEN));
                isInSettingsMenu = true;
            }
        });
        super.addGuis(settingsButton.getAllGuis());

        TitleButton quitButton = new TitleButton(LevelInitializer.isOnlineGame ? "Disconnect" : "Quit");
        quitButton.button.setClickAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                isOpen = false;

                GameLoader loader = EngineUtils.gameLoader;

                if (GlobalConstants.IS_QUICK_START || loader instanceof OnlineGameLoader) {
                    queueScreenForAddition(new TitleScreen());
                } else {
                    queueScreenForAddition(new SceneSelectScreen());
                }

                if (loader instanceof LocalGameLoader) {
                    LevelInitializer levelInitializer = ((LocalGameLoader) loader).levelInitializer;
                    levelInitializer.shouldRun = false;
                    try {
                        levelInitializer.thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Level.clearNameToLevelMap();
                    ServerThreadManager.clearEverything();
                } else if (loader instanceof OnlineGameLoader) {
                    Client.get().disconnect();
                }

                ClientLevel.delete();
                EngineUtils.gameLoader = null;
            }
        });
        super.addGuis(quitButton.getAllGuis());

        if (LevelInitializer.isOnlineGame) {
            Collection<CachedConnectedPlayer> cachedConnectedPlayers = ClientLevel.get().getCachedConnectedPlayers();
            this.playersOnline = cachedConnectedPlayers.size() + 1;
            createMultiplayerHeaderInfo(cachedConnectedPlayers);
        }
    }

    @Override
    public void update() {
        if (ClientLevel.get() == null) {
             super.markForDeletion();
             isOpen = false;
        }

        ClientLevel clientLevel = ClientLevel.get();

        if (LevelInitializer.isOnlineGame && clientLevel != null) {
            Collection<CachedConnectedPlayer> cachedConnectedPlayers = clientLevel.getCachedConnectedPlayers();
            int newPlayersOnline = cachedConnectedPlayers.size() + 1;

            if (playersOnline != newPlayersOnline) {
                playersOnline = newPlayersOnline;
                createMultiplayerHeaderInfo(cachedConnectedPlayers);
            }
        }

        super.update();
    }

    private void createMultiplayerHeaderInfo(Collection<CachedConnectedPlayer> cachedConnectedPlayers) {
        for (int i = 0; i < multiplayerHeaderInfo.length; i++) {
            PersistentGuiText persistentGuiText = multiplayerHeaderInfo[i];

            if (persistentGuiText != null) {
                super.removeGui(persistentGuiText.getMesh());
                multiplayerHeaderInfo[i] = null;
            }
        }

        String headerText;
        if (playersOnline > 1) {
            headerText = playersOnline + " players online";
        } else {
            headerText = playersOnline + " player online";
        }

        addMultiplayerHeaderInfoEntry(headerText, MULTIPLAYER_INFO_START_POSITION, 0);
        boolean notEnoughRoom = playersOnline > multiplayerHeaderInfo.length - 1;

        int currentPosition = MULTIPLAYER_INFO_START_POSITION - 2 * PIXELS_BETWEEN_MULTIPLAYER_INFO_ENTRIES;
        int currentIndex = 1;

        addMultiplayerHeaderInfoEntry(PlayerHandler.localUsername, currentPosition, currentIndex);

        for (CachedConnectedPlayer cachedConnectedPlayer : cachedConnectedPlayers) {
            currentPosition -= PIXELS_BETWEEN_MULTIPLAYER_INFO_ENTRIES;
            currentIndex++;

            if (notEnoughRoom && currentIndex == multiplayerHeaderInfo.length - 1) {
                int playersRemaining = playersOnline - multiplayerHeaderInfo.length + 2;
                String finalText = "... and " + playersRemaining + " more";
                addMultiplayerHeaderInfoEntry(finalText, currentPosition, currentIndex);
                break;
            }

            addMultiplayerHeaderInfoEntry(cachedConnectedPlayer.username, currentPosition, currentIndex);
        }
    }

    private void addMultiplayerHeaderInfoEntry(String text, int position, int index) {
        PersistentGuiText entry = new PersistentGuiText(text, Main.dustismo, 0.3f, new AnchoredPosition(AnchorPoint.TOP, new ScreenSize(0, position)));
        entry.setAlignment(TextAlignment.CENTER_ALIGNED);
        entry.apply();
        super.addGui(entry.getMesh());
        multiplayerHeaderInfo[index] = entry;
    }

    public static void toggle() {
        if (!isInSettingsMenu) {
            if (isOpen) {
                close();
            } else {
                open();
            }
        }
    }

    public static void open() {
        currentInstance = new PauseScreen();
        queueScreenForAddition(currentInstance);
        GLFWUtilities.lockCursor(false);
        isOpen = true;
    }

    public static void tempOpen() {
        currentInstance = new PauseScreen();
        queueScreenForAddition(currentInstance);
        isOpen = true;
    }

    public static void close() {
        currentInstance.markForDeletion();
        GLFWUtilities.lockCursor(true);
        isOpen = false;
    }

    public static void tempClose() {
        currentInstance.markForDeletion();
        isOpen = false;
    }

}
