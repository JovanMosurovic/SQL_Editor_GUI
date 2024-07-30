package app.mainwindow;

import app.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainWindow extends Window {
    @Override
    public void init(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("app/mainwindow/mainwindow.fxml")));
            stage.setTitle("SQL Editor");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
            this.stage = stage;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
