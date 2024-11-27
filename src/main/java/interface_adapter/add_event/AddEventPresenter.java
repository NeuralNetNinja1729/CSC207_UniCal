package interface_adapter.add_event;

import use_case.add_event.AddEventOutputBoundary;
import use_case.add_event.AddEventOutputData;

public class AddEventPresenter implements AddEventOutputBoundary {
  private final AddEventViewModel addEventViewModel;
  private final interface_adapter.change_calendar_day.ChangeCalendarDayViewModel dayViewModel;

  public AddEventPresenter(AddEventViewModel addEventViewModel,
                           interface_adapter.change_calendar_day.ChangeCalendarDayViewModel dayViewModel) {
    this.addEventViewModel = addEventViewModel;
    this.dayViewModel = dayViewModel;
  }

  @Override
  public void prepareSuccessView(AddEventOutputData event) {
    // Update day view to show the new event
    dayViewModel.addEvent(event.getEvent());

    // Clear the add event view state
    AddEventState state = addEventViewModel.getState();
    state.setEventName("");
    state.setDate("");
    state.setSelectedCalendar(null);
    addEventViewModel.setState(state);

    // Notify views
    addEventViewModel.firePropertyChanged();
    dayViewModel.firePropertyChanged();
  }

  @Override
  public void prepareFailView(String error) {
    AddEventState state = addEventViewModel.getState();
    if (error.contains("name")) {
      state.setEventNameError(error);
    } else if (error.contains("date")) {
      state.setDateError(error);
    } else {
      state.setCalendarError(error);
    }
    addEventViewModel.setState(state);
    addEventViewModel.firePropertyChanged();
  }
}
