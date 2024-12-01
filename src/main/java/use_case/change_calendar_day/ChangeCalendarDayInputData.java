package use_case.change_calendar_day;

import entity.Calendar;
import java.util.ArrayList;

/**
 * Input data for changing the calendar by day.
 */
public class ChangeCalendarDayInputData {
    private final ArrayList<Calendar> calendarList;
    private final String date;

    public ChangeCalendarDayInputData(ArrayList<Calendar> calendarList, String date) {
        this.calendarList = calendarList;
        this.date = date;
    }

    public ArrayList<Calendar> getCalendarList() {
        return new ArrayList<>(calendarList);
    }

    public String getDate() {
        return date;
    }
}

