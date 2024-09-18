package app.sql;

/**
 * Custom exception class for handling SQL syntax errors.
 */
public class MySQLSyntaxErrorException extends Exception {
    /**
     * The error type for the exception.
     */
    private static final String ERROR_TYPE = "SYNTAX ERROR";
    /**
     * The main error message for the exception.
     */
    private static final String MAIN_ERROR = "Syntax error";
    /**
     * The specific error message for the exception.
     */
    private final String specificError;
    /**
     * The detailed description of the error.
     */
    private final String errorDescription;

    /**
     * Creates a new instance of {@link MySQLSyntaxErrorException} with the specified specific error and error description.
     *
     * @param specificError   the specific error message
     * @param errorDescription the detailed description of the error
     */
    public MySQLSyntaxErrorException(String specificError, String errorDescription) {
        super(specificError);
        this.specificError = specificError;
        this.errorDescription = errorDescription;
    }

    /**
     * Gets the error type for the exception.
     *
     * @return the error type
     */
    public String getErrorType() {
        return ERROR_TYPE;
    }

    /**
     * Gets the main error message for the exception.
     *
     * @return the main error message
     */
    public String getMainError() {
        return MAIN_ERROR;
    }

    /**
     * Gets the specific error message for the exception.
     *
     * @return the specific error message
     */
    public String getSpecificError() {
        return specificError;
    }

    /**
     * Gets the detailed description of the error.
     *
     * @return the error description
     */
    public String getErrorDescription() {
        return errorDescription;
    }
}
