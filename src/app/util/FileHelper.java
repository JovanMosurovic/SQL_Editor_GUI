package app.util;

import app.Window;
import app.mainwindow.MainWindowController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Utility class for handling file operations in the application.
 * <p>This class provides methods for opening files, checking for errors, and loading tables from files.
 * <p>It also contains helper methods for reading table names and creating table tabs in the main window.</p>
 */
public class FileHelper {

    /**
     * The default file name for output.
     */
    public static final String FILE_NAME = "output.txt";

    /**
     * Logger for debugging and error messages.
     */
    private static final Logger LOGGER = Logger.getLogger(FileHelper.class.getName());

    /**
     * Opens the file at the specified path and returns it.
     * If the file does not exist or is a directory, an error message is displayed in the console.
     *
     * @param filePath The path to the file to be opened.
     * @return The {@link File} object representing the opened file, or null if the file does not exist or is a directory.
     */
    public static File openFile(String filePath) {
        MainWindowController mainWindowController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
        File file = new File(trimFilePath(filePath));
        if (!file.exists()) {
            TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow, "\n[ERROR] File not found: " + filePath, Color.RED, true);
            return null;
        }

        if (file.isDirectory()) {
            TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow, "\n[ERROR] File is a directory: " + filePath, Color.RED, true);
            return null;
        }

        return file;
    }

    /**
     * Checks the file for errors and displays them in the console.
     * If the file starts with '!', the content is read and displayed as an error message.
     *
     * @param file           The file to be checked.
     * @param consoleTextFlow The console {@link TextFlow} where the error messages are displayed.
     * @return {@code true} if the file contains errors, {@code false} otherwise.
     */
    public static boolean checkErrors(File file, TextFlow consoleTextFlow) {
        if (file == null) {
            TextFlowHelper.updateResultTextFlow(consoleTextFlow, "\n[ERROR] File is null", Color.RED, true);
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
            TextFlowHelper.updateResultTextFlow(consoleTextFlow, "\n[ERROR] Error reading file: " + e.getMessage(), Color.RED, true);
            return false;
        }
    }

    /**
     * Checks if the output file contains tables.
     * If the file contains more than one line, and it is not '!', it is considered to contain tables.
     *
     * @param outputFilePath The path to the output file.
     * @return {@code true} if the file contains tables, {@code false} otherwise.
     */
    public static boolean hasTablesInOutput(String outputFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFilePath))) {
            int lineCount = 0;
            while (reader.readLine() != null) {
                lineCount++;
                if (lineCount > 1) {
                    return true;
                }
            }
        } catch (IOException e) {
            LOGGER.severe("[ERROR] Error reading file: " + e.getMessage());
        }
        return false;
    }

    /**
     * Loads tables from the specified file and creates tabs for each table in the main window.
     * If the file is a SELECT query, the first line is skipped.
     * If the file contains multiple tables, each table is separated by a line starting with a tab character.
     * The tables are created as {@link TableView} objects and added to the result tab pane.
     *
     * @param fileName      The path to the file containing the tables.
     * @param isSelectQuery {@code true} if the file was created by a SELECT query, {@code false} otherwise.
     * @return {@code true} if tabs were created, {@code false} otherwise.
     */
    public static boolean loadTablesFromFile(String fileName, boolean isSelectQuery) {
        MainWindowController mainWindowController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
        File file = openFile(fileName);

        if (file == null || checkErrors(file, mainWindowController.consoleTextFlow)) {
            return false;
        }

        boolean tabsCreated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String currentTableName = null;
            List<String> headers = null;
            List<List<String>> data = new ArrayList<>();

            // Skip first line only if it's not a SELECT query
            if (!isSelectQuery) {
                br.readLine();
            }

            // Remove all tabs except the console tab only if it's not a SELECT query
            if (!isSelectQuery) {
                while (mainWindowController.resultTabPane.getTabs().size() > 1) {
                    mainWindowController.resultTabPane.getTabs().remove(1);
                }
            }

            while ((line = br.readLine()) != null) {
                if (line.startsWith("\t")) {
                    // New table
                    if (currentTableName != null) {
                        assert headers != null;
                        createTableTab(mainWindowController, currentTableName, headers, data, isSelectQuery);
                        tabsCreated = true;
                    }
                    currentTableName = line.trim();
                    headers = null;
                    data.clear();
                } else if (line.equals("#")) {
                    // End of table
                    assert headers != null;
                    createTableTab(mainWindowController, currentTableName, headers, data, isSelectQuery);
                    tabsCreated = true;
                    currentTableName = null;
                } else {
                    // Table data
                    List<String> rowData = Arrays.asList(line.split("~"));
                    if (headers == null) {
                        headers = rowData;
                    } else {
                        data.add(rowData);
                    }
                }
            }

            // In case the last table is not followed by "#"
            if (currentTableName != null) {
                assert headers != null;
                createTableTab(mainWindowController, currentTableName, headers, data, isSelectQuery);
                tabsCreated = true;
            }
        } catch (IOException e) {
            TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow, "\n[ERROR] Error reading file: " + e.getMessage(), Color.RED, true);
            return false;
        }

        return tabsCreated;
    }

    /**
     * Reads the table names from the specified file and returns them as a list.
     * The table names are lines starting with a tab character.
     *
     * @param fileName The path to the file containing the table names.
     * @return A {@link List} of table names read from the file.
     */
    public static List<String> readTableNames(String fileName) {
        List<String> tableNames = new ArrayList<>();
        File file = openFile(fileName);
        if (file == null) {
            return tableNames;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\t")) {
                    tableNames.add(line.trim());
                }
            }
        } catch (IOException e) {
            LOGGER.severe("[ERROR] Error reading table names: " + e.getMessage());
        }
        return tableNames;
    }

    /**
     * Reads the table column names from the file and returns them as a map of table names to column lists.
     * Each table in the file starts with a line beginning with a tab character, followed by column names.
     * Tables are separated by '#' characters.
     *
     * @param fileName The path to the file containing the table column names.
     * @return A {@link Map} where keys are table names and values are {@link List}s of column names for each table.
     */
    public static Map<String, List<String>> readTableColumnNames(String fileName) {
        Map<String, List<String>> tableColumns = new LinkedHashMap<>();
        File file = openFile(fileName);
        if (file == null) {
            return tableColumns;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String currentTableName = null;

            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.startsWith("\t")) {
                    currentTableName = line.trim();
                } else if (!line.equals("#") && currentTableName != null) {
                    List<String> columns = Arrays.asList(line.split("~"));
                    tableColumns.put(currentTableName, columns);

                    while ((line = br.readLine()) != null && !line.equals("#")) {
                        // Skip data rows
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.severe("[ERROR] Error reading table column names: " + e.getMessage());
        }
        return tableColumns;
    }

    /**
     * Creates a new table tab in the main window with the specified table name, headers and data.
     * The table is created as a {@link TableView} object and added to the result {@link TabPane}.
     *
     * @param mainWindowController The {@link MainWindowController} instance of the main window.
     * @param tableName            The name of the table.
     * @param headers              The headers of the table.
     * @param data                 The data of the table.
     * @param isSelectQuery        {@code true} if the table was created by a SELECT query, {@code false} otherwise.
     */
    private static void createTableTab(MainWindowController mainWindowController, String tableName, List<String> headers, List<List<String>> data, boolean isSelectQuery) {
        TableView<ObservableList<String>> tableView = new TableView<>();

        for (int i = 0; i < headers.size(); i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(headers.get(i));
            column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(colIndex)));

            if(i == 0) {
                column.getStyleClass().add("first-column");
            }

            tableView.getColumns().add(column);
        }

        ObservableList<ObservableList<String>> observableData = FXCollections.observableArrayList();
        for (List<String> row : data) {
            observableData.add(FXCollections.observableArrayList(row));
        }
        tableView.setItems(observableData);

        Image image;
        if (isSelectQuery) {
            image = new Image(Objects.requireNonNull(FileHelper.class.getResourceAsStream("../resources/icons/result_icon.png")));
        } else {
            image = new Image(Objects.requireNonNull(FileHelper.class.getResourceAsStream("../resources/icons/table_icon.png")));
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);

        Label tabLabel = new Label(tableName);
        tabLabel.setGraphic(imageView);
        tabLabel.setContentDisplay(ContentDisplay.LEFT);

        Tab tab = new Tab();
        HBox tabBox = new HBox(imageView, tabLabel);
        tab.setGraphic(tabBox);
        tab.setContent(tableView);

        ContextMenu tabContextMenu = ContextMenuHelper.createTabContextMenu(mainWindowController.resultTabPane, tab);
        tab.setContextMenu(tabContextMenu);

        tabBox.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                ContextMenuHelper.closeTabIfNotConsole(mainWindowController.resultTabPane, tab);
            }
        });

        // Add the new tab to the result tab pane after the console tab
        mainWindowController.resultTabPane.getTabs().add(tab);
    }

    /**
     * Writes an error message to a file, starting with an exclamation mark.
     *
     * @param errorMessage The error message to write.
     * @param fileName The name of the file to write to (default is "error_output.txt").
     */
    public static void writeErrorToFile(String errorMessage, String fileName) {
        System.out.println("Writing error to file: " + errorMessage);
        fileName = (fileName != null && !fileName.isEmpty()) ? fileName : FILE_NAME;
        try (FileWriter writer = new FileWriter(fileName, false)) {
            writer.write("!\n");
            writer.write(errorMessage);
        } catch (IOException e) {
            LOGGER.severe("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Overloaded method that uses the default file name.
     *
     * @param errorMessage The error message to write.
     */
    public static void writeErrorToFile(String errorMessage) {
        writeErrorToFile(errorMessage, null);
    }


    /**
     * Trims the specified file path and removes quotes from it.
     *
     * @param filePath The file path to be trimmed.
     * @return The trimmed file path without quotes.
     */
    private static String trimFilePath(String filePath) {
        return filePath.trim().replaceAll("\"", "").replaceAll("'", "");
    }

}
