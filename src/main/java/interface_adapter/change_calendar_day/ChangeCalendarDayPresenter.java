// Second, ChangeCalendarDayPresenter.java
package interface_adapter.change_calendar_day;

import entity.Calendar;
import use_case.change_calendar_day.ChangeCalendarDayOutputBoundary;
import use_case.change_calendar_day.ChangeCalendarDayOutputData;
import entity.Event;

import java.time.LocalDate;
import java.util.List;

public class ChangeCalendarDayPresenter implements ChangeCalendarDayOutputBoundary {
  private final ChangeCalendarDayViewModel changeCalendarDayViewModel;

  public ChangeCalendarDayPresenter(ChangeCalendarDayViewModel viewModel) {
    this.changeCalendarDayViewModel = viewModel;
  }

  @Override
  public void prepareSuccessView(ChangeCalendarDayOutputData outputData) {
    // Get current state and update it
    ChangeCalendarDayState state = changeCalendarDayViewModel.getState();

    // Update the state with new data
    state.setCalendarList(outputData.getCalendarList());
    state.setEventList(outputData.getEventList());

    // Sort events by time if needed
    sortEvents(state.getEventList());

    // Update the view model
    changeCalendarDayViewModel.setState(state);

    // Notify observers of the change
    changeCalendarDayViewModel.firePropertyChanged();
  }

  /**
   * Helper method to sort events by time
   * @param events list of events to sort
   */
  private void sortEvents(List<Event> events) {
    events.sort((e1, e2) -> {
      // First compare by date
      int dateCompare = e1.getDate().compareTo(e2.getDate());
      if (dateCompare != 0) {
        return dateCompare;
      }
      // If dates are equal, compare by name
      return e1.getEventName().compareTo(e2.getEventName());
    });
  }

  /**
   * Updates the view with the events for a specific date
   * @param date the date to show events for
   */
  public void updateViewForDate(LocalDate date) {
    ChangeCalendarDayState state = changeCalendarDayViewModel.getState();
    state.setDate(date);
    changeCalendarDayViewModel.setState(state);
    changeCalendarDayViewModel.firePropertyChanged();
  }

  /**
   * Updates the view with a new list of calendars
   * @param calendars the new list of calendars
   */
  public void updateCalendarList(List<Calendar> calendars) {
    ChangeCalendarDayState state = changeCalendarDayViewModel.getState();
    state.setCalendarList(calendars);
    changeCalendarDayViewModel.setState(state);
    changeCalendarDayViewModel.firePropertyChanged();
  }

  /**
   * Updates the view with new events
   * @param events the new list of events
   */
  public void updateEvents(List<Event> events) {
    ChangeCalendarDayState state = changeCalendarDayViewModel.getState();
    state.setEventList(events);
    sortEvents(state.getEventList());
    changeCalendarDayViewModel.setState(state);
    changeCalendarDayViewModel.firePropertyChanged();
  }

  /**
   * Clears all events from the view
   */
  public void clearEvents() {
    ChangeCalendarDayState state = changeCalendarDayViewModel.getState();
    state.getEventList().clear();
    changeCalendarDayViewModel.setState(state);
    changeCalendarDayViewModel.firePropertyChanged();
  }
}
