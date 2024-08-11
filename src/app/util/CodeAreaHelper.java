package app.util;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;

import java.util.function.Consumer;

public class CodeAreaHelper {
    private static final double FONT_SIZE_STEP = 2.0;

    public static void setupCodeAreaFont(TextArea codeArea) {
        final double currentFontSize = FontSizeConfig.getEditorFontSize();
        final Font jetBrainsMono = Font.loadFont(CodeAreaHelper.class.getResourceAsStream("/app/resources/fonts/JetBrainsMonoNL-Regular.ttf"), currentFontSize);
        final Font fontToUse = (jetBrainsMono != null) ? jetBrainsMono : Font.font("Monospaced", currentFontSize);
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

    public static void increaseCodeAreaFontSize(TextFlow resultTextFlow, Node node) {
        double currentFontSize = FontSizeConfig.getEditorFontSize();
        if(currentFontSize < FontSizeConfig.getEditorMaxFontSize()) {
            FontHelper.increaseFontSize(FONT_SIZE_STEP, node);
            FontSizeConfig.setEditorFontSize(currentFontSize + FONT_SIZE_STEP);
        } else {
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "\n[FONT SIZE]: Maximum font size  reached", Color.RED, true);
        }
    }

    public static void decreaseCodeAreaFontSize(TextFlow resultTextFlow, Node node) {
        double currentFontSize = FontSizeConfig.getEditorFontSize();
        if(currentFontSize > FontSizeConfig.getEditorMinFontSize()) {
            FontHelper.decreaseFontSize(FONT_SIZE_STEP, node);
            FontSizeConfig.setEditorFontSize(currentFontSize - FONT_SIZE_STEP);
        } else {
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "\n[FONT SIZE]: Minimum font size for code area reached", Color.RED, true);
        }
    }
}
