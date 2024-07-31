package app.savingwindow;

import app.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SavingWindow extends Window {
    @Override
    public void init(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("savingwindow.fxml")));
            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../resources/styles/savingwindow.css")).toExternalForm());

            stage.setTitle("Save as");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            this.stage = stage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
