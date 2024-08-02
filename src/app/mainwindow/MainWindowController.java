package app.mainwindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController extends ControllerBase {

    public Button runButton;
    public ListView<String> tablesListView;
    public TextArea codeArea;
    public TextArea resultTextArea;
    public TableView<String> resultTableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getResourceAsStream("app/resources/fonts/JetBrainsMonoNL-Regular.ttf"), 16);
        codeArea.setFont(Font.font("JetBrains Mono NL", 16));
        resultTextArea.setFont(Font.font("JetBrains Mono NL", 16));
        resultTextArea.setFocusTraversable(false);
        Window.getWindowAt(Window.MAIN_WINDOW).setController(this);
    }

    public void handleSaveAs() {
        WindowHelper.showWindow(Window.SAVE_WINDOW);
    }

    public void handleImportDatabase() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Database");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SQL Files", "*.sql"),
                new FileChooser.ExtensionFilter("Custom format files", "*dbexp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage stage = (Stage) tablesListView.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile == null) {
            resultTextArea.setText("No file selected. Please select a valid .sql or .dbexp file.");
            return;
        }

        String filePath = selectedFile.getAbsolutePath();
        if (filePath.endsWith(".sql") || filePath.endsWith(".dbexp")) {
            resultTextArea.setText("Selected file: " + filePath);
            // TODO: import database
        } else {
            resultTextArea.setText("Invalid file type selected. Please select a .sql or .dbexp file.");
        }


    }

    public void handleClose() {}

    public void handleRun() {
        System.out.println("Run button clicked");
        //todo: run the code
    }
}
