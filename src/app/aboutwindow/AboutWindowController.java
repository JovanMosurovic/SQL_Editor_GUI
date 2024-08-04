package app.aboutwindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutWindowController extends ControllerBase {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.ABOUT_WINDOW).setController(this);
    }

    public void handleClose() {
        WindowHelper.hideWindow(Window.ABOUT_WINDOW);
    }
}
