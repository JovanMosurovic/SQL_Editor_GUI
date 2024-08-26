package app.sql;

public class OrderByClause {
    private final String column;
    private final boolean isAscending;

    public OrderByClause(String column, boolean isAscending) {
        this.column = column;
        this.isAscending = isAscending;
    }

    public String getColumn() {
        return column;
    }

    public boolean isAscending() {
        return isAscending;
    }

    @Override
    public String toString() {
        return "OrderByClause {column='" + column + "', isAscending=" + isAscending + '}';
    }
}
