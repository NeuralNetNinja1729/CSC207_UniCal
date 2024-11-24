package data_access;

import entity.Event;
import entity.GoogleCalendar;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Data Access Object for Google Calendar.
 */
public class GoogleCalendarDataAccessObject implements GetEventsDataAccessInterface, AddEventDataAccessInterface {

    private final GoogleCalendar calendar;

    public GoogleCalendarDataAccessObject(GoogleCalendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public ArrayList<Event> fetchEventsDay(LocalDate date) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Event> fetchEventsMonth(LocalDate date) {
        return new ArrayList<>();
    }

    @Override
    public boolean addEvent(Event event) {
        // Implementation TBD
        return false;
    }
}

