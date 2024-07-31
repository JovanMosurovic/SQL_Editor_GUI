package app.closingwindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class ClosingWindowController extends ControllerBase {


    public Button saveButton;
    public Button dontSaveButton;
    public Button cancelButton;

    public void handleSave() {
        WindowHelper.hideWindow(Window.CLOSE_WINDOW);
        WindowHelper.showWindow(Window.SAVE_WINDOW);
    }

    public void handleDontSave() {
        WindowHelper.hideWindow(Window.CLOSE_WINDOW);
        WindowHelper.hideWindow(Window.MAIN_WINDOW);
    }

    public void handleCancel() {
        WindowHelper.hideWindow(Window.CLOSE_WINDOW);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.CLOSE_WINDOW).setController(this);
    }
}
