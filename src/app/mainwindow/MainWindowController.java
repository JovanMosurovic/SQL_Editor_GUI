package app.mainwindow;

import app.ControllerBase;
import app.Window;
import app.util.*;
import cpp.JavaInterface;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController extends ControllerBase {

    public Button runButton;
    public ListView<String> tablesListView;
    public CodeArea editorArea;
    public ScrollPane resultScrollPane;
    public TextFlow consoleTextFlow;
    public TabPane resultTabPane;

    public JavaInterface databaseManager;
    private SQLExecutor sqlExecutor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.MAIN_WINDOW).setController(this);
        setupEditorArea();
        setupContextMenus();
        databaseManager = new JavaInterface();
        sqlExecutor = new SQLExecutor(databaseManager, consoleTextFlow);
    }

    private void setupEditorArea() {
        EditorHelper.setupEditorFont(editorArea);
        EditorHelper.setupEditorArea(editorArea);
    }

    private void setupContextMenus() {
        ContextMenuHelper.setupConsoleContextMenu(consoleTextFlow);
        ContextMenuHelper.setupTablesContextMenu(tablesListView);
        ContextMenuHelper.setupEditorAreaContextMenu(editorArea, consoleTextFlow);
        ContextMenuHelper.setupTabContextMenus(resultTabPane);
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
        System.out.println("[RUN] Run button clicked");
        String code = editorArea.getText();
        sqlExecutor.executeQueries(code);
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
