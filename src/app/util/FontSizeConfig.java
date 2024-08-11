package app.util;

public class FontSizeConfig {

    private static final double DEFAULT_FONT_SIZE = 16.0;
    private static final double FONT_STEP = 2.0;
    private static final double EDITOR_MAX_FONT_SIZE = 42.0;
    private static final double EDITOR_MIN_FONT_SIZE = 10.0;
    private static final double CONSOLE_MAX_FONT_SIZE = 42.0;
    private static final double CONSOLE_MIN_FONT_SIZE = 10.0;

    private static double editorFontSize;
    private static double consoleFontSize;

    static {
        editorFontSize = DEFAULT_FONT_SIZE;
        consoleFontSize = DEFAULT_FONT_SIZE;
    }

    public static double getDefaultFontSize() {
        return DEFAULT_FONT_SIZE;
    }

    public static double getFontStep() {
        return FONT_STEP;
    }

    public static double getEditorMaxFontSize() {
        return EDITOR_MAX_FONT_SIZE;
    }

    public static double getEditorMinFontSize() {
        return EDITOR_MIN_FONT_SIZE;
    }

    public static double getConsoleMaxFontSize() {
        return CONSOLE_MAX_FONT_SIZE;
    }

    public static double getConsoleMinFontSize() {
        return CONSOLE_MIN_FONT_SIZE;
    }

    public static double getEditorFontSize() {
        return editorFontSize;
    }

    public static void setEditorFontSize(double editorFontSize) {
        FontSizeConfig.editorFontSize = editorFontSize;
    }

    public static double getConsoleFontSize() {
        return consoleFontSize;
    }

    public static void setConsoleFontSize(double consoleFontSize) {
        FontSizeConfig.consoleFontSize = consoleFontSize;
    }

}
