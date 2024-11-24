package use_case.change_calendar_month;

import entity.Calendar;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Input data for changing the calendar by month.
 */
public class ChangeCalendarMonthInputData {

    private final ArrayList<Calendar> calendarList;
    private final LocalDate date;

    public ChangeCalendarMonthInputData(ArrayList<Calendar> calendarList, String date) {
        this.calendarList = calendarList;
        this.date = LocalDate.parse(date);
    }

    public ArrayList<Calendar> getCalendarList() {
        return calendarList;
    }

    public LocalDate getDate() {
        return date;
    }
}
