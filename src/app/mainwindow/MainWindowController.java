package app.mainwindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;
import app.util.CodeAreaHelper;
import app.util.DatabaseManager;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
        return DatabaseManager.showImportDatabaseDialog(
                ownerStage,
                resultTextFlow,
                isFromWelcomeWindow,
                new FileChooser.ExtensionFilter("SQL Files", "*.sql"),
                new FileChooser.ExtensionFilter("Custom format files", "*dbexp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
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
