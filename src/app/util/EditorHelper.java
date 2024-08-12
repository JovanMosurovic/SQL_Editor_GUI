package app.util;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;

import java.util.function.Consumer;

public class EditorHelper {

    public static void setupEditorFont(TextArea codeArea) {
        final double currentFontSize = FontConfig.getEditorFontSize();
        final Font jetBrainsMono = Font.loadFont(EditorHelper.class.getResourceAsStream("/app/resources/fonts/JetBrainsMonoNL-Regular.ttf"), currentFontSize);
        final Font fontToUse = (jetBrainsMono != null) ? jetBrainsMono : Font.font(FontConfig.MONOSPACED_FONT, currentFontSize);
        if(jetBrainsMono == null) {
            System.out.println("[CODE AREA]: Failed to load JetBrains Mono font. Using default monospaced font.");
        }
        codeArea.setFont(fontToUse);

        codeArea.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if(!newVal) {
                codeArea.setFont(fontToUse);
            }
        });
    }

    public static void handleEditAction(TextArea codeArea, Consumer<TextArea> action) {
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
