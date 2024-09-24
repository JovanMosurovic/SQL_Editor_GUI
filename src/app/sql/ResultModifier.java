package app.sql;

import app.Window;
import app.mainwindow.MainWindowController;
import app.util.TextFlowHelper;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * Utility class for applying modifiers to the result data.
 */
public class ResultModifier {

    /**
     * Applies the query modifiers to the result data.
     *
     * @param dataLines  the list of data lines
     * @param headerLine the header line
     * @param modifiers  the query modifiers
     * @return the list of data lines with the query modifiers applied
     * @throws MySQLSyntaxErrorException if there is a syntax error in the SQL query
     */
    public static List<String> applyModifiers(List<String> dataLines, String headerLine, QueryModifiers modifiers) throws MySQLSyntaxErrorException {
        List<String> result = new ArrayList<>(dataLines);
        String newHeaderLine = headerLine;

        if (!modifiers.getAggregateFunctions().isEmpty()) {
            if (modifiers.getGroupByColumns().isEmpty()) {
                // Apply aggregate functions without GROUP BY
                List<String> aggregatedResult = applyAggregateFunctions(result, headerLine, modifiers.getAggregateFunctions());
                newHeaderLine = aggregatedResult.get(0);
                result = new ArrayList<>(aggregatedResult.subList(1, aggregatedResult.size()));
            } else {
                // Apply GROUP BY with aggregate functions
                List<String> groupedResult = applyGroupBy(result, headerLine, modifiers.getGroupByColumns(), modifiers.getAggregateFunctions(), modifiers.getHavingClause());
                newHeaderLine = groupedResult.get(0);
                result = new ArrayList<>(groupedResult.subList(1, groupedResult.size()));
            }
        }

        if (!modifiers.getDistinctColumns().isEmpty()) {
            result = applyDistinct(result, newHeaderLine, modifiers.getDistinctColumns());
        }

        if (!modifiers.getOrderByClauses().isEmpty()) {
            result = new ArrayList<>(applyOrderBy(result, newHeaderLine, modifiers.getOrderByClauses()));
        }

        if (modifiers.getLimitOffsetClause() != null) {
            result = applyLimitOffset(result, modifiers.getLimitOffsetClause());
        }

        List<String> finalResult = new ArrayList<>();
        finalResult.add(newHeaderLine);
        finalResult.addAll(result);
        return finalResult;
    }

