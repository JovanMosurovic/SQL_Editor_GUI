package app.forcequitwindow;

import app.ControllerBase;
import app.Window;

import java.net.URL;
import java.util.ResourceBundle;

public class ForceQuitWindowController extends ControllerBase {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.FORCE_QUIT_WINDOW).setController(this);
    }

    public void handleCancel() {
        Window.hideWindow(Window.FORCE_QUIT_WINDOW);
    }

    public void handleForceQuit() {
        Window.closeAllWindows();
    }
}
