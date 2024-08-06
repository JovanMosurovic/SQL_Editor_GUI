package app.util;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class CodeAreaHelper {

    private static final double MAX_FONT_SIZE = 72.0;
    private static final double MIN_FONT_SIZE = 2.0;
    private static double currentFontSize = 16.0;
    private static final double FONT_SIZE_STEP = 2.0;


    public static void setupCodeAreaFont(TextArea codeArea) {
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

    public static void increaseFontSize(TextFlow resultTextFlow, Node node) {
        if(currentFontSize < MAX_FONT_SIZE) {
            changeFontSize(node, FONT_SIZE_STEP);
        } else {
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "\n[FONT SIZE]: Maximum font size reached", Color.RED, true);
        }
    }

    public static void decreaseFontSize(TextFlow resultTextFlow, Node node) {
        if(currentFontSize > MIN_FONT_SIZE) {
            changeFontSize(node, -FONT_SIZE_STEP);
        } else {
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "\n[FONT SIZE]: Minimum font size reached", Color.RED, true);
        }
    }

    private static void changeFontSize(Node node, double fontSizeStep) {
        currentFontSize += fontSizeStep;
        try {
            Method setFontMethod = node.getClass().getMethod("setFont", Font.class);
            Font currentFont = (Font) node.getClass().getMethod("getFont").invoke(node);
            Font newFont = Font.font(currentFont.getFamily(), currentFontSize);
            setFontMethod.invoke(node, newFont);
        } catch (Exception e) {
            System.out.println("This component does not support font changes: " + node.getClass().getName());
        }
    }
}
