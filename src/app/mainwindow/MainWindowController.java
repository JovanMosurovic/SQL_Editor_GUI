package app.mainwindow;

import app.ControllerBase;
import app.Window;
import app.util.*;
import cpp.JavaInterface;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.MAIN_WINDOW).setController(this);
        setupEditorArea();
        setupContextMenus();
        databaseManager = new JavaInterface();
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
        String code = EditorHelper.trimCode(editorArea.getText());
        System.out.println("[RUN] Code: " + code);

        String[] splitCode = code.split(";");

        long startTime = System.nanoTime();
        boolean hasError = false;

        for(String s : splitCode) {
            if(s.isEmpty()) {
                continue;
            }
            String formattedQuery = SQLFormatter.formatSQLQuery(s.trim());
            System.out.println("[RUN] Executing formatted query: " + formattedQuery);
            databaseManager.executeQuery(formattedQuery);

            File outputFile = new File("output.txt");
            if(FileHelper.checkErrors(outputFile, consoleTextFlow)) {
                hasError = true;
                break;
            }

            boolean isSelectQuery = formattedQuery.startsWith("SELECT");
            FileHelper.loadTablesFromFile("output.txt", isSelectQuery);
        }

        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;

        if(!hasError) {
            AnsiTextParser.parseAnsiText("\nQuery has been \033[1;32m\033[1msuccessfully\033[0m executed!\n", consoleTextFlow);
            AnsiTextParser.parseAnsiText("\033[1m\033[4mExecution time\033[0m: ", consoleTextFlow);
            TextFlowHelper.updateResultTextFlow(consoleTextFlow, String.format("%.2f ms\n", (double) executionTime / 1000000), Color.BLACK, true);
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
