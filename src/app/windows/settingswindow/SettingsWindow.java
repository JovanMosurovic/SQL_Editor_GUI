package app.windows.settingswindow;

import app.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import org.fxmisc.richtext.CodeArea;

/**
 * Represents the Settings Window of the application.
 * <p>This window is displayed when the user clicks the "Settings" button in the Menu</p>
 * The window provides options to change the theme of the application and
 * the font size and font family of the {@link CodeArea} and Console {@link TextFlow} components.
 * <p>Note: Appearance settings are not yet implemented in this version of the application.</p>
 *
 * @see app.Window
 */
public class SettingsWindow extends Window {

    /**
     * Initializes the Settings Window and sets its properties, such as title, icon, and modality.
     * <p>This method loads the FXML layout, applies the stylesheet, and configures the window's
     * appearance and behavior.</p>
     * <p>The window is centered on the screen and is set to be non-resizable.</p>
     *
     * @param stage The primary {@link Stage} on which this window is displayed.
     */
    @Override
    public void init(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("settingswindow.fxml")));
            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/app/resources/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("Settings");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/app/resources/icons/sql_icon.png"))));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            Window.centerOnScreen(Window.SETTINGS_WINDOW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
