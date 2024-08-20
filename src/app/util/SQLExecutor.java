package app.util;

import app.Window;
import app.mainwindow.MainWindowController;
import cpp.JavaInterface;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SQLExecutor {

    private final JavaInterface databaseManager;
    private final TextFlow consoleTextFlow;

    public SQLExecutor(JavaInterface databaseManager, TextFlow consoleTextFlow) {
        this.databaseManager = databaseManager;
        this.consoleTextFlow = consoleTextFlow;
    }

    public void executeQueries(String code, boolean isFromEditor) {
        System.out.println("[RUN] Executing queries");
        String[] splitCode = SQLFormatter.trimCode(code).split(";");

        long startTime = System.nanoTime();
        boolean hasError = false;
        List<String> executedQueries = new ArrayList<>();
        boolean tabsCreated = false;

        for (String query : splitCode) {
            if (query.isEmpty()) continue;

            String formattedQuery = SQLFormatter.formatSQLQuery(query.trim());
            System.out.println("[RUN] Executing formatted query: " + formattedQuery);

            databaseManager.executeQuery(formattedQuery);

            File outputFile = new File("output.txt");
            if (FileHelper.checkErrors(outputFile, consoleTextFlow)) {
                hasError = true;
                break;
            }

            boolean isSelectQuery = formattedQuery.startsWith("SELECT");
            tabsCreated |= FileHelper.loadTablesFromFile("output.txt", isSelectQuery);

            executedQueries.add(formattedQuery);

            if (formattedQuery.startsWith("DROP TABLE") && isFromEditor) {
                databaseManager.executeQuery("SHOW TABLES");
                FileHelper.loadTablesFromFile("output.txt", false);
            } else if (formattedQuery.startsWith("CREATE TABLE") ||
                    formattedQuery.startsWith("DROP TABLE")) {
                ((MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController()).updateTablesList();
            }
        }

        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;

        if (!hasError && isFromEditor) {
            displaySuccessMessage(executionTime);
            checkForEmptyTables(executedQueries);
        }

        // Set focus based on the execution result
        MainWindowController mainWindowController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
        if (hasError) {
            mainWindowController.resultTabPane.getSelectionModel().select(0); // Select console tab
        } else if (tabsCreated) {
            mainWindowController.resultTabPane.getSelectionModel().select(mainWindowController.resultTabPane.getTabs().size() - 1); // Select last created tab
        } else if (executedQueries.stream().anyMatch(query -> query.equals("SHOW TABLES"))) {
            mainWindowController.resultTabPane.getSelectionModel().select(1); // Select first tab after console
        }
    }

    private void displaySuccessMessage(long executionTime) {
        AnsiTextParser.parseAnsiText("\nQuery has been \033[1;32m\033[1msuccessfully\033[0m executed!", consoleTextFlow);
        AnsiTextParser.parseAnsiText("\n\033[1m\033[4mExecution time\033[0m: ", consoleTextFlow);
        TextFlowHelper.updateResultTextFlow(consoleTextFlow,
                String.format("%.2f ms\n", (double) executionTime / 1000000), Color.BLACK, true);
    }

    private void checkForEmptyTables(List<String> executedQueries) {
        boolean showTablesExecuted = executedQueries.stream()
                .anyMatch(query -> query.equals("SHOW TABLES"));

        if (showTablesExecuted && !FileHelper.hasTablesInOutput("output.txt")) {
            TextFlowHelper.updateResultTextFlow(consoleTextFlow,
                    "No tables found in the database.\n", Color.RED, true);
        }
    }
}