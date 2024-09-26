package app.windows.historywindow;

import app.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HistoryWindow extends Window {

    @Override
    public void init(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("historywindow.fxml")));
            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/app/resources/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("History");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/app/resources/icons/sql_icon.png"))));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            Window.centerOnScreen(Window.FORCE_QUIT_WINDOW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
