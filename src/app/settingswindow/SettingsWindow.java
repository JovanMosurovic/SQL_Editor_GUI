package app.settingswindow;

import app.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents the Settings Window of the application.
 * This window allows users to configure the application settings, such as themes and fonts.
 * It provides an interface to customize the appearance and behavior of the application,
 * ensuring that users can tailor the application to their preferences.
 *
 * @see app.Window
 */
public class SettingsWindow extends Window {

    /**
     * Initializes the Settings Window and sets its properties, such as title, icon, and modality.
     * This method loads the FXML layout, applies the stylesheet, and configures the window's
     * appearance and behavior. The window is centered on the screen and is set to be non-resizable.
     *
     * @param stage The primary {@link Stage} on which this window is displayed.
     */
    @Override
    public void init(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("settingswindow.fxml")));
            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../resources/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("Settings");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../resources/icons/sql_icon.png"))));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            Window.centerOnScreen(Window.SETTINGS_WINDOW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
