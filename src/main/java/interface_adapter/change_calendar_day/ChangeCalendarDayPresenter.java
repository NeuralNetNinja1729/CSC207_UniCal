package interface_adapter.change_calendar_day;

import entity.Event;
import interface_adapter.ViewManagerModel;
import use_case.change_calendar_day.ChangeCalendarDayOutputBoundary;
import use_case.change_calendar_day.ChangeCalendarDayOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * The Presenter for the Change Day Calendar Use Case.
 */
public class ChangeCalendarDayPresenter implements ChangeCalendarDayOutputBoundary {

    private final ChangeCalendarDayViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public ChangeCalendarDayPresenter(ChangeCalendarDayViewModel changeCalendarDayViewModel,
                                      ViewManagerModel viewManagerModel) {
        this.viewModel = changeCalendarDayViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(ChangeCalendarDayOutputData outputData) {
        List<String> eventsList = new ArrayList<>();

        // Format the events
        for (Event event : outputData.getEventList()) {
            String eventName = event.getEventName();
            String calendarApiName = event.getCalendarApi().getCalendarApiName(); // Assuming Event has a method getCalendarApiName()

            // Add the event name and calendar API name to the list
            eventsList.add(eventName + " (" + calendarApiName + ")");
        }

        // Update the view model with formatted data
        ChangeCalendarDayState state = this.viewModel.getState();
        state.setCurrCalendarList(outputData.getCalendarList());


        // Store events as a list for easy UI rendering
        state.setEventList(eventsList);
        state.setCurrMonth(outputData.getDate().getMonth().toString());
        state.setCurrYear(outputData.getDate().getYear());
        state.setCurrDay(outputData.getDate().getDayOfMonth());

        // Notify the view model about the state change
        this.viewModel.setState(state);
        this.viewModel.firePropertyChanged();

        this.viewManagerModel.setState(viewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }
}
