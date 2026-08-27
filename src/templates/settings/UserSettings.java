package templates.settings;

public class UserSettings {

    public static final float DEFAULT_REGULAR_FOV = 90.0f;
    public static final float DEFAULT_SPRINT_FOV = DEFAULT_REGULAR_FOV + 7.5f;
    public static final float DEFAULT_CROUCH_FOV = DEFAULT_REGULAR_FOV - 7.5f;

    public static float fov = DEFAULT_REGULAR_FOV;
    public static float mouseSensitivity = 0.15f;

}
