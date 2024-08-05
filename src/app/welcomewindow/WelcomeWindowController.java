package app.welcomewindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;
import app.mainwindow.MainWindowController;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeWindowController extends ControllerBase {

    public Label statusMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.WELCOME_WINDOW).setController(this);
    }

    public void handleImportDatabase() {
        MainWindowController mainWindowController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
        if(mainWindowController.handleImportDatabase(true)) {
            statusMessage.setText("Database imported successfully!");
            statusMessage.setStyle("-fx-text-fill: green;");
            WindowHelper.hideWindow(Window.WELCOME_WINDOW);
            WindowHelper.showWindow(Window.MAIN_WINDOW);
        } else {
            statusMessage.setText("Database import failed. Please try again.");
            statusMessage.setStyle("-fx-text-fill: red;");
        }

    }

    public void handleCreateNewDatabase() {
        WindowHelper.hideWindow(Window.WELCOME_WINDOW);
        WindowHelper.showWindow(Window.MAIN_WINDOW);
    }
}
