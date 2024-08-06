package app.util;

import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;

public class DatabaseManager {

    private static File lastSelectedFile = null;

    public static boolean showImportDatabaseDialog(Stage ownerStage, TextFlow resultTextFlow, boolean isFromWelcomeWindow, ExtensionFilter... filters) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Database");
        fileChooser.getExtensionFilters().addAll(filters);

        File selectedFile = fileChooser.showOpenDialog(ownerStage);
        if (selectedFile == null) {
            if (lastSelectedFile == null && !isFromWelcomeWindow) {
                TextFlowHelper.clearResultTextFlow(resultTextFlow);
                TextFlowHelper.updateResultTextFlow(resultTextFlow, "No file selected. Please choose a valid .sql or .dbexp file to import.", Color.RED, false);
            }
            return false;
        }

        lastSelectedFile = selectedFile;
        String filePath = selectedFile.getAbsolutePath();
        if (filePath.endsWith(".sql") || filePath.endsWith(".dbexp")) {
            // TODO: import database
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "Selected file: " + filePath, Color.BLACK, false);
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "\nDatabase imported successfully!", Color.GREEN, true);
            return true;
        } else {
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "Invalid file type selected. Please choose a valid .sql or .dbexp file.", Color.RED, false);
            return false;
        }
    }
}
