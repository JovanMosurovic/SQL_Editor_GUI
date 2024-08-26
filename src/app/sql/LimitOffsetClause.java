package app.sql;

/**
 * Represents the LIMIT and OFFSET clauses in a SQL query.
 */
public class LimitOffsetClause {

    /**
     * The limit value for the query.
     */
    private final int limit;
    /**
     * The offset value for the query.
     */
    private final int offset;

    /**
     * Creates a new instance of {@link LimitOffsetClause} with the specified limit and offset values.
     *
     * @param limit  the limit value for the query
     * @param offset the offset value for the query
     */
    public LimitOffsetClause(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    /**
     * Gets the limit value for the query.
     *
     * @return the limit value
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Gets the offset value for the query.
     *
     * @return the offset value
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Returns the string representation of the limit and offset clause (can be used for debugging).
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return "LimitOffsetClause{limit=" + limit + ", offset=" + offset + '}';
    }
}
