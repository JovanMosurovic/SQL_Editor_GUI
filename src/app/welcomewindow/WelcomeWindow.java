package app.welcomewindow;

import app.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * The welcome window of the application that is displayed when the application is launched.
 * <p>It provides options to open an existing database, create a new database, or exit the application.</p>
 */
public class WelcomeWindow extends Window {

    /**
     * Initializes the Welcome Window and sets its properties, such as title, icon, and modality.
     * This method loads the FXML layout, applies the stylesheet, and configures the window's
     * appearance and behavior. The window is centered on the screen and is set to be non-resizable.
     *
     * @param stage The primary stage on which this window is displayed.
     */
    @Override
    public void init(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcomewindow.fxml")));
            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../resources/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("Welcome");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../resources/icons/sql_icon.png"))));
            stage.setScene(scene);
            stage.setResizable(false);
            Window.centerOnScreen(Window.WELCOME_WINDOW);

            stage.setOnCloseRequest(event -> {
                event.consume();
                Window.closeAllWindows();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
