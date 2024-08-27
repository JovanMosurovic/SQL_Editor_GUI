package app.historywindow;

import app.ControllerBase;
import app.Window;
import app.mainwindow.MainWindowController;
import app.util.ContextMenuHelper;
import app.util.HistoryEntry;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;
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
     * The table column for displaying the status of the history entry.
     */
    @FXML
    private TableColumn<HistoryEntry, Boolean> statusColumn;
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
        setupStatusColumn();
        loadHistoryData();
        setupContextMenu();
    }

    /**
     * Sets up the table columns for the history table view.
     */
    private void setupTableColumns() {
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        queryColumn.setCellValueFactory(new PropertyValueFactory<>("query"));
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().successProperty());
    }

    /**
     * Sets up the status column for the history table view.
     */
    private void setupStatusColumn() {
        statusColumn.setCellFactory(column -> new TableCell<HistoryEntry, Boolean>() {
            private final ImageView imageView = new ImageView();
            private final Tooltip tooltip = new Tooltip();

            {
                imageView.setFitHeight(16);
                imageView.setFitWidth(16);
                setGraphic(imageView);
                setTooltip(tooltip);
            }

            @Override
            protected void updateItem(Boolean success, boolean empty) {
                super.updateItem(success, empty);
                if (empty || success == null) {
                    imageView.setImage(null);
                    tooltip.setText("");
                } else {
                    Image image = getStatusImage(success);
                    imageView.setImage(image);
                    String tooltipText = success ? "Successfully executed" : "Execution failed";
                    tooltip.setText(tooltipText);
                }
            }
        });
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
    private void handleClearHistory() {
        mainController.getQueryHistory().clear();
    }

    /**
     * Handles the event when the user clicks on the close button.
     */
    @FXML
    private void handleClose() {
        Window.hideWindow(Window.HISTORY_WINDOW);
    }

    /**
     * Returns the image for the status column based on the success of the history entry.
     *
     * @param success The success status of the history entry.
     * @return The image for the status column.
     */
    private Image getStatusImage(boolean success) {
        String imagePath = success ? "../resources/icons/check_icon.png" : "../resources/icons/error_icon.png";
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }
}