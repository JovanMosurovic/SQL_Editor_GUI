package app.util;

import cpp.JavaInterface;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;

/**
 * Utility class for managing database operations in this application.
 * <p>Provides methods to import and export database files.</p>
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
                TextFlowHelper.clearTextFlow(resultTextFlow);
                TextFlowHelper.updateResultTextFlow(resultTextFlow, "No file selected. Please choose a valid .sql or .dbexp file to import.", Color.RED, false);
            }
            return false;
        }

        lastSelectedFile = selectedFile;
        String filePath = selectedFile.getAbsolutePath();
        TextFlowHelper.clearTextFlow(resultTextFlow);
        if (filePath.endsWith(".sql") || filePath.endsWith(".dbexp")) {
            JavaInterface.getInstance().importDatabase(filePath);
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "Selected file: " + filePath, Color.BLACK, false);
            AnsiTextParser.parseAnsiText("\n\u001B[1m\u001B[32mDatabase imported successfully!\u001B[0m\n", resultTextFlow);

            return true;
        } else {
            AnsiTextParser.parseAnsiText("\u001B[1m\u001B[31mDatabase import failed!\u001B[0m\n", resultTextFlow);
            TextFlowHelper.updateResultTextFlow(resultTextFlow, "Invalid file type selected. Please choose a valid .sql or .dbexp file.", Color.DARKRED, true);
            return false;
        }
    }

    /**
     * Gets the last selected file in the file chooser dialog.
     * @return the last selected {@link File} object
     */
    public static File getLastSelectedFile() {
        return lastSelectedFile;
    }
}
