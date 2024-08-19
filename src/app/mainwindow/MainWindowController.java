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

import java.io.File;
import java.net.URL;
import java.util.List;
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
    private boolean isQueryFromEditor = true;

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
        boolean result = DatabaseManager.showImportDatabaseDialog(
                ownerStage,
                consoleTextFlow,
                isFromWelcomeWindow,
                new FileChooser.ExtensionFilter("SQL Files", "*.sql"),
                new FileChooser.ExtensionFilter("Custom format files", "*dbexp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        if (result) {
            updateTablesList();
        }

        return result;
    }

    public void handleRun() {
        System.out.println("[RUN] Run button clicked");
        String selectedText = editorArea.getSelectedText();
        String code = selectedText.isEmpty() ? editorArea.getText() : selectedText;
        sqlExecutor.executeQueries(code, true);
    }

    public void executeQuery(String query) {
        sqlExecutor.executeQueries(query, isQueryFromEditor);
        isQueryFromEditor = true; // Reset flag after execution
    }

    public void executeQueryFromContextMenu(String query) {
        isQueryFromEditor = false;
        executeQuery(query);
    }

    public void updateTablesList() {
        databaseManager.executeQuery("SHOW TABLES");
        File outputFile = new File("output.txt");
        if (!FileHelper.checkErrors(outputFile, consoleTextFlow)) {
            List<String> tableNames = FileHelper.readTableNames("output.txt");
            tablesListView.getItems().setAll(tableNames);
        }
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
