package app.util;

import app.mainwindow.MainWindowController;
import cpp.JavaInterface;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * @param databaseManager        the {@link JavaInterface} instance for executing SQL queries
     * @param consoleTextFlow        the {@link TextFlow} component for displaying messages
     * @param mainWindowController   the {@link MainWindowController} instance for updating the tables list
     */
    public SQLExecutor(JavaInterface databaseManager, TextFlow consoleTextFlow, MainWindowController mainWindowController) {
        this.databaseManager = databaseManager;
        this.consoleTextFlow = consoleTextFlow;
        this.mainWindowController = mainWindowController;
    }

    /**
     * Executes the SQL queries from the given code in the database and displays the results in the application.
     * Results include the execution time, success message, and error message if any.
     *
     * @param code         the SQL code to execute
     * @param isFromEditor true if the code is executed from the editor, false if executed from the other sources
     */
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

            if(isModifyingQuery(formattedQuery)) {
                mainWindowController.setHasUnsavedChanges(true);
            }

            databaseManager.executeQuery(formattedQuery);

            File outputFile = new File("output.txt");
            if (FileHelper.checkErrors(outputFile, consoleTextFlow)) {
                hasError = true;
                break;
            }

            boolean isSelectQuery = formattedQuery.startsWith("SELECT");
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
     * @param executionTime the execution time of the query in nanoseconds
     */
    private void displaySuccessMessage(long executionTime) {
        Text spacer = new Text("\n");
        spacer.setStyle("-fx-font-size: 2px;");
        consoleTextFlow.getChildren().add(spacer);

        AnsiTextParser.parseAnsiText("\nQuery has been \033[1;32m\033[1msuccessfully\033[0m executed!\n", consoleTextFlow);

        ImageView timeIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../resources/icons/time_icon.png"))));
        timeIcon.setFitWidth(16);
        timeIcon.setFitHeight(16);

        HBox executionTimeBox = new HBox();
        executionTimeBox.getChildren().add(timeIcon);

        Text spaceBeforeExecutionTime = new Text(" ");

        Text executionTimeText = new Text("Execution time");
        executionTimeText.setStyle("-fx-font-weight: bold; -fx-underline: true;");

        Text colonText = new Text(":");
        colonText.setStyle("-fx-font-weight: bold;");

        Text executionTimeValue = new Text(String.format(" %.2f ms", (double) executionTime / 1000000));

        executionTimeBox.getChildren().addAll(spaceBeforeExecutionTime, executionTimeText, colonText, executionTimeValue);

        consoleTextFlow.getChildren().add(executionTimeBox);
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
                    "\nNo tables found in the database.\n", Color.RED, true);
        }
    }

    /**
     * Extracts the table name from the given SQL query.
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
}