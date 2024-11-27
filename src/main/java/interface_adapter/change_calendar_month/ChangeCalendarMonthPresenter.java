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
    ChangeCalendarMonthState state = new ChangeCalendarMonthState();
    state.setCurrCalendarList(outputData.getCalendarList());
    viewModel.setState(state);
    viewModel.firePropertyChanged();
  }
}
