package interface_adapter.change_calendar_day;

import use_case.change_calendar_day.ChangeCalendarDayOutputBoundary;
import use_case.change_calendar_day.ChangeCalendarDayOutputData;
import entity.Event;
import java.time.LocalTime;
import java.util.List;

public class ChangeCalendarDayPresenter implements ChangeCalendarDayOutputBoundary {
  private final ChangeCalendarDayViewModel changeCalendarDayViewModel;

  public ChangeCalendarDayPresenter(ChangeCalendarDayViewModel viewModel) {
    this.changeCalendarDayViewModel = viewModel;
  }

  @Override
  public void prepareSuccessView(ChangeCalendarDayOutputData outputData) {
    ChangeCalendarDayState state = new ChangeCalendarDayState();
    state.setCalendarList(outputData.getCalendarList());
    state.setEventList(outputData.getEventList());
    state.setUseCaseFailed(false);
    state.setError("");

    // Sort events by time
    sortEvents(state.getEventList());

    changeCalendarDayViewModel.setState(state);
    changeCalendarDayViewModel.firePropertyChanged();
  }

  @Override
  public void prepareFailView(String error) {
    ChangeCalendarDayState state = new ChangeCalendarDayState();
    state.setError(error);
    state.setUseCaseFailed(true);
    changeCalendarDayViewModel.setState(state);
    changeCalendarDayViewModel.firePropertyChanged();
  }

  private void sortEvents(List<Event> events) {
    events.sort((e1, e2) -> {
      // First compare by date
      int dateCompare = e1.getDate().compareTo(e2.getDate());
      if (dateCompare != 0) {
        return dateCompare;
      }

      // If dates are equal, compare by start time
      LocalTime time1 = e1.getStartTime();
      LocalTime time2 = e2.getStartTime();
      int timeCompare = time1.compareTo(time2);
      if (timeCompare != 0) {
        return timeCompare;
      }

      // If start times are equal, compare by end time
      int endTimeCompare = e1.getEndTime().compareTo(e2.getEndTime());
      if (endTimeCompare != 0) {
        return endTimeCompare;
      }

      // If all times are equal, sort by event name
      return e1.getEventName().compareTo(e2.getEventName());
    });
  }
}