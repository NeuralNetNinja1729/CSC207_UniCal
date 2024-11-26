package interface_adapter.change_calendar_day;

import use_case.change_calendar_day.ChangeCalendarDayOutputBoundary;
import use_case.change_calendar_day.ChangeCalendarDayOutputData;

public class ChangeCalendarDayPresenter implements ChangeCalendarDayOutputBoundary {
    private final ChangeCalendarDayViewModel changeCalendarDayViewModel;

    public ChangeCalendarDayPresenter(ChangeCalendarDayViewModel changeCalendarDayViewModel) {
        this.changeCalendarDayViewModel = changeCalendarDayViewModel;
    }

    @Override
    public void prepareSuccessView(ChangeCalendarDayOutputData outputData) {
        ChangeCalendarDayState currentState = changeCalendarDayViewModel.getState();
        ChangeCalendarDayState newState = new ChangeCalendarDayState(currentState);

        // Update state with data from the use case output
        newState.setCalendars(outputData.getCalendarList());
        newState.setEvents(outputData.getEventList());
        newState.setError(null);

        changeCalendarDayViewModel.setState(newState);
        changeCalendarDayViewModel.updateEvents();
    }

    /**
     * Handles preparing the failure view when an error occurs
     */
    public void prepareFailView(String error) {
        ChangeCalendarDayState currentState = changeCalendarDayViewModel.getState();
        ChangeCalendarDayState newState = new ChangeCalendarDayState(currentState);
        newState.setError(error);

        changeCalendarDayViewModel.setState(newState);
        changeCalendarDayViewModel.updateError();
    }
}