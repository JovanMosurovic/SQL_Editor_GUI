package app.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for formatting SQL queries.
 * <p>Provides methods to trim and format SQL code for better readability and parsing.</p>
 */
public class SQLFormatter {

    /**
     * The list of SQL keywords for formatting.
     */
    private static final String[] KEYWORDS = {"SELECT", "FROM", "WHERE", "INSERT", "INTO", "VALUES",
            "DELETE", "UPDATE", "SET", "CREATE", "TABLE", "DROP",
            "SHOW", "TABLES", "INNER", "JOIN", "ON", "AS", "AND", "OR", ";"};

    /**
     * Trims the given SQL code by removing leading and trailing whitespace and normalizing internal whitespace.
     *
     * @param code the SQL code to trim
     * @return the trimmed SQL code
     */
    public static String trimCode(String code) {
        return code.replaceAll("\\s+", " ").trim();
    }

    /**
     * Formats the given SQL query by normalizing whitespace and capitalizing keywords.
     *
     * @param query the SQL query to format
     * @return the formatted SQL query
     */
    public static String formatSQLQuery(String query) {
        StringBuilder formattedQuery = new StringBuilder();

        // Normalize whitespace, preserving newlines
        query = query.replaceAll("\\h+", " ").trim();

        // Create a pattern to match keywords and non-keywords
        String keywordPattern = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
        Pattern pattern = Pattern.compile(keywordPattern + "|('[^']*')|([^\\s']+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // It's a keyword
                if (formattedQuery.length() > 0 && formattedQuery.charAt(formattedQuery.length() - 1) != '\n') {
                    formattedQuery.append(" ");
                }
                formattedQuery.append(matcher.group(1).toUpperCase());
            } else {
                // It's not a keyword
                if (formattedQuery.length() > 0 && formattedQuery.charAt(formattedQuery.length() - 1) != '\n') {
                    formattedQuery.append(" ");
                }
                formattedQuery.append(matcher.group());
            }
        }

        return formattedQuery.toString();
    }

}
