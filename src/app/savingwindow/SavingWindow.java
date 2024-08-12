package app.savingwindow;

import app.Window;
import app.WindowHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SavingWindow extends Window {

    @Override
    public void init(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("savingwindow.fxml")));
            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../resources/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("Save");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../resources/icons/sql_icon.png"))));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            WindowHelper.centerOnScreen(Window.SAVE_WINDOW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
