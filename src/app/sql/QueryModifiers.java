package app.sql;


import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for storing the query modifiers.
 */
public class QueryModifiers {

    /**
     * The list of order by clauses.
     */
    private List<OrderByClause> orderByClauses;
    /**
     * The limit offset clause.
     */
    private LimitOffsetClause limitOffsetClause;
    /**
     * The list of distinct columns.
     */
    private List<String> distinctColumns;
    /**
     * The list of aggregate functions.
     */
    private List<AggregateFunction> aggregateFunctions;

    /**
     * Creates a new instance of {@link QueryModifiers} with the default values.
     */
    public QueryModifiers() {
        this.orderByClauses = new ArrayList<>();
        this.limitOffsetClause = null;
        this.distinctColumns = new ArrayList<>();
        this.aggregateFunctions = new ArrayList<>();
    }

    //region GETTERS AND SETTERS
    /**
     * Returns the list of order by clauses.
     *
     * @return the list of order by clauses
     */
    public List<OrderByClause> getOrderByClauses() {
        return orderByClauses;
    }

    /**
     * Sets the list of order by clauses.
     *
     * @param orderByClauses the list of order by clauses
     */
    public void setOrderByClauses(List<OrderByClause> orderByClauses) {
        this.orderByClauses = orderByClauses;
    }

    /**
     * Returns the limit offset clause.
     *
     * @return the limit offset clause
     */
    public LimitOffsetClause getLimitOffsetClause() {
        return limitOffsetClause;
    }

    /**
     * Sets the limit offset clause.
     *
     * @param limitOffsetClause the limit offset clause
     */
    public void setLimitOffsetClause(LimitOffsetClause limitOffsetClause) {
        this.limitOffsetClause = limitOffsetClause;
    }

    /**
     * Returns the list of distinct columns.
     *
     * @return the list of distinct columns
     */
    public List<String> getDistinctColumns() {
        return distinctColumns;
    }

    /**
     * Sets the list of distinct columns.
     *
     * @param distinctColumns the list of distinct columns
     */
    public void setDistinctColumns(List<String> distinctColumns) {
        this.distinctColumns = distinctColumns;
    }

    /**
     * Returns the list of aggregate functions.
     *
     * @return the list of aggregate functions
     */
    public List<AggregateFunction> getAggregateFunctions() {
        return aggregateFunctions;
    }

    /**
     * Sets the list of aggregate functions.
     *
     * @param aggregateFunctions the list of aggregate functions
     */
    public void setAggregateFunctions(List<AggregateFunction> aggregateFunctions) {
        this.aggregateFunctions = aggregateFunctions;
    }

    //endregion

}