package app.mainwindow;

import app.ControllerBase;
import app.Window;
import app.sql.SQLExecutor;
import app.util.*;
import cpp.JavaInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Main Window in the application.
 * <p>This class handles the user interactions within the Main Window,
 * such as executing SQL queries, importing databases, saving changes,
 * and displaying the results in the console and result tabs.</p>
 *
 * @see app.ControllerBase
 * @see app.Window
 */
public class MainWindowController extends ControllerBase {

    /**
     * The button for running the SQL queries.
     */
    public Button runButton;

    /**
     * The list view for displaying the tables in the database.
     */
    public ListView<String> tablesListView;

    /**
     * The code editor area for writing SQL queries.
     */
    public CodeArea editorArea;

    /**
     * The scroll pane where the console output is displayed.
     */
    public ScrollPane resultScrollPane;

    /**
     * The text flow for displaying the console output.
     */
    public TextFlow consoleTextFlow;

    /**
     * The tab pane for displaying the result sets of the executed queries.
     */
    public TabPane resultTabPane;

    /**
     * The {@link JavaInterface} instance for executing SQL queries in the database.
     */
    public JavaInterface databaseManager;

    /**
     * The {@link SQLExecutor} instance for executing SQL queries and handling the results.
     */
    private SQLExecutor sqlExecutor;

    /**
     * The list of history entries for the executed queries.
     */
    private final ObservableList<HistoryEntry> queryHistory = FXCollections.observableArrayList();

    /**
     * The file that was imported into the database.
     */
    private File importedFile = null;

    /**
     * The name of the current database in the editor.
     */
    private String currentDatabaseName = null;

    /**
     * Flag indicating whether the query was executed from the editor.
     */
    private boolean isQueryFromEditor = true;

    /**
     * Flag indicating whether there are unsaved changes in the database.
     */
    private boolean hasUnsavedChanges = false;

    /**
     * Initializes the controller and associates it with the Main Window.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.MAIN_WINDOW).setController(this);
        setupEditorArea();
        setupContextMenus();
        databaseManager = new JavaInterface();
        sqlExecutor = new SQLExecutor(databaseManager, consoleTextFlow, this);
    }

    /**
     * Sets up the editor area with appropriate font and functionality.
     */
    private void setupEditorArea() {
        EditorHelper.setupEditorFont(editorArea);
        EditorHelper.setupEditorArea(editorArea);
    }

    /**
     * Sets up context menus for the editor area, tables list, console, and result tabs.
     */
    private void setupContextMenus() {
        ContextMenuHelper.setupConsoleContextMenu(consoleTextFlow);
        ContextMenuHelper.setupTablesContextMenu(tablesListView);
        ContextMenuHelper.setupEditorAreaContextMenu(editorArea, consoleTextFlow);
        ContextMenuHelper.setupTabContextMenus(resultTabPane);
    }

    /**
     * Handles the action of importing a database file into the application.
     * This method is invoked when the user clicks the "Import Database" option in the File menu.
     *
     * @return true if the database was imported successfully, false otherwise
     */
    public boolean handleImportDatabase() {
        return handleImportDatabase(Window.getWindowAt(Window.MAIN_WINDOW).getStage(), false);
    }

    /**
     * Handles the action of importing a database file into the application.
     *
     * @param ownerStage          the owner {@link Stage} for the file chooser dialog
     * @param isFromWelcomeWindow {@code true} if the import is from the welcome window, {@code false} otherwise
     * @return {@code true} if the database was imported successfully, {@code false} otherwise
     */
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
            clearTablesList();
            updateTablesList();
            importedFile = DatabaseManager.getLastSelectedFile();
            hasUnsavedChanges = false;

