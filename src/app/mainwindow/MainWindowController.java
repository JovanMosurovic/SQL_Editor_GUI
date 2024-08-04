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
    private double currentFontSize = 16.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupCodeAreaFont();
        Window.getWindowAt(Window.MAIN_WINDOW).setController(this);
        setupSceneListener();
    }

    private void setupCodeAreaFont() {
        Font jetBrainsMono = Font.loadFont(getClass().getResourceAsStream("/app/resources/fonts/JetBrainsMonoNL-Regular.ttf"), 16);
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
            updateResultTextFlow("No file selected. Please select a valid .sql or .dbexp file.", Color.RED);
            return false;
        }

        String filePath = selectedFile.getAbsolutePath();
        if (filePath.endsWith(".sql") || filePath.endsWith(".dbexp")) {
            // TODO: import database
            updateResultTextFlow("Selected file: " + filePath, Color.BLACK);
            updateResultTextFlow("\nDatabase imported successfully!", Color.GREEN);
            return true;
        } else {
            updateResultTextFlow("Invalid file type selected. Please select a .sql or .dbexp file.", Color.RED);
            return false;
        }
    }

    private void updateResultTextFlow(String message, Color color) {
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
        codeArea.setFont(Font.font(currentFontSize));
    }

    public void increaseFontSize() {
        changeFontSize(2.0);
    }

    public void decreaseFontSize() {
        if(currentFontSize > 2.0) {
            changeFontSize(-2.0);
        }
    }

    public void handleAbout() {
        //todo: show about dialog
    }
}
