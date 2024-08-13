package app;

import app.aboutwindow.AboutWindow;
import app.aboutwindow.AboutWindowController;
import app.closingwindow.ClosingWindow;
import app.mainwindow.MainWindow;
import app.savingwindow.SavingWindow;
import app.settingswindow.SettingsWindow;
import app.welcomewindow.WelcomeWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {

        Window.setWindowAt(Window.MAIN_WINDOW, new MainWindow());
        Window.setWindowAt(Window.CLOSE_WINDOW, new ClosingWindow());
        Window.setWindowAt(Window.SAVE_WINDOW, new SavingWindow());
        Window.setWindowAt(Window.WELCOME_WINDOW, new WelcomeWindow());
        Window.setWindowAt(Window.ABOUT_WINDOW, new AboutWindow());
        Window.setWindowAt(Window.SETTINGS_WINDOW, new SettingsWindow());
        Window.initAllWindows();
        Window.showOnly(Window.WELCOME_WINDOW);

        AboutWindowController aboutWindowController = (AboutWindowController) Window.getWindowAt(Window.ABOUT_WINDOW).getController();
        aboutWindowController.setHostServices(getHostServices());
    }

    public static void main(String[] args) {
        launch(args);
    }
}