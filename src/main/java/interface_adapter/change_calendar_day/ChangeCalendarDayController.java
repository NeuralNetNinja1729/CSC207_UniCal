package interface_adapter.change_calendar_day;

import entity.Calendar;
import use_case.change_calendar_day.ChangeCalendarDayInputBoundary;
import use_case.change_calendar_day.ChangeCalendarDayInputData;

import java.util.ArrayList;

public class ChangeCalendarDayController {
    final ChangeCalendarDayInputBoundary changeCalendarDayUseCaseInteractor;

    public ChangeCalendarDayController(ChangeCalendarDayInputBoundary changeCalendarDayUseCaseInteractor) {
        this.changeCalendarDayUseCaseInteractor = changeCalendarDayUseCaseInteractor;
    }

    /**
     * Executes the change calendar day use case.
     * @param calendar the current calendar being viewed
     * @param dateString the selected date in YYYY-MM-DD format
     */
    public void execute(Calendar calendar, String dateString) {
        try {
            // Create list with single calendar
            ArrayList<Calendar> calendars = new ArrayList<>();
            calendars.add(calendar);

            // Create input data and execute use case
            ChangeCalendarDayInputData inputData = new ChangeCalendarDayInputData(calendars, dateString);
            changeCalendarDayUseCaseInteractor.execute(inputData);
        } catch (Exception e) {
            // The presenter will handle the error display
            throw new RuntimeException("Error fetching daily events: " + e.getMessage());
        }
    }
}