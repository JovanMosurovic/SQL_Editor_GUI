package app.util;

/**
 * Utility class for managing font settings in this JavaFX application.
 * This class is used to store and retrieve the current font family and size for the editor and console components.
 */
public class FontConfig {

    /**
     * The default font family for the console area and other components.
     */
    public static final String DEFAULT_FONT_FAMILY = "Calibri";
    /**
     * The Monospaced font family for the code editor area and console area.
     */
    public static final String MONOSPACED_FONT = "Monospaced";
    /**
     * The path to the JetBrains Mono font file.
     */
    public static final String JETBRAINS_MONO_FONT_PATH = "/app/resources/fonts/JetBrainsMonoNL-Regular.ttf";

    /**
     * The list of font families available for the code editor area.
     */
    public static final String[] EDITOR_FONT_FAMILIES = {
            DEFAULT_FONT_FAMILY,
            MONOSPACED_FONT
    };

    /**
     * The list of font families available for the console area.
     */
    public static final String[] CONSOLE_FONT_FAMILIES = {
            DEFAULT_FONT_FAMILY,
            MONOSPACED_FONT
    };

    /**
     * The default font size for the editor and console areas.
     */
    public static final double DEFAULT_FONT_SIZE = 16.0;
    /**
     * The step value to increase or decrease the font size by.
     */
    public static final double FONT_STEP = 2.0;
    /**
     * The maximum and minimum font sizes for the editor and console areas.
     */
    public static final double EDITOR_MAX_FONT_SIZE = 42.0;
    public static final double EDITOR_MIN_FONT_SIZE = 10.0;
    /**
     * The maximum and minimum font sizes for the console area.
     */
    public static final double CONSOLE_MAX_FONT_SIZE = 42.0;
    public static final double CONSOLE_MIN_FONT_SIZE = 10.0;

    /**
     * The current font family and size for the code editor area.
     */
    private static String editorFontFamily;
    private static double editorFontSize;
    /**
     * The current font family and size for the console area.
     */
    private static String consoleFontFamily;
    private static double consoleFontSize;

    static {
        editorFontFamily = MONOSPACED_FONT;
        consoleFontFamily = DEFAULT_FONT_FAMILY;
        editorFontSize = DEFAULT_FONT_SIZE;
        consoleFontSize = DEFAULT_FONT_SIZE;
    }

    /**
     * Retrieves the current font family for the code editor area.
     *
     * @return the current font family for the code editor area
     */
    public static String getEditorFontFamily() {
        return editorFontFamily;
    }

    /**
     * Sets the font family for the code editor area.
     *
     * @param fontFamily the new font family for the code editor area
     */
    public static void setEditorFontFamily(String fontFamily) {
        FontConfig.editorFontFamily = fontFamily;
    }

    /**
     * Retrieves the current font size for the code editor area.
     *
     * @return the current font size for the code editor area
     */
    public static double getEditorFontSize() {
        return editorFontSize;
    }

    /**
     * Sets the font size for the code editor area.
     *
     * @param fontSize the new font size for the code editor area
     */
    public static void setEditorFontSize(double fontSize) {
        FontConfig.editorFontSize = fontSize;
    }

    /**
     * Retrieves the current font family for the console area.
     *
     * @return the current font family for the console area
     */
    public static String getConsoleFontFamily() {
        return consoleFontFamily;
    }

    /**
     * Sets the font family for the console area.
     *
     * @param fontFamily the new font family for the console area
     */
    public static void setConsoleFontFamily(String fontFamily) {
        FontConfig.consoleFontFamily = fontFamily;
    }

    /**
     * Retrieves the current font size for the console area.
     *
     * @return the current font size for the console area
     */
    public static double getConsoleFontSize() {
        return consoleFontSize;
    }

    /**
     * Sets the font size for the console area.
     *
     * @param fontSize the new font size for the console area
     */
    public static void setConsoleFontSize(double fontSize) {
        FontConfig.consoleFontSize = fontSize;
    }

}
