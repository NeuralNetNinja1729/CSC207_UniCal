package use_case.delete_event;

import data_access.CalendarDataAccessObjectFactory;
import data_access.DeleteEventDataAccessInterface;
import entity.Calendar;
import interface_adapter.delete_event.DeleteEventPresenter;

public class DeleteEventInteractor implements DeleteEventInputBoundary {
  private final CalendarDataAccessObjectFactory calendarDataAccessObjectFactory;
  private final DeleteEventPresenter deleteEventPresenter;

  public DeleteEventInteractor(CalendarDataAccessObjectFactory calendarDataAccessObjectFactory,
                               DeleteEventPresenter deleteEventPresenter) {
    this.calendarDataAccessObjectFactory = calendarDataAccessObjectFactory;
    this.deleteEventPresenter = deleteEventPresenter;
  }

  @Override
  public void execute(DeleteEventInputData inputData) {
    final Calendar calendar = inputData.getEvent().getCalendarApi();

    final DeleteEventDataAccessInterface deleteEventDataAccessObject =
      (DeleteEventDataAccessInterface) this.calendarDataAccessObjectFactory
        .getCalendarDataAccessObject(calendar);

    if (!deleteEventDataAccessObject.deleteEvent(inputData.getEvent())) {
      this.deleteEventPresenter.prepareFailView("Unable to Delete Event!");
    } else {
      final DeleteEventOutputData deleteEventOutputData =
        new DeleteEventOutputData(inputData.getEvent());

      this.deleteEventPresenter.prepareSuccessView(deleteEventOutputData);
    }
  }
}
