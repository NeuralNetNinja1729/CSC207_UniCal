package use_case.add_event;

import entity.Calendar;
import entity.Event;

import java.time.LocalDate;

/**
 * Input data for adding an event to the calendar.
 */
public class AddEventInputData {

    private final String eventName;
    private final Calendar calendarApi;
    private final LocalDate date;
    private final Event event;

    public AddEventInputData(String eventName, String date, Calendar calendarApi) {
        this.eventName = eventName;
        this.date = LocalDate.parse(date);
        this.calendarApi = calendarApi;
        this.event = new Event(eventName, LocalDate.parse(date), calendarApi);
    }

    public String getEventName() {
        return eventName;
    }

    public Calendar getCalendarApi() {
        return calendarApi;
    }

    public LocalDate getDate() {
        return date;
    }

    public Event getEvent() {
        return event;
    }

}
