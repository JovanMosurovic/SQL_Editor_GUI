package app.sql;


import java.util.ArrayList;
import java.util.List;

public class QueryModifiers {
    private List<OrderByClause> orderByClauses;
    private LimitOffsetClause limitOffsetClause;
    private List<String> distinctColumns;

    public QueryModifiers() {
        this.orderByClauses = new ArrayList<>();
        this.limitOffsetClause = null;
        this.distinctColumns = new ArrayList<>();
    }

    public List<OrderByClause> getOrderByClauses() {
        return orderByClauses;
    }

    public void setOrderByClauses(List<OrderByClause> orderByClauses) {
        this.orderByClauses = orderByClauses;
    }

    public LimitOffsetClause getLimitOffsetClause() {
        return limitOffsetClause;
    }

    public void setLimitOffsetClause(LimitOffsetClause limitOffsetClause) {
        this.limitOffsetClause = limitOffsetClause;
    }

    public List<String> getDistinctColumns() {
        return distinctColumns;
    }

    public void setDistinctColumns(List<String> distinctColumns) {
        this.distinctColumns = distinctColumns;
    }
}