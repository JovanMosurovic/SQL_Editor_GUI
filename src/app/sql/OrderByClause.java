package app.sql;

/**
 * Utility class for storing the order by clause.
 */
public class OrderByClause {

    /**
     * The column name.
     */
    private final String column;
    /**
     * The sort order (true for ascending, false for descending).
     */
    private final boolean isAscending;

    /**
     * Creates a new instance of {@link OrderByClause} with the specified column name and sort order.
     *
     * @param column      the column name
     * @param isAscending the sort order (true for ascending, false for descending)
     */
    public OrderByClause(String column, boolean isAscending) {
        this.column = column;
        this.isAscending = isAscending;
    }

    /**
     * Returns the column name.
     *
     * @return the column name
     */
    public String getColumn() {
        return column;
    }

    /**
     * Returns the sort order (true for ascending, false for descending).
     *
     * @return the sort order
     */
    public boolean isAscending() {
        return isAscending;
    }

    /**
     * Returns the string representation of the order by clause (can be used for debugging).
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return "OrderByClause {column='" + column + "', isAscending=" + isAscending + '}';
    }
}
