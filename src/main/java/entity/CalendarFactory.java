package entity;

/**
 * Factory interface for creating calendar objects.
 */
public interface CalendarFactory {

    /**
     * Creates a calendar by its API name.
     * @param calendarApiName the API name of the calendar.
     * @return an instance of a Calendar.
     */
    Calendar createCalendar(String calendarApiName);
}
