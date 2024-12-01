package use_case.change_calendar_day;

import entity.Calendar;
import entity.Event;

import java.time.LocalDate;
import java.util.List;

/**
 * Output data for changing the calendar by day.
 */
public class ChangeCalendarDayOutputData {

    private final List<Calendar> calendarList;
    private final List<Event> eventList;
    private final LocalDate date;

    public ChangeCalendarDayOutputData(List<Calendar> calendarList, List<Event> eventList, LocalDate date) {
        this.calendarList = calendarList;
        this.eventList = eventList;
        this.date = date;
    }

    public List<Calendar> getCalendarList() {
        return calendarList;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public LocalDate getDate() {
        return date;
    }
}
