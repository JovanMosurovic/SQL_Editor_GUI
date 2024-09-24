package app.util;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for setting up and managing the behavior of a code editor area.
 * <p>It includes methods for configuring font settings, handling edit actions and syntax highlighting.</p>
 */
public class EditorHelper {

    /**
     * A map of SQL keywords to their associated colors for syntax highlighting.
     */
    private static final HashMap<String, String> KEYWORD_COLORS = EditorHelper.initKeywordColors();

    /**
     * A pattern to match SQL keywords for syntax highlighting.
     */
    private static final Pattern KEYWORD_PATTERN = Pattern.compile(
            "(?<=\\s|^)(" + String.join("|", KEYWORD_COLORS.keySet()) + ")(?=\\s|$|;|\\()|" +
                    "(AVG|SUM|COUNT|MAX|MIN)\\s*\\([^)]*\\)|" +
                    "(\"[^\"]*\"|'[^']*')|" +
                    "(;)|" +
                    "(--.*$)", // Add this line to match comments
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
    );

    /**
     * Sets up the font for the code editor area based on the current font configuration.
     *
     * @param editorArea the {@link CodeArea} component representing the code editor
     */
    public static void setupEditorFont(CodeArea editorArea) {
        FontHelper.setFontSize((int) FontConfig.getEditorFontSize(), editorArea);
        editorArea.setId(FontConfig.CONSOLAS_FONT.toLowerCase());
    }

    /**
     * Sets up the code editor area, adding line numbers and configuring behavior for key events
     * to trigger syntax highlighting.
     *
     * @param editorArea the {@link CodeArea} component representing the code editor
     */
    public static void setupEditorArea(CodeArea editorArea) {
        editorArea.setParagraphGraphicFactory(LineNumberFactory.get(editorArea));

        editorArea.textProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> {
            int caretPosition = editorArea.getCaretPosition();
            editorArea.setStyleSpans(0, computeHighlighting(editorArea));
            editorArea.moveTo(caretPosition);
        }));

        editorArea.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.ENTER) {
                Platform.runLater(() -> {
                    int caretPosition = editorArea.getCaretPosition();
                    editorArea.setStyleSpans(0, computeHighlighting(editorArea));
                    editorArea.moveTo(caretPosition);
                });
            }
        });
    }

    /**
     * Computes and returns the highlighting styles for the code in the editor area.
     *
     * @param editorArea The {@link CodeArea} representing the code editor.
     * @return A {@link StyleSpans} object containing the styles for syntax highlighting.
     */
    public static StyleSpans<Collection<String>> computeHighlighting(CodeArea editorArea) {
        String text = editorArea.getText();
        Matcher matcher = KEYWORD_PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        boolean textChanged = false;

        while (matcher.find()) {
            String matchedText = matcher.group();
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);

            if (matchedText.startsWith("--")) {
                // It's a comment
                spansBuilder.add(Collections.singleton("comment"), matchedText.length());
            } else if ((matchedText.startsWith("\"") && matchedText.endsWith("\"")) ||
                    (matchedText.startsWith("'") && matchedText.endsWith("'"))) {
                // It's a quoted string
                spansBuilder.add(Collections.singleton("string"), matchedText.length());
            } else if (matchedText.equals(";")) {
                // It's a semicolon
                spansBuilder.add(Collections.singleton("semicolon"), 1);
            } else {
                // It's a keyword or other text
                String upperCaseKeyword = matchedText.toUpperCase();
                String styleClass = KEYWORD_COLORS.get(upperCaseKeyword);
                if (styleClass != null) {
                    spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
                    if (!matchedText.equals(upperCaseKeyword)) {
                        // Replace the matched text with uppercase version only if it's different
                        text = text.substring(0, matcher.start()) + upperCaseKeyword + text.substring(matcher.end());
                        textChanged = true;
                    }
                } else {
                    spansBuilder.add(Collections.emptyList(), matcher.end() - matcher.start());
                }
            }

            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);

        // Update the text in the editor with uppercase keywords only if changes were made
        if (textChanged) {
            editorArea.replaceText(text);
        }

        return spansBuilder.create();
    }

    /**
     * Initializes the color mappings for SQL keywords used in syntax highlighting.
     *
     * @return A map of SQL keywords to their associated colors.
     */
    public static HashMap<String, String> initKeywordColors() {
        HashMap<String, String> keywordColors = new HashMap<>();
        keywordColors.put("SELECT", "red");
        keywordColors.put("DISTINCT", "red");
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
        keywordColors.put("AS", "green");
        keywordColors.put(",", "orange");
        keywordColors.put("GROUP", "darkorchid");
        keywordColors.put("BY", "darkorchid");
        keywordColors.put("HAVING", "darkorchid");
        keywordColors.put("ORDER", "darkorchid");
        keywordColors.put("ASC", "darkorchid");
        keywordColors.put("DESC", "darkorchid");
        keywordColors.put("LIMIT", "darkslateblue");
        keywordColors.put("OFFSET", "darkslateblue");
        keywordColors.put("AVG", "skyblue");
        keywordColors.put("SUM", "skyblue");
        keywordColors.put("COUNT", "skyblue");
        keywordColors.put("MAX", "skyblue");
        keywordColors.put("MIN", "skyblue");
        return keywordColors;
    }

    /**
     * Executes an editing action on the code area.
     *
     * @param codeArea The {@link CodeArea} where the action will be performed.
     * @param action   The {@link Consumer} representing the editing action (e.g., undo, redo).
     */
    public static void handleEditAction(CodeArea codeArea, Consumer<CodeArea> action) {
        action.accept(codeArea);
    }

    /**
     * Increases the font size of the code editor by the step defined in the FontConfig class and updates the font size in the configuration.
     *
     * @param resultTextFlow The {@link TextFlow} component for displaying console messages.
     * @param codeArea           The {@link CodeArea} (code editor) whose font size will be increased.
     */
    public static void increaseEditorFontSize(TextFlow resultTextFlow, CodeArea codeArea) {
        double currentFontSize = FontConfig.getEditorFontSize();
        if (currentFontSize < FontConfig.EDITOR_MAX_FONT_SIZE) {
            FontHelper.increaseFontSize(FontConfig.FONT_STEP, codeArea);
            FontConfig.updateFontSizeConfig(currentFontSize + FontConfig.FONT_STEP, codeArea);
        } else {
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "\n[FONT SIZE]: Maximum font size  reached", Color.RED, true);
        }
    }

    /**
     * Decreases the font size of the code editor by the step defined in the FontConfig class and updates the font size in the configuration.
     *
     * @param resultTextFlow The {@link TextFlow} component for displaying console messages.
     * @param codeArea           The {@link CodeArea} (code editor) whose font size will be increased.
     */
    public static void decreaseEditorFontSize(TextFlow resultTextFlow, CodeArea codeArea) {
        double currentFontSize = FontConfig.getEditorFontSize();
        if (currentFontSize > FontConfig.EDITOR_MIN_FONT_SIZE) {
            FontHelper.decreaseFontSize(FontConfig.FONT_STEP, codeArea);
            FontConfig.updateFontSizeConfig(currentFontSize - FontConfig.FONT_STEP, codeArea);
        } else {
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "\n[FONT SIZE]: Minimum font size for code area reached", Color.RED, true);
        }
    }
}
