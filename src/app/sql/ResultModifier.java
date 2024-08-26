package app.sql;

import app.Window;
import app.mainwindow.MainWindowController;
import app.util.TextFlowHelper;

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
        if (!modifiers.getDistinctColumns().isEmpty()) {
            dataLines = applyDistinct(dataLines, headerLine, modifiers.getDistinctColumns());
        }
        if (!modifiers.getOrderByClauses().isEmpty()) {
            dataLines = applyOrderBy(dataLines, headerLine, modifiers.getOrderByClauses());
        }
        if (modifiers.getLimitOffsetClause() != null) {
            dataLines = applyLimitOffset(dataLines, modifiers.getLimitOffsetClause());
        }
        return dataLines;
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
        int offset = limitOffsetClause.getOffset();
        int limit = limitOffsetClause.getLimit();

        MainWindowController mainWindowController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();

        if (offset >= dataLines.size()) {
            TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow,
                    "\n\nWARNING: OFFSET is greater than or equal to the number of rows. Returning empty result.", TextFlowHelper.warningYellow, true);
            return new ArrayList<>();
        }

        int endIndex = Math.min(offset + limit, dataLines.size());

        return new ArrayList<>(dataLines.subList(offset, endIndex));
    }
}
