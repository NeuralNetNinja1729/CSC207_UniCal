package use_case.change_calendar_day;

import entity.Calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Input data for changing the calendar by day.
 */
public class ChangeCalendarDayInputData {

    private final List<Calendar> calendarList;
    private final LocalDate date;

    public ChangeCalendarDayInputData(List<Calendar> calendarList, String date) {
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

