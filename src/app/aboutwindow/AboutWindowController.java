package app.aboutwindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;
import javafx.application.HostServices;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutWindowController extends ControllerBase {
    private HostServices hostServices;
    private static final String PROJECT_GITHUB_URL = "https://github.com/JovanMosurovic/Elemental_SQL_Implementation_with_GUI";
    private static final String AUTHOR_GITHUB_URL = "https://github.com/JovanMosurovic";
    private static final String INSTRUCTIONS_GITHUB_URL = "https://github.com/JovanMosurovic/Elemental_SQL_Implementation_with_GUI/blob/master/instructions.pdf";
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.ABOUT_WINDOW).setController(this);
    }

    public void handleClose() {
        WindowHelper.hideWindow(Window.ABOUT_WINDOW);
    }

    public void handleOpenProjectGitHub() {
        openLink(PROJECT_GITHUB_URL);
    }

    public void handleOpenAuthorGitHub() {
        openLink(AUTHOR_GITHUB_URL);
    }

    public void handleOpenInstructionsLink() {
        openLink(INSTRUCTIONS_GITHUB_URL);
    }

    private void openLink(String url) {
        if (hostServices != null) {
            hostServices.showDocument(url);
        }
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
}
