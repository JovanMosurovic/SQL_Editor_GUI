package app.sql;

public class LimitOffsetClause {
    private final int limit;
    private final int offset;

    public LimitOffsetClause(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "LimitOffsetClause{limit=" + limit + ", offset=" + offset + '}';
    }
}
