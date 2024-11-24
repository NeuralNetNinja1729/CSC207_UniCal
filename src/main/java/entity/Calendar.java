package entity;

/**
 * A general interface for calendars in our system.
 */
public interface Calendar {

    /**
     * Returns the name of the calendar API.
     * @return the name of the calendar API.
     */
    String getCalendarApiName();

    /**
     * Returns the name of the calendar.
     * @return the name of the calendar.
     */
    String getCalendarName();
}
