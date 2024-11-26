package use_case.change_calendar_month;

import entity.Calendar;
import entity.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Output data for changing the calendar by month.
 */
public class ChangeCalendarMonthOutputData {

    private final List<Calendar> calendarList;
    private final List<Event> eventList;

    public ChangeCalendarMonthOutputData(List<Calendar> calendarList, List<Event> eventList) {
        this.calendarList = calendarList;
        this.eventList = eventList;
    }

    public List<Calendar> getCalendarList() {
        return calendarList;
    }

    public List<Event> getEventList() {
        return eventList;
    }
}

