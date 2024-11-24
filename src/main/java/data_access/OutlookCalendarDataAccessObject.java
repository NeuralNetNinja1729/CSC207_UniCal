package data_access;

import entity.Event;
import entity.OutlookCalendar;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Data Access Object for Outlook Calendar.
 */
public class OutlookCalendarDataAccessObject implements GetEventsDataAccessInterface, AddEventDataAccessInterface {

    private final OutlookCalendar calendar;

    public OutlookCalendarDataAccessObject(OutlookCalendar calendar) {
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
