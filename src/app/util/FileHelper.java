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

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class FileHelper {

    // logger
    private static final Logger LOGGER = Logger.getLogger(FileHelper.class.getName());

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

    public static void loadTablesFromFile(String fileName, boolean isSelectQuery) {
        MainWindowController mainWindowController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
        File file = openFile(fileName);

        if (file == null || checkErrors(file, mainWindowController.consoleTextFlow)) {
            return;
        }

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
                    }
                    currentTableName = line.trim();
                    headers = null;
                    data.clear();
                } else if (line.equals("#")) {
                    // End of table
                    assert headers != null;
                    createTableTab(mainWindowController, currentTableName, headers, data, isSelectQuery);
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
            }
        } catch (IOException e) {
            TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow, "\n[ERROR] Error reading file: " + e.getMessage(), Color.RED, true);
        }
    }

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

    private static String trimFilePath(String filePath) {
        return filePath.trim().replaceAll("\"", "").replaceAll("'", "");
    }

}
