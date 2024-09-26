package app.windows.forcequitwindow;

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
 * Represents the Force Quit Window of the application.
 * <p>This window is displayed when the user clicks the "Force Quit" button in the Menu</p>
 * The window prompts the user to confirm the action and provides options to cancel the operation.
 *
 * @see app.Window
 */
public class ForceQuitWindow extends Window {

    /**
     * Initializes the Force Quit Window and sets its properties, such as title, icon, and modality.
     * <p>This method loads the FXML layout, applies the stylesheet, and configures the window's
     * appearance and behavior.
     * <p>The window is centered on the screen and is set to be non-resizable.</p>
     *
     * @param stage The primary stage on which this window is displayed.
     */
    @Override
    public void init(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("forcequitwindow.fxml")));
            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/app/resources/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("Warning");
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
