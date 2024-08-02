package app.mainwindow;

import app.Window;
import app.WindowHelper;
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

public class MainWindow extends Window {
    @Override
    public void init(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainwindow.fxml")));
            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../resources/styles/mainwindow.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("SQL Editor");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../resources/icons/sql_icon.png"))));
            stage.setScene(scene);
            stage.setResizable(false);
            WindowHelper.centerOnScreen(Window.MAIN_WINDOW);

            KeyCombination runShortcut = new KeyCodeCombination(KeyCode.F10, KeyCombination.SHIFT_DOWN);
            scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                if (runShortcut.match(event)) {
                    MainWindowController mainWindowController = (MainWindowController) Window.getWindowAt(MAIN_WINDOW).getController();
                    mainWindowController.handleRun();
                }
            });

            this.stage.setOnCloseRequest(event -> {
                event.consume();
                Window.getWindowAt(Window.CLOSE_WINDOW).getStage().show();
            });
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
