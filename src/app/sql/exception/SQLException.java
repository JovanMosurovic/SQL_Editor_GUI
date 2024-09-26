package app.sql.exception;

public abstract class SQLException extends Exception {

    public SQLException(String message) {
        super(message);
    }

    public abstract String getErrorType();
    public abstract String getMainError();
    public abstract String getSpecificError();
    public abstract String getErrorDescription();

}