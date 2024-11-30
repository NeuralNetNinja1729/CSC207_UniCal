package interface_adapter.change_calendar_month;

import entity.Event;
import use_case.change_calendar_month.ChangeCalendarMonthOutputBoundary;
import use_case.change_calendar_month.ChangeCalendarMonthOutputData;

/**
 * The Presenter for the Change Month Calendar Use Case.
 */
public class ChangeCalendarMonthPresenter implements ChangeCalendarMonthOutputBoundary {
  private final ChangeCalendarMonthViewModel viewModel;

  public ChangeCalendarMonthPresenter(ChangeCalendarMonthViewModel viewModel) {
    this.viewModel = viewModel;
  }

  @Override
  public void prepareSuccessView(ChangeCalendarMonthOutputData outputData) {
    ChangeCalendarMonthState state = viewModel.getState();
    state.setCurrCalendarList(outputData.getCalendarList());
    state.setCurrEvents(outputData.getEventList());  // Make sure events are being set
    System.out.println("Presenter received " +
      (outputData.getEventList() != null ? outputData.getEventList().size() : 0) +
      " events"); // Debug print
    viewModel.setState(state);
    viewModel.firePropertyChanged();
  }
}
