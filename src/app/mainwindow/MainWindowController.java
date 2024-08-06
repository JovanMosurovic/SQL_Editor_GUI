package app.mainwindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;
import app.util.CodeAreaHelper;
import app.util.TextFlowHelper;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController extends ControllerBase {

    public Button runButton;
    public ListView<String> tablesListView;
    public TextArea codeArea;
    public ScrollPane resultScrollPane;
    public TextFlow resultTextFlow;
    public TableView<String> resultTableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CodeAreaHelper.setupCodeAreaFont(codeArea);
        Window.getWindowAt(Window.MAIN_WINDOW).setController(this);
        setupSceneListener();
    }

    private void setupSceneListener() {
        codeArea.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                CodeAreaHelper.addShortcuts(
                        codeArea,
                        this::handleSaveAs,
                        this::handleImportDatabase,
                        this::handleUndo,
                        this::handleRedo,
                        this::handleCut,
                        this::handleCopy,
                        this::handlePaste,
                        this::handleSelectAll,
                        this::increaseFontSize,
                        this::decreaseFontSize
                );
            }
        });
    }

    public void handleSaveAs() {
        WindowHelper.showWindow(Window.SAVE_WINDOW);
    }

    public boolean handleImportDatabase() {
        return handleImportDatabase(Window.getWindowAt(Window.MAIN_WINDOW).getStage(), false);
    }

    public boolean handleImportDatabase(Stage ownerStage, boolean isFromWelcomeWindow) {
        return showImportDatabaseDialog(
                ownerStage,
                isFromWelcomeWindow,
                new FileChooser.ExtensionFilter("SQL Files", "*.sql"),
                new FileChooser.ExtensionFilter("Custom format files", "*dbexp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
    }

    private boolean showImportDatabaseDialog(Stage ownerStage, boolean isFromWelcomeWindow, FileChooser.ExtensionFilter... filters) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Database");
        fileChooser.getExtensionFilters().addAll(filters);

        File selectedFile = fileChooser.showOpenDialog(ownerStage);
        TextFlowHelper.clearResultTextFlow(resultTextFlow);
        if (selectedFile == null) {
            if(!isFromWelcomeWindow) {
                TextFlowHelper.updateResultTextFlow(resultTextFlow, "No file selected. Please select a valid .sql or .dbexp file.", Color.RED, false);
            }
            return false;
        }

        String filePath = selectedFile.getAbsolutePath();
        if (filePath.endsWith(".sql") || filePath.endsWith(".dbexp")) {
            // TODO: import database
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "Selected file: " + filePath, Color.BLACK, false);
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "\nDatabase imported successfully!", Color.GREEN, true);
            return true;
        } else {
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "Invalid file type selected. Please select a .sql or .dbexp file.", Color.RED, false);
            return false;
        }
    }

    public void handleClose() {}

    public void handleRun() {
        System.out.println("Run button clicked");
        //todo: run the code
    }


    public void handleUndo() {
        CodeAreaHelper.handleEditAction(codeArea, TextArea::undo);
    }

    public void handleRedo() {
        CodeAreaHelper.handleEditAction(codeArea, TextArea::redo);
    }

    public void handleCut() {
        CodeAreaHelper.handleEditAction(codeArea, TextArea::cut);
    }

    public void handleCopy() {
        CodeAreaHelper.handleEditAction(codeArea, TextArea::copy);
    }

    public void handlePaste() {
        CodeAreaHelper.handleEditAction(codeArea, TextArea::paste);
    }

    public void handleSelectAll() {
        CodeAreaHelper.handleEditAction(codeArea, TextArea::selectAll);
    }

    public void increaseFontSize() {
        CodeAreaHelper.increaseFontSize(resultTextFlow, codeArea);
    }

    public void decreaseFontSize() {
        CodeAreaHelper.decreaseFontSize(resultTextFlow, codeArea);
    }

    public void handleAbout() {
        WindowHelper.showWindow(Window.ABOUT_WINDOW);
    }
}
