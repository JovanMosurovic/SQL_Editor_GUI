package app.util;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import org.fxmisc.richtext.CodeArea;

import java.util.HashMap;
import java.util.function.Consumer;

public class EditorHelper {

    public static void setupEditorFont(CodeArea codeArea) {
        final double currentFontSize = FontConfig.getEditorFontSize();
        final Font jetBrainsMono = Font.loadFont(EditorHelper.class.getResourceAsStream("/app/resources/fonts/JetBrainsMonoNL-Regular.ttf"), currentFontSize);
        final Font fontFamily = (jetBrainsMono != null) ? jetBrainsMono : Font.font(FontConfig.MONOSPACED_FONT, currentFontSize);
        if(jetBrainsMono == null) {
            System.out.println("[CODE AREA]: Failed to load JetBrains Mono font. Using default monospaced font.");
        }

        String fontStyle = "-fx-font-family: " + fontFamily.getFamily() + "; -fx-font-size: " + currentFontSize + "px;";
        codeArea.setStyle(fontStyle);

        codeArea.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if(!newVal) {
                codeArea.setStyle(fontStyle);
            }
        });
    }

    //put("SELECT", "red");
    //    put("FROM", "red");
    //    put("WHERE", "red");
    //    put("INNER", "red");
    //    put("JOIN", "red");
    //    put("ON", "red");
    //    put("DELETE", "red");
    //    put("CREATE", "yellow");
    //    put("TABLE", "yellow");
    //    put("DROP", "yellow");
    //    put("INSERT", "yellow");
    //    put("INTO", "yellow");
    //    put("VALUES", "yellow");
    //    put("UPDATE", "cyan");
    //    put("SET", "cyan");
    //    put("SHOW", "magenta");
    //    put("TABLES", "magenta");
    //    put("AND", "green");
    //    put("OR", "green");

    public static HashMap<String, String> initKeywordColors() {
        HashMap<String, String> keywordColors = new HashMap<>();
        keywordColors.put("SELECT", "red");
        keywordColors.put("FROM", "red");
        keywordColors.put("WHERE", "red");
        keywordColors.put("INNER", "red");
        keywordColors.put("JOIN", "red");
        keywordColors.put("ON", "red");
        keywordColors.put("DELETE", "red");
        keywordColors.put("CREATE", "yellow");
        keywordColors.put("TABLE", "yellow");
        keywordColors.put("DROP", "yellow");
        keywordColors.put("INSERT", "yellow");
        keywordColors.put("INTO", "yellow");
        keywordColors.put("VALUES", "yellow");
        keywordColors.put("UPDATE", "cyan");
        keywordColors.put("SET", "cyan");
        keywordColors.put("SHOW", "magenta");
        keywordColors.put("TABLES", "magenta");
        keywordColors.put("AND", "green");
        keywordColors.put("OR", "green");
        return keywordColors;
    }

    public static void handleEditAction(CodeArea codeArea, Consumer<CodeArea> action) {
        action.accept(codeArea);
    }

    public static void increaseEditorFontSize(TextFlow resultTextFlow, Node node) {
        double currentFontSize = FontConfig.getEditorFontSize();
        if(currentFontSize < FontConfig.EDITOR_MAX_FONT_SIZE) {
            FontHelper.increaseFontSize(FontConfig.FONT_STEP, node);
            FontConfig.setEditorFontSize(currentFontSize + FontConfig.FONT_STEP);
        } else {
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "\n[FONT SIZE]: Maximum font size  reached", Color.RED, true);
        }
    }

    public static void decreaseEditorFontSize(TextFlow resultTextFlow, Node node) {
        double currentFontSize = FontConfig.getEditorFontSize();
        if(currentFontSize > FontConfig.EDITOR_MIN_FONT_SIZE) {
            FontHelper.decreaseFontSize(FontConfig.FONT_STEP, node);
            FontConfig.setEditorFontSize(currentFontSize - FontConfig.FONT_STEP);
        } else {
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "\n[FONT SIZE]: Minimum font size for code area reached", Color.RED, true);
        }
    }
}
