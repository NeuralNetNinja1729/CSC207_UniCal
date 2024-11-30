package use_case.add_event;

import entity.Calendar;
import entity.Event;

/**
 * Output data for adding an event.
 */
public class AddEventOutputData {

    private final Calendar calendar;
    private final Event event;

    public AddEventOutputData(Calendar calendar, Event event) {
        this.calendar = calendar;
        this.event = event;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public Event getEvent() {
        return event;
    }
}
