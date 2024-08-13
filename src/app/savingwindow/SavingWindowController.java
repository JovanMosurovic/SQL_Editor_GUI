package app.savingwindow;

import app.ControllerBase;
import app.Window;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SavingWindowController extends ControllerBase {

    private static final Logger logger = Logger.getLogger(SavingWindowController.class.getName());

    @FXML
    private Button customFormatButton;
    @FXML
    private Button SQLFormatButton;
    @FXML
    private Button cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.SAVE_WINDOW).setController(this);
    }

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

    @FXML
    private void handleCancel() {
        Window.hideWindow(Window.SAVE_WINDOW);
        Window.hideWindow(Window.CLOSE_WINDOW);
        Window.showWindow(Window.MAIN_WINDOW);
    }

    private void saveFile(File file, String format) {
        try (FileWriter writer = new FileWriter(file)) {
            if ("sql".equals(format)) {
                System.out.println("Saving as SQL");
                //todo SQL save logic
            } else if ("dbexp".equals(format)) {
                System.out.println("Saving as Custom Format");
                //todo Custom save logic
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while saving the file.", e);
        }
    }
}
