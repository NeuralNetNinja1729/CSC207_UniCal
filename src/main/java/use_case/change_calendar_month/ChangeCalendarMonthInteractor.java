package use_case.change_calendar_month;

import data_access.CalendarDataAccessObjectFactory;
import data_access.GetEventsDataAccessInterface;
import entity.Calendar;
import entity.Event;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The Change Month Calendar Interactor.
 */
public class ChangeCalendarMonthInteractor implements ChangeCalendarMonthInputBoundary {
  private final CalendarDataAccessObjectFactory calendarDataAccessObjectFactory;
  private final ChangeCalendarMonthOutputBoundary changeCalendarMonthPresenter;

  public ChangeCalendarMonthInteractor(
          CalendarDataAccessObjectFactory calendarDataAccessObjectFactory,
          ChangeCalendarMonthOutputBoundary changeCalendarMonthPresenter) {
    this.calendarDataAccessObjectFactory = calendarDataAccessObjectFactory;
    this.changeCalendarMonthPresenter = changeCalendarMonthPresenter;
  }

  @Override
  public void execute(ChangeCalendarMonthInputData inputData) {
    try {
      List<Event> events = new ArrayList<>();
      List<Calendar> calendars = inputData.getCalendarList();

      for (Calendar calendar : calendars) {
        GetEventsDataAccessInterface getEventsDataAccessObject =
                (GetEventsDataAccessInterface) calendarDataAccessObjectFactory
                        .getCalendarDataAccessObject(calendar);

        events.addAll(getEventsDataAccessObject.fetchEventsMonth(LocalDate.parse(inputData.getDate())));
      }

      ChangeCalendarMonthOutputData outputData = new ChangeCalendarMonthOutputData(calendars, events);
      changeCalendarMonthPresenter.prepareSuccessView(outputData);
    } catch (Exception e) {
      changeCalendarMonthPresenter.prepareFailView("Error fetching calendar data: " + e.getMessage());
    }
  }
}

