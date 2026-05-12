package com.james.renderEngine.ui;

import com.james.renderEngine.uiElements.GuiButton;
import com.james.tools.RenderingMath;
import com.james.input.ClickInput;
import com.james.renderEngine.ui.dataTypes.MixedPosition;
import com.james.renderEngine.ui.dataTypes.NormalizedPosition;
import com.james.renderEngine.ui.dataTypes.ScreenPosition;
import com.james.tools.Time;

import java.util.*;

/**
 * Creates a new screen, which is basically just a collection of guis that should belong grouped together.
 * You can have many screens instantiated at once. A list of all screens can be found in the UiHandler class.
 * @see UiHandler
 */
public class Screen {

    /**
     * A list of all guis that belong to this screen. The list is saved so that later when deleting the screen,
     * we know which guis belonged to this screen, and thus we are able to only remove these guis from
     * rendering. Not only that, but this list is also used in the update() method.
     */
    protected final List<Gui> guisOfScreen = new ArrayList<>();
    private boolean shouldDelete = false;
    public boolean shouldDelete() { return shouldDelete; }

    private final List<GuiButton> guiButtons = new ArrayList<>();

    private final Map<Gui, List<GuiAnimationData>> animatedGuis = new HashMap<>();

    /**
     * Adds a gui to the list of guis present in this class, and adds it to the list made for rendering in the
     * UiHandler class. This method should only be called in subclasses of Screen, as when creating guis for a
     * screen, that should always be done in a child of the screen.
     *
     * Also, if the gui is an instance of GuiButton, adds it to the GuiButton list. This list is used for
     * handling hover states of these special guis. (Check the update() method.)
     */
    public void addGui(Gui gui) {
        guisOfScreen.add(gui);
        UiHandler.guisToRender.add(gui);

        if (gui instanceof GuiButton) {
            guiButtons.add((GuiButton) gui);
        }
    }

    /**
     * Plural version of above method. Is useful for GuiGroups, such as GuiTexts.
     */
    public void addGuis(List<Gui> guis) {
        for (Gui gui : guis) {
            addGui(gui);
        }
    }

    /**
     * Removes a gui from the list of guis present in this class, and from the list made for rendering in the
     * UiHandler class.
     *
     * Also, if the gui is an instance of GuiButton, removes it from the GuiButton list.
     */
    public void removeGui(Gui gui) {
        boolean success1 = guisOfScreen.remove(gui);
        boolean success2 = UiHandler.guisToRender.remove(gui);

        if (!success1 || !success2)
            throw new RuntimeException();

        if (gui instanceof GuiButton) {
            boolean success3 = guiButtons.remove((GuiButton) gui);

            if (!success3)
                throw new RuntimeException();
        }
    }

    /**
     * Plural version of above method. Is useful for GuiGroups, such as GuiTexts.
     */
    public void removeGuis(List<Gui> guis) {
        for (Gui gui : guis) {
            removeGui(gui);
        }
    }

    /**
     * Adds animation data to a desired gui.
     * @see GuiAnimationData
     */
    protected void addAnimationForGui(Gui gui, GuiAnimationData data) {
        if (animatedGuis.containsKey(gui)) {
            List<GuiAnimationData> batch = animatedGuis.get(gui);
            batch.add(data);
        } else {
            List<GuiAnimationData> newBatch = new ArrayList<>();
            newBatch.add(data);
            animatedGuis.put(gui, newBatch);
        }
    }

    /**
     * Update method called for all screens. You can also override this method in child classes for custom
     * functionality. Updates the guis of this screen. To be called once per frame.
     */
    public void update() {
        for (GuiButton button : guiButtons) {
            button.resetHoverState();
        }

        for (Gui gui : guisOfScreen) {
            if (gui instanceof HoveredComponent && gui.isEnabled && UiHandler.isMouseOver(gui)) {
                ((HoveredComponent) gui).onHovered();

                if (gui instanceof ClickedComponent) {
                    if (ClickInput.isLeftClickDown()) {
                        ((ClickedComponent) gui).onClicked(ClickedComponent.MouseButton.LEFT);
                    } else if (ClickInput.isRightClickDown()) {
                        ((ClickedComponent) gui).onClicked(ClickedComponent.MouseButton.RIGHT);
                    }
                }
            }
        }

        for (GuiButton button : guiButtons) {
            button.checkHoverStateChanged();
        }

        animateGuis();
    }

