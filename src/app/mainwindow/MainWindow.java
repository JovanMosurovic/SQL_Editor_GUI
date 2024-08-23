package app.mainwindow;

import app.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.MenuBar;
import javafx.scene.text.TextFlow;
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

import org.fxmisc.richtext.CodeArea;

/**
 * Represents the Main Window of the application.
 * <p>This window is displayed after welcome window and is the main interface for the user to interact with the application.</p>
 * The window provides a {@link CodeArea} for writing SQL queries, a Console {@link TextFlow} for displaying messages and errors,
 * and a {@link TableView} in {@link TabPane} for showing the results of the queries.
 * <p>It also allows users to save their work, open existing scripts, and create new scripts.</p>
 * <p>This window also contains a {@link MenuBar} with various options,
 * such as File, Edit, View, and Help with their respective submenus.</p>
 *
 * @see app.Window
 * @see CodeArea
 * @see TextFlow
 * @see TableView
 * @see TabPane
 * @see MenuBar
 */
public class MainWindow extends Window {

    /**
     * Initializes the Main Window and sets its properties, such as title, icon, and modality.
     * <p>This method loads the FXML layout, applies the stylesheet, and configures the window's
     * appearance and behavior.</p>
     * <p>The window is centered on the screen and is set to be non-resizable.</p>
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

    /**
     * Updates the title of the Main Window with the name of the database being edited.
     * <p>If the database name is null or empty, the default title "SQL Editor" is displayed.</p>
     *
     * @param databaseName The name of the database being edited.
     */
    public void updateTite(String databaseName) {
        if(databaseName != null && !databaseName.isEmpty()) {
            stage.setTitle("SQL Editor - " + databaseName);
        } else {
            stage.setTitle("SQL Editor");
        }
    }
}
