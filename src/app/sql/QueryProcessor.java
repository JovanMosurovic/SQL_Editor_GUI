package app.sql;

import app.Window;
import app.mainwindow.MainWindowController;
import app.sql.exception.ColumnAccessException;
import app.sql.exception.MySQLSyntaxErrorException;
import app.sql.exception.SQLException;
import app.util.FileHelper;
import app.util.TextFlowHelper;
import cpp.JavaInterface;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for processing SQL queries.
 */
public class QueryProcessor {

    /**
     * Processes the given SQL query with the specified query modifiers.
     *
     * @param query     the SQL query to process
     * @param modifiers the query modifiers
     * @return the processed SQL query
     * @throws SQLException if there is an error in the SQL query
     */
    public static String processQuery(String query, QueryModifiers modifiers) throws SQLException {
        System.out.println("Original query: " + query);
        query = processAggregateFunctions(query, modifiers);
        System.out.println("After aggregate functions: " + query);
        query = processDistinctQuery(query, modifiers);
        System.out.println("After distinct: " + query);
        query = processGroupByClause(query, modifiers);
        System.out.println("After group by: " + query);
        query = processOrderByClause(query, modifiers);
        System.out.println("After order by: " + query);
        query = processLimitOffsetClause(query, modifiers);
        System.out.println("Final query: " + query);
        return query;
    }

    /**
     * Processes the aggregate functions in the SQL query and sets the aggregate functions in the query modifiers.
     *
     * @param query     the SQL query to process
     * @param modifiers the query modifiers
     * @return the processed SQL query
     */
    private static String processAggregateFunctions(String query, QueryModifiers modifiers) {
        Pattern pattern = Pattern.compile("SELECT\\s+(.*?)\\s+FROM", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            String selectPart = matcher.group(1);
            List<String> columns = Arrays.asList(selectPart.split(","));
            List<AggregateFunction> aggregateFunctions = new ArrayList<>();
            List<String> newColumns = new ArrayList<>();

            for (int i = 0; i < columns.size(); i++) {
                String column = columns.get(i).trim();
                if (column.toUpperCase().startsWith("SUM(") ||
                        column.toUpperCase().startsWith("AVG(") ||
                        column.toUpperCase().startsWith("MAX(") ||
                        column.toUpperCase().startsWith("MIN(") ||
                        column.toUpperCase().startsWith("COUNT(")) {
                    String functionName = column.substring(0, column.indexOf('(')).toUpperCase();
                    String argument = column.substring(column.indexOf('(') + 1, column.length() - 1).trim();
                    aggregateFunctions.add(new AggregateFunction(functionName, argument, i));
                    if (!argument.equals("*") || !functionName.equals("COUNT")) {
                        newColumns.add(argument);
                    }
                } else {
                    newColumns.add(column);
                    aggregateFunctions.add(new AggregateFunction("NONE", column, i));
                }
            }

            if (aggregateFunctions.stream().anyMatch(af -> !af.getFunction().equals("NONE"))) {
                modifiers.setAggregateFunctions(aggregateFunctions);
                String newSelectPart = newColumns.isEmpty() ? "*" : String.join(", ", newColumns);
                return query.replace(selectPart, newSelectPart);
            }
        }

        return query;
    }

    /**
     * Processes the DISTINCT clause in the SQL query and sets the distinct columns in the query modifiers.
     *
     * @param query     the SQL query to process
     * @param modifiers the query modifiers
     * @return the processed SQL query
     */
    private static String processDistinctQuery(String query, QueryModifiers modifiers) {
        Pattern pattern = Pattern.compile("SELECT\\s+DISTINCT\\s*(?:\\((.*?)\\))?\\s*(.*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            String distinctColumnsStr = matcher.group(1);
            String restOfQuery = matcher.group(2);

            if (distinctColumnsStr != null) {
                modifiers.setDistinctColumns(Arrays.asList(distinctColumnsStr.split(",\\s*")));
            } else {
                modifiers.setDistinctColumns(Collections.singletonList("*"));
            }

            return "SELECT " + restOfQuery;
        }
        return query;
    }

    /**
     * Processes the ORDER BY clause in the SQL query and sets the order by clauses in the query modifiers.
     *
     * @param query     the SQL query to process
     * @param modifiers the query modifiers
     * @return the processed SQL query
     * @throws SQLException if there is an error in the ORDER BY clause
     */
    private static String processOrderByClause(String query, QueryModifiers modifiers) throws SQLException {
        Pattern pattern = Pattern.compile("(.*?)\\s+ORDER\\s+BY\\s+(.+?)(\\s+LIMIT\\s+.*|$)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            String beforeOrderBy = matcher.group(1);
            String orderByClause = matcher.group(2);
            String afterOrderBy = matcher.group(3);

            // Validate the ORDER BY clause syntax
            Pattern orderByPattern = Pattern.compile("^\\s*([\\w.]+\\s*(ASC|DESC)?\\s*(,\\s*[\\w.]+\\s*(ASC|DESC)?\\s*)*)$", Pattern.CASE_INSENSITIVE);
            if (!orderByPattern.matcher(orderByClause).matches()) {
                throw new MySQLSyntaxErrorException("Invalid ORDER BY clause syntax.", "Place where error occurred: \u001B[3m" + orderByClause + "\u001B[0m");
            }

            // Validate the columns in the ORDER BY clause
            List<String> orderByColumns = new ArrayList<>();
            String[] parts = orderByClause.split(",");
            for (String part : parts) {
                String column = part.trim().split("\\s+")[0];
                orderByColumns.add(column);
            }

            validateColumns(beforeOrderBy, orderByColumns);

            modifiers.setOrderByClauses(parseOrderByClauses(orderByClause));
            return beforeOrderBy + afterOrderBy;
        }
        return query;
    }

