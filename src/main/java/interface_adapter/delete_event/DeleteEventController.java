package interface_adapter.delete_event;

import entity.Calendar;
import entity.Event;
import use_case.delete_event.DeleteEventInputBoundary;
import use_case.delete_event.DeleteEventInputData;

public class DeleteEventController {
  private final DeleteEventInputBoundary deleteEventUseCaseInteractor;

  public DeleteEventController(DeleteEventInputBoundary deleteEventUseCaseInteractor) {
    this.deleteEventUseCaseInteractor = deleteEventUseCaseInteractor;
  }

  public void execute(Event event, Calendar calendar) {

    // Find the opening and closing parentheses

    final DeleteEventInputData deleteEventInputData = new DeleteEventInputData(event, calendar);
    deleteEventUseCaseInteractor.execute(deleteEventInputData);
  }
}
