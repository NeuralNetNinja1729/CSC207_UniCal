package use_case.change_calendar_month;

import entity.Calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Input data for changing the calendar by month.
 */
public class ChangeCalendarMonthInputData {

    private final List<Calendar> calendarList;
    private final LocalDate date;

    public ChangeCalendarMonthInputData(List<Calendar> calendarList, String date) {
        this.calendarList = calendarList;
        this.date = LocalDate.parse(date);
    }

    public List<Calendar> getCalendarList() {
        return calendarList;
    }

    public LocalDate getDate() {
        return date;
    }
}

