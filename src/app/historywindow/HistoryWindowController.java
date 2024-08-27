package app.historywindow;

import app.ControllerBase;
import app.Window;
import app.mainwindow.MainWindowController;
import app.util.ContextMenuHelper;
import app.util.HistoryEntry;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller class for the history window.
 */
public class HistoryWindowController extends ControllerBase {

    /**
     * The table view for displaying the history entries.
     */
    @FXML
    private TableView<HistoryEntry> historyTableView;
    /**
     * The table column for displaying the date and time of the history entry.
     */
    @FXML
    private TableColumn<HistoryEntry, String> dateTimeColumn;
    /**
     * The table column for displaying the query of the history entry.
     */
    @FXML
    private TableColumn<HistoryEntry, String> queryColumn;
    /**
     * The main window controller field
     */
    private MainWindowController mainController;

    /**
     * Initializes the controller and associates it with the History Window.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.HISTORY_WINDOW).setController(this);
        mainController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
        
        setupTableColumns();
        loadHistoryData();
        setupContextMenu();
    }

    /**
     * Sets up the table columns for the history table view.
     */
    private void setupTableColumns() {
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        queryColumn.setCellValueFactory(new PropertyValueFactory<>("query"));
    }

    /**
     * Loads the history data from the main controller and sets it to the history table view.
     */
    private void loadHistoryData() {
        historyTableView.setItems(mainController.getQueryHistory());
    }

    /**
     * Sets up the context menu for the history table view.
     */
    private void setupContextMenu() {
        ContextMenuHelper.setupHistoryContextMenu(historyTableView);
    }

    /**
     * Handles the event when the user clicks on the clear history button.
     */
    @FXML
    public void handleClearHistory() {
        mainController.getQueryHistory().clear();
    }

    /**
     * Handles the event when the user clicks on the close button.
     */
    @FXML
    public void handleClose() {
        Window.hideWindow(Window.HISTORY_WINDOW);
    }
}