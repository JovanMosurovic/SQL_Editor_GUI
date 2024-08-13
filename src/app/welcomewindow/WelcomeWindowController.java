package app.welcomewindow;

import app.ControllerBase;
import app.Window;
import app.mainwindow.MainWindowController;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
        Stage welcomeWindoStage = Window.getWindowAt(Window.WELCOME_WINDOW).getStage();
        if(mainWindowController.handleImportDatabase(welcomeWindoStage, true)) {
            statusMessage.setText("Database imported successfully!");
            statusMessage.setStyle("-fx-text-fill: green;");
            Window.hideWindow(Window.WELCOME_WINDOW);
            Window.showWindow(Window.MAIN_WINDOW);
        } else {
            statusMessage.setText("Database import failed. Please try again.");
            statusMessage.setStyle("-fx-text-fill: red;");
        }

    }

    public void handleCreateNewDatabase() {
        Window.hideWindow(Window.WELCOME_WINDOW);
        Window.showWindow(Window.MAIN_WINDOW);
    }
}
