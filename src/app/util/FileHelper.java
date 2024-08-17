package app.util;

import app.Window;
import app.mainwindow.MainWindowController;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;


import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class FileHelper {

    public static File openFile(String filePath) {
        MainWindowController mainWindowController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
        File file = new File(trimFilePath(filePath));
        if (!file.exists()) {
            TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow, "[ERROR] File not found: " + filePath, Color.RED, true);
            return null;
        }

        if (file.isDirectory()) {
            TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow, "[ERROR] File is a directory: " + filePath, Color.RED, true);
            return null;
        }

        return file;
    }

    public static boolean checkErrors(File file, TextFlow consoleTextFlow) {
        if (file == null) {
            TextFlowHelper.updateResultTextFlow(consoleTextFlow, "[ERROR] File is null", Color.RED, true);
            return true;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int firstChar = reader.read();
            if (firstChar == '!') {
                // If the file starts with '!', read the rest of the file and output errors
                StringBuilder contentBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    contentBuilder.append(line).append("\n");
                }
                String content = contentBuilder.toString();

                consoleTextFlow.getChildren().clear();
                AnsiTextParser.parseAnsiText(content, consoleTextFlow);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            TextFlowHelper.updateResultTextFlow(consoleTextFlow, "[ERROR] Error reading file: " + e.getMessage(), Color.RED, true);
            return false;
        }
    }

    private static String trimFilePath(String filePath) {
        return filePath.trim().replaceAll("\"", "").replaceAll("'", "");
    }

}
