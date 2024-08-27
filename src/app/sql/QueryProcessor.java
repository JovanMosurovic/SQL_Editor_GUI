package app.sql;

import app.Window;
import app.mainwindow.MainWindowController;
import app.util.TextFlowHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
     */
    public static String processQuery(String query, QueryModifiers modifiers) {
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
     */
    private static String processOrderByClause(String query, QueryModifiers modifiers) {
        Pattern pattern = Pattern.compile("(.*?)\\s+ORDER\\s+BY\\s+(.+?)(\\s+LIMIT\\s+.*|$)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            String beforeOrderBy = matcher.group(1);
            String orderByClause = matcher.group(2);
            String afterOrderBy = matcher.group(3);
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
                TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow,
                        "Warning: Limit is negative", TextFlowHelper.warningYellow, true);
                limit = Integer.MAX_VALUE;
            }
            if (offset < 0) {
                TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow,
                        "Warning: Offset is negative", TextFlowHelper.warningYellow, true);
                offset = 0;
            }

            modifiers.setLimitOffsetClause(new LimitOffsetClause(limit, offset));

            return beforeLimit.trim();
        }
        return query;
    }

    private static String processGroupByClause(String query, QueryModifiers modifiers) {
        Pattern pattern = Pattern.compile("(.*?)\\s+GROUP\\s+BY\\s+(.+?)(\\s+HAVING\\s+.*|\\s+ORDER\\s+BY\\s+.*|\\s+LIMIT\\s+.*|$)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            String beforeGroupBy = matcher.group(1);
            String groupByClause = matcher.group(2);
            String afterGroupBy = matcher.group(3);
            modifiers.setGroupByColumns(Arrays.asList(groupByClause.split("\\s*,\\s*")));
            return beforeGroupBy + afterGroupBy;
        }
        return query;
    }
}