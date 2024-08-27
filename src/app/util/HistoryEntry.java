package app.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a history entry class in the application.
 */
public class HistoryEntry {

    /**
     * The date and time of the history entry.
     */
    private final StringProperty dateTime;
    /**
     * The SQL query of the history entry.
     */
    private final StringProperty query;

    /**
     * Creates a new instance of {@link HistoryEntry} with the specified date and time and SQL query.
     *
     * @param dateTime the date and time of the history entry
     * @param query    the SQL query of the history entry
     */
    public HistoryEntry(String dateTime, String query) {
        this.dateTime = new SimpleStringProperty(dateTime);
        this.query = new SimpleStringProperty(query);
    }

    /**
     * Gets the date and time of the history entry.
     *
     * @return the date and time of the history entry
     */
    public String getDateTime() {
        return dateTime.get();
    }

    /**
     * Gets the SQL query of the history entry.
     *
     * @return the SQL query of the history entry
     */
    public String getQuery() {
        return query.get();
    }

    /**
     * Gets the date and time property of the history entry.
     *
     * @return the date and time property of the history entry
     */
    public StringProperty dateTimeProperty() {
        return dateTime;
    }

    /**
     * Gets the query property of the history entry.
     *
     * @return the query property of the history entry
     */
    public StringProperty queryProperty() {
        return query;
    }
}