package app.aboutwindow;

import app.ControllerBase;
import app.Window;
import javafx.application.HostServices;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller for the About Window in the application.
 * <p>This class handles the user interactions within the About Window,
 * such as opening links to the project repository, author's GitHub page,
 * and the instructions document.</p>
 *
 * @see app.ControllerBase
 * @see app.Window
 */
public class AboutWindowController extends ControllerBase {

    /**
     * URL to the project's GitHub repository.
     */
    private static final String PROJECT_GITHUB_URL = "https://github.com/JovanMosurovic/Elemental_SQL_Implementation_with_GUI";

    /**
     * URL to the author's GitHub profile.
     */
    private static final String AUTHOR_GITHUB_URL = "https://github.com/JovanMosurovic";

    /**
     * URL to the instructions document in the project's GitHub repository.
     */
    private static final String INSTRUCTIONS_GITHUB_URL = "https://github.com/JovanMosurovic/Elemental_SQL_Implementation_with_GUI/blob/master/instructions.pdf";

    /**
     * HostServices instance for showing documents and handling external links.
     */
    private HostServices hostServices;

    /**
     * Initializes the controller and associates it with the About Window.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.ABOUT_WINDOW).setController(this);
    }

    /**
     * Handles the action of closing the About Window.
     * This method is invoked when the user clicks the close button.
     */
    @FXML
    private void handleClose() {
        Window.hideWindow(Window.ABOUT_WINDOW);
    }

    /**
     * Handles the action of opening the project GitHub repository.
     * This method is invoked when the user clicks the corresponding button.
     */
    @FXML
    private void handleOpenProjectGitHub() {
        openLink(PROJECT_GITHUB_URL);
    }

    /**
     * Handles the action of opening the author's GitHub profile.
     * This method is invoked when the user clicks the corresponding button.
     */
    @FXML
    private void handleOpenAuthorGitHub() {
        openLink(AUTHOR_GITHUB_URL);
    }

    /**
     * Handles the action of opening the instructions document link.
     * This method is invoked when the user clicks the corresponding button.
     */
    @FXML
    private void handleOpenInstructionsLink() {
        openLink(INSTRUCTIONS_GITHUB_URL);
    }

    /**
     * Opens a link using the HostServices instance.
     *
     * @param url The URL to open.
     */
    private void openLink(String url) {
        if (hostServices != null) {
            hostServices.showDocument(url);
        }
    }

    /**
     * Sets the HostServices instance used for handling external links.
     *
     * @param hostServices The HostServices instance to set.
     */
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
}
