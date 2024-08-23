package app.aboutwindow;

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
 * Represents the About Window of the application.
 * <p>This window displays information about the application,
 * such as why it was created, the author, version, and other relevant details.</p>
 * <p>The About Window is displayed when the user clicks the "About" button in the Menu.</p>
 * <p>The window provides links to the project's GitHub repository, the author's GitHub page
 * and button to close the window.</p>
 *
 * @see app.Window
 */
public class AboutWindow extends Window {

    /**
     * Initializes the About Window and sets its properties, such as title, icon, and modality.
     * <p>This method loads the FXML layout, applies the stylesheet, and configures the window's
     * appearance and behavior. </p>
     * The window is centered on the screen and is set to be non-resizable.
     *
     * @param stage The primary stage on which this window is displayed.
     */
    @Override
    public void init(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("aboutwindow.fxml")));
            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../resources/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("About");
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
