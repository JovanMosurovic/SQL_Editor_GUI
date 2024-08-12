package app.closingwindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class ClosingWindowController extends ControllerBase {

    @FXML
    private Button saveButton;
    @FXML
    private Button dontSaveButton;
    @FXML
    private Button cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.CLOSE_WINDOW).setController(this);
    }

    @FXML
    private void handleSave() {
        WindowHelper.hideWindow(Window.CLOSE_WINDOW);
        WindowHelper.showWindow(Window.SAVE_WINDOW);
    }

    @FXML
    private void handleDontSave() {
        WindowHelper.closeAllWindows();
    }

    @FXML
    private void handleCancel() {
        WindowHelper.hideWindow(Window.CLOSE_WINDOW);
    }
}
