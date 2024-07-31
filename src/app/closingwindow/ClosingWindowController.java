package app.closingwindow;

import app.ControllerBase;
import app.Window;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class ClosingWindowController extends ControllerBase implements Initializable {


    public Button saveButton;
    public Button dontSaveButton;
    public Button cacnelButton;

    public void handleSave(ActionEvent actionEvent) {
    }

    public void handleDontSave(ActionEvent actionEvent) {
    }

    public void handleCancel(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.CLOSE_WINDOW).setController(this);
    }
}
