package app.sql;

import app.windows.mainwindow.MainWindowController;
import app.sql.exceptions.SQLException;
import app.util.*;
import cpp.JavaInterface;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;

/**
 * Utility class for executing SQL queries in the database and handling the results.
 */
public class SQLExecutor {

    /**
     * The {@link JavaInterface} instance for executing SQL queries in the database.
     */
    private final JavaInterface databaseManager;

    /**
     * The {@link TextFlow} component for displaying messages in the console.
     */
    private final TextFlow consoleTextFlow;

    /**
     * The {@link MainWindowController} instance for updating the tables list.
     */
    private final MainWindowController mainWindowController;

    /**
     * Creates a new instance of {@link SQLExecutor} with the specified database manager, console text flow,
     * and main window controller.
     *
     * @param databaseManager      the {@link JavaInterface} instance for executing SQL queries
     * @param consoleTextFlow      the {@link TextFlow} component for displaying messages
     * @param mainWindowController the {@link MainWindowController} instance for updating the tables list
     */
    public SQLExecutor(JavaInterface databaseManager, TextFlow consoleTextFlow, MainWindowController mainWindowController) {
        this.databaseManager = databaseManager;
        this.consoleTextFlow = consoleTextFlow;
        this.mainWindowController = mainWindowController;
    }

    /**
     * Executes the SQL queries from the given code in the database and displays the results in the application.
     * <p>Results include the execution time, success message, and error message if any.</p>
     * Adds the executed queries to the history and updates the tables list if necessary.
     *
     * @param code         the SQL code to execute
     * @param isFromEditor true if the code is executed from the editor, false if executed from the other sources
     */
    public void executeQueries(String code, boolean isFromEditor) {
        TextFlowHelper.clearErrorMessage(consoleTextFlow);
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
            String historyQuery = formattedQuery;

            QueryModifiers modifiers = new QueryModifiers();

            try {
                formattedQuery = QueryProcessor.processQuery(formattedQuery, modifiers);
            } catch (SQLException e) {
                TextFlowHelper.addErrorMessage(
                        consoleTextFlow,
                        e.getErrorType(),
                        e.getMainError(),
                        e.getSpecificError(),
                        e.getErrorDescription()
                );
                hasError = true;
                break;
            }

            if (isModifyingQuery(formattedQuery)) {
                mainWindowController.setHasUnsavedChanges(true);
            }

            databaseManager.executeQuery(formattedQuery);

            File outputFile = new File("output.txt");
            boolean querySuccess = !FileHelper.checkErrors(outputFile, consoleTextFlow);
            mainWindowController.addToHistory(historyQuery, querySuccess);

            if (!querySuccess) {
                hasError = true;
                break;
            }

            boolean isSelectQuery = formattedQuery.toLowerCase().startsWith("select");
            if (isSelectQuery) {
                try {
                    applyQueryModifiers(outputFile, modifiers);
                } catch (SQLException e) {
                    TextFlowHelper.addErrorMessage(
                            consoleTextFlow,
                            e.getErrorType(),
                            e.getMainError(),
                            e.getSpecificError(),
                            e.getErrorDescription()
                    );
                    hasError = true;
                    break;
                }
            }

            tabsCreated |= FileHelper.loadTablesFromFile("output.txt", isSelectQuery);

            executedQueries.add(formattedQuery);

            if (formattedQuery.startsWith("DROP TABLE")) {
                String tableName = extractTableName(formattedQuery);
                mainWindowController.removeTableFromList(tableName);
            } else if (formattedQuery.startsWith("CREATE TABLE")) {
                mainWindowController.updateTablesList();
            }
        }

        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;

        if (!hasError && isFromEditor) {
            displaySuccessMessage(executionTime);
            checkForEmptyTables(executedQueries);
        }

        // Set focus based on the execution result
        if (hasError) {
            mainWindowController.resultTabPane.getSelectionModel().select(0); // Select console tab
        } else if (tabsCreated) {
            mainWindowController.resultTabPane.getSelectionModel().select(mainWindowController.resultTabPane.getTabs().size() - 1); // Select last created tab
        } else if (executedQueries.stream().anyMatch(query -> query.equals("SHOW TABLES"))) {
            mainWindowController.resultTabPane.getSelectionModel().select(1); // Select first tab after console
        }
    }

    /**
     * Checks if the given SQL query is a modifying query (INSERT, UPDATE, DELETE, CREATE, DROP).
     *
     * @param query the SQL query to check
     * @return {@code true} if the query is a modifying query, {@code false} otherwise
     */
    public boolean isModifyingQuery(String query) {
        String upperCaseQuery = query.toUpperCase().trim();
        return upperCaseQuery.startsWith("INSERT") ||
                upperCaseQuery.startsWith("UPDATE") ||
                upperCaseQuery.startsWith("DELETE") ||
                upperCaseQuery.startsWith("CREATE") ||
                upperCaseQuery.startsWith("DROP");
    }

    /**
     * Displays a success message in the console with the execution time of the query.
     *
     * @param executionTime the execution time of the query in nanoseconds
     */
    private void displaySuccessMessage(long executionTime) {

        AnsiTextParser.parseAnsiText("\nQuery has been \033[1;32m\033[1msuccessfully\033[0m executed!", consoleTextFlow);

        TextFlowHelper.addExecutionTime(consoleTextFlow, executionTime);
    }

    /**
     * Checks if the executed queries contain the "SHOW TABLES" query and if the output file is empty.
     * Displays a message in the console if no tables are found in the database.
     *
     * @param executedQueries the {@link List} of executed queries
     */
    private void checkForEmptyTables(List<String> executedQueries) {
        boolean showTablesExecuted = executedQueries.stream()
                .anyMatch(query -> query.equals("SHOW TABLES"));

        if (showTablesExecuted && !FileHelper.hasTablesInOutput("output.txt")) {
            TextFlowHelper.updateResultTextFlow(consoleTextFlow,
                    "\nNo tables found in the database.", Color.RED, true);
        }
    }

    /**
     * Extracts the table name from the given SQL query.
     *
     * @param query the SQL query to extract the table name from
     *              <p>(e.g., "DROP TABLE `table_name`" -> "table_name")</p>
     * @return the table name extracted from the query
     */
    private String extractTableName(String query) {
        String[] parts = query.split("\\s+");
        if (parts.length >= 3) {
            return parts[2].replace("`", "").replace("'", "").replace("\"", "");
        }
        return "";
    }

    /**
     * Applies the query modifiers (DISTINCT, ORDER BY, LIMIT, OFFSET) to the result data in the output file.
     *
     * @param outputFile the output file containing the result data
     * @param modifiers  the {@link QueryModifiers} object containing the query modifiers
     */
    private void applyQueryModifiers(File outputFile, QueryModifiers modifiers) throws SQLException {
        try {
            List<String> lines = Files.readAllLines(outputFile.toPath());
            if (lines.size() < 3) return; // No data or only headers

            String resultLine = lines.get(0);
            String headerLine = lines.get(1);
            List<String> dataLines = new ArrayList<>(lines.subList(2, lines.size()));

            List<String> modifiedLines = ResultModifier.applyModifiers(dataLines, headerLine, modifiers);

            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
                writer.println(resultLine);
                for (String line : modifiedLines) {
                    writer.println(line);
                }
            }
        } catch (IOException e) {
            TextFlowHelper.updateResultTextFlow(consoleTextFlow,
                    "\n[ERROR] Error applying query modifiers: " + e.getMessage(), Color.RED, true);
        }
    }

}