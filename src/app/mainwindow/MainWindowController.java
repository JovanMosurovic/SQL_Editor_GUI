package app.mainwindow;

import app.ControllerBase;
import app.Window;
import app.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
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
    public TableView<String> resultTableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EditorHelper.setupEditorFont(editorArea);
        Window.getWindowAt(Window.MAIN_WINDOW).setController(this);
        setupContextMenus();
    }

    private void setupContextMenus() {
        setupConsoleContextMenu();
        setupTablesContextMenu();
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
