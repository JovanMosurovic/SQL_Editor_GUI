package app.util;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnsiTextParser {
    private static final Pattern ANSI_PATTERN = Pattern.compile("\u001B\\[(\\d+(?:;\\d+)*)m");

    // Text styles
    private static final String RESET = "0";
    private static final String BOLD = "1";
    private static final String UNDERLINE = "4";

    // Standard colors
    private static final String RED = "31";
    private static final String GREEN = "32";
    private static final String YELLOW = "33";
    private static final String BLUE = "34";
    private static final String MAGENTA = "35";
    private static final String CYAN = "36";
    private static final String WHITE = "37";
    private static final String GRAY = "90";
    private static final String LIGHT_BLUE = "94";
    private static final String LIGHT_GREEN = "92";

    // Background colors
    private static final String BG_BLACK = "40";
    private static final String BG_RED = "41";
    private static final String BG_GREEN = "42";
    private static final String BG_YELLOW = "43";
    private static final String BG_BLUE = "44";
    private static final String BG_MAGENTA = "45";
    private static final String BG_CYAN = "46";
    private static final String BG_WHITE = "47";
    private static final String BG_GRAY = "100";

    public static void parseAnsiText(String ansiText, TextFlow textFlow) {
        Matcher matcher = ANSI_PATTERN.matcher(ansiText);
        int lastEnd = 0;
        boolean isBold = false;
        boolean isUnderline = false;
        Color currentColor = Color.BLACK;
        Color currentBgColor = null;

        while (matcher.find()) {
            int start = matcher.start();
            if (start > lastEnd) {
                Text text = createStyledText(ansiText.substring(lastEnd, start), currentColor, currentBgColor, isBold, isUnderline);
                textFlow.getChildren().add(text);
            }

            String[] codes = matcher.group(1).split(";");
            for (String code : codes) {
                switch (code) {
                    case RESET:
                        isBold = false;
                        isUnderline = false;
                        currentColor = Color.BLACK;
                        currentBgColor = null;
                        break;
                    case BOLD:
                        isBold = true;
                        break;
                    case UNDERLINE:
                        isUnderline = true;
                        break;
                    case RED:
                        currentColor = Color.RED;
                        break;
                    case GREEN:
                        currentColor = Color.GREEN;
                        break;
                    case YELLOW:
                        currentColor = Color.YELLOW;
                        break;
                    case BLUE:
                        currentColor = Color.BLUE;
                        break;
                    case MAGENTA:
                        currentColor = Color.MAGENTA;
                        break;
                    case CYAN:
                        currentColor = Color.CYAN;
                        break;
                    case WHITE:
                        currentColor = Color.WHITE;
                        break;
                    case GRAY:
                        currentColor = Color.GRAY;
                        break;
                    case LIGHT_BLUE:
                        currentColor = Color.LIGHTBLUE;
                        break;
                    case LIGHT_GREEN:
                        currentColor = Color.LIGHTGREEN;
                        break;
                    case BG_BLACK:
                        currentBgColor = Color.BLACK;
                        break;
                    case BG_RED:
                        currentBgColor = Color.RED;
                        break;
                    case BG_GREEN:
                        currentBgColor = Color.GREEN;
                        break;
                    case BG_YELLOW:
                        currentBgColor = Color.YELLOW;
                        break;
                    case BG_BLUE:
                        currentBgColor = Color.BLUE;
                        break;
                    case BG_MAGENTA:
                        currentBgColor = Color.MAGENTA;
                        break;
                    case BG_CYAN:
                        currentBgColor = Color.CYAN;
                        break;
                    case BG_WHITE:
                        currentBgColor = Color.WHITE;
                        break;
                    case BG_GRAY:
                        currentBgColor = Color.GRAY;
                        break;
                }
            }

            lastEnd = matcher.end();
        }

        if (lastEnd < ansiText.length()) {
            Text text = createStyledText(ansiText.substring(lastEnd), currentColor, currentBgColor, isBold, isUnderline);
            textFlow.getChildren().add(text);
        }
    }

    private static Text createStyledText(String content, Color color, Color bgColor, boolean bold, boolean underline) {
        Text text = new Text(content);
        text.setFill(color);

        StringBuilder style = new StringBuilder();
        if (bold) {
            style.append("-fx-font-weight: bold;");
        }
        if (underline) {
            style.append("-fx-underline: true;");
        }
        if (bgColor != null) {
            style.append("-fx-background-color: ").append(toRGBCode(bgColor)).append(";");
        }

        text.setStyle(style.toString());
        return text;
    }

    private static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}