package entity;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents an event in the system.
 */
public class Event {
    private final String eventName;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Calendar calendarApi;

    public Event(String eventName, LocalDate date, LocalTime startTime, LocalTime endTime, Calendar calendarApi) {
        this.eventName = eventName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.calendarApi = calendarApi;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Calendar getCalendarApi() {
        return calendarApi;
    }
}