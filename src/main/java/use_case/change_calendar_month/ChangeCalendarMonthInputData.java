
package use_case.change_calendar_month;

import entity.Calendar;
import java.util.List;

/**
 * Input data for changing the calendar by month.
 */
public class ChangeCalendarMonthInputData {
    private final List<Calendar> calendarList;
    private final String date;

    public ChangeCalendarMonthInputData(List<Calendar> calendarList, String date) {
        this.calendarList = calendarList;
        this.date = date;
    }

    public List<Calendar> getCalendarList() {
        return calendarList;
    }

    public String getDate() {
        return date;
    }
}
