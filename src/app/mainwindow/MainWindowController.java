package app.mainwindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;
import app.util.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController extends ControllerBase {

    public Button runButton;
    public ListView<String> tablesListView;
    public TextArea editorArea;
    public ScrollPane resultScrollPane;
    public TextFlow consoleTextFlow;
    public TableView<String> resultTableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EditorHelper.setupEditorFont(editorArea);
        Window.getWindowAt(Window.MAIN_WINDOW).setController(this);
        setupContextMenu();
    }

    private void setupContextMenu() {
        ContextMenu consoleContextMenu = ContextMenuHelper.createConsoleContextMenu(consoleTextFlow);

        consoleTextFlow.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                consoleContextMenu.show(consoleTextFlow, event.getScreenX(), event.getScreenY());
            } else {
                consoleContextMenu.hide();
            }
        });

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

    //region Code area actions

    public void handleUndo() {
        EditorHelper.handleEditAction(editorArea, TextArea::undo);
    }

    public void handleRedo() {
        EditorHelper.handleEditAction(editorArea, TextArea::redo);
    }

    public void handleCut() {
        EditorHelper.handleEditAction(editorArea, TextArea::cut);
    }

    public void handleCopy() {
        EditorHelper.handleEditAction(editorArea, TextArea::copy);
    }

    public void handlePaste() {
        EditorHelper.handleEditAction(editorArea, TextArea::paste);
    }

    public void handleSelectAll() {
        EditorHelper.handleEditAction(editorArea, TextArea::selectAll);
    }

    //endregion

    public void handleSaveAs() {
        WindowHelper.showWindow(Window.SAVE_WINDOW);
    }

    public void handleSettings() {
        WindowHelper.showWindow(Window.SETTINGS_WINDOW);
    }
    public void handleAbout() {
        WindowHelper.showWindow(Window.ABOUT_WINDOW);
    }

    public void handleExit() {
        WindowHelper.closeAllWindows();
    }

}
