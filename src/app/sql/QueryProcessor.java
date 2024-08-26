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
import java.util.stream.Collectors;

public class QueryProcessor {

    public static String processQuery(String query, QueryModifiers modifiers) {
        query = processDistinctQuery(query, modifiers);
        query = processOrderByClause(query, modifiers);
        query = processLimitOffsetClause(query, modifiers);
        return query;
    }

    private static String processDistinctQuery(String query, QueryModifiers modifiers) {
        Pattern pattern = Pattern.compile("SELECT\\s+DISTINCT\\s*(?:\\((.*?)\\))?\\s*(.*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            String distinctColumnsStr = matcher.group(1);
            String restOfQuery = matcher.group(2);

            if (distinctColumnsStr != null) {
                if (distinctColumnsStr.trim().equals("*")) {
                    modifiers.setDistinctColumns(Collections.singletonList("*"));
                } else {
                    modifiers.setDistinctColumns(Arrays.stream(distinctColumnsStr.split(","))
                            .map(String::trim)
                            .collect(Collectors.toList()));
                }
            } else {
                modifiers.setDistinctColumns(Collections.singletonList("*"));
            }

            return "SELECT " + (distinctColumnsStr != null ? distinctColumnsStr : "") + " " + restOfQuery;
        } else {
            return query;
        }
    }

    private static String processOrderByClause(String query, QueryModifiers modifiers) {
        Pattern pattern = Pattern.compile("(.*\\S)\\s+ORDER\\s+BY\\s+(.+?)(\\s+LIMIT\\s+\\d+(?:\\s+OFFSET\\s+\\d+)?)?$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            String mainQuery = matcher.group(1);
            String orderByClauseString = matcher.group(2);
            String limitPart = matcher.group(3);
            modifiers.setOrderByClauses(parseOrderByClauses(orderByClauseString));
            return mainQuery + (limitPart != null ? limitPart : "");
        } else {
            return query;
        }
    }

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

    private static String processLimitOffsetClause(String query, QueryModifiers modifiers) {
        Pattern pattern = Pattern.compile("(.*\\S)\\s+LIMIT\\s+([-]?\\d+)(?:\\s+OFFSET\\s+([-]?\\d+))?$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        MainWindowController mainWindowController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();

        if (matcher.find()) {
            String mainQuery = matcher.group(1);
            int limit = Integer.parseInt(matcher.group(2));
            int offset = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : 0;

            if (limit < 0) {
                TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow,
                        "\n\nWARNING: Negative LIMIT value. Treating as unlimited.", TextFlowHelper.warningYellow, true);
                limit = Integer.MAX_VALUE;
            }
            if (offset < 0) {
                TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow,
                        "\n\nWARNING: Negative OFFSET value. Setting OFFSET to 0.", TextFlowHelper.warningYellow, true);
                offset = 0;
            }

            modifiers.setLimitOffsetClause(new LimitOffsetClause(limit, offset));
            return mainQuery;
        } else {
            return query;
        }
    }
}