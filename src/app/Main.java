package app;

import app.aboutwindow.AboutWindow;
import app.aboutwindow.AboutWindowController;
import app.closingwindow.ClosingWindow;
import app.forcequitwindow.ForceQuitWindow;
import app.mainwindow.MainWindow;
import app.savingwindow.SavingWindow;
import app.settingswindow.SettingsWindow;
import app.welcomewindow.WelcomeWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main class that launches the JavaFX application.
 */
public class Main extends Application {

    static {
        System.loadLibrary("native");
    }

    /**
     * Starts the JavaFX application by setting up the main window and showing the welcome window.
     *
     * @param primaryStage the primary {@link Stage} of the application
     */
    @Override
    public void start(Stage primaryStage) {

        Window.setWindowAt(Window.WELCOME_WINDOW, new WelcomeWindow());
        Window.setWindowAt(Window.MAIN_WINDOW, new MainWindow());
        Window.setWindowAt(Window.CLOSE_WINDOW, new ClosingWindow());
        Window.setWindowAt(Window.FORCE_QUIT_WINDOW, new ForceQuitWindow());
        Window.setWindowAt(Window.SAVE_WINDOW, new SavingWindow());
        Window.setWindowAt(Window.ABOUT_WINDOW, new AboutWindow());
        Window.setWindowAt(Window.SETTINGS_WINDOW, new SettingsWindow());
        Window.initAllWindows();
        Window.showOnly(Window.WELCOME_WINDOW);

        AboutWindowController aboutWindowController = (AboutWindowController) Window.getWindowAt(Window.ABOUT_WINDOW).getController();
        aboutWindowController.setHostServices(getHostServices());
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}