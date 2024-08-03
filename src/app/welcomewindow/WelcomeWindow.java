package app.welcomewindow;

import app.Window;
import app.WindowHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class WelcomeWindow extends Window {
    @Override
    public void init(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcomewindow.fxml")));
            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../resources/styles/welcomewindow.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("Welcome");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../resources/icons/sql_icon.png"))));
            stage.setScene(scene);
            stage.setResizable(false);
            WindowHelper.centerOnScreen(Window.WELCOME_WINDOW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}