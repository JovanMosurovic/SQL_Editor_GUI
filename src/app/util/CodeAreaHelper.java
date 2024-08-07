package app.util;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;

import java.util.function.Consumer;

public class CodeAreaHelper {
    private static final double FONT_SIZE_STEP = 2.0;

    public static void setupCodeAreaFont(TextArea codeArea) {
        double currentFontSize = FontSizeConfig.getCodeAreaFontSize();
        Font jetBrainsMono = Font.loadFont(CodeAreaHelper.class.getResourceAsStream("/app/resources/fonts/JetBrainsMonoNL-Regular.ttf"), currentFontSize);
        if(jetBrainsMono == null) {
            jetBrainsMono = Font.font("Monospaced", currentFontSize);
            System.out.println("[CODE AREA]: Failed to load JetBrains Mono font. Using default monospaced font.");
        }
        codeArea.setFont(jetBrainsMono);
    }

    public static void addShortcuts(TextArea codeArea, Runnable handleSaveAs, Runnable handleImportDatabase, Runnable handleUndo, Runnable handleRedo, Runnable handleCut, Runnable handleCopy, Runnable handlePaste, Runnable handleSelectAll, Runnable increaseFontSize, Runnable decreaseFontSize) {
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), handleSaveAs);
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN), handleImportDatabase);
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN), handleUndo);
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN), handleRedo);
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN), handleCut);
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN), handleCopy);
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN), handlePaste);
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN), handleSelectAll);
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.EQUALS, KeyCombination.CONTROL_DOWN), increaseFontSize);
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.MINUS, KeyCombination.CONTROL_DOWN), decreaseFontSize);
    }

    public static void handleEditAction(TextArea codeArea, Consumer<TextArea> action) {
        action.accept(codeArea);
    }

    public static void increaseCodeAreaFontSize(TextFlow resultTextFlow, Node node) {
        double currentFontSize = FontSizeConfig.getCodeAreaFontSize();
        if(currentFontSize < FontSizeConfig.getCodeAreaMaxFontSize()) {
            FontHelper.increaseFontSize(FONT_SIZE_STEP, node);
            FontSizeConfig.setCodeAreaFontSize(currentFontSize + FONT_SIZE_STEP);
        } else {
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "\n[FONT SIZE]: Maximum font size  reached", Color.RED, true);
        }
    }

    public static void decreaseCodeAreaFontSize(TextFlow resultTextFlow, Node node) {
        double currentFontSize = FontSizeConfig.getCodeAreaFontSize();
        if(currentFontSize > FontSizeConfig.getCodeAreaMinFontSize()) {
            FontHelper.decreaseFontSize(FONT_SIZE_STEP, node);
            FontSizeConfig.setCodeAreaFontSize(currentFontSize - FONT_SIZE_STEP);
        } else {
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "\n[FONT SIZE]: Minimum font size for code area reached", Color.RED, true);
        }
    }
}
