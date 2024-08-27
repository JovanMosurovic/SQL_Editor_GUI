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
     */
    public static List<String> applyModifiers(List<String> dataLines, String headerLine, QueryModifiers modifiers) {
        List<String> result = new ArrayList<>(dataLines);
        String newHeaderLine = headerLine;

        if (!modifiers.getAggregateFunctions().isEmpty()) {
            List<String> aggregateResult = applyAggregateFunctions(result, headerLine, modifiers.getAggregateFunctions());
            newHeaderLine = aggregateResult.get(0);
            result = new ArrayList<>(aggregateResult.subList(1, aggregateResult.size()));
        } else {
            if (!modifiers.getDistinctColumns().isEmpty()) {
                result = applyDistinct(result, headerLine, modifiers.getDistinctColumns());
            }
        }

        if (!modifiers.getOrderByClauses().isEmpty()) {
            result = applyOrderBy(result, newHeaderLine, modifiers.getOrderByClauses());
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
                if (func.getFunction().equals("COUNT") && func.getArgument().equals("*")) {
                    continue; // handle COUNT(*) separately
                } else {
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
                return dataLines; // Vraćamo originalne podatke bez sortiranja
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
            TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow,
                    "\n\nWARNING: OFFSET is greater than or equal to the number of rows. Returning empty result.", TextFlowHelper.warningYellow, true);
            return new ArrayList<>();
        }

        // If limit is 0, return empty result
        if (limit == 0) {
            TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow,
                    "\n\nWARNING: LIMIT is 0. Returning empty result.", TextFlowHelper.warningYellow, true);
            return new ArrayList<>();
        }

        int endIndex = Math.min(offset + limit, dataLines.size());

        return new ArrayList<>(dataLines.subList(offset, endIndex));
    }
}
