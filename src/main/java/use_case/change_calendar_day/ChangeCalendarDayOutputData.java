package use_case.change_calendar_day;

import entity.Calendar;
import entity.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Output data for changing the calendar by day.
 */
public class ChangeCalendarDayOutputData {

    private final List<Calendar> calendarList;
    private final List<Event> eventList;

    public ChangeCalendarDayOutputData(List<Calendar> calendarList, List<Event> eventList) {
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
