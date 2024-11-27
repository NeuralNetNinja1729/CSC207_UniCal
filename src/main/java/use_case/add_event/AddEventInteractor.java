package use_case.add_event;

import data_access.AddEventDataAccessInterface;
import data_access.CalendarDataAccessObjectFactory;
import entity.Calendar;
import entity.Event;
import interface_adapter.add_event.AddEventPresenter;

import java.time.LocalDate;

/**
 * The Change Day Calendar Interactor.
 */
public class AddEventInteractor implements AddEventInputBoundary {

    private final CalendarDataAccessObjectFactory calendarDataAccessObjectFactory;
    private final AddEventPresenter addEventPresenter;

    public AddEventInteractor(CalendarDataAccessObjectFactory calendarDataAccessObjectFactory,
                              AddEventPresenter addEventPresenter) {
        this.calendarDataAccessObjectFactory = calendarDataAccessObjectFactory;
        this.addEventPresenter = addEventPresenter;
    }

  @Override
  public void execute(AddEventInputData inputData) {
    try {
      Calendar calendar = inputData.getCalendar();
      Event event = new Event(
        inputData.getEventName(),
        LocalDate.parse(inputData.getDate()),
        calendar
      );

      AddEventDataAccessInterface addEventDataAccessObject =
        (AddEventDataAccessInterface) calendarDataAccessObjectFactory
          .getCalendarDataAccessObject(calendar);

      if (!addEventDataAccessObject.addEvent(event)) {
        addEventPresenter.prepareFailView("Failed to add event");
        return;
      }

      addEventPresenter.prepareSuccessView(new AddEventOutputData(event));

    } catch (Exception e) {
      addEventPresenter.prepareFailView("Error adding event: " + e.getMessage());
    }
  }
}