    /**
     * Applies the aggregate functions to the result data.
     *
     * @param dataLines           the list of data lines
     * @param headerLine          the header line
     * @param aggregateFunctions  the list of aggregate functions
     * @return the list of data lines with the aggregate functions applied
     */
    private static List<String> applyAggregateFunctions(List<String> dataLines, String headerLine, List<AggregateFunction> aggregateFunctions) {
        List<String> headers = Arrays.asList(headerLine.split("~"));
        Map<String, Double> sums = new HashMap<>();
        Map<String, Integer> counts = new HashMap<>();
        Map<String, String> mins = new HashMap<>();
        Map<String, String> maxs = new HashMap<>();
        Map<String, String> firstValues = new HashMap<>();

        int totalCount = dataLines.size();

        for (String line : dataLines) {
            String[] values = line.split("~");
            for (AggregateFunction func : aggregateFunctions) {
                if (!(func.getFunction().equals("COUNT") && func.getArgument().equals("*"))) {
                    // handle COUNT(*) separately
                    int colIndex = headers.indexOf(func.getArgument());
                    if (colIndex != -1 && colIndex < values.length) {
                        String value = values[colIndex];
                        if (func.getFunction().equals("NONE")) {
                            if (!firstValues.containsKey(func.getArgument())) {
                                firstValues.put(func.getArgument(), value);
                            }
                        } else {
                            switch (func.getFunction()) {
                                case "SUM":
                                case "AVG":
                                    try {
                                        double numericValue = Double.parseDouble(value);
                                        sums.merge(func.getArgument(), numericValue, Double::sum);
                                        counts.merge(func.getArgument(), 1, Integer::sum);
                                    } catch (NumberFormatException e) {
                                        // Ignore non-numeric values for SUM and AVG
                                    }
                                    break;
                                case "MIN":
                                    mins.merge(func.getArgument(), value, (oldVal, newVal) -> oldVal.compareTo(newVal) <= 0 ? oldVal : newVal);
                                    break;
                                case "MAX":
                                    maxs.merge(func.getArgument(), value, (oldVal, newVal) -> oldVal.compareTo(newVal) >= 0 ? oldVal : newVal);
                                    break;
                                case "COUNT":
                                    counts.merge(func.getArgument(), 1, Integer::sum);
                                    break;
                            }
                        }
                    }
                }
            }
        }

        StringBuilder resultLine = new StringBuilder();
        StringBuilder newHeaderLine = new StringBuilder();

        for (AggregateFunction func : aggregateFunctions) {
            if (func.getFunction().equals("NONE")) {
                resultLine.append(firstValues.getOrDefault(func.getArgument(), "")).append("~");
                newHeaderLine.append(func.getArgument()).append("~");
            } else {
                String value = "";
                switch (func.getFunction()) {
                    case "SUM":
                        value = String.format("%.2f", sums.getOrDefault(func.getArgument(), 0.0));
                        break;
                    case "AVG":
                        value = String.format("%.2f", sums.getOrDefault(func.getArgument(), 0.0) / counts.getOrDefault(func.getArgument(), 1));
                        break;
                    case "MIN":
                        value = mins.getOrDefault(func.getArgument(), "");
                        break;
                    case "MAX":
                        value = maxs.getOrDefault(func.getArgument(), "");
                        break;
                    case "COUNT":
                        if (func.getArgument().equals("*")) {
                            value = String.valueOf(totalCount);
                        } else {
                            value = String.valueOf(counts.getOrDefault(func.getArgument(), 0));
                        }
                        break;
                }
                resultLine.append(value).append("~");
                newHeaderLine.append(func.getFunction()).append("(").append(func.getArgument()).append(")").append("~");
            }
        }

        if (resultLine.length() > 0) {
            resultLine.setLength(resultLine.length() - 1); // Remove last ~
            newHeaderLine.setLength(newHeaderLine.length() - 1); // Remove last ~
        }

        List<String> result = new ArrayList<>();
        result.add(newHeaderLine.toString());
        result.add(resultLine.toString());

        return result;
    }

    /**
     * Applies the DISTINCT modifier to the result data.
     *
     * @param dataLines        the list of data lines
     * @param headerLine       the header line
     * @param distinctColumns  the list of distinct columns
     * @return the list of data lines with distinct rows
     */
    private static List<String> applyDistinct(List<String> dataLines, String headerLine, List<String> distinctColumns) {
        List<String> headerList = Arrays.asList(headerLine.split("~"));
        Set<String> uniqueRows = new LinkedHashSet<>();
        boolean isAllColumns = distinctColumns.contains("*") || distinctColumns.size() == headerList.size();

        for (String line : dataLines) {
            String[] values = line.split("~");
            StringBuilder distinctValues = new StringBuilder();

            if (isAllColumns) {
                distinctValues.append(line);
            } else {
                for (String column : distinctColumns) {
                    int index = headerList.indexOf(column);
                    if (index != -1 && index < values.length) {
                        distinctValues.append(values[index]).append("~");
                    }
                }
                distinctValues.setLength(distinctValues.length() - 1); // Remove last ~
            }

            uniqueRows.add(distinctValues.toString());
        }

        return new ArrayList<>(uniqueRows);
    }

    /**
     * Applies the ORDER BY modifier to the result data.
     *
     * @param dataLines       the list of data lines
     * @param headerLine      the header line
     * @param orderByClauses  the list of ORDER BY clauses
     * @return the list of data lines sorted by the ORDER BY clauses
     */
    private static List<String> applyOrderBy(List<String> dataLines, String headerLine, List<OrderByClause> orderByClauses) {
        List<String> headerList = Arrays.asList(headerLine.split("~"));
        MainWindowController mainWindowController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();

        // Checking if all columns in the ORDER BY clause exist
        for (OrderByClause clause : orderByClauses) {
            if (!headerList.contains(clause.getColumn())) {
                TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow,
                        "\n\nERROR: Column '" + clause.getColumn() + "' in ORDER BY clause does not exist in the result set.", Color.RED, true); // todo insert error into file
                return dataLines; // Return original data without sorting

            }
        }

