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
    // Update both view models
    dayViewModel.removeEvent(outputData.getEvent());
    dayViewModel.firePropertyChanged();

    DeleteEventState state = new DeleteEventState();
    state.setSelectedEvent(null);
    state.setUseCaseFailed(false);
    deleteEventViewModel.setState(state);
    deleteEventViewModel.firePropertyChanged();
  }

  @Override
  public void prepareFailView(String error) {
    DeleteEventState state = new DeleteEventState();
    state.setError(error);
    state.setUseCaseFailed(true);
    deleteEventViewModel.setState(state);
    deleteEventViewModel.firePropertyChanged();
  }
}
