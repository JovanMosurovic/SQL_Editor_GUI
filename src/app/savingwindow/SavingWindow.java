package app.savingwindow;

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
 * Represents the Saving Window of the application.
 * <p>This window allows users to save their work in different formats, such as SQL or a custom format.</p>
 * <p>It provides an interface to choose the file type and location, ensuring that users can easily
 * save their progress before closing the application or moving on to other tasks.</p>
 *
 * @see app.Window
 */
public class SavingWindow extends Window {

    /**
     * Initializes the Saving Window and sets its properties, such as title, icon, and modality.
     * <p> This method loads the FXML layout, applies the stylesheet, and configures the window's
     * appearance and behavior. The window is centered on the screen and is set to be non-resizable. </p>
     *
     * @param stage The primary stage on which this window is displayed.
     */
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
            Window.centerOnScreen(Window.SAVE_WINDOW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