    /**
     * Method that updates all animated guis. To be called once per frame.
     */
    private void animateGuis() {
        for (Gui gui : animatedGuis.keySet()) {
            List<GuiAnimationData> dataList = animatedGuis.get(gui);

            Iterator<GuiAnimationData> iterator = dataList.iterator();
            while (iterator.hasNext()) {
                GuiAnimationData data = iterator.next();

                float timeSinceAnimationStart = Time.getCurrentTime() - data.startTime;

                // the animation hasn't begun yet
                if (timeSinceAnimationStart < 0) {
                    continue;
                }

                // animation has finished
                if (timeSinceAnimationStart > data.animationLength) {
                    if (data.repeat) {
                        timeSinceAnimationStart %= data.animationLength;
                    } else {
                        iterator.remove();
                        continue;
                    }
                }

                float normalizedProgress = timeSinceAnimationStart / data.animationLength;
                float easingProgress = data.mathFunction.apply(normalizedProgress);

                float finalValue = RenderingMath.linearlyInterpolate(data.startValue, data.endValue, easingProgress);

                if (data.attribute == GuiAnimationData.Attribute.PositionX) {

                    if (gui.position instanceof NormalizedPosition)
                        ((NormalizedPosition) gui.position).x = finalValue;
                    else if (gui.position instanceof ScreenPosition)
                        ((ScreenPosition) gui.position).x = (int) finalValue;
                    else if (gui.position instanceof MixedPosition)
                        ((MixedPosition) gui.position).offset.x = (int) finalValue;
                    else
                        throw new RuntimeException();

                } else if (data.attribute == GuiAnimationData.Attribute.PositionY) {

                    if (gui.position instanceof NormalizedPosition)
                        ((NormalizedPosition) gui.position).y = finalValue;
                    else if (gui.position instanceof ScreenPosition)
                        ((ScreenPosition) gui.position).y = (int) finalValue;
                    else if (gui.position instanceof MixedPosition)
                        ((MixedPosition) gui.position).offset.y = (int) finalValue;
                    else
                        throw new RuntimeException();

                }

            }
        }
    }

    protected void markForDeletion() {
        if (shouldDelete)
            throw new RuntimeException("shouldDelete is already true!");

        shouldDelete = true;
    }

    /**
     * Removes all guis that belonged to this screen from the list made for rendering in the UiHandler class.
     * This should only ever be called from the UiHandler class. If you want a Screen to be deleted, call
     * markForDeletion(), and the UiHandler will take care of the rest.
     */
    public void delete() {
        for (Gui gui : guisOfScreen) {
            UiHandler.guisToRender.remove(gui);
        }
    }

    /**
     * List of Screens to be added to the main list of Screens in the UiHandler class once it is done
     * looping through them. (This is done to avoid ConcurrentModificationExceptions.)
     */
    private static final List<Screen> screensToBeAdded = new ArrayList<>();

    /**
     * Adds a screen to the screensToBeAdded addition queue.
     *
     * @implNote If it's the first screen being created (i.e. we are not currently iterating through the
     * screens list in UiHandler), then we can call UiHandler.screens.add() directly.
     */
    public static void queueScreenForAddition(Screen screen) {
        screensToBeAdded.add(screen);
    }

    /**
     * Returns the list of screensToBeAdded and clears that list to prepare for the next frame. This method
     * gets called in the UiHandler class, once iteration of all screens has finished.
     *
     * @implNote We are copying the list so that it is possible to clear it and return it in the same method.
     * @see UiHandler
     */
    public static List<Screen> getQueuedScreensAndClear() {
        List<Screen> copy = new ArrayList<>(screensToBeAdded);
        screensToBeAdded.clear();
        return copy;
    }

}
