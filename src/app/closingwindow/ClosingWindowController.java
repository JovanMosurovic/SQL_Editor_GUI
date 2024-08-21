package app.closingwindow;

import app.ControllerBase;
import app.Window;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Closing Window in the application.
 * <p>This class handles the user interactions within the Closing Window,
 * such as saving changes, closing all windows without saving, and canceling the close operation.</p>
 * <p>The Closing Window is displayed when the user attempts to close the application or open a new file
 * without saving the changes made to the current file.</p>
 *
 * @see app.ControllerBase
 * @see app.Window
 */
public class ClosingWindowController extends ControllerBase {

    /**
     * Initializes the controller and associates it with the Closing Window.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.CLOSE_WINDOW).setController(this);
    }

    /**
     * Handles the action of saving changes and then closing the window.
     * This method is invoked when the user clicks the "Save" button.
     * It hides the current window and opens the Save Window.
     */
    @FXML
    private void handleSave() {
        Window.hideWindow(Window.CLOSE_WINDOW);
        Window.showWindow(Window.SAVE_WINDOW);
    }

    /**
     * Handles the action of closing all windows without saving changes.
     * This method is invoked when the user clicks the "Don't Save" button.
     */
    @FXML
    private void handleDontSave() {
        Window.closeAllWindows();
    }

    /**
     * Handles the action of canceling the close operation.
     * This method is invoked when the user clicks the "Cancel" button.
     * It hides the Closing Window without performing any other actions.
     */
    @FXML
    private void handleCancel() {
        Window.hideWindow(Window.CLOSE_WINDOW);
    }
}
