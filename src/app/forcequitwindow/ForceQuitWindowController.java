package app.forcequitwindow;

import app.ControllerBase;
import app.Window;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Force Quit Window in the application.
 * <p>This class handles the user interactions within the Force Quit Window,
 * such as confirming the force quit operation and canceling the operation.</p>
 *
 * @see app.ControllerBase
 * @see app.Window
 */
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
