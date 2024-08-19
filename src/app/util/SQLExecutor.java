package app.util;

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

    public void executeQueries(String code) {
        System.out.println("[RUN] Executing queries");
        String[] splitCode = SQLFormatter.trimCode(code).split(";");

        long startTime = System.nanoTime();
        boolean hasError = false;
        List<String> executedQueries = new ArrayList<>();

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
            FileHelper.loadTablesFromFile("output.txt", isSelectQuery);

            executedQueries.add(formattedQuery);
        }

        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;

        if (!hasError) {
            displaySuccessMessage(executionTime);
            checkForEmptyTables(executedQueries);
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