            currentDatabaseName = importedFile.getName();
            MainWindow mainWindow = (MainWindow) Window.getWindowAt(Window.MAIN_WINDOW);
            mainWindow.updateTite(currentDatabaseName);
        }

        return result;
    }

    /**
     * Handles the action of running the SQL queries from the editor area.
     * This method is invoked when the user clicks the "Run" button or presses the SHIFT + F10 shortcut.
     */
    public void handleRun() {
        System.out.println("[RUN] Run button clicked");
        String selectedText = editorArea.getSelectedText();
        String code = selectedText.isEmpty() ? editorArea.getText() : selectedText;
        if (!code.isEmpty())
            executeQuery(code);
        else
            TextFlowHelper.updateResultTextFlow(consoleTextFlow, "\nNo SQL query to execute.", Color.BLACK, true);
    }

    /**
     * Executes the given SQL queries in the database and displays the results in the application.
     *
     * @param queries the SQL queries to execute
     */
    public void executeQuery(String queries) {
        boolean hasModifyingQuery = false;
        for (String query : queries.split(";")) {
            if (!query.trim().isEmpty() && sqlExecutor.isModifyingQuery(query.trim())) {
                hasModifyingQuery = true;
                break;
            }
        }
        if (hasModifyingQuery) {
            setHasUnsavedChanges(true);
        }
        sqlExecutor.executeQueries(queries, isQueryFromEditor);
        isQueryFromEditor = true; // Reset flag after execution
    }

    /**
     * Helper function for showing/dropping tables from the tables list in the Main Window.
     *
     * @param query the SQL query to execute
     */
    public void executeQueryFromContextMenu(String query) {
        isQueryFromEditor = false;
        executeQuery(query);
    }

    /**
     * Updates the list of tables displayed in the Main Window.
     */
    public void updateTablesList() {
        databaseManager.executeQuery("SHOW TABLES");
        File outputFile = new File("output.txt");
        if (!FileHelper.checkErrors(outputFile, consoleTextFlow)) {
            List<String> tableNames = FileHelper.readTableNames("output.txt");
            tablesListView.getItems().clear();
            tablesListView.getItems().addAll(tableNames);
        }
    }

    /**
     * Removes the specified table from the list of tables displayed in the Main Window.
     * This method is invoked when the user drops a table from the tables list.
     *
     * @param tableName the name of the table to remove
     */
    public void removeTableFromList(String tableName) {
        tablesListView.getItems().remove(tableName);
    }

    /**
     * Clears the list of tables displayed in the Main Window.
     * This method is invoked when the user imports a new database file.
     */
    public void clearTablesList() {
        tablesListView.getItems().clear();
    }

    //region Editor actions

    /**
     * Handles the action of undoing the last edit in the editor area.
     * <p> Note: Does not undo any action that is not a text action in the editor. </p>
     * <p> Text actions include typing, deleting, copying, cutting, pasting, and selecting text. </p>
     */
    @FXML
    private void handleUndo() {
        EditorHelper.handleEditAction(editorArea, CodeArea::undo);
    }

    /**
     * Handles the action of redoing the last undone edit in the editor area.
     * <p>Note: Does not redo any action that is not a text action in the editor. </p>
     * <p>Text actions include typing, deleting, copying, cutting, pasting, and selecting text. </p>
     */
    @FXML
    private void handleRedo() {
        EditorHelper.handleEditAction(editorArea, CodeArea::redo);
    }

    /**
     * Handles the action of cutting the selected text in the editor area.
     */
    @FXML
    private void handleCut() {
        EditorHelper.handleEditAction(editorArea, CodeArea::cut);
    }

    /**
     * Handles the action of copying the selected text in the editor area.
     */
    @FXML
    private void handleCopy() {
        EditorHelper.handleEditAction(editorArea, CodeArea::copy);
    }

    /**
     * Handles the action of pasting the copied/cut text in the editor area.
     */
    @FXML
    private void handlePaste() {
        EditorHelper.handleEditAction(editorArea, CodeArea::paste);
    }

    /**
     * Handles the action of selecting all text in the editor area.
     */
    @FXML
    private void handleSelectAll() {
        EditorHelper.handleEditAction(editorArea, CodeArea::selectAll);
    }

    //endregion

    /**
     * Handles the action of saving the database to the imported file.
     * <p>This method is invoked when the user clicks the "Save" option in the File menu. </p>
     */
    @FXML
    private void handleSave() {
        if (importedFile != null) {
            // Save to the imported file
            saveFile(importedFile);
        } else {
            // If no file has been imported, behave like Save As
            handleSaveAs();
        }
    }

    /**
     * Handles the action of saving the database to a new file.
     * <p>This method is invoked when the user clicks the "Save As" option in the File menu or when new database is created. </p>
     */
    @FXML
    private void handleSaveAs() {
        Window.showWindow(Window.SAVE_WINDOW);
    }

    /**
     * Saves the database to the specified file.
     *
     * @param file the {@link File} to save the database to
     */
    public void saveFile(File file) {
        boolean isSaveAs = !file.equals(importedFile);

        if (!hasUnsavedChanges && !isSaveAs) {
            TextFlowHelper.updateResultTextFlow(consoleTextFlow, "\n[INFO] No changes to save.", Color.BLACK, true);
            return;
        }

        try {
            String format = file.getName().endsWith(".sql") ? "sql" : "dbexp";
            JavaInterface.getInstance().exportDatabase(format, file.getAbsolutePath());
            TextFlowHelper.updateResultTextFlow(consoleTextFlow, "\n[SAVE] File saved successfully: " + file.getAbsolutePath(), Color.GREEN, true);
            hasUnsavedChanges = false;
            importedFile = file;
        } catch (Exception e) {
            TextFlowHelper.updateResultTextFlow(consoleTextFlow, "\n[ERROR] Error saving file: " + e.getMessage(), Color.RED, true);
        }
    }

    /**
     * Handles the action of showing the settings window.
     * <p>This method is invoked when the user clicks the "Settings" option in the File menu. </p>
     */
    @FXML
    private void handleSettings() {
        Window.showWindow(Window.SETTINGS_WINDOW);
    }

    /**
     * Handles the action of showing the history window.
     * <p>This method is invoked when the user clicks the "History" option in the File menu. </p>
     */
    @FXML
    private void handleShowHistory() {
        Window.showWindow(Window.HISTORY_WINDOW);
    }

    /**
     * Handles the action of showing the about window.
     * <p>This method is invoked when the user clicks the "About" option in the File menu. </p>
     */
    @FXML
    private void handleAbout() {
        Window.showWindow(Window.ABOUT_WINDOW);
    }

    /**
     * Handles the action of closing the application.
     * <p>This method is invoked when the user clicks the "Force quit" option in the File menu. </p>
     */
    @FXML
    private void handleExit() {
        Window.showWindow(Window.FORCE_QUIT_WINDOW);
    }


    /**
     * Getter for the name of the current database in the editor.
     *
     * @param name the name of the current database
     */
    public void setCurrentDatabaseName(String name) {
        this.currentDatabaseName = name;
        MainWindow mainWindow = (MainWindow) Window.getWindowAt(Window.MAIN_WINDOW);
        mainWindow.updateTite(this.currentDatabaseName);
    }

    /**
     * Getter for the flag indicating whether there are unsaved changes in the database.
     *
     * @return {@code true} if there are unsaved changes, {@code false} otherwise
     */
    public boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }

    /**
     * Setter for the flag indicating whether there are unsaved changes in the database.
     *
     * @param value the value to set
     */
    public void setHasUnsavedChanges(boolean value) {
        hasUnsavedChanges = value;
    }

    /**
     * Setter for the text in the editor area.
     * <p>Used for history entries and copying queries from the history window to the editor area. </p>
     *
     * @param text the text to set in the editor area
     */
    public void setEditorText(String text) {
        editorArea.replaceText(text);
    }

    public void addToHistory(String query) {
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        queryHistory.add(new HistoryEntry(timestamp, query));
    }

    public ObservableList<HistoryEntry> getQueryHistory() {
        return queryHistory;
    }

}
