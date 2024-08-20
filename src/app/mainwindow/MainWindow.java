package app.mainwindow;

import app.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents the Main Window of the application.
 * This window is the primary window of the application and is used to interact with the user.
 * It provides a text area for entering SQL queries, a table view for displaying query results,
 * and buttons for executing queries and managing the application.
 * The window is styled and behaves consistently with the application's overall theme.
 *
 * @see app.Window
 */
public class MainWindow extends Window {

    /**
     * Initializes the Main Window and sets its properties, such as title, icon, and modality.
     * This method loads the FXML layout, applies the stylesheet, and configures the window's
     * appearance and behavior. The window is centered on the screen and is set to be non-resizable.
     *
     * @param stage The primary {@link Stage} on which this window is displayed.
     */
    @Override
    public void init(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainwindow.fxml")));
            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../resources/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("SQL Editor");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../resources/icons/sql_icon.png"))));
            stage.setScene(scene);
            stage.setResizable(false);
            Window.centerOnScreen(Window.MAIN_WINDOW);

            KeyCombination runShortcut = new KeyCodeCombination(KeyCode.F10, KeyCombination.SHIFT_DOWN);
            scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                if (runShortcut.match(event)) {
                    MainWindowController mainWindowController = (MainWindowController) Window.getWindowAt(MAIN_WINDOW).getController();
                    mainWindowController.handleRun();
                }
            });

            this.stage.setOnCloseRequest(event -> {
                event.consume();
                MainWindowController mainWindowController = (MainWindowController) Window.getWindowAt(MAIN_WINDOW).getController();
                if (mainWindowController.hasUnsavedChanges()) {
                    Window.getWindowAt(Window.CLOSE_WINDOW).getStage().show();
                } else {
                    Window.closeAllWindows();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
