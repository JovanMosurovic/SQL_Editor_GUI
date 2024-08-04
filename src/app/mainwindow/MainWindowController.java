package app.mainwindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainWindowController extends ControllerBase {

    public Button runButton;
    public ListView<String> tablesListView;
    public TextArea codeArea;
    public ScrollPane resultScrollPane;
    public TextFlow resultTextFlow;
    public TableView<String> resultTableView;
    private static final double MAX_FONT_SIZE = 72.0;
    private static final double MIN_FONT_SIZE = 2.0;
    private double currentFontSize = 16.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupCodeAreaFont();
        Window.getWindowAt(Window.MAIN_WINDOW).setController(this);
        setupSceneListener();
    }

    private void setupCodeAreaFont() {
        Font jetBrainsMono = Font.loadFont(getClass().getResourceAsStream("/app/resources/fonts/JetBrainsMonoNL-Regular.ttf"), currentFontSize);
        if(jetBrainsMono == null) {
            jetBrainsMono = Font.font("Monospaced", currentFontSize);
            System.out.println("Failed to load JetBrains Mono font. Using default monospaced font.");
        }
        codeArea.setFont(jetBrainsMono);
    }

    private void setupSceneListener() {
        codeArea.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                addShortcuts();
            }
        });
    }

    private void addShortcuts() {
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN), this::handleUndo);
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN), this::handleRedo);
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN), this::handleCut);
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN), this::handleCopy);
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN), this::handlePaste);
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN), this::handleSelectAll);
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.EQUALS, KeyCombination.CONTROL_DOWN), this::increaseFontSize);
        codeArea.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.MINUS, KeyCombination.CONTROL_DOWN), this::decreaseFontSize);
    }

    public void handleSaveAs() {
        WindowHelper.showWindow(Window.SAVE_WINDOW);
    }

    public boolean handleImportDatabase() {
        return showImportDatabaseDialog(
                new FileChooser.ExtensionFilter("SQL Files", "*.sql"),
                new FileChooser.ExtensionFilter("Custom format files", "*dbexp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
    }

    private boolean showImportDatabaseDialog(FileChooser.ExtensionFilter... filters) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Database");
        fileChooser.getExtensionFilters().addAll(filters);

        Stage stage = (Stage) tablesListView.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        clearResultTextFlow();
        if (selectedFile == null) {
            updateResultTextFlow("No file selected. Please select a valid .sql or .dbexp file.", Color.RED, false);
            return false;
        }

        String filePath = selectedFile.getAbsolutePath();
        if (filePath.endsWith(".sql") || filePath.endsWith(".dbexp")) {
            // TODO: import database
            updateResultTextFlow("Selected file: " + filePath, Color.BLACK, false);
            updateResultTextFlow("\nDatabase imported successfully!", Color.GREEN, true);
            return true;
        } else {
            updateResultTextFlow("Invalid file type selected. Please select a .sql or .dbexp file.", Color.RED, false);
            return false;
        }
    }

    private void updateResultTextFlow(String message, Color color, boolean append) {
        if (!append) {
            clearResultTextFlow();
        }
        Text text = new Text(message);
        text.setFill(color);
        resultTextFlow.getChildren().add(text);
    }

    private void clearResultTextFlow() {
        resultTextFlow.getChildren().clear();
    }

    public void handleClose() {}

    public void handleRun() {
        System.out.println("Run button clicked");
        //todo: run the code
    }

    private void handleEditAction(Consumer<TextArea> action) {
        action.accept(codeArea);
    }

    public void handleUndo() {
        handleEditAction(TextArea::undo);
    }

    public void handleRedo() {
        handleEditAction(TextArea::redo);
    }

    public void handleCut() {
        handleEditAction(TextArea::cut);
    }

    public void handleCopy() {
        handleEditAction(TextArea::copy);
    }

    public void handlePaste() {
        handleEditAction(TextArea::paste);
    }

    public void handleSelectAll() {
        handleEditAction(TextArea::selectAll);
    }

    private void changeFontSize(double size) {
        currentFontSize += size;
        codeArea.setFont(Font.font(codeArea.getFont().getFamily(), currentFontSize));
    }

    public void increaseFontSize() {
        if(currentFontSize < MAX_FONT_SIZE) {
            changeFontSize(2.0);
        }
        else {
            updateResultTextFlow("\nCannot increase font size further.", Color.RED, true);
        }
    }

    public void decreaseFontSize() {
        if(currentFontSize > MIN_FONT_SIZE) {
            changeFontSize(-2.0);
        }
        else {
            updateResultTextFlow("\nCannot decrease font size further.", Color.RED, true);
        }
    }

    public void handleAbout() {
        WindowHelper.showWindow(Window.ABOUT_WINDOW);
    }
}
