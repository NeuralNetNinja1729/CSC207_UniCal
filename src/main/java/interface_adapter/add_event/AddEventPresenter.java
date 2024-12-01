package interface_adapter.add_event;

import interface_adapter.change_calendar_day.ChangeCalendarDayViewModel;
import use_case.add_event.AddEventOutputBoundary;
import use_case.add_event.AddEventOutputData;

public class AddEventPresenter implements AddEventOutputBoundary {
  private final AddEventViewModel addEventViewModel;
  private final ChangeCalendarDayViewModel dayViewModel;

  public AddEventPresenter(AddEventViewModel addEventViewModel,
                           ChangeCalendarDayViewModel dayViewModel) {
    this.addEventViewModel = addEventViewModel;
    this.dayViewModel = dayViewModel;
  }

  @Override
  public void prepareSuccessView(AddEventOutputData event) {
    // Update day view to show the new event
    dayViewModel.addEvent(event.getEvent());

    // Clear the add event view state
    AddEventState state = new AddEventState();  // Create fresh state
    addEventViewModel.setState(state);

    // Notify both views
    addEventViewModel.firePropertyChanged();
    dayViewModel.firePropertyChanged();
  }

  @Override
  public void prepareFailView(String error) {
    AddEventState state = addEventViewModel.getState();
    state = new AddEventState(state); // Create copy

    if (error.toLowerCase().contains("name")) {
      state.setEventNameError(error);
    } else if (error.toLowerCase().contains("date")) {
      state.setDateError(error);
    } else {
      state.setCalendarError(error);
    }

    state.setUseCaseFailed(true);
    addEventViewModel.setState(state);
    addEventViewModel.firePropertyChanged();
  }
}