    /**
     * Parses the ORDER BY clauses from the given ORDER BY clause string.
     *
     * @param orderByClauseString the ORDER BY clause string
     * @return the list of ORDER BY clauses
     */
    private static List<OrderByClause> parseOrderByClauses(String orderByClauseString) {
        List<OrderByClause> clauses = new ArrayList<>();
        String[] parts = orderByClauseString.split(",");
        for (String part : parts) {
            String[] columnAndDirection = part.trim().split("\\s+");
            String column = columnAndDirection[0];
            boolean isAscending = true;
            if (columnAndDirection.length > 1) {
                String direction = columnAndDirection[columnAndDirection.length - 1].toUpperCase();
                isAscending = !direction.equals("DESC");
            }
            clauses.add(new OrderByClause(column, isAscending));
        }
        return clauses;
    }

    /**
     * Processes the LIMIT and OFFSET clauses in the SQL query and sets the limit offset clause in the query modifiers.
     *
     * @param query     the SQL query to process
     * @param modifiers the query modifiers
     * @return the processed SQL query
     */
    private static String processLimitOffsetClause(String query, QueryModifiers modifiers) {
        Pattern pattern = Pattern.compile("(.*?\\s+LIMIT\\s+)(-?\\d+)(?:\\s+OFFSET\\s+(-?\\d+))?$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            String beforeLimit = matcher.group(1);
            int limit = Integer.parseInt(matcher.group(2));
            int offset = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : 0;

            // Negative values are treated as neutral
            MainWindowController mainWindowController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
            if (limit < 0) {
                TextFlowHelper.addWarningMessage(mainWindowController.consoleTextFlow, "Limit is negative");
                limit = Integer.MAX_VALUE;
            }
            if (offset < 0) {
                TextFlowHelper.addWarningMessage(mainWindowController.consoleTextFlow, "Offset is negative");
                offset = 0;
            }

            modifiers.setLimitOffsetClause(new LimitOffsetClause(limit, offset));

            return beforeLimit.trim();
        }
        return query;
    }

    /**
     * Processes the GROUP BY clause in the SQL query and sets the group by columns in the query modifiers.
     * <p>Also processes the HAVING clause if present.</p>
     *
     * @param query     the SQL query to process
     * @param modifiers the query modifiers
     * @return the processed SQL query
     * @throws SQLException if there is an error in the GROUP BY clause
     */
    private static String processGroupByClause(String query, QueryModifiers modifiers) throws SQLException {
        Pattern pattern = Pattern.compile("(.*?)\\s+GROUP\\s+BY\\s+(.+?)(\\s+HAVING\\s+(.+?))?(\\s+ORDER\\s+BY\\s+.*|\\s+LIMIT\\s+.*|$)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            String beforeGroupBy = matcher.group(1);
            String groupByClause = matcher.group(2);
            String havingClause = matcher.group(4); // This will be null if HAVING is not present
            String afterHaving = matcher.group(5);

            // Validate the GROUP BY clause syntax
            Pattern groupByPattern = Pattern.compile("^\\s*([\\w.]+\\s*(,\\s*[\\w.]+\\s*)*)$", Pattern.CASE_INSENSITIVE);
            if (!groupByPattern.matcher(groupByClause).matches()) {
                throw new MySQLSyntaxErrorException("Invalid GROUP BY clause syntax", "Place where error occurred: \u001B[3m" + groupByClause + "\u001B[0m");
            }

            List<String> groupByColumns = new ArrayList<>();
            String[] parts = groupByClause.split("\\s*,\\s*");
            for (String part : parts) {
                String column = part.trim();
                groupByColumns.add(column);
            }

            // Validate columns
            Set<String> availableColumns = validateColumns(beforeGroupBy, groupByColumns);

            // Validate HAVING clause if present
            if (havingClause != null) {
                System.out.println("Having clause: " + havingClause);
                validateHavingClause(havingClause.trim(), availableColumns);
            }

            modifiers.setGroupByColumns(groupByColumns);
            if (havingClause != null) {
                modifiers.setHavingClause(havingClause.trim());
                return beforeGroupBy + afterHaving;
            }
            return beforeGroupBy + afterHaving;
        }
        return query;
    }

