package app.mainwindow;

import app.ControllerBase;
import app.Window;
import app.util.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainWindowController extends ControllerBase {

    public Button runButton;
    public ListView<String> tablesListView;
    public CodeArea editorArea;
    public ScrollPane resultScrollPane;
    public TextFlow consoleTextFlow;
    public TableView<String> resultTableView;

    private static final HashMap<String, String> KEYWORD_COLORS = EditorHelper.initKeywordColors();

    private static final Pattern KEYWORD_PATTERN = Pattern.compile(
            "\\b(" + String.join("|", KEYWORD_COLORS.keySet()) + ")\\b",
            Pattern.CASE_INSENSITIVE
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EditorHelper.setupEditorFont(editorArea);
        Window.getWindowAt(Window.MAIN_WINDOW).setController(this);
        setupEditorArea();
        setupContextMenus();
    }

    private void setupEditorArea() {
        IntFunction<Node> numberFactory = LineNumberFactory.get(editorArea);
        IntFunction<Node> graphicFactory = line -> {
            HBox hbox = new HBox(
                    numberFactory.apply(line)
            );
            hbox.setAlignment(Pos.CENTER_RIGHT);
            hbox.setPrefWidth(30);
            return hbox;
        };
        editorArea.setParagraphGraphicFactory(graphicFactory);

        editorArea.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.ENTER) {
                Platform.runLater(() -> {
                    int caretPosition = editorArea.getCaretPosition();
                    editorArea.setStyleSpans(0, computeHighlighting(editorArea.getText()));
                    editorArea.moveTo(caretPosition);
                });
            }
        });
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = KEYWORD_PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        while (matcher.find()) {
            String matchedText = matcher.group();
            String upperCaseKeyword = matchedText.toUpperCase();
            String styleClass = KEYWORD_COLORS.get(upperCaseKeyword);

            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();

            // Replace the matched text with uppercase version
            text = text.substring(0, matcher.start()) + upperCaseKeyword + text.substring(matcher.end());
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);

        // Update the text in the editor with uppercase keywords
        editorArea.replaceText(text);

        return spansBuilder.create();
    }

    private void setupContextMenus() {
        setupConsoleContextMenu();
        setupTablesContextMenu();
        //todo setupEditorAreaContextMenu();
    }

    private void setupConsoleContextMenu() {
        ContextMenu consoleContextMenu = ContextMenuHelper.createConsoleContextMenu(consoleTextFlow);

        consoleTextFlow.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                consoleContextMenu.show(consoleTextFlow, event.getScreenX(), event.getScreenY());
            } else {
                consoleContextMenu.hide();
            }
        });
    }

    private void setupTablesContextMenu() {
        ContextMenu tablesContextMenu = ContextMenuHelper.createTableListViewContextMenu();

        tablesListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY && !tablesListView.getSelectionModel().isEmpty()) {
                tablesContextMenu.show(tablesListView, event.getScreenX(), event.getScreenY());
            } else {
                tablesContextMenu.hide();
            }
        });
    }

    public boolean handleImportDatabase() {
        return handleImportDatabase(Window.getWindowAt(Window.MAIN_WINDOW).getStage(), false);
    }

    public boolean handleImportDatabase(Stage ownerStage, boolean isFromWelcomeWindow) {
        return DatabaseManager.showImportDatabaseDialog(
                ownerStage,
                consoleTextFlow,
                isFromWelcomeWindow,
                new FileChooser.ExtensionFilter("SQL Files", "*.sql"),
                new FileChooser.ExtensionFilter("Custom format files", "*dbexp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
    }

    public void handleRun() {
        System.out.println("Run button clicked");
        String code = editorArea.getText();
        System.out.println("Code: |" + code + "|");

    }

    //region Editor actions

    @FXML
    private void handleUndo() {
        EditorHelper.handleEditAction(editorArea, CodeArea::undo);
    }

    @FXML
    private void handleRedo() {
        EditorHelper.handleEditAction(editorArea, CodeArea::redo);
    }

    @FXML
    private void handleCut() {
        EditorHelper.handleEditAction(editorArea, CodeArea::cut);
    }

    @FXML
    private void handleCopy() {
        EditorHelper.handleEditAction(editorArea, CodeArea::copy);
    }

    @FXML
    private void handlePaste() {
        EditorHelper.handleEditAction(editorArea, CodeArea::paste);
    }

    @FXML
    private void handleSelectAll() {
        EditorHelper.handleEditAction(editorArea, CodeArea::selectAll);
    }

    //endregion

    @FXML
    private void handleSaveAs() {
        Window.showWindow(Window.SAVE_WINDOW);
    }

    @FXML
    private void handleSettings() {
        Window.showWindow(Window.SETTINGS_WINDOW);
    }

    @FXML
    private void handleAbout() {
        Window.showWindow(Window.ABOUT_WINDOW);
    }

    @FXML
    private void handleExit() {
        Window.closeAllWindows();
    }

}
