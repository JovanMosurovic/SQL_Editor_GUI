package app.savingwindow;

import app.ControllerBase;
import app.Window;
import cpp.JavaInterface;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the Saving Window.
 * This controller handles the logic behind saving the user's work in different formats,
 * such as SQL or a custom format. It also manages the interactions between the user and
 * the Saving Window UI, including file selection and save operation.
 *
 * @see app.Window
 */
public class SavingWindowController extends ControllerBase {

    private static final Logger logger = Logger.getLogger(SavingWindowController.class.getName());

    /**
     * Initializes the Saving Window Controller.
     * This method sets up the controller by associating it with the corresponding window,
     * ensuring that it manages the correct UI elements and handles events properly.
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
     * This method opens a FileChooser dialog for the user to select the location and name of the SQL file.
     * It then proceeds to save the content in the chosen location.
     */
    @FXML
    private void handleSaveAsSQL() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as SQL");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQL Files", "*.sql"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            saveFile(file, "sql");
        }
        Window.hideWindow(Window.SAVE_WINDOW);
    }

    /**
     * Handles the action of saving the file as a custom format.
     * This method opens a FileChooser dialog for the user to select the location and name of the custom file.
     * It then proceeds to save the content in the chosen location.
     */
    @FXML
    private void handleSaveAsCustom() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as Custom Format");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Custom Files", "*.dbexp"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            saveFile(file, "dbexp");
        }
        Window.hideWindow(Window.SAVE_WINDOW);
    }

    /**
     * Saves the file in the specified format.
     * This private method handles the actual file writing operation, saving the content in either SQL or a custom format.
     *
     * @param file   The file to be saved.
     * @param format The format in which the file should be saved (either "sql" or "dbexp").
     */
    private void saveFile(File file, String format) {
        try (FileWriter ignored = new FileWriter(file)) {
            JavaInterface.getInstance().exportDatabase(format, file.getAbsolutePath());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while saving the file.", e);
        }
    }

    /**
     * Handles the action of canceling the save operation.
     * This method closes the Saving Window and returns the user to the main application window.
     */
    @FXML
    private void handleCancel() {
        Window.hideWindow(Window.SAVE_WINDOW);
        Window.hideWindow(Window.CLOSE_WINDOW);
        Window.showWindow(Window.MAIN_WINDOW);
    }
}
