package app.savingwindow;

import app.ControllerBase;
import app.Window;
import app.mainwindow.MainWindowController;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Saving Window.
 * <p>This controller handles the logic behind saving the user's work in different formats,
 * such as SQL or a custom format. </p>
 * <p> It also manages the interactions between the user and
 * the Saving Window UI, including file selection and save operation.</p>
 *
 * @see app.Window
 */
public class SavingWindowController extends ControllerBase {

    /**
     * Initializes the Saving Window Controller.
     * <p>This method sets up the controller by associating it with the corresponding window,
     * ensuring that it manages the correct UI elements and handles events properly.</p>
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.SAVE_WINDOW).setController(this);
    }

    /**
     * Handles the action of saving the file as an SQL file.
     * <p>This method opens a FileChooser dialog for the user to select the location and name of the SQL file.</p>
     * It then proceeds to save the content in the chosen location.
     */
    @FXML
    private void handleSaveAsSQL() {
        saveAs("sql");
    }

    /**
     * Handles the action of saving the file as a custom format.
     * <p>This method opens a FileChooser dialog for the user to select the location and name of the custom file.</p>
     * It then proceeds to save the content in the chosen location.
     */
    @FXML
    private void handleSaveAsCustom() {
        saveAs("dbexp");
    }

    /**
     * Saves the file in the specified format.
     * <p>This private method handles the actual file writing operation, saving the content in either SQL or a custom format.</p>
     *
     * @param format The format in which the file should be saved (either "sql" or "dbexp").
     */
    private void saveAs(String format) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as " + format.toUpperCase());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(format.toUpperCase() + " Files", "*." + format));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            MainWindowController mainController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
            mainController.saveFile(file);
        }
        Window.hideWindow(Window.SAVE_WINDOW);
    }

    /**
     * Handles the action of canceling the save operation.
     * <p>This method closes the Saving Window and returns the user to the main application window.</p>
     */
    @FXML
    private void handleCancel() {
        Window.hideWindow(Window.SAVE_WINDOW);
        Window.hideWindow(Window.CLOSE_WINDOW);
        Window.showWindow(Window.MAIN_WINDOW);
    }
}
