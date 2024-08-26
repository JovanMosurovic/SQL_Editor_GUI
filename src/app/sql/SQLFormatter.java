package app.sql;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SQLFormatter {

    private static final String[] KEYWORDS = {
            "SELECT", "FROM", "WHERE", "INSERT", "INTO", "VALUES",
            "DELETE", "UPDATE", "SET", "CREATE", "TABLE", "DROP",
            "SHOW", "TABLES", "INNER", "JOIN", "ON", "AS", "AND", "OR",
            "ORDER", "BY", "LIMIT", "OFFSET", "DISTINCT", ";"
    };

    public static String trimCode(String code) {
        return code.replaceAll("\\s+", " ").trim();
    }

    public static String formatSQLQuery(String query) {
        StringBuilder formattedQuery = new StringBuilder();
        query = query.replaceAll("\\h+", " ").trim();

        String keywordPattern = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
        Pattern pattern = Pattern.compile(keywordPattern + "|\\*|('[^']*')|([^\\s']+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        boolean lastWasSelect = false;
        boolean hasLimit = false;

        while (matcher.find()) {
            String token = matcher.group().toUpperCase();

            if (Arrays.asList(KEYWORDS).contains(token)) {
                if (formattedQuery.length() > 0 && formattedQuery.charAt(formattedQuery.length() - 1) != '\n') {
                    formattedQuery.append(" ");
                }
                formattedQuery.append(token);

                switch (token) {
                    case "SELECT":
                        lastWasSelect = true;
                        break;
                    case "ORDER":
                        if (hasLimit) {
                            writeSyntaxError();
                            return formattedQuery.toString();
                        }
                        break;
                    case "LIMIT":
                        hasLimit = true;
                        break;
                }
            } else if (token.equals("*")) {
                if (lastWasSelect) {
                    formattedQuery.append(" ");
                }
                formattedQuery.append("*");
            } else {
                if (formattedQuery.length() > 0 && formattedQuery.charAt(formattedQuery.length() - 1) != '\n') {
                    formattedQuery.append(" ");
                }
                formattedQuery.append(matcher.group());
            }
            if (!token.equals("SELECT")) {
                lastWasSelect = false;
            }
        }

        return formattedQuery.toString();
    }

    private static void writeSyntaxError() {
        try (PrintWriter out = new PrintWriter(new FileWriter("output.txt"))) {
            out.println("!");
            out.println("\u001B[1;31m[SYNTAX ERROR]\u001B[0m \u001B[1mSyntax error:\u001B[0m Invalid SQL syntax.");
        } catch (IOException e) {
            Logger.getLogger(SQLFormatter.class.getName()).severe("Error writing syntax error to file: " + e.getMessage());
        }
    }
}