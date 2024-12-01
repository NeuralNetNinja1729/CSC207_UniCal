package interface_adapter.change_calendar_day;

import use_case.change_calendar_day.ChangeCalendarDayInputBoundary;
import use_case.change_calendar_day.ChangeCalendarDayInputData;
import entity.Calendar;
import java.util.ArrayList;
import java.util.List;

public class ChangeCalendarDayController {
  private final ChangeCalendarDayInputBoundary changeCalendarDayUseCaseInteractor;

  public ChangeCalendarDayController(ChangeCalendarDayInputBoundary changeCalendarDayUseCaseInteractor) {
    this.changeCalendarDayUseCaseInteractor = changeCalendarDayUseCaseInteractor;
  }

  /**
   * Executes the change calendar day use case with the given calendars and date.
   * @param calendarList list of calendars to fetch events from
   * @param date the date in YYYY-MM-DD format
   */
  public void execute(List<Calendar> calendarList, String date) {
    ChangeCalendarDayInputData inputData = new ChangeCalendarDayInputData(
            new ArrayList<>(calendarList),
            date
    );

    changeCalendarDayUseCaseInteractor.execute(inputData);
  }

  /**
   * Convenience method for single calendar execution
   */
  public void execute(Calendar calendar, String date) {
    ArrayList<Calendar> calendars = new ArrayList<>();
    calendars.add(calendar);
    execute(calendars, date);
  }
}