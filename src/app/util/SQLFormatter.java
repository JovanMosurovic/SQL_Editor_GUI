package app.util;

public class SQLFormatter {

    public static String formatSQLQuery(String query) {
        String[] keywords = {"SELECT", "FROM", "WHERE", "INSERT", "INTO", "VALUES", "DELETE", "UPDATE", "SET", "CREATE", "TABLE", "DROP", "SHOW", "TABLES", "INNER", "JOIN", "ON", "AS"};

        StringBuilder formattedQuery = new StringBuilder();
        boolean inQuotes = false;
        String upperQuery = query.toUpperCase();

        for (int i = 0; i < query.length(); i++) {
            char c = query.charAt(i);
            if (c == '\'' || c == '"') {
                inQuotes = !inQuotes;
                formattedQuery.append(c);
            } else if (!inQuotes) {
                boolean keywordFound = false;
                for (String keyword : keywords) {
                    if (upperQuery.startsWith(keyword, i) &&
                            (i == 0 || !Character.isLetterOrDigit(query.charAt(i-1))) &&
                            (i + keyword.length() == query.length() || !Character.isLetterOrDigit(query.charAt(i + keyword.length())))) {
                        formattedQuery.append(" ").append(keyword).append(" ");
                        i += keyword.length() - 1;
                        keywordFound = true;
                        break;
                    }
                }
                if (!keywordFound) {
                    formattedQuery.append(c);
                }
            } else {
                formattedQuery.append(c);
            }
        }

        // Remove extra spaces
        String result = formattedQuery.toString().replaceAll("\\s+", " ").trim();

        // Edge case for *
        result = result.replaceAll("\\s*\\*\\s*", " * ");

        return result;
    }

}
