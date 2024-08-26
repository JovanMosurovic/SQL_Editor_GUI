package app.sql;

public class AggregateFunction {
    private final String function;
    private final String argument;
    private int columnIndex;

    public AggregateFunction(String function, String argument, int columnIndex) {
        this.function = function;
        this.argument = argument;
        this.columnIndex = columnIndex;
    }

    //region GETTERS AND SETTERS
    public String getFunction() {
        return function;
    }

    public String getArgument() {
        return argument;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    //endregion

}
