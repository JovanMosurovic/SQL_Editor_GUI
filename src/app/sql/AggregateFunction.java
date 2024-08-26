package app.sql;

/**
 * Utility class for storing the aggregate function data.
 */
public class AggregateFunction {

    /**
     * The function name.
     */
    private String function;
    /**
     * The argument of the function.
     */
    private String argument;
    /**
     * The index of the column in the result data.
     */
    private int columnIndex;

    /**
     * Creates a new instance of {@link AggregateFunction} with the specified function, argument, and column index.
     *
     * @param function    the function name
     * @param argument    the argument of the function
     * @param columnIndex the index of the column in the result data
     */
    public AggregateFunction(String function, String argument, int columnIndex) {
        this.function = function;
        this.argument = argument;
        this.columnIndex = columnIndex;
    }

    //region GETTERS AND SETTERS

    /**
     * Returns the function name.
     *
     * @return the function name
     */
    public String getFunction() {
        return function;
    }

    /**
     * Sets the function name.
     *
     * @param function the function name
     */
    public void setFunction(String function) {
        this.function = function;
    }

    /**
     * Returns the argument of the function.
     *
     * @return the argument of the function
     */
    public String getArgument() {
        return argument;
    }

    /**
     * Sets the argument of the function.
     *
     * @param argument the argument of the function
     */
    public void setArgument(String argument) {
        this.argument = argument;
    }

    /**
     * Returns the index of the column in the result data.
     *
     * @return the index of the column in the result data
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * Sets the index of the column in the result data.
     *
     * @param columnIndex the index of the column in the result data
     */
    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    //endregion

}
