// First, ChangeCalendarDayController.java
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
    // Convert List to ArrayList as required by InputData
    ArrayList<Calendar> calendars = new ArrayList<>(calendarList);

    ChangeCalendarDayInputData changeCalendarDayInputData =
      new ChangeCalendarDayInputData(calendars, date);

    changeCalendarDayUseCaseInteractor.execute(changeCalendarDayInputData);
  }

  /**
   * Executes the change calendar day use case for a single calendar.
   * @param calendar the calendar to fetch events from
   * @param date the date in YYYY-MM-DD format
   */
  public void execute(Calendar calendar, String date) {
    ArrayList<Calendar> calendars = new ArrayList<>();
    calendars.add(calendar);

    ChangeCalendarDayInputData changeCalendarDayInputData =
      new ChangeCalendarDayInputData(calendars, date);

    changeCalendarDayUseCaseInteractor.execute(changeCalendarDayInputData);
  }
}

