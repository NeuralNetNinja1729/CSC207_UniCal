package interface_adapter.delete_event;

import interface_adapter.change_calendar_day.ChangeCalendarDayViewModel;
import use_case.delete_event.DeleteEventOutputBoundary;
import use_case.delete_event.DeleteEventOutputData;

public class DeleteEventPresenter implements DeleteEventOutputBoundary {
  private final DeleteEventViewModel deleteEventViewModel;
  private final ChangeCalendarDayViewModel dayViewModel;

  public DeleteEventPresenter(DeleteEventViewModel deleteEventViewModel,
                              ChangeCalendarDayViewModel dayViewModel) {
    this.deleteEventViewModel = deleteEventViewModel;
    this.dayViewModel = dayViewModel;
  }

  @Override
  public void prepareSuccessView(DeleteEventOutputData outputData) {
    // Update the current state
    DeleteEventState state = deleteEventViewModel.getState();
    state.setEventName(outputData.getEvent().getEventName());
    deleteEventViewModel.setState(state);

    // Notify view of successful deletion
    deleteEventViewModel.firePropertyChanged();
  }

  @Override
  public void prepareFailView(String error) {
    DeleteEventState state = deleteEventViewModel.getState();
    state.setErrorMessage(error);
    deleteEventViewModel.setState(state);
    deleteEventViewModel.firePropertyChanged();
  }
}
