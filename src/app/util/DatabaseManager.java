package app.util;

import cpp.JavaInterface;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;

/**
 * Utility class for managing database operations in this JavaFX application.
 * Provides methods to import and export database files.
 */
public class DatabaseManager {

    /**
     * The last selected file in the file chooser dialog.
     * Used to remember the last selected file for importing the database.
     */
    private static File lastSelectedFile = null;

    /**
     * Shows a file chooser dialog for exporting the database to a .sql or .dbexp file.
     *
     * @param ownerStage          the owner {@link Stage} for the file chooser dialog
     * @param resultTextFlow      the {@link TextFlow} component for displaying messages
     * @param isFromWelcomeWindow true if the method is called from the welcome window, false otherwise
     * @param filters             the file extension filters for the file chooser dialog
     * @return true if the database was exported successfully, false otherwise
     */
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
            JavaInterface.getInstance().importDatabase(filePath);
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "Selected file: " + filePath, Color.BLACK, false);
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "\nDatabase imported successfully!", Color.GREEN, true);
            return true;
        } else {
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "Invalid file type selected. Please choose a valid .sql or .dbexp file.", Color.RED, false);
            return false;
        }
    }
}
