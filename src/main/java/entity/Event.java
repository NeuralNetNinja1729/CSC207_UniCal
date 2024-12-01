package entity;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents an event in the system.
 */
public class Event {

    private final String eventName;
    private final LocalDate date;
    // TODO: Add LocalTime startTime and endTime instance variables
    // TODO: Add a String eventDescription instance variable
    private final Calendar calendarApi;

    public Event(String eventName, LocalDate date, Calendar calendarApi) {
        this.eventName = eventName;
        this.date = date;
        this.calendarApi = calendarApi;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDate getDate() {
        return date;
    }

    public Calendar getCalendarApi() {
        return calendarApi;
    }

    public String getName() {
        return this.eventName;
    }
}
