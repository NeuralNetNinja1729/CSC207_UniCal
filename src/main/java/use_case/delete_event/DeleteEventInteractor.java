package use_case.delete_event;

import data_access.CalendarDataAccessObjectFactory;
import data_access.DeleteEventDataAccessInterface;
import entity.Calendar;

public class DeleteEventInteractor implements DeleteEventInputBoundary {
  private final CalendarDataAccessObjectFactory calendarDataAccessObjectFactory;
  private final DeleteEventOutputBoundary deleteEventPresenter;

  public DeleteEventInteractor(
          CalendarDataAccessObjectFactory calendarDataAccessObjectFactory,
          DeleteEventOutputBoundary deleteEventPresenter) {
    this.calendarDataAccessObjectFactory = calendarDataAccessObjectFactory;
    this.deleteEventPresenter = deleteEventPresenter;
  }

  @Override
  public void execute(DeleteEventInputData inputData) {
    try {
      Calendar calendar = inputData.getEvent().getCalendarApi();
      DeleteEventDataAccessInterface deleteEventDataAccessObject =
              (DeleteEventDataAccessInterface) calendarDataAccessObjectFactory
                      .getCalendarDataAccessObject(calendar);

      if (!deleteEventDataAccessObject.deleteEvent(inputData.getEvent())) {
        deleteEventPresenter.prepareFailView("Failed to delete event from calendar");
        return;
      }

      DeleteEventOutputData outputData = new DeleteEventOutputData(inputData.getEvent(), false);
      deleteEventPresenter.prepareSuccessView(outputData);
    } catch (Exception e) {
      deleteEventPresenter.prepareFailView("Error deleting event: " + e.getMessage());
    }
  }
}
