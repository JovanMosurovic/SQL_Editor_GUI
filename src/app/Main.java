package app;

import app.windows.aboutwindow.AboutWindow;
import app.windows.aboutwindow.AboutWindowController;
import app.windows.closingwindow.ClosingWindow;
import app.windows.forcequitwindow.ForceQuitWindow;
import app.windows.historywindow.HistoryWindow;
import app.windows.mainwindow.MainWindow;
import app.windows.savingwindow.SavingWindow;
import app.windows.settingswindow.SettingsWindow;
import app.windows.welcomewindow.WelcomeWindow;
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
        Window.setWindowAt(Window.HISTORY_WINDOW, new HistoryWindow());
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