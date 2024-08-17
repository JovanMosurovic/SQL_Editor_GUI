package app.util;

import app.Window;
import app.mainwindow.MainWindowController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static void loadTablesFromFile(String fileName) {
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

            // Preskočimo prvu liniju (Database name)
            br.readLine();

            // Uklonimo sve postojeće tabove osim prvog (Console)
            while (mainWindowController.resultTabPane.getTabs().size() > 1) {
                mainWindowController.resultTabPane.getTabs().remove(1);
            }

            while ((line = br.readLine()) != null) {
                if (line.startsWith("\t")) {
                    // Nova tabela
                    if (currentTableName != null) {
                        createTableTab(mainWindowController, currentTableName, headers, data);
                    }
                    currentTableName = line.trim();
                    headers = null;
                    data.clear();
                } else if (line.equals("#")) {
                    // Kraj tabele
                    createTableTab(mainWindowController, currentTableName, headers, data);
                    currentTableName = null;
                } else {
                    // Podaci tabele
                    List<String> rowData = Arrays.asList(line.split("~"));
                    if (headers == null) {
                        headers = rowData;
                    } else {
                        data.add(rowData);
                    }
                }
            }

            // Za slučaj da fajl ne završava sa "#"
            if (currentTableName != null) {
                createTableTab(mainWindowController, currentTableName, headers, data);
            }

            TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow, "Tables loaded successfully", Color.GREEN, true);
        } catch (IOException e) {
            TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow, "[ERROR] Error reading file: " + e.getMessage(), Color.RED, true);
        }
    }

    private static void createTableTab(MainWindowController mainWindowController, String tableName, List<String> headers, List<List<String>> data) {
        TableView<ObservableList<String>> tableView = new TableView<>();

        for (int i = 0; i < headers.size(); i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(headers.get(i));
            column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(colIndex)));
            tableView.getColumns().add(column);
        }

        ObservableList<ObservableList<String>> observableData = FXCollections.observableArrayList();
        for (List<String> row : data) {
            observableData.add(FXCollections.observableArrayList(row));
        }
        tableView.setItems(observableData);

        Tab tab = new Tab(tableName);
        tab.setContent(tableView);

        // Dodajemo novi tab nakon "Console" taba
        mainWindowController.resultTabPane.getTabs().add(tab);
    }

    private static String trimFilePath(String filePath) {
        return filePath.trim().replaceAll("\"", "").replaceAll("'", "");
    }

}
