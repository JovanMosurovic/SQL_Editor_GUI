package app.windows.welcomewindow;

import app.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

//Represents the Closing Window of the application.
// * <p>This window is displayed when the user attempts to close the application without saving
// * the changes made to the SQL script. </p>
// * The window prompts the user to confirm the action and
// * provides options to save the script, discard the changes, or cancel the operation.

/**
 * Represents the Welcome Window of the application.
 * <p>This window is displayed when the application is launched and provides options to import or create a database.</p>
 * The window contains buttons to import an existing database from a file (.sql or .dbexp) or create a new database.
 *
 */
public class WelcomeWindow extends Window {

    /**
     * Initializes the Welcome Window and sets its properties, such as title, icon, and modality.
     * <p>This method loads the FXML layout, applies the stylesheet, and configures the window's
     * appearance and behavior.</p>
     * <p>The window is centered on the screen and is set to be non-resizable.</p>
     *
     * @param stage The primary stage on which this window is displayed.
     */
    @Override
    public void init(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcomewindow.fxml")));
            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/app/resources/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("Welcome");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/app/resources/icons/sql_icon.png"))));
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
