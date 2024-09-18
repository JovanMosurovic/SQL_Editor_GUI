package app.sql;

public class MySQLSyntaxErrorException extends Exception {
    private static final String ERROR_TYPE = "SYNTAX ERROR";
    private static final String MAIN_ERROR = "Syntax error";
    private final String specificError;
    private final String errorDescription;

    public MySQLSyntaxErrorException(String specificError, String errorDescription) {
        super(specificError);
        this.specificError = specificError;
        this.errorDescription = errorDescription;
    }

    public String getErrorType() {
        return ERROR_TYPE;
    }

    public String getMainError() {
        return MAIN_ERROR;
    }

    public String getSpecificError() {
        return specificError;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
