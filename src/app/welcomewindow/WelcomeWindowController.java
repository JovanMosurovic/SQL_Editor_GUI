package app.welcomewindow;

import app.ControllerBase;
import app.Window;
import app.mainwindow.MainWindowController;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller class for the welcome window of the application.
 * It handles user actions and events on the welcome window.
 */
public class WelcomeWindowController extends ControllerBase {

    /**
     * The status message label on the welcome window.
     */
    public Label statusMessage;

    /**
     * Initializes the controller class for the welcome window.
     *
     * @param location  the {@link URL} location of the FXML file
     * @param resources the {@link ResourceBundle} resources for the FXML file
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.WELCOME_WINDOW).setController(this);
    }

    /**
     * Handles the action when the user clicks the "Open Existing Database" button.
     * It imports the database and switches to the main window if successful.
     */
    public void handleImportDatabase() {
        MainWindowController mainWindowController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
        Stage welcomeWindoStage = Window.getWindowAt(Window.WELCOME_WINDOW).getStage();
        if (mainWindowController.handleImportDatabase(welcomeWindoStage, true)) {
            statusMessage.setText("Database imported successfully!");
            statusMessage.setStyle("-fx-text-fill: green;");
            Window.hideWindow(Window.WELCOME_WINDOW);
            Window.showWindow(Window.MAIN_WINDOW);
        } else {
            statusMessage.setText("Database import failed. Please try again.");
            statusMessage.setStyle("-fx-text-fill: red;");
        }

    }

    /**
     * Handles the action when the user clicks the "Create New Database" button.
     * It switches to the main window to create a new database.
     */
    public void handleCreateNewDatabase() {
        Window.hideWindow(Window.WELCOME_WINDOW);
        Window.showWindow(Window.MAIN_WINDOW);
    }
}