    /**
     * Validates the HAVING clause against the available columns.
     *
     * @param havingClause     the HAVING clause to validate
     * @param availableColumns the available columns
     * @throws SQLException if there is an error in the HAVING clause
     */
    private static void validateHavingClause(String havingClause, Set<String> availableColumns) throws SQLException {
        Pattern pattern = Pattern.compile(
                "\\b([\\w.]+|(?:COUNT|SUM|AVG|MIN|MAX)\\([\\w.*]+\\))\\s*([<>=!]+|LIKE|NOT LIKE|IN|NOT IN|BETWEEN)\\s*(.+)",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(havingClause);

        if (!matcher.matches()) {
            throw new MySQLSyntaxErrorException("Invalid HAVING clause syntax", "Place where error occurred: \u001B[3m" + havingClause + "\u001B[0m");
        }

        String columnOrFunction = matcher.group(1);
        String operator = matcher.group(2);
        String value = matcher.group(3);

        // Validate column or function
        if (!isValidColumnOrFunction(columnOrFunction, availableColumns)) {
            throw new ColumnAccessException("Cannot access column or process the function.", "Place where error occurred: \u001B[1m" + columnOrFunction + "\u001B[0m");
        }

        // Validate operator
        if (!isValidOperator(operator)) {
            throw new MySQLSyntaxErrorException("Invalid operator in HAVING clause", "Invalid operator: \u001B[1m" + operator + "\u001B[0m");
        }

        // Validate value (basic check)
        if (value.trim().isEmpty()) {
            throw new MySQLSyntaxErrorException("Missing value in HAVING clause", "Place where error occurred: \u001B[3m" + havingClause + "\u001B[0m");
        }
    }

    /**
     * Checks if the given column or function is valid.
     *
     * @param columnOrFunction   The column or function to check.
     * @param availableColumns   The available columns.
     * @return {@code true} if the column or function is valid, {@code false} otherwise.
     */
    private static boolean isValidColumnOrFunction(String columnOrFunction, Set<String> availableColumns) {
        if (availableColumns.contains(columnOrFunction)) {
            return true;
        }
        Pattern functionPattern = Pattern.compile("(COUNT|SUM|AVG|MIN|MAX)\\(([\\w.*]+)\\)", Pattern.CASE_INSENSITIVE);
        Matcher functionMatcher = functionPattern.matcher(columnOrFunction);
        if (functionMatcher.matches()) {
            String argument = functionMatcher.group(2);
            return argument.equals("*") || availableColumns.contains(argument);
        }
        return false;
    }

    /**
     * Checks if the given operator is valid.
     *
     * @param operator The operator to check.
     * @return {@code true} if the operator is valid, {@code false} otherwise.
     */
    private static boolean isValidOperator(String operator) {
        Set<String> validOperators = new HashSet<>(Arrays.asList(
                "=", "<>", "!=", ">", "<", ">=", "<="
        ));
        return validOperators.contains(operator.toUpperCase());
    }

    /**
     * Validates columns against available columns in the specified tables.
     *
     * @param query          The SQL query being processed.
     * @param columnsToCheck List of column names to validate.
     * @return Set of available columns.
     * @throws SQLException If an invalid column is found.
     */
    private static Set<String> validateColumns(String query, List<String> columnsToCheck) throws SQLException {
        Set<String> tableNames = extractTableNamesFromFromStatement(query);
        Set<String> availableColumns = new HashSet<>();

        for (String tableName : tableNames) {
            JavaInterface.getInstance().executeQuery("SELECT * FROM " + tableName);
            Map<String, List<String>> tableColumns = FileHelper.readTableColumnNames(FileHelper.FILE_NAME);
            for (List<String> columns : tableColumns.values()) {
                availableColumns.addAll(columns);
            }
        }

        for (String column : columnsToCheck) {
            if (!availableColumns.contains(column)) {
                throw new ColumnAccessException("Cannot access column with the provided name.", "\u001B[1m\u001B[31mColumn: \u001B[0m" + column + "\u001B[1m\u001B[31m does not exist in the table\u001B[0m");
            }
        }

        return availableColumns;
    }

    /**
     * Extracts table names from the FROM clause of a SQL query.
     *
     * @param query the SQL query to extract table names from
     * @return a Set of table names extracted from the query
     */
    private static Set<String> extractTableNamesFromFromStatement(String query) {
        Set<String> tableNames = new HashSet<>();
        Pattern pattern = Pattern.compile("(?i)select\\s+[^;]+\\s+from\\s+([a-zA-Z0-9_]+)(?:\\s+as\\s+[a-zA-Z0-9_]+\\s+inner\\s+join\\s+([a-zA-Z0-9_]+)\\s+as\\s+[a-zA-Z0-9_]+\\s+on\\s+[a-zA-Z0-9_]+\\.[a-zA-Z0-9_]+\\s*=\\s*[a-zA-Z0-9_]+\\.[a-zA-Z0-9_]+)?\\s*.*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            tableNames.add(matcher.group(1));
            if (matcher.group(2) != null) {
                tableNames.add(matcher.group(2));
            }
        }

        return tableNames;
    }
}