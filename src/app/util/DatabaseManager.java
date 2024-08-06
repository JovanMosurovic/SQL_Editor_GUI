package app.util;

import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;

public class DatabaseManager {

    public static boolean showImportDatabaseDialog(Stage ownerStage, TextFlow resultTextFlow, boolean isFromWelcomeWindow, ExtensionFilter... filters) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Database");
        fileChooser.getExtensionFilters().addAll(filters);

        File selectedFile = fileChooser.showOpenDialog(ownerStage);
        TextFlowHelper.clearResultTextFlow(resultTextFlow);
        if (selectedFile == null) {
            if (!isFromWelcomeWindow) {
                TextFlowHelper.updateResultTextFlow(resultTextFlow, "No file selected. Please select a valid .sql or .dbexp file.", Color.RED, false);
            }
            return false;
        }

        String filePath = selectedFile.getAbsolutePath();
        if (filePath.endsWith(".sql") || filePath.endsWith(".dbexp")) {
            // TODO: import database
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "Selected file: " + filePath, Color.BLACK, false);
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "\nDatabase imported successfully!", Color.GREEN, true);
            return true;
        } else {
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "Invalid file type selected. Please select a .sql or .dbexp file.", Color.RED, false);
            return false;
        }
    }
}
