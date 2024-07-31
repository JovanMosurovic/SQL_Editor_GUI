package app.closingwindow;

import app.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ClosingWindow extends Window {
    @Override
    public void init(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("closingwindow.fxml")));
            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../resources/styles/closingStyle.css")).toExternalForm());

            stage.setTitle("SQL Editor");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            this.stage = stage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
