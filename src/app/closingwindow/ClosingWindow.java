package app.closingwindow;

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
 * Represents the Closing Window of the application.
 * This window is shown when the user attempts to close the application or a specific window.
 * It provides options to save changes, discard changes, or cancel the close operation.
 * The window is styled and behaves consistently with the application's overall theme.
 *
 * @see app.Window
 */
public class ClosingWindow extends Window {

    /**
     * Initializes the Closing Window and sets its properties, such as title, icon, and modality.
     * This method loads the FXML layout, applies the stylesheet, and configures the window's
     * appearance and behavior. The window is centered on the screen and is set to be non-resizable.
     *
     * @param stage The primary stage on which this window is displayed.
     */
    @Override
    public void init(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("closingwindow.fxml")));
            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../resources/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("SQL Editor");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../resources/icons/sql_icon.png"))));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            Window.centerOnScreen(Window.CLOSE_WINDOW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
