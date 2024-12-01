package interface_adapter.delete_event;

import entity.Event;
import interface_adapter.ViewManagerModel;
import interface_adapter.change_calendar_day.ChangeCalendarDayState;
import interface_adapter.change_calendar_day.ChangeCalendarDayViewModel;
import use_case.delete_event.DeleteEventOutputBoundary;
import use_case.delete_event.DeleteEventOutputData;


public class DeleteEventPresenter implements DeleteEventOutputBoundary {
  private final ChangeCalendarDayViewModel viewModel;
  private final ViewManagerModel viewManagerModel;


  public DeleteEventPresenter(ChangeCalendarDayViewModel dayViewModel, ViewManagerModel viewManagerModel) {
      this.viewModel = dayViewModel;
      this.viewManagerModel = viewManagerModel;
  }

  @Override
  public void prepareSuccessView(DeleteEventOutputData outputData) {
    // Update the current state
    System.out.println("Successfully added event");
    Event event = outputData.getEvent();
    String eventName = event.getEventName();
    String calendarApiName = event.getCalendarApi().getCalendarApiName();
    ChangeCalendarDayState state = this.viewModel.getState();
    state.deleteEvent(eventName + " (" + calendarApiName + ")");
    this.viewModel.setState(state);
    this.viewModel.firePropertyChanged();

    this.viewManagerModel.setState(viewModel.getViewName());
    this.viewManagerModel.firePropertyChanged();

  }

  @Override
  public void prepareFailView(String error) {
    // TODO
  }
}
