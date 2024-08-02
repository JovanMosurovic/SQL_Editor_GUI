package app.mainwindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController extends ControllerBase {

    public Button runButton;
    public ListView<String> tablesListView;
    public TextArea codeArea;
    public ScrollPane resultScrollPane;
    public TextFlow resultTextFlow;
    public TableView<String> resultTableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getResourceAsStream("app/resources/fonts/JetBrainsMonoNL-Regular.ttf"), 16);
        codeArea.setFont(Font.font("JetBrains Mono NL", 16));
        Window.getWindowAt(Window.MAIN_WINDOW).setController(this);
    }

    public void handleSaveAs() {
        WindowHelper.showWindow(Window.SAVE_WINDOW);
    }

    public boolean handleImportDatabase() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Database");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SQL Files", "*.sql"),
                new FileChooser.ExtensionFilter("Custom format files", "*dbexp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage stage = (Stage) tablesListView.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        clearResultTextFlow();
        if (selectedFile == null) {
            updateResultTextFlow("No file selected. Please select a valid .sql or .dbexp file.", Color.RED);
            return false;
        }

        String filePath = selectedFile.getAbsolutePath();
        if (filePath.endsWith(".sql") || filePath.endsWith(".dbexp")) {
            updateResultTextFlow("Selected file: " + filePath, Color.BLACK);
            updateResultTextFlow("\nDatabase imported successfully!", Color.GREEN);
            // TODO: import database
            return true;
        } else {
            updateResultTextFlow("Invalid file type selected. Please select a .sql or .dbexp file.", Color.RED);
            return false;
        }
    }

    private void updateResultTextFlow(String message, Color color) {
        Text text = new Text(message);
        text.setFill(color);
        resultTextFlow.getChildren().add(text);
    }

    private void clearResultTextFlow() {
        resultTextFlow.getChildren().clear();
    }

    public void handleClose() {}

    public void handleRun() {
        System.out.println("Run button clicked");
        //todo: run the code
    }
}
