package app;

import app.mainwindow.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Window.setWindowAt(Window.MAIN_WINDOW, new MainWindow());
        // Closing window
    }

    public static void main(String[] args) {
        launch(args);
    }
}