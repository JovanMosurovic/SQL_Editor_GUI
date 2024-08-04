package app;

import app.aboutwindow.AboutWindow;
import app.closingwindow.ClosingWindow;
import app.mainwindow.MainWindow;
import app.savingwindow.SavingWindow;
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
        Window.initAllWindows();
        WindowHelper.showOnly(Window.WELCOME_WINDOW);
        WindowHelper.centerOnScreen(Window.WELCOME_WINDOW);
    }

    public static void main(String[] args) {
        launch(args);
    }
}