        dataLines.sort((line1, line2) -> {
            String[] values1 = line1.split("~");
            String[] values2 = line2.split("~");
            for (OrderByClause clause : orderByClauses) {
                int columnIndex = headerList.indexOf(clause.getColumn());
                if (columnIndex != -1 && columnIndex < values1.length && columnIndex < values2.length) {
                    int comparison = values1[columnIndex].compareTo(values2[columnIndex]);
                    if (comparison != 0) {
                        return clause.isAscending() ? comparison : -comparison;
                    }
                }
            }
            return 0;
        });

        return dataLines;
    }

    /**
     * Applies the LIMIT and OFFSET modifiers to the result data.
     *
     * @param dataLines        the list of data lines
     * @param limitOffsetClause  the LIMIT and OFFSET clauses
     * @return the list of data lines with the LIMIT and OFFSET applied
     */
    private static List<String> applyLimitOffset(List<String> dataLines, LimitOffsetClause limitOffsetClause) {
        if (limitOffsetClause == null) {
            return dataLines;
        }

        int offset = limitOffsetClause.getOffset();
        int limit = limitOffsetClause.getLimit();

        MainWindowController mainWindowController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();

        // If offset is greater than or equal to the number of rows, return empty result
        if (offset >= dataLines.size()) {
            TextFlowHelper.addWarningMessage(mainWindowController.consoleTextFlow, "OFFSET is greater than or equal to the number of rows. Returning empty result.");
            return new ArrayList<>();
        }

        // If limit is 0, return empty result
        if (limit == 0) {
            TextFlowHelper.addWarningMessage(mainWindowController.consoleTextFlow, "LIMIT is 0. Returning empty result.");
            return new ArrayList<>();
        }

        int endIndex = Math.min(offset + limit, dataLines.size());

        return new ArrayList<>(dataLines.subList(offset, endIndex));
    }

    /**
     * Applies the GROUP BY modifier to the result data.
     *
     * @param dataLines        the list of data lines
     * @param headerLine       the header line
     * @param groupByColumns   the list of GROUP BY columns
     * @param aggregateFunctions  the list of aggregate functions
     * @param havingClause     the HAVING clause
     * @return the list of data lines with the GROUP BY clause applied
     */
    private static List<String> applyGroupBy(List<String> dataLines, String headerLine, List<String> groupByColumns, List<AggregateFunction> aggregateFunctions, String havingClause) throws MySQLSyntaxErrorException {
        List<String> headers = Arrays.asList(headerLine.split("~"));
        Map<String, List<String>> groupedData = new HashMap<>();

        // Grouping data
        for (String line : dataLines) {
            String[] values = line.split("~");
            StringBuilder groupKey = new StringBuilder();
            for (String column : groupByColumns) {
                int index = headers.indexOf(column);
                if (index != -1 && index < values.length) {
                    groupKey.append(values[index]).append("~");
                }
            }
            groupedData.computeIfAbsent(groupKey.toString(), k -> new ArrayList<>()).add(line);
        }

        // Applying aggregate functions to each group
        List<String> result = new ArrayList<>();
        StringBuilder newHeaderLine = new StringBuilder();

        // Building new header line
        for (String column : groupByColumns) {
            newHeaderLine.append(column).append("~");
        }
        for (AggregateFunction func : aggregateFunctions) {
            if (!func.getFunction().equals("NONE")) {
                newHeaderLine.append(func.getFunction()).append("(").append(func.getArgument()).append(")").append("~");
            }
        }
        String finalHeaderLine = newHeaderLine.substring(0, newHeaderLine.length() - 1);
        result.add(finalHeaderLine);

        // Processing each group
        for (List<String> group : groupedData.values()) {
            StringBuilder resultLine = new StringBuilder();
            String[] firstLineValues = group.get(0).split("~");

            // Adding group by columns
            for (String column : groupByColumns) {
                int index = headers.indexOf(column);
                if (index != -1 && index < firstLineValues.length) {
                    resultLine.append(firstLineValues[index]).append("~");
                }
            }

            // Calculating and adding aggregate function results
            for (AggregateFunction func : aggregateFunctions) {
                if (!func.getFunction().equals("NONE")) {
                    String aggregateResult = calculateAggregate(group, headers, func);
                    resultLine.append(aggregateResult).append("~");
                }
            }

            String groupResult = resultLine.substring(0, resultLine.length() - 1);

            // Applying HAVING clause
            if (havingClause == null || evaluateHavingClause(groupResult, finalHeaderLine, havingClause)) {
                result.add(groupResult);
            }
        }

        return result;
    }

    /**
     * Calculates the aggregate result for a group.
     *
     * @param group  the group of data lines
     * @param headers  the header line
     * @param func  the aggregate function
     * @return the aggregate result
     */
    private static String calculateAggregate(List<String> group, List<String> headers, AggregateFunction func) {
        int columnIndex = headers.indexOf(func.getArgument());
        switch (func.getFunction()) {
            case "COUNT":
                return String.valueOf(group.size());
            case "SUM":
            case "AVG":
                double sum = 0;
                int count = 0;
                for (String line : group) {
                    String[] values = line.split("~");
                    if (columnIndex != -1 && columnIndex < values.length) {
                        try {
                            sum += Double.parseDouble(values[columnIndex]);
                            count++;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
                return func.getFunction().equals("SUM") ? String.format("%.2f", sum) : String.format("%.2f", sum / count);
            case "MIN":
            case "MAX":
                String result = null;
                for (String line : group) {
                    String[] values = line.split("~");
                    if (columnIndex != -1 && columnIndex < values.length) {
                        String value = values[columnIndex];
                        if (result == null || (func.getFunction().equals("MIN") && value.compareTo(result) < 0) ||
                                (func.getFunction().equals("MAX") && value.compareTo(result) > 0)) {
                            result = value;
                        }
                    }
                }
                return result != null ? result : "";
            default:
                return "";
        }
    }

    /**
     * Evaluates the HAVING clause for a group result.
     *
     * @param groupResult  the group result
     * @param headerLine   the header line
     * @param havingClause the HAVING clause
     * @return true if the group result satisfies the HAVING clause, false otherwise
     */
    private static boolean evaluateHavingClause(String groupResult, String headerLine, String havingClause) throws MySQLSyntaxErrorException {
        String[] headers = headerLine.split("~");
        String[] values = groupResult.split("~");

        // Parse the HAVING clause
        String[] parts = havingClause.split("\\s+", 3); // Split into max 3 parts
        if (parts.length != 3) {
            // Invalid HAVING clause format
            return true;
        }

        String column = parts[0];
        String operator = parts[1];
        String valueToCompare = parts[2].replace("'", "").replace("\"", "");

        // Find the column index, considering aggregate functions
        int columnIndex = -1;
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equals(column) || headers[i].contains("(" + column + ")")) {
                columnIndex = i;
                break;
            }
        }

        if (columnIndex == -1) {
            // Column not found
            throw new MySQLSyntaxErrorException("Invalid column", "Column does not exist: \u001B[1m" + column + "\u001B[0m");
        }

        String actualValue = values[columnIndex].replace("'", "").replace("\"", "");

        // Compare values based on the operator
        try {
            double actualDouble = Double.parseDouble(actualValue);
            double compareDouble = Double.parseDouble(valueToCompare);

            switch (operator) {
                case ">":
                    return actualDouble > compareDouble;
                case "<":
                    return actualDouble < compareDouble;
                case "=":
                    return actualDouble == compareDouble;
                case ">=":
                    return actualDouble >= compareDouble;
                case "<=":
                    return actualDouble <= compareDouble;
                case "<>":
                case "!=":
                    return actualDouble != compareDouble;
                default:
                    // Unsupported operator
                    TextFlowHelper.addWarningMessage(((MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController()).consoleTextFlow,
                            "Unsupported operator: " + operator);
                    return true;
            }
        } catch (NumberFormatException e) {
            // If parsing to double fails, compare as strings
            switch (operator) {
                case "=":
                    return actualValue.equals(valueToCompare);
                case "<>":
                case "!=":
                    return !actualValue.equals(valueToCompare);
                default:
                    // Unsupported operator for string comparison
                    TextFlowHelper.addWarningMessage(((MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController()).consoleTextFlow,
                            "Unsupported operator for string comparison: " + operator);
                    return true;
            }
        }
    }
}
