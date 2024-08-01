package app;

import app.closingwindow.ClosingWindow;
import app.mainwindow.MainWindow;
import app.savingwindow.SavingWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Window.setWindowAt(Window.MAIN_WINDOW, new MainWindow());
        Window.setWindowAt(Window.CLOSE_WINDOW, new ClosingWindow());
        Window.setWindowAt(Window.SAVE_WINDOW, new SavingWindow());
        Window.initAllWindows();
        WindowHelper.showOnly(Window.MAIN_WINDOW);
        WindowHelper.centerOnScreen(Window.MAIN_WINDOW);
    }

    public static void main(String[] args) {
        launch(args);
    }
}