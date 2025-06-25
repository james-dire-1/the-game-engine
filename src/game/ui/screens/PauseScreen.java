package game.ui.screens;

import com.james.networking.Client;
import com.james.renderEngine.ui.ClickedComponent;
import com.james.renderEngine.ui.Screen;
import com.james.renderEngine.utilities.GLFWUtilities;
import com.james.serverSide.LevelInitializer;
import com.james.simulation.ClientLevel;
import templates.gameplay.LocalGameLoader;
import game.main.Main;
import game.ui.uiElements.TitleButton;
import game.ui.uiElements.TitleHeader;
import templates.gameplay.GameLoader;
import templates.gameplay.OnlineGameLoader;

public class PauseScreen extends Screen {

    public static boolean isOpen = false;
    public static boolean isInSettingsMenu = false;
    private static PauseScreen currentInstance;

    private PauseScreen() {
        TitleHeader header = new TitleHeader("Game Paused");
        super.addGuis(header.getAllGuis());

        TitleButton.resetCurrentVerticalPosition();

        TitleButton resumeButton = new TitleButton("Resume Game");
        resumeButton.button.setAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                if (isOpen) {
                    close();
                }
            }
        });
        super.addGuis(resumeButton.getAllGuis());

        TitleButton settingsButton = new TitleButton("Settings...");
        settingsButton.button.setAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                tempClose();
                queueScreenForAddition(new SettingsScreen(SettingsScreen.PreviousScreen.PAUSE_SCREEN));
                isInSettingsMenu = true;
            }
        });
        super.addGuis(settingsButton.getAllGuis());

        TitleButton quitButton = new TitleButton("Quit");
        quitButton.button.setAction((ClickedComponent.MouseButton button) -> {
            if (button == ClickedComponent.MouseButton.LEFT) {
                super.markForDeletion();
                isOpen = false;
                queueScreenForAddition(new TitleScreen());

                GameLoader loader = Main.gameLoader;
                if (loader instanceof LocalGameLoader) {
                    LevelInitializer levelInitializer = ((LocalGameLoader) loader).levelInitializer;
                    levelInitializer.shouldRun = false;
                    try {
                        levelInitializer.thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (loader instanceof OnlineGameLoader) {
                    Client.get().disconnect();
                }

                ClientLevel.delete();
                Main.gameLoader = null;
            }
        });
        super.addGuis(quitButton.getAllGuis());
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
