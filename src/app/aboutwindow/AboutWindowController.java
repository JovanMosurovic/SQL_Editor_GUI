package app.aboutwindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutWindowController extends ControllerBase {
    private HostServices hostServices;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.ABOUT_WINDOW).setController(this);
    }

    public void handleClose() {
        WindowHelper.hideWindow(Window.ABOUT_WINDOW);
    }

    public void handleOpenLink(ActionEvent event) {
        Hyperlink source = (Hyperlink) event.getSource();
        String url = source.getText();
        if(hostServices != null) {
            hostServices.showDocument(url);
        }
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
}
