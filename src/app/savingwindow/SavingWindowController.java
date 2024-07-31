package app.savingwindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class SavingWindowController extends ControllerBase {
    public Button customFormatButton;
    public Button SQLFormatButton;
    public Button cancelButton;

    public void handleSaveAsSQL(ActionEvent actionEvent) {
        WindowHelper.hideWindow(Window.SAVE_WINDOW);
    }

    public void handleSaveAsCustom(ActionEvent actionEvent) {
        WindowHelper.hideWindow(Window.SAVE_WINDOW);
    }

    public void handleCancel(ActionEvent actionEvent) {
        WindowHelper.hideWindow(Window.SAVE_WINDOW);
        WindowHelper.hideWindow(Window.CLOSE_WINDOW);
        WindowHelper.showWindow(Window.MAIN_WINDOW);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.SAVE_WINDOW).setController(this);
    }

    public void handleDontSave(ActionEvent actionEvent) {
    }

    public void handleSave(ActionEvent actionEvent) {
    }
}
