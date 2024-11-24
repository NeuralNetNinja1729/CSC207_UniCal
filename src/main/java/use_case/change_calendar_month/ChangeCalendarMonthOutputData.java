package use_case.change_calendar_month;

import entity.Calendar;
import entity.Event;

import java.util.ArrayList;

/**
 * Output data for changing the calendar by month.
 */
public class ChangeCalendarMonthOutputData {

    private final ArrayList<Calendar> calendarList;
    private final ArrayList<Event> eventList;

    public ChangeCalendarMonthOutputData(ArrayList<Calendar> calendarList, ArrayList<Event> eventList) {
        this.calendarList = calendarList;
        this.eventList = eventList;
    }

    public ArrayList<Calendar> getCalendarList() {
        return calendarList;
    }

    public ArrayList<Event> getEventList() {
        return eventList;
    }
}

