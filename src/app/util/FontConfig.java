package app.util;

/**
 * Utility class for managing font settings in this JavaFX application.
 * This class is used to store and retrieve the current font family and size for the editor and console components.
 */
public class FontConfig {

    public static final String DEFAULT_FONT_FAMILY = "Calibri";
    public static final String JETBRAINS_MONO_NL_FONT = "JetBrains Mono NL";
    public static final String MONOSPACED_FONT = "Monospaced";

    public static final String[] EDITOR_FONT_FAMILIES = {
        JETBRAINS_MONO_NL_FONT,
        MONOSPACED_FONT
    };

    public static final String[] CONSOLE_FONT_FAMILIES = {
        DEFAULT_FONT_FAMILY,
        JETBRAINS_MONO_NL_FONT,
        MONOSPACED_FONT
    };

    public static final double DEFAULT_FONT_SIZE = 16.0;
    public static final double FONT_STEP = 2.0;
    public static final double EDITOR_MAX_FONT_SIZE = 42.0;
    public static final double EDITOR_MIN_FONT_SIZE = 10.0;
    public static final double CONSOLE_MAX_FONT_SIZE = 42.0;
    public static final double CONSOLE_MIN_FONT_SIZE = 10.0;

    private static String editorFontFamily;
    private static String consoleFontFamily;

    private static double editorFontSize;
    private static double consoleFontSize;

    static {
        editorFontFamily = JETBRAINS_MONO_NL_FONT;
        consoleFontFamily = DEFAULT_FONT_FAMILY;
        editorFontSize = DEFAULT_FONT_SIZE;
        consoleFontSize = DEFAULT_FONT_SIZE;
    }

    public static String getEditorFontFamily() {
        return editorFontFamily;
    }

    public static void setEditorFontFamily(String editorFontFamily) {
        FontConfig.editorFontFamily = editorFontFamily;
    }

    public static String getConsoleFontFamily() {
        return consoleFontFamily;
    }

    public static void setConsoleFontFamily(String consoleFontFamily) {
        FontConfig.consoleFontFamily = consoleFontFamily;
    }

    public static double getEditorFontSize() {
        return editorFontSize;
    }

    public static void setEditorFontSize(double editorFontSize) {
        FontConfig.editorFontSize = editorFontSize;
    }

    public static double getConsoleFontSize() {
        return consoleFontSize;
    }

    public static void setConsoleFontSize(double consoleFontSize) {
        FontConfig.consoleFontSize = consoleFontSize;
    }

